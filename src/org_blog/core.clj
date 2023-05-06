(ns org-blog.core
  (:require
   [clojure.java.io :as io]
   [clojure.java.shell :as shell]
   [clojure.string :refer [blank? split] :as string]
   [clojure.term.colors :as c]
   [clojure.tools.namespace.repl :as ns-repl]
   [clojure.walk :as walk]
   [hawk.core :as hawk]
   [hiccup.core :refer [html]]
   [hiccup.page :refer [include-css]]
   [hickory.core :as hickory]
   [org-blog.pages.home :refer [gen-home]]
   [org.httpkit.server :as http-kit])
  )

(defn org-to-html [org-file]
  (let [absolute-org-file (-> (java.io.File. org-file) (.getCanonicalFile))
        cmd (str "pandoc -f org -t html5  " absolute-org-file)
        {:keys [exit out err]} (shell/sh "sh" "-c" cmd)]
    (if (zero? exit)
      out
      (do (println "Error:" err)
          nil))))

(defn add-prism-class [hiccup]
  (walk/postwalk
   (fn [node]
     (if (and (vector? node)
              (= :code (first node)))
       (let [class      (->> (second node)
                             :class)
             lang       (when (not (blank? class))
                          (->> class
                               (re-seq #"(?<=sourceCode\s)\w+")
                               first))
             lang-class (str "language-" lang)]
         (-> node
             (update 1 #(assoc % :class lang-class))))
       node))
   hiccup))

(defn wrap-in-hiccup [html-string]
  (let [parsed-html (hickory/parse-fragment html-string)
        hiccup-form (->> parsed-html (map hickory/as-hiccup) add-prism-class)]
    (html [:div.content-container hiccup-form])))

(defn post-header [body]
  [:html
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
    [:title "Generated Site"]
    (include-css "../../css/output.css")
    #_(include-js "/js/scripts.js")
    [:link {:rel            "stylesheet"
            :href           "https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/themes/prism-okaidia.min.css"
            :crossorigin    "anonymous"
            :referrerpolicy "no-referrer"}]
    [:script {:src            "https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-core.min.js"
              :crossorigin    "anonymous"
              :referrerpolicy "no-referrer"}]
    [:script {:src            "https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/plugins/autoloader/prism-autoloader.min.js"
              :crossorigin    "anonymous"
              :referrerpolicy "no-referrer"}]]

   [:body
    [:header
     [:nav]]
    body
    [:footer]]])

(defn ensure-directories-exist [path]
  (let [parent (-> path io/file .getParentFile)]
    (when-not (.exists parent)
      (.mkdirs parent))))

(defn spit-with-path [path content]
  (ensure-directories-exist path)
  (spit path content))

(defn gen-post [org-file out-dir]
  (let [post (->> org-file
                  org-to-html
                  wrap-in-hiccup
                  post-header
                  html)
        post-name (str (-> org-file
                           (java.io.File.)
                           (.getName)
                           (clojure.string/replace #"\.org$" "")))
        post-file-path (str out-dir "/" post-name "/index.html")]
    (spit-with-path post-file-path post)))

(defn gen-posts []
  (let [org-dir "./posts"
        out-dir "./static/posts"]
    (-> "Generating posts..." c/blue println)
    (-> out-dir io/file .mkdirs)
    (let [org-files (->> org-dir
                         io/file
                         file-seq
                         (filter #(re-matches #".*\.org" (.getName %)))
                         (map #(.getCanonicalPath %)))]
      (doseq [org-file org-files]
        (-> "  Generating html for  " (str org-file) c/blue println)
        (gen-post org-file out-dir)))
    (-> "Done!" c/blue println)))

(defn -main [& args]
  (println "I don't do anything yet")
  #_(System/exit 0))

(defn regenerate-site []
  (gen-home)
  (gen-posts))

(def content-types
  {"html" "text/html"
   "css"  "text/css"
   ;; Add other content types as needed
   })

(defn content-type-for [filename]
  (let [ext (->> (split filename #"\.")
                 last
                 (get content-types))]
    (or ext "text/plain")))

(defn handler [req]
  (let [resource-path (str "static" (:uri req))
        file (io/file resource-path)]
    (if (.exists file)
      (if (.isDirectory file)
        {:status  200
         :headers {"Content-Type" "text/html"}
         :body    (slurp (io/file (str resource-path "/index.html")))}
        {:status  200
         :headers {"Content-Type" (content-type-for resource-path)}
         :body    (slurp file)})
      {:status  404
       :headers {"Content-Type" "text/plain"}
       :body    "Not Found"})))

(defn start-server []
  (http-kit/run-server handler {:port 8080}))

(defonce server-atom (atom nil))

(when (nil? @server-atom)
  (-> "starting server" c/blue println)
  (regenerate-site)
  (reset! server-atom (start-server)))

(defn watch-source-files [dirs]
  (let [handler      (fn [ctx e]
                       (when (= (:kind e) :modify)
                         (println "File modified:" (:file e))
                         ;; Calling `ns-repl/refresh` in another thread (hawk must run this handler in a another thread)
                         ;; generates an error
                         ;; By wrapping in future, by some magic, the function calls within are scheduled on the main thread
                         (future
                          (println "Refreshing repl ...")
                          (ns-repl/refresh)
                          (regenerate-site))))
        watch-config (hawk/watch! [{:paths   dirs
                                    :handler handler}])]
    watch-config))

(defonce source-watchers (atom nil))

(when (nil? @source-watchers)
  (reset! source-watchers (watch-source-files ["src"])))

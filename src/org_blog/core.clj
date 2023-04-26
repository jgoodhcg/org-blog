(ns org-blog.core
  (:require
   [clojure.walk :as walk]
   [clojure.java.io :as io]
   [clojure.java.shell :as shell]
   [clojure.term.colors :as c]
   [hiccup.core :refer [html]]
   [hiccup.page :refer [include-css include-js]]
   [hickory.core :as hickory]))

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
       (let [lang       (->> (second node)
                             :class
                             (re-seq #"(?<=sourceCode\s)\w+")
                             first)
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
    [:title "Generated Site"]
    (include-css "../css/output.css")
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

(defn gen-post [org-file out-dir]
  (let [post (->> org-file
                  org-to-html
                  wrap-in-hiccup
                  post-header
                  html)
        post-file-name (str (-> org-file
                                (java.io.File.)
                                (.getName)
                                (clojure.string/replace #"\.org$" ".html")))
        post-file-path (str out-dir "/" post-file-name)]
    (spit post-file-path post)))

(defn gen-index []
  (-> [:html.dark
       [:head
        [:meta {:charset "utf-8"}]
        [:title "Generated Site"]
        (include-css "./css/output.css")
        #_(include-js "/js/scripts.js")]
       [:body
        [:header
         [:nav]]
        [:div.content-container
         [:h1 "yo"]
         [:a {:href "posts/hello-world.html"} "hello world post"]]
        [:footer]]]
      html
      (->> (spit "./static/index.html"))))

(defn -main [& args]
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
        (-> "Generating html for  " (str org-file) c/blue println)
        (gen-post org-file out-dir)))
    (-> "Done!" c/blue println)
    #_(System/exit 0)))

(comment
  (->> "./posts/hello-world.org"
       org-to-html
       wrap-in-hiccup
       )

  (-main)

  (gen-index)
  )

(-main)

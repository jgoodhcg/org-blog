(ns gen-site
  (:require
   [clojure.java.io :as io]
   [clojure.java.shell :as shell]
   [clojure.term.colors :as c]
   [hiccup.core :refer [html]]
   [hiccup.page :refer [include-css include-js]]
   [hickory.core :as hickory]))

(defn org-to-html [org-file]
  (let [absolute-org-file (-> (java.io.File. org-file) (.getCanonicalFile))
        cmd (str "pandoc -f org -t html5 " absolute-org-file)
        {:keys [exit out err]} (shell/sh "sh" "-c" cmd)]
    (if (zero? exit)
      out
      (do (println "Error:" err)
          nil))))

(defn wrap-in-hiccup [html-string]
  (let [parsed-html (hickory/parse-fragment html-string)
        hiccup-form (->> parsed-html (map hickory/as-hiccup))]
    (html [:div.content-container hiccup-form])))

(defn header [body]
  [:html.dark
   [:head
    [:meta {:charset "utf-8"}]
    [:title "Generated Site"]
    (include-css "./static/css/output.css")
    #_(include-js "/js/scripts.js")]
   [:body
    [:header
     [:nav]]
    body
    [:footer]]])

(defn render-post [org-file out-dir]
  (let [post (->> org-file
                  org-to-html
                  wrap-in-hiccup
                  header
                  html)
        post-file-name (str (-> org-file
                                (java.io.File.)
                                (.getName)
                                (clojure.string/replace #"\.org$" ".html")))
        post-file-path (str out-dir "/" post-file-name)]
    (spit post-file-path post)))

(defn generate-index [org-files out-dir]
  (let [latest-posts (map (fn [org-file]
                             (let [post (->> org-file
                                            org-to-html
                                            wrap-in-hiccup)]
                               [:div.post-preview post])) org-files)
        index (-> [:div latest-posts] header html)]
    (spit (str out-dir "/index.html") index)))

(defn -main [& args]
  (let [org-dir "./posts"
        out-dir "./"]
    (-> "Generating site..." c/blue println)
    (-> out-dir io/file .mkdirs)
    (let [org-files (->> org-dir
                         io/file
                         file-seq
                         (filter #(re-matches #".*\.org" (.getName %)))
                         (map #(.getCanonicalPath %)))]
      (doseq [org-file org-files]
        (render-post org-file out-dir))
      (generate-index org-files out-dir))
    (-> "Done!" c/blue println)
    #_(System/exit 0)))

(comment
  (->> "./posts/hello-world.org"
       org-to-html
       wrap-in-hiccup
       header
       )

  (-main)
  )

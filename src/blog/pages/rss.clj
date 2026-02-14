(ns blog.pages.rss
  (:require
   [clojure.data.xml :as xml]
   [clojure.java.io :as io]
   [blog.common.files :refer [posts-dir spit-with-path]]
   [blog.posts :as posts]
   [potpuri.core :as pot]
   [clojure.pprint :refer [pprint]]))

(defn create-rss-item [post-name]
  (xml/element :item {}
               (xml/element :title {} post-name)
               (xml/element :link {} (str "http://jgood.online/posts/" post-name)))) ;; TODO

(defn create-rss-feed [items]
  (xml/element :rss {:version "2.0"}
               (xml/element :channel {}
                            (xml/element :title {} "Justin Good's Blog")
                            (xml/element :link {} "http://jgood.online")
                            (xml/element :description {} "Still trying to figure out what to write about")
                            items)))

;; TODO Add more metadata
;; <description> or <content>: A summary or the full content of the post.
;; <pubDate>: The publication date of the post.
;; <guid>: A globally unique identifier for the post.
;; TODO XSLT (Extensible Stylesheet Language Transformations)
(defn gen []
  (println "Generating RSS feed")
  (let [items (->> posts-dir
                   io/file
                   file-seq
                   (filter #(re-matches #".*\.md" (.getName %)))
                   (sort)
                   (map #(str (.getCanonicalPath %)))
                   (map (fn [md-file]
                          (let [post-name (posts/get-post-name md-file)]
                            (create-rss-item post-name)))))
        rss-feed (create-rss-feed items)]
    (with-open [out-file (java.io.FileWriter. "./static/rss.xml")]
      (xml/emit rss-feed out-file))))

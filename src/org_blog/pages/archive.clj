(ns org-blog.pages.archive
  (:require
   [clojure.java.io :as io]
   [clojure.term.colors :as c]
   [hiccup.core :refer [html]]
   [org-blog.common.components :as comps]
   [org-blog.common.files :refer [posts-dir spit-with-path]]
   [org-blog.posts :as posts]))

(defn gen []
  (-> "Generating archive page" c/blue println)
  (-> [:html {:lang "en"} ; Add language attribute
       (comps/head)
       (comps/body
        [:main.p-4
         [:div.w-full
          [:h1 "All posts"]
          [:ul.flex.flex-col
           (->> posts-dir
                io/file
                file-seq
                (filter #(re-matches #".*\.md" (.getName %)))
                (sort)
                (reverse)
                (map #(str (.getCanonicalPath %)))
                (map (fn [md-file]
                       (let [post-name (posts/get-post-name md-file)]
                         [:a {:href (str "/posts/" post-name)} post-name]))))]]])]
      html
      (->> (spit-with-path "./static/archive/index.html"))))

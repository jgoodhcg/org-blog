(ns org-blog.pages.archive
  (:require
   [clojure.java.io :as io]
   [clojure.term.colors :as c]
   [hiccup.core :refer [html]]
   [org-blog.common.components :as comps]
   [org-blog.common.files :refer [posts-org-dir spit-with-path]]
   [org-blog.posts :as posts]))

(defn gen []
  (-> "Generating archive page" c/blue println)
  (-> [:html {:lang "en"} ; Add language attribute
       (comps/head)
       (comps/body
        [:header
         (comps/nav)]
        [:main
         [:div.lcars-bottom-border.lcars-border-purple.pl-8.md:pl-40
          [:div.p-4.w-full.rounded-tl-lg.bg-black
           [:h1 "All posts"]
           [:ul.flex.flex-col
            (->> posts-org-dir
                 io/file
                 file-seq
                 (filter #(re-matches #".*\.org" (.getName %)))
                 (sort)
                 (reverse)
                 (map #(str (.getCanonicalPath %)))
                 (map (fn [org-file]
                        (let [post-name (posts/get-org-file-name org-file)]
                          [:a {:href (str "/posts/" post-name)} post-name]))))]]]])]
      html
      (->> (spit-with-path "./static/archive/index.html"))))

(ns org-blog.pages.archive
  (:require
   [clojure.java.io :as io]
   [clojure.term.colors :as c]
   [hiccup.core :refer [html]]
   [org-blog.common.components :as comps]
   [org-blog.common.files :refer [posts-dir spit-with-path]]
   [org-blog.common.markdown :refer [extract-metadata]]
   [org-blog.posts :as posts]))

(defn gen []
  (-> "Generating archive page" c/blue println)
  (-> [:html {:lang "en"}
       (comps/head {:title "Archive - Justin Good"})
       (comps/body
        [:main.max-w-2xl.mx-auto.px-4
         [:h1.text-3xl.font-bold.mb-8 "Archive"]
         [:ul.list-none.pl-0.space-y-8
          (->> posts-dir
               io/file
               file-seq
               (filter #(re-matches #".*\.md" (.getName %)))
               (sort)
               (reverse)
               (map #(str (.getCanonicalPath %)))
               (map (fn [md-file]
                      (let [metadata  (extract-metadata (slurp md-file))
                            post-name (posts/get-post-name md-file)]
                        [:li
                         [:a.no-underline.flex.justify-between.items-baseline.group
                          {:href (str "/posts/" post-name)}
                          [:span.text-text.group-hover:text-accent
                           (or (:title metadata) post-name)]
                          [:span.text-text-secondary.text-sm (:date metadata)]]]))))]])]
      html
      (->> (spit-with-path "./static/archive/index.html"))))

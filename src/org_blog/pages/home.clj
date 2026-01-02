(ns org-blog.pages.home
  (:require
   [clojure.java.io :as io]
   [clojure.term.colors :as c]
   [hiccup.core :refer [html]]
   [org-blog.common.components :as comps]
   [org-blog.common.files :refer [posts-dir spit-with-path]]
   [org-blog.common.markdown :refer [estimate-read-time extract-metadata]]
   [org-blog.posts :as posts]))

(defn gen []
  (-> "Generating home (index) page" c/blue println)
  (-> [:html {:lang "en"} ; Add language attribute
       (comps/head)
       (comps/body
        [:main.grid.grid-cols-5
         [:div.col-span-5.p-4.md:col-span-3.md:col-start-2
          [:div.flex.flow-row.items-center
           [:span.mt-8.mb-2.text-3xl.mr-2 "🚧"]
           [:h1 " WIP"]
           [:span.mt-8.mb-2.text-3xl.ml-2 "🚧"]]
          [:p "Still trying to figure out what to write about. "]
          [:p "If you want to see any of my software projects, they are all on "
           [:a {:href "https://github.com/jgoodhcg"} "github."]]
          [:p "Here is my " [:a {:href "/resume"} "resume"] ", if you are looking for that."]
          [:p "If you are curious about what I'm up to check out my "
           [:a {:href "/now"} "now page"] "."]
          [:div.mt-16 [:span.text-2xl.text-cyan.font-bold "Recent Posts"]]
          [:ul.mt-4
           (->> posts-dir
                io/file
                file-seq
                (filter #(re-matches #".*\.md" (.getName %)))
                (sort)
                (reverse)
                (take 8)
                (map #(str (.getCanonicalPath %)))
                (map (fn [md-file]
                       (let [md-content (slurp md-file)
                             metadata   (extract-metadata md-content)
                             read-time  (estimate-read-time md-content)
                             post-name  (posts/get-post-name md-file)]
                         [:li.no-bullet.my-4.p-0
                          [:hr]
                          [:a.no-underline {:href (str "/posts/" post-name)}
                           [:h2.mb-0 (:title metadata)]
                           [:p (:description metadata)]
                           [:div.flex.flex-col
                            [:p.text-white-900 (:date metadata)]
                            [:p.text-white-900.mr-4 (str "Read time: " read-time " mins")]
                            #_
                            [:div.flex.flex-row
                             (->> metadata
                                  :tags
                                  (map (fn [t]
                                         [:a.mr-2.text-sm.rounded-lg
                                          {:href (str "/tags/" t)}
                                          t])))]]]]))))]]])]

      html
      (->> (spit-with-path "./static/index.html"))))

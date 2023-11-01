(ns org-blog.pages.home
  (:require
   [clojure.java.io :as io]
   [clojure.term.colors :as c]
   [hiccup.core :refer [html]]
   [potpuri.core :as pot]
   [org-blog.common.components :as comps]
   [org-blog.common.org :refer [count-words estimate-read-time]]
   [org-blog.common.files :refer [posts-org-dir spit-with-path]]
   [org-blog.posts :as posts]))

(defn extract-org-metadata [org-content]
  (let [title-pattern       #"\#\+title: (.+)"
        date-pattern        #"\#\+date:<(\d{4}-\d{2}-\d{2}).+>"
        description-pattern #"\#\+description: (.+)"
        thumbnail-pattern   #"\#\+thumbnail: (.+)"
        title               (second (re-find title-pattern org-content))
        date                (second (re-find date-pattern org-content))
        thumbnail           (second (re-find thumbnail-pattern org-content))
        description         (second (re-find description-pattern org-content))]

    (pot/map-of title date description thumbnail)))

(defn gen []
  (-> "Generating home (index) page" c/blue println)
  (-> [:html {:lang "en"} ; Add language attribute
       (comps/head)
       (comps/body
        [:header (comps/nav)]
        [:main.grid.grid-cols-5
         [:div.col-span-5.p-4.md:col-span-3.md:col-start-2
          [:div.flex.flow-row.items-center
           [:span.mt-8.mb-2.text-3xl.mr-2 "🚧"]
           [:h1 " WIP"]
           [:span.mt-8.mb-2.text-3xl.ml-2 "🚧"]]
          [:p "Still trying to figure out what to write about. "]
          [:p "If you want to see any of my software projects, they are all on "
           [:a {:href "https://github.com/jgoodhcg"} "github."]]
          [:p "Here is my" [:a {:href "/resume"} " resume, "]" if you are looking for that."]
          [:p "If you are curious about what I'm up to check out my "
           [:a {:href "/now"} "now page"] "."]
          [:h3.mt-8 "Recent Posts"]
          [:ul.mt-4
           (->> posts-org-dir
                io/file
                file-seq
                (filter #(re-matches #".*\.org" (.getName %)))
                (sort)
                (reverse)
                (take 8)
                (map #(str (.getCanonicalPath %)))
                (map (fn [org-file]
                       (let [org-content (slurp org-file)
                             metadata    (extract-org-metadata org-content)
                             read-time   (estimate-read-time org-content)
                             post-name   (posts/get-org-file-name org-file)]
                         [:li.no-bullet.my-4.p-0
                          [:hr]
                          [:a.no-underline {:href (str "/posts/" post-name)}
                           [:h2.mb-0 (:title metadata)]
                           [:p (:description metadata)]
                           [:p.text-white-900 (:date metadata)]
                           [:p.text-white-900 (str "Read time: " read-time " mins")]]]))))]]])]
      html
      (->> (spit-with-path "./static/index.html"))))

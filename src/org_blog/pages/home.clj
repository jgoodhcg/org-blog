(ns org-blog.pages.home
  (:require
   [clojure.java.io :as io]
   [clojure.term.colors :as c]
   [hiccup.core :refer [html]]
   [potpuri.core :as pot]
   [org-blog.common.components :as comps]
   [org-blog.common.files :refer [posts-org-dir]]
   [org-blog.common.files :refer [spit-with-path]]
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

(defn count-words [text]
  (count (re-seq #"\b\w+\b" text)))

(defn estimate-read-time [text]
  (let [word-count (count-words text)]
    (int (Math/ceil (/ word-count 250.0)))))

(defn gen []
  (-> "Generating home (index) page" c/blue println)
  (-> [:html {:lang "en"} ; Add language attribute
       (comps/head)
       (comps/body
        [:header (comps/nav)]
        [:main
         [:h1 "Justin's Blog"]
         [:p "If you are thinking about hiring me check out my "
          [:a {:href "https://github.com/jgoodhcg"} "github"]
          " and my "
          [:a {:href "/resume"} "resume"]
          "."]
         [:p "If you are curious about what I'm up to check out my "
          [:a {:href "/now"} "now page"]
          "."]
         [:h2 "Recent Posts"]
         [:ul
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
                        [:li.no-bullet.p-0.mb-4
                         [:a.no-underline {:href (str "/posts/" post-name)}
                          [:div.p-4.rounded-lg.flex.flex-row.items-center.bg-grey-100.hover:shadow-md.
                           [:img.w-24.h-24.md:w-64.md:h-64.object-cover.rounded-lg.mr-2.md:mr-4
                            {:src (:thumbnail metadata)}]
                           [:div
                            [:h3 (:title metadata)]
                            [:p (:description metadata)]
                            [:p.text-white-900 (:date metadata)]
                            [:p.text-white-900 (str "Read time: " read-time " mins")]]]]]))))]])]
      html
      (->> (spit-with-path "./static/index.html"))))

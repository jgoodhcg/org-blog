(ns org-blog.pages.home
  (:require
   [clojure.java.io :as io]
   [org-blog.common.components :as comps]
   [clojure.term.colors :as c]
   [hiccup.core :refer [html]]
   [hiccup.page :refer [include-css]]
   [org-blog.posts :as posts]))

(defn gen-home []
  (-> "Generating home (index) page" c/blue println)
  (-> [:html {:lang "en"} ; Add language attribute
       [:head
        [:meta {:charset "utf-8"}]
        [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
        [:meta {:name    "description" ; Add a description meta tag
                :content "Justin Good's personal blog about software engineering, permaculture, and more."}]
        ; Add Open Graph tags
        [:meta {:property "og:title" :content "Jgood Blog"}]
        [:meta {:property "og:description" :content "Justin Good's personal blog about software engineering, permaculture, and more."}]
        [:meta {:property "og:type" :content "website"}]
        [:meta {:property "og:url" :content "https://your-domain.com"}] ;; TODO
        [:meta {:property "og:image" :content "https://your-domain.com/path/to/your/image.jpg"}] ;; TODO

        [:title "Jgood Blog"]
        (include-css "./css/output.css")
        [:link {:rel "icon" :href "/path/to/favicon.ico"}] ;; TODO
        ]
       [:body
        [:header
         [:div.lcars-top-border.lcars-border-green.pl-8.md:pl-40
          [:div.p-4.rounded-bl-lg.bg-black
           [:h1.text-4xl.font-bold.mb-2.bg-clip-text.text-transparent.bg-gradient-to-b.from-green-100.to-cyan-100
            "JGood Blog"]
           [:nav.flex.flex-col.md:flex-row
            [:a.text-xl.font-bold.m-2 {:href "/"} "Home"]
            [:a.text-xl.font-bold.m-2 {:href "/"} "Archive"]
            [:a.text-xl.font-bold.m-2 {:href "/"} "Resume"]]]]]

        [:main
         [:div.lcars-bottom-border.lcars-border-purple.pl-8.md:pl-40
          [:div.p-4.w-full.rounded-tl-lg.bg-black
           [:h2 "Things I've got going on"]
           [:p "More stuff maybe"]
           [:h2 "Blog posts"]
           [:ul.flex.flex-col
            (->> posts/org-dir
                 io/file
                 file-seq
                 (filter #(re-matches #".*\.org" (.getName %)))
                 (sort)
                 (take 5)
                 (map #(str (.getCanonicalPath %)))
                 (map (fn [org-file]
                        (let [post-name (posts/get-org-file-name org-file)]
                          [:a {:href (str "/posts/" post-name)} post-name]))))]
           ]]]
        [:footer
         [:p "I'll have something one day"]]]]
      html
      (->> (spit "./static/index.html"))))

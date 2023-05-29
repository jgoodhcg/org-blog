(ns org-blog.common.components
  (:require
   [hiccup.page :refer [include-css]]))

(defn body [& content]
  [:body.container.md:mx-auto.md:max-w-screen-md.lg:max-w-screen-lg
   content])

(defn nav []
  [:div.lcars-top-border.lcars-border-green.pl-8.md:pl-40
   [:div.p-8.rounded-bl-lg.bg-black
    [:div.text-4xl.font-bold.mb-2.bg-clip-text.text-transparent.bg-gradient-to-b.from-green-100.to-cyan-100
     "JGood Blog"]
    [:nav.flex.flex-col.md:flex-row
     [:a.text-xl.font-bold.m-2 {:href "/"} "Home"]
     [:a.text-xl.font-bold.m-2 {:href "/archive"} "Archive"]
     [:a.text-xl.font-bold.m-2 {:href "/resume"} "Resume"]
     [:a.text-xl.font-bold.m-2 {:href "/rss.xml"} "RSS"]]]])

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
   [:meta {:name    "description" ;; Add a description meta tag
           :content "Justin Good's personal blog"}]
   ;; Add Open Graph tags
   [:meta {:property "og:title" :content "Jgood Blog"}]
   [:meta {:property "og:description" :content "Justin Good's personal blog"}]
   [:meta {:property "og:type" :content "website"}]
   [:meta {:property "og:url" :content "https://jgood.online"}]
   [:meta {:property "og:image" :content "https://jgood.online/img/2023-05-28-og-image-robot-steps.png"}]

   [:title "Jgood Blog"]
   [:link {:href "/css/output.css" :rel "stylesheet" :type "text/css"}]
   [:link {:rel "icon" :href "/img/2023-05-28-robot-head-favicon.png"}]
   ])

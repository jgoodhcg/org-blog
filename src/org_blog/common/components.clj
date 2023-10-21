(ns org-blog.common.components
  (:require
   [hiccup.page :refer [include-css]]))

(defn body [& content]
  [:body.container.mx-auto.bg-grey
   content])

(defn nav []
  [:nav.flex.flex-row.mb-8.justify-end.w-full.p-2
   [:a.text-2xl.font-bold.mr-2.text-white.no-underline.grow
    {:href "/"}
    [:span.text-cyan "Justin"]
    [:span.text-green "Good"]]
   [:a.text-xl.mr-2 {:href "/now"} "now"]
   [:a.text-xl.mr-2 {:href "/archive"} "archive"]
   [:a.text-xl.mr-2 {:href "/rss.xml"} "feed"]])

(defn head
  ([] (head {}))
  ([{:keys [prism]}]
   (cond->
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
     [:link {:rel "icon" :href "/img/2023-06-03-vaporwave-wigle-favicon.png"}]]

     (some? prism)
     (concat [;; prism code syntax highlighting
              [:link {:href "/css/prism-synthwave-84.css" :rel "stylesheet" :type "text/css"}]
               ;; [:link {:href "/css/prism-vsc-dark-plus.css" :rel "stylesheet" :type "text/css"}]
              [:script {:src            "https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-core.min.js"
                        :crossorigin    "anonymous"
                        :referrerpolicy "no-referrer"}]
              [:script {:src            "https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/plugins/autoloader/prism-autoloader.min.js"
                        :crossorigin    "anonymous"
                        :referrerpolicy "no-referrer"}]

               ;; LaTeX rendering
              [:script {:src "https://polyfill.io/v3/polyfill.min.js?features=es6"}]
              [:script {:id "MathJax-script", :async true, :src "https://cdn.jsdelivr.net/npm/mathjax@3/es5/tex-mml-chtml.js"}]])
     :default
     vec)))

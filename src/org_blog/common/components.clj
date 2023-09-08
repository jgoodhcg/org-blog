(ns org-blog.common.components
  (:require
   [hiccup.page :refer [include-css]]))

(defn body [& content]
  [:body.px-2.container.md:mx-auto.md:max-w-screen-md.lg:max-w-screen-lg.bg-grey
   content])

(defn nav []
  [:div.md:flex.md:flex-row.mt-4
   [:div.text-xl.font-bold.mr-8.text-white "JGood Blog"]
   [:nav.flex.flex-col.md:flex-row.mb-8
    [:a.text-xl.font-bold.mr-2 {:href "/"} "Home"]
    [:a.text-xl.font-bold.mr-2 {:href "/archive"} "Archive"]
    [:a.text-xl.font-bold.mr-2 {:href "/resume"} "Resume"]
    [:a.text-xl.font-bold.mr-2 {:href "/rss.xml"} "RSS"]]])

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
     vec
    )))

(ns org-blog.common.components
  (:require
   [hiccup.page :refer [include-css]]))

(defn head
  ([] (head {}))
  ([{:keys [prism mermaid title]}]
   (cond->
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
     [:meta {:name    "description"
             :content "Justin Good's blog"}]
     ;; Add Open Graph tags
     [:meta {:property "og:title" :content "Jgood Blog"}]
     [:meta {:property "og:description" :content "Jgood's blog"}]
     [:meta {:property "og:type" :content "website"}]
     [:meta {:property "og:url" :content "https://jgood.online"}]
     [:meta {:property "og:image" :content "https://jgood.online/img/2023-05-28-og-image-robot-steps.png"}]

     [:title (or title "jGood Blog")]
     [:link {:href "/css/output.css" :rel "stylesheet" :type "text/css"}]
     [:link {:rel "icon" :href "/img/2023-06-03-vaporwave-wigle-favicon.png"}]
     [:link {:rel "preconnect" :href "https://fonts.googleapis.com"}]
     [:link {:rel "preconnect" :href "https://fonts.gstatic.com" :crossorigin true}]
     [:link {:rel "stylesheet" :href "https://fonts.googleapis.com/css2?family=Fira+Sans:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&family=Schibsted+Grotesk:ital,wght@0,400;0,500;0,600;0,700;0,800;0,900;1,400;1,500;1,600;1,700;1,800;1,900&display=swap"}]
     ;; plausible analytics
     [:script {:defer true :data-domain "jgood.online" :src "https://plausible.io/js/script.js"}]]

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

(defn header []
  [:header.mb-8
   [:div.container.mx-auto.px-4
    [:div.flex.flex-col.md:flex-row.justify-between.items-center
     [:div.mb-4.md:mb-0
      [:div.flex.items-center
       [:a.text-xl.font-bold.text-gray-800.no-underline.hover:text-gray-600.transition-colors
        {:href "/"}
        [:span.text-cyan.font-grotesk "Justin"]
        [:span.text-green.font-grotesk "Good"]]]]

     [:div.flex.flex-col.md:flex-row.space-y-2.md:space-y-0.md:space-x-6.text-center.md:text-left
      [:a.text-gray-600.hover:text-gray-900.transition-colors {:href "/"} "Home"]
      [:a.text-gray-600.hover:text-gray-900.transition-colors {:href "/now"} "Now"]
      [:a.text-gray-600.hover:text-gray-900.transition-colors {:href "/archive"} "Archive"]
      [:a.text-gray-600.hover:text-gray-900.transition-colors {:href "/resume"} "Resume"]
      [:a.text-gray-600.hover:text-gray-900.transition-colors {:href "/rss.xml"} "RSS"]]]]])

(defn footer []
  [:footer.mt-12.py-8
   [:hr]
   [:div.container.mx-auto.px-4
    [:div.mt-6.text-center.text-gray-500.text-sm
     [:p "© " (.getYear (java.time.LocalDate/now)) " Justin Good. Built with "
      [:a.text-cyan.hover:underline
       {:href "https://github.com/jgoodhcg/org-blog"
        :target "_blank"
        :rel "noopener"}
       "Clojure and OrgMode"]]
     [:p.mt-2 "All content is © Justin Good. Code snippets may be used under MIT license unless noted otherwise."]]]])

(defn body [& content]
  [:body.container.mx-auto.bg-grey
   (header)
   content
   (footer)])

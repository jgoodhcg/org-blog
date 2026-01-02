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
     [:meta {:property "og:title" :content "Justin Good"}]
     [:meta {:property "og:description" :content "Personal blog"}]
     [:meta {:property "og:type" :content "website"}]
     [:meta {:property "og:url" :content "https://jgood.online"}]

     [:title (or title "Justin Good")]
     [:link {:href "/css/output.css" :rel "stylesheet" :type "text/css"}]
     [:link {:rel "preconnect" :href "https://fonts.googleapis.com"}]
     [:link {:rel "preconnect" :href "https://fonts.gstatic.com" :crossorigin true}]
     [:link {:rel "stylesheet" :href "https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap"}]
     ;; plausible analytics
     [:script {:defer true :data-domain "jgood.online" :src "https://plausible.io/js/script.js"}]]

     (some? prism)
     (concat [;; prism code syntax highlighting - using a calm theme
              [:link {:href "https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/themes/prism.min.css"
                      :rel "stylesheet" :type "text/css"}]
              [:script {:src            "https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-core.min.js"
                        :crossorigin    "anonymous"
                        :referrerpolicy "no-referrer"}]
              [:script {:src            "https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/plugins/autoloader/prism-autoloader.min.js"
                        :crossorigin    "anonymous"
                        :referrerpolicy "no-referrer"}]

              ;; LaTeX rendering
              [:script {:id "MathJax-script", :async true, :src "https://cdn.jsdelivr.net/npm/mathjax@3/es5/tex-mml-chtml.js"}]])

     :default
     vec)))

(defn header []
  [:header.py-8.mb-8
   [:div.max-w-2xl.mx-auto.px-4
    [:div.flex.flex-col.sm:flex-row.justify-between.items-baseline
     [:a.text-xl.font-semibold.no-underline.text-text.hover:text-accent
      {:href "/"}
      "Justin Good"]
     [:nav.flex.gap-6.mt-4.sm:mt-0.text-sm
      [:a.text-text-secondary.no-underline.hover:text-accent {:href "/"} "Home"]
      [:a.text-text-secondary.no-underline.hover:text-accent {:href "/now"} "Now"]
      [:a.text-text-secondary.no-underline.hover:text-accent {:href "/archive"} "Archive"]
      [:a.text-text-secondary.no-underline.hover:text-accent {:href "/resume"} "Resume"]
      [:a.text-text-secondary.no-underline.hover:text-accent {:href "/rss.xml"} "RSS"]]]]])

(defn footer []
  [:footer.mt-16.py-8.border-t.border-border
   [:div.max-w-2xl.mx-auto.px-4.text-center.text-sm.text-text-secondary
    [:p "© " (.getYear (java.time.LocalDate/now)) " Justin Good"]]])

(defn body [& content]
  [:body.bg-paper.min-h-screen
   (header)
   content
   (footer)])

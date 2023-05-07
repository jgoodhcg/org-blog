(ns org-blog.pages.home
  (:require
   [clojure.term.colors :as c]
   [hiccup.core :refer [html]]
   [hiccup.page :refer [include-css]]))

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
         [:nav]]
        [:main
         [:div.pb-1.pl-8.md:pl-32.rounded-bl-3xl.bg-yellow-900.lcars-top-border.
          [:div.p-4.rounded-bl-lg.bg-black
           [:h1 "Jgood Blog"]
           [:p (str "I'm Justin Good, a full stack software engineer with a wide range of interests and skills. "
                    "My professional experience mainly revolves around web applications "
                    "and mobile development using React Native on Expo, with a focus on writing Clojure code. "
                    "When I step away from the keyboard, "
                    "I enjoy transforming my lawn into a food forest and a biodiversity hotspot, doodling cartoons, "
                    "reading sci-fi novels, and listening to audiobooks on ancient history to fall asleep. "
                    "I care about addressing the climate crisis, promoting socialism, advocating for abolition, and advancing longevity research. "
                    "Welcome to my website, where I share my work, interests, and values. "
                    )]]]
         [:div.pt-1.mt-2.w-full.rounded-tl-3xl.bg-purple-900.flex.flex-row.lcars-bottom-border
          [:div.py-4.w-8.md:w-48.bg-gradient-to-b.hover:from-purple-900.hover:to-purple-900.hover:via-purple
           (for [i (-> (range 25))]
             [:div.px-4.hidden.md:block.border-t-2.border-black.hover:bg-purple-100 "side panel content here"])]
          [:div.p-4.w-full.rounded-tl-lg.bg-black
           [:h2.text-purple "Whatever"]
           [:a {:href "posts/hello-world"} "hello world post"]
           ]]]
        [:footer]]]
      html
      (->> (spit "./static/index.html"))))

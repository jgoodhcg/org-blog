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
         [:div.lcars-top-border.lcars-border-green
          [:div.p-4.rounded-bl-lg.bg-black
           [:h1 "Jgood Blog"]
           [:p (str "👋🏻 Greetings, fellow Earthling, I am Justin Good. "
                 "Capitalism dictates my identity so the most relevant thing about me is that I work as a "
                 "full stack software engineer. "
                 "I've specialized in web and mobile application development "
                 "with a recent focus in Clojure. "
                 "If you are interested, here is ")
            [:a {:href "/resume"} "my resume."]]]]
         [:div.lcars-bottom-border.lcars-border-purple
          [:div.pt-8.w-8.md:w-48
           (for [i (-> (range 25))]
             [:div.px-4.hidden.md:block.border-t-2.border-black.hover:bg-purple-100
              "side panel content here"])]
          [:div.p-4.w-full.rounded-tl-lg.bg-black
           [:h2 "Whatever"]
           [:a {:href "posts/hello-world"} "hello world post"]
           ]]]
        [:footer]]]
      html
      (->> (spit "./static/index.html"))))

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
        [:meta {:name "description" ; Add a description meta tag
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
        [:main.content-container ; Use semantic HTML tags
         [:h1 "Welcome to Jgood Blog"]
         [:p (str "I'm Justin Good, a full stack software engineer with a wide range of interests and skills. "
                  "My professional experience mainly revolves around web applications "
                  "and mobile development using React Native on Expo, with a focus on writing Clojure code. "
                  "When I step away from the keyboard, "
                  "I enjoy transforming my lawn into a food forest and a biodiversity haven, doodling cartoons, "
                  "delving into sci-fi novels, and immersing myself in ancient history and anthropology through audio-books. "
                  "I care about addressing the climate crisis, promoting socialism, advocating for abolition, and advancing longevity research. "
                  "Welcome to my website, where I share my work, interests, and values. "
                  )]]
        [:footer]]]
      html
      (->> (spit "./static/index.html"))))

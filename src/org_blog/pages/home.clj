(ns org-blog.pages.home
  (:require
   [clojure.term.colors :as c]
   [hiccup.core :refer [html]]
   [hiccup.page :refer [include-css]]))

(defn gen-home []
  (-> "Generating home (index) page ..." c/blue println)
  (-> [:html.dark
       [:head
        [:meta {:charset "utf-8"}]
        [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
        [:title "Generated Site"]
        (include-css "./css/output.css")
        #_(include-js "/js/scripts.js")]
       [:body
        [:header
         [:nav]]
        [:div.content-container
         [:h1 "One day this will be a real blog!"]
         [:p (str "I'm Justin Good. "
                  "As a full stack software engineer, I have a diverse range of interests and skills. "
                  "My professional experience mainly revolves around web applications "
                  "and mobile development using React Native on Expo, with a focus on writing Clojure code. "
                  "When I step away from the keyboard, "
                  "I enjoy transforming my lawn into a food forest and a biodiversity haven, doodling cartoons, "
                  "delving into sci-fi novels, and immersing myself in ancient history and anthropology through audio-books. "
                  "I care about addressing the climate crisis, promoting socialism, advocating for abolition, and advancing longevity research. Welcome to my website, where I share my work, interests, and the values. "
                  )]]
        [:footer]]]
      html
      (->> (spit "./static/index.html"))))

(ns org-blog.pages.resume
  (:require
   [clojure.java.io :as io]
   [clojure.term.colors :as c]
   [hiccup.core :refer [html]]
   [hickory.core :as hickory]
   [org-blog.common.components :as comps]
   [org-blog.common.files :refer [pages-org-dir spit-with-path]]
   [org-blog.common.org :refer [org->html
                                add-prism-class]]))

(defn gen []
  (-> "Generating resume page" c/blue println)
  (-> [:html {:lang "en"} ; Add language attribute
       (-> (comps/head)
           ;; TODO build into head
           (concat [[:link {:rel            "stylesheet"
                            :href           "https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/themes/prism-okaidia.min.css"
                            :crossorigin    "anonymous"
                            :referrerpolicy "no-referrer"}]
                    [:script {:src            "https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-core.min.js"
                              :crossorigin    "anonymous"
                              :referrerpolicy "no-referrer"}]
                    [:script {:src            "https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/plugins/autoloader/prism-autoloader.min.js"
                              :crossorigin    "anonymous"
                              :referrerpolicy "no-referrer"}]])
           vec)
       (comps/body
        [:header
         (comps/nav)]
        [:main
         [:div.lcars-bottom-border.lcars-border-purple.pl-8.md:pl-40
          [:div.p-4.w-full.rounded-tl-lg.bg-black
           (let [org-html   (->> (str pages-org-dir "/resume.org")
                                 io/file
                                 (.getCanonicalPath)
                                 org->html
                                 second)
                 org-parsed (hickory/parse-fragment org-html)
                 org-hiccup (->> org-parsed
                                 (map hickory/as-hiccup)
                                 add-prism-class)]
             org-hiccup)]]])]
      html
      (->> (spit-with-path "./static/resume/index.html"))))

(ns org-blog.pages.now
  (:require
   [clojure.java.io :as io]
   [clojure.term.colors :as c]
   [hiccup.core :refer [html]]
   [hickory.core :as hickory]
   [org-blog.common.components :as comps]
   [org-blog.common.files :refer [pages-org-dir spit-with-path]]
   [org-blog.posts :refer [post-page-hiccup]]
   [org-blog.common.org :refer [org->html
                                add-prism-class]]))

(defn gen []
  (-> "Generating now page" c/blue println)
  (dorun
   (->> (str pages-org-dir "/now.org")
        io/file
        (.getCanonicalPath)
        org->html
        post-page-hiccup
        html
        (spit-with-path "./static/now/index.html"))))

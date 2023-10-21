(ns org-blog.pages.resume
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
  (-> "Generating resume page" c/blue println)
  (dorun
   (->> (str pages-org-dir "/resume.org")
        io/file
        (.getCanonicalPath)
        org->html
        (#(assoc % :include-toc false))
        (#(assoc % :include-read-time false))
        post-page-hiccup
        html
        (spit-with-path "./static/resume/index.html"))))

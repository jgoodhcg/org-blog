(ns org-blog.posts
  (:require
   [clojure.java.io :as io]
   [org-blog.common.components :as comps]
   [clojure.string :as string]
   [clojure.term.colors :as c]
   [hiccup.core :refer [html]]
   [org-blog.common.files :refer [spit-with-path
                                  posts-org-dir
                                  posts-out-dir]]
   [org-blog.common.org :refer [org->html
                                add-prism-class]]
   [hiccup.page :refer [include-css]]
   [hickory.core :as hickory]))

(comment
  (->> "./posts/2023-04-22-kitchen-sink.org"
       org->html
       first
       hickory/parse-fragment
       (map hickory/as-hiccup)
       first
       )
  )

(defn post-page-hiccup [[toc-string body-string]]
  (let [parsed-toc  (hickory/parse-fragment toc-string)
        parsed-body (hickory/parse-fragment body-string)
        hiccup-toc  (->> parsed-toc (map hickory/as-hiccup))
        hiccup-body (->> parsed-body (map hickory/as-hiccup) add-prism-class)]
    [:html
     [:head
      [:meta {:charset "utf-8"}]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
      [:title "Generated Site"]
      (include-css "../../css/output.css")
      #_(include-js "/js/scripts.js")
      [:link {:rel            "stylesheet"
              :href           "https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/themes/prism-okaidia.min.css"
              :crossorigin    "anonymous"
              :referrerpolicy "no-referrer"}]
      [:script {:src            "https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-core.min.js"
                :crossorigin    "anonymous"
                :referrerpolicy "no-referrer"}]
      [:script {:src            "https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/plugins/autoloader/prism-autoloader.min.js"
                :crossorigin    "anonymous"
                :referrerpolicy "no-referrer"}]
      [:script {:src "https://polyfill.io/v3/polyfill.min.js?features=es6"}]
      [:script {:id "MathJax-script", :async true, :src "https://cdn.jsdelivr.net/npm/mathjax@3/es5/tex-mml-chtml.js"}]]

     (comps/body
      [:header
       (comps/nav)]
      [:main
       [:div.lcars-bottom-border.lcars-border-purple.pl-8.md:pl-40
        ;; TOC is hard
        #_[:header.h-fit.sticky.top-0.absolute.right-0.bg-purple-900.rounded.mt-4
         hiccup-toc]
        [:div.p-4.w-full.rounded-tl-lg.bg-black.h-fit
         [:div.w-fit
          hiccup-body]]]])]))

(defn get-org-file-name [org-file]
  (str (-> org-file
           (java.io.File.)
           (.getName)
           (clojure.string/replace #"\.org$" ""))))

(defn gen-post [org-file out-dir]
  (let [post           (->> org-file
                            org->html
                            post-page-hiccup
                            html)
        post-name      (get-org-file-name org-file)
        post-file-path (str out-dir "/" post-name "/index.html")]
    (spit-with-path post-file-path post)))

(defn gen []
  (-> "Generating posts..." c/blue println)
    (-> posts-out-dir io/file .mkdirs)
    (let [org-files (->> posts-org-dir
                         io/file
                         file-seq
                         (filter #(re-matches #".*\.org" (.getName %)))
                         (map #(.getCanonicalPath %)))]
      (doseq [org-file org-files]
        (-> "  Generating html for  " (str org-file) c/blue println)
        (gen-post org-file posts-out-dir)))
    (-> "Done!" c/blue println))

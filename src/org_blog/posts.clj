(ns org-blog.posts
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [clojure.term.colors :as c]
   [clojure.walk :as walk]
   [hiccup.core :refer [html]]
   [hiccup.page :refer [include-css]]
   [hickory.core :as hickory]
   [org-blog.common.components :as comps]
   [org-blog.common.files :refer [posts-org-dir posts-out-dir spit-with-path]]
   [org-blog.common.org :refer [add-prism-class org->html]]))

(defn get-a-elements
  [form]
  (if (and (coll? form) (= :a (first form)))
    [form]
    (if (coll? form)
      (apply concat (map get-a-elements form))
      [])))

(defn get-links-from-toc
  [toc]
  (get-a-elements toc))

(comment
  (->> "./posts/2023-04-22-kitchen-sink.org"
       org->html
       first
       hickory/parse-fragment
       (map hickory/as-hiccup)
       first
       get-links-from-toc
       (cons {:id "toc"})
       (cons :ul)
       vec
       )
  )

(defn post-page-hiccup [[toc-string body-string]]
  (let [parsed-toc  (hickory/parse-fragment toc-string)
        parsed-body (hickory/parse-fragment body-string)
        hiccup-toc  (->> parsed-toc
                         (map hickory/as-hiccup)
                         first
                         get-links-from-toc
                         (cons {:id "toc"})
                         (cons :nav.flex.flex-col)
                         vec)
        hiccup-body (->> parsed-body (map hickory/as-hiccup) add-prism-class)]
    [:html
     (-> (comps/head)
         (concat [;; prism code syntax highlighting
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

                  ;; LaTeX rendering
                  [:script {:src "https://polyfill.io/v3/polyfill.min.js?features=es6"}]
                  [:script {:id "MathJax-script", :async true, :src "https://cdn.jsdelivr.net/npm/mathjax@3/es5/tex-mml-chtml.js"}]])
         vec)


     (comps/body
      [:header
       (comps/nav)]
      [:main
       [:div.lcars-bottom-border.from-purple-900.via-pink-900.to-purple-900.flex.flex-row.md:pl-0
        [:header.hidden.md:block.md:mt-24.border-b-2.border-black.h-fit.sticky.top-0.z-50.md:w-48.overflow-auto
         hiccup-toc]
        [:div.p-8.w-full.rounded-tl-lg.bg-black.h-fit
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
    (->> org-files
         (pmap (fn [org-file]
                 (-> "  Generating html for  " (str org-file) c/blue println)
                 (gen-post org-file posts-out-dir)))
         doall))
  (-> "Done!" c/blue println))


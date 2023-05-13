(ns org-blog.posts
  (:require
   [clojure.java.io :as io]
   [clojure.java.shell :as shell]
   [org-blog.common.components :as comps]
   [clojure.string :refer [blank?] :as string]
   [clojure.term.colors :as c]
   [clojure.walk :as walk]
   [hiccup.core :refer [html]]
   [hiccup.page :refer [include-css]]
   [hickory.core :as hickory]))

(defn full-path [rel-path]
  (-> (java.io.File. rel-path) (.getCanonicalFile)))

(defn org-to-html
  "Requires at least pandoc 3.1.2"
  [org-file]

  (let [absolute-org-file  (full-path org-file)
        toc-template-path  (full-path "./src/org_blog/pandoc-template-toc.html")
        body-template-path (full-path "./src/org_blog/pandoc-template-body.html")
        toc-cmd            (str "pandoc -f org -t html "
                                    "--template=" toc-template-path " "
                                    "--table-of-contents " absolute-org-file)
        body-cmd           (str "pandoc -f org -t html "
                                     "--template=" body-template-path " "
                                     absolute-org-file)
        toc-result         (shell/sh "sh" "-c" toc-cmd)
        body-result        (shell/sh "sh" "-c" body-cmd)]
    (if (and (zero? (:exit toc-result))
             (zero? (:exit body-result)))
      [(:out toc-result)
       (:out body-result)]
      (do (println (str "Error(s):" [(:error toc-result) (:error body-result)]))
          nil))))

(comment
  (->> "./posts/2023-04-22-kitchen-sink.org"
       org-to-html
       first
       hickory/parse-fragment
       (map hickory/as-hiccup)
       first
       )
  )

(defn add-prism-class [hiccup]
  (walk/postwalk
   (fn [node]
     (if (and (vector? node)
              (= :code (first node)))
       (let [class      (->> (second node)
                             :class)
             lang       (when (not (blank? class))
                          (->> class
                               (re-seq #"(?<=sourceCode\s)\w+")
                               first))
             lang-class (str "language-" lang)]
         (-> node
             (update 1 #(assoc % :class lang-class))))
       node))
   hiccup))

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

     [:body.container.md:mx-auto.md:max-w-screen-md.lg:max-w-screen-lg
      [:header
       (comps/nav)]
      [:main
       [:div.lcars-bottom-border.lcars-border-purple.pl-8.md:pl-40
        ;; TOC is hard
        #_[:header.h-fit.sticky.top-0.absolute.right-0.bg-purple-900.rounded.mt-4
         hiccup-toc]
        [:div.p-4.w-full.rounded-tl-lg.bg-black.h-fit
         [:div.w-fit
          hiccup-body]]]]

      [:footer]]]))

(defn ensure-directories-exist [path]
  (let [parent (-> path io/file .getParentFile)]
    (when-not (.exists parent)
      (.mkdirs parent))))

(defn spit-with-path [path content]
  (ensure-directories-exist path)
  (spit path content))

(defn get-org-file-name [org-file]
  (str (-> org-file
           (java.io.File.)
           (.getName)
           (clojure.string/replace #"\.org$" ""))))

(defn gen-post [org-file out-dir]
  (let [post           (->> org-file
                            org-to-html
                            post-page-hiccup
                            html)
        post-name      (get-org-file-name org-file)
        post-file-path (str out-dir "/" post-name "/index.html")]
    (spit-with-path post-file-path post)))

(def org-dir "./posts")

(def out-dir "./static/posts")

(defn gen-posts []
  (-> "Generating posts..." c/blue println)
    (-> out-dir io/file .mkdirs)
    (let [org-files (->> org-dir
                         io/file
                         file-seq
                         (filter #(re-matches #".*\.org" (.getName %)))
                         (map #(.getCanonicalPath %)))]
      (doseq [org-file org-files]
        (-> "  Generating html for  " (str org-file) c/blue println)
        (gen-post org-file out-dir)))
    (-> "Done!" c/blue println))

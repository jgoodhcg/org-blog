(ns org-blog.posts
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [clojure.term.colors :as c]
   [hiccup.core :refer [html]]
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
       vec))

(defn post-page-hiccup [{:keys [toc
                                body
                                read-time
                                include-toc
                                include-read-time]
                         :or {include-toc true
                              include-read-time true}}]
  (let [parsed-toc  (hickory/parse-fragment toc)
        parsed-body (hickory/parse-fragment body)
        hiccup-toc  (->> parsed-toc
                         (map hickory/as-hiccup)
                         first
                         vec)
        hiccup-body (->> parsed-body (map hickory/as-hiccup) add-prism-class)]
    [:html
     (comps/head {:prism true})
     (comps/body
      [:header (comps/nav)]
      [:main.max-w-screen-lg.mx-auto.grid.grid-cols-4.gap-4.p-4
       [:div.col-span-4.md:col-span-3.w-full
        {:class ".md:w-3/4"}
        (when include-read-time
          [:p.text-white-900.italic (str "Read time: " read-time " mins")])
        [:div hiccup-body]]
       (when include-toc
         [:div.hidden.md:block.md:h-full.w-full
          {:class ".md:w-1/4"}
          [:div.sticky.top-0
           [:h4 "Table of Contents"]
           hiccup-toc]])])]))

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

(ns org-blog.posts
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [clojure.term.colors :as c]
   [hiccup.core :refer [html]]
   [hickory.core :as hickory]
   [org-blog.common.components :as comps]
   [org-blog.common.files :refer [posts-dir posts-out-dir spit-with-path]]
   [org-blog.common.markdown :refer [add-prism-class md->html]]))

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
  (->> "./posts/2023-04-22-kitchen-sink.md"
       md->html
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
     (comps/head {:prism true :mermaid true})
     (comps/body
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
           hiccup-toc]])]
      ;; render mermaid diagrams
      [:script {:type "module"}
       "import mermaid from 'https://cdn.jsdelivr.net/npm/mermaid@10/dist/mermaid.esm.min.mjs';"]
      )]))

(defn get-post-name
  "Extracts the post name from a markdown file path (without extension)."
  [md-file]
  (-> md-file
      (java.io.File.)
      (.getName)
      (clojure.string/replace #"\.md$" "")))

(defn gen-post [md-file out-dir]
  (let [post           (->> md-file
                            md->html
                            post-page-hiccup
                            html)
        post-name      (get-post-name md-file)
        post-file-path (str out-dir "/" post-name "/index.html")]
    (spit-with-path post-file-path post)))

(defn gen []
  (-> "Generating posts..." c/blue println)
  (-> posts-out-dir io/file .mkdirs)
  (let [md-files (->> posts-dir
                      io/file
                      file-seq
                      (filter #(re-matches #".*\.md" (.getName %)))
                      (map #(.getCanonicalPath %)))]
    (->> md-files
         (pmap (fn [md-file]
                 (-> "  Generating html for  " (str md-file) c/blue println)
                 (gen-post md-file posts-out-dir)))
         doall))
  (-> "Done!" c/blue println))

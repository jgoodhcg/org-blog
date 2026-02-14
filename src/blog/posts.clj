(ns blog.posts
  (:require
    [clojure.java.io       :as io]
    [clojure.string        :as string]
    [clojure.term.colors   :as c]
    [hiccup.core           :refer [html]]
    [hickory.core          :as hickory]
    [blog.common.components :as comps]
    [blog.common.files :refer [posts-dir posts-out-dir spit-with-path]]
    [blog.common.markdown :refer [add-prism-class md->html]]))

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

(defn post-page-hiccup
  [{:keys [toc
           body
           read-time
           metadata
           include-toc
           include-read-time],
    :or   {include-toc       true,
           include-read-time true}}]
  (let [;; Infrastructure for TOC is kept here (parsed from Pandoc output)
        parsed-toc  (hickory/parse-fragment toc)
        parsed-body (hickory/parse-fragment body)
        hiccup-toc  (->> parsed-toc
                         (map hickory/as-hiccup)
                         first
                         vec)
        ;; Getting the design right for the table of contents was difficult
        ;; I decided to exclude it from rendering but leave available
        ;;  for potential future use.
        _ hiccup-toc
        _ include-toc
        hiccup-body (->> parsed-body
                         (map hickory/as-hiccup)
                         add-prism-class)
        {:keys [title date updated]} metadata]
    [:html
     (comps/head {:prism true, :mermaid true, :title title})
     (comps/body
[:main.max-w-2xl.mx-auto.px-4
       ;; TOC Sidebar - Navigation Landmark
       ;; Fixed to the bottom-left of the screen
       (when include-toc
         [:nav {:class "hidden xl:block fixed bottom-8 left-8 w-64 max-h-[70vh] overflow-y-auto pr-4"
                :aria-label "Table of Contents"}
          [:div.flex.flex-col.justify-end.h-full
           [:p.text-xs.uppercase.tracking-wide.text-text-secondary.mb-2 "Contents"]
           hiccup-toc]])

       [:article.min-w-0
         [:header.mb-12
          [:h1.text-3xl.font-bold.mb-2 title]
          [:div.flex.flex-col.gap-1.text-sm.text-text-secondary
           (when date
             [:span "Published: " date])
           (when updated
             [:span "Updated: " updated])
           (when include-read-time
             [:span (str read-time " min read")])]]
         [:div hiccup-body]]])
     ;; render mermaid diagrams
     [:script {:type "module"}
      "import mermaid from 'https://cdn.jsdelivr.net/npm/mermaid@10/dist/mermaid.esm.min.mjs';"]]))

(defn get-post-name
  "Extracts the post name from a markdown file path (without extension)."
  [md-file]
  (-> md-file
      (java.io.File.)
      (.getName)
      (clojure.string/replace #"\.md$" "")))

(defn gen-post
  [md-file out-dir]
  (let [post           (->> md-file
                            md->html
                            post-page-hiccup
                            html)
        post-name      (get-post-name md-file)
        post-file-path (str out-dir "/" post-name "/index.html")]
    (spit-with-path post-file-path post)))

(defn gen
  []
  (-> "Generating posts..."
      c/blue
      println)
  (-> posts-out-dir
      io/file
      .mkdirs)
  (let [md-files (->> posts-dir
                      io/file
                      file-seq
                      (filter #(re-matches #".*\.md" (.getName %)))
                      (map #(.getCanonicalPath %)))]
    (->> md-files
         (pmap (fn [md-file]
                 (-> "  Generating html for  "
                     (str md-file)
                     c/blue
                     println)
                 (gen-post md-file posts-out-dir)))
         doall))
  (-> "Done!"
      c/blue
      println))

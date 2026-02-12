(ns org-blog.pages.snapshots
  (:require
   [clojure.java.io :as io]
   [clojure.term.colors :as c]
   [hiccup.core :refer [html]]
   [hickory.core :as hickory]
   [org-blog.common.components :as comps]
   [org-blog.common.files :refer [posts-dir spit-with-path]]
   [org-blog.common.markdown :refer [extract-metadata md->html add-prism-class]]
   [org-blog.posts :as posts]))

(def page-size 5)

(defn get-snapshot-posts []
  (->> posts-dir
       io/file
       file-seq
       (filter #(re-matches #".*\.md" (.getName %)))
       (keep (fn [file]
               (let [md-file    (str (.getCanonicalPath file))
                     md-content (slurp md-file)
                     metadata   (extract-metadata md-content)]
                 (when (some #{"snapshot"} (:tags metadata))
                   {:file md-file
                    :metadata metadata
                    :post-name (posts/get-post-name md-file)}))))
       (sort-by #(-> % :metadata :date) #(compare %2 %1))))

(defn render-snapshot-item [post]
  (println "Rendering snapshot:" (:file post))
  (let [result (md->html (:file post))]
    (if (nil? result)
      (do
        (println "ERROR: md->html returned nil for" (:file post))
        [:div.error "Error rendering content"])
      (let [body (-> result :body hickory/parse-fragment (->> (map hickory/as-hiccup)) add-prism-class)
            {:keys [title date]} (:metadata post)
            permalink (str "/posts/" (:post-name post))]
        [:article.mb-16.border-b.border-border.pb-12
         [:header.mb-6
          [:h2.text-2xl.font-semibold.mb-2
           [:a.no-underline.text-text.hover:text-accent {:href permalink} title]]
          [:div.text-sm.text-text-secondary date]]
         [:div.prose body]]))))
(defn render-next-trigger [next-page-idx]
  (when next-page-idx
    [:div.py-8.text-center.text-text-secondary.italic.w-full
     {:hx-get (str "/snapshots/page/" next-page-idx "/")
      :hx-trigger "revealed"
      :hx-select "#feed-stream > *"
      :hx-swap "outerHTML"}
     "Loading more..."]))

(defn render-page [batches idx]
  (let [page-num (inc idx)
        is-first-page? (= page-num 1)
        next-page-num (when (< page-num (count batches)) (inc page-num))
        batch (nth batches idx)
        content [:div#feed-stream
                 (map render-snapshot-item batch)
                 (render-next-trigger next-page-num)]
        path (if is-first-page?
               "./static/snapshots/index.html"
               (str "./static/snapshots/page/" page-num "/index.html"))]
    (-> [:html {:lang "en"}
         (comps/head {:title (str "Project Snapshots" (when-not is-first-page? (str " - Page " page-num))) 
                      :prism true})
         (comps/body
          [:script {:src "https://unpkg.com/htmx.org@1.9.10"}]
          [:main.max-w-2xl.mx-auto.px-4
           [:header.mb-12
            [:h1.text-3xl.font-bold "Stream"]
            [:p.text-text-secondary.mt-2 "A feed of project snapshots, notes, and visual updates."]]

           content])]
        html
        (->> (spit-with-path path)))))

(defn gen []
  (-> "Generating snapshot stream..." c/blue println)
  (let [all-snapshots (get-snapshot-posts)
        batches       (partition-all page-size all-snapshots)]
    (if (seq batches)
      (dotimes [idx (count batches)]
        (render-page batches idx))
      ;; If no snapshots, render empty page 1
      (render-page [[]] 0))))

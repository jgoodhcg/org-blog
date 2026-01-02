(ns org-blog.common.markdown
  (:require
   [clojure.java.shell :as shell]
   [clojure.string :as str]
   [clojure.walk :as walk]
   [org-blog.common.files :refer [full-path]]))

(defn add-prism-class
  "Adds Prism.js language classes to code blocks for syntax highlighting."
  [hiccup]
  (walk/postwalk
   (fn [node]
     (if (and (vector? node)
              (= :code (first node)))
       (let [class      (->> (second node)
                             :class)
             lang       (when (not (str/blank? class))
                          (->> class
                               (re-seq #"(?<=sourceCode\s)\w+")
                               first))
             lang-class (str "language-" lang)]
         (-> node
             (update 1 #(assoc % :class lang-class))))
       node))
   hiccup))

(defn count-words [text]
  (count (re-seq #"\b\w+\b" text)))

(defn estimate-read-time
  "Estimates read time in minutes based on 250 words per minute."
  [text]
  (let [word-count (count-words text)]
    (int (Math/ceil (/ word-count 250.0)))))

(defn parse-yaml-frontmatter
  "Extracts YAML frontmatter from markdown content.
   Returns a map with :frontmatter (parsed) and :content (remaining markdown)."
  [md-content]
  (if (str/starts-with? md-content "---")
    (let [end-index (str/index-of md-content "---" 3)]
      (if end-index
        (let [yaml-str (subs md-content 3 end-index)
              content  (str/trim (subs md-content (+ end-index 3)))
              ;; Simple YAML parsing for key: value pairs
              pairs    (->> (str/split-lines yaml-str)
                            (map str/trim)
                            (filter #(not (str/blank? %)))
                            (map (fn [line]
                                   (let [[k & v] (str/split line #":\s*" 2)
                                         k       (keyword (str/trim k))
                                         v       (str/trim (str/join ": " v))
                                         ;; Handle array syntax [item1, item2]
                                         v       (if (and (str/starts-with? v "[")
                                                          (str/ends-with? v "]"))
                                                   (->> (subs v 1 (dec (count v)))
                                                        (#(str/split % #",\s*"))
                                                        (map str/trim)
                                                        vec)
                                                   ;; Remove quotes if present
                                                   (str/replace v #"^[\"']|[\"']$" ""))]
                                     [k v])))
                            (into {}))]
          {:frontmatter pairs
           :content     content})
        {:frontmatter {}
         :content     md-content}))
    {:frontmatter {}
     :content     md-content}))

(defn extract-metadata
  "Extracts metadata from markdown YAML frontmatter.
   Expected format:
   ---
   title: My Post Title
   date: 2024-01-15
   updated: 2024-01-20
   description: A description of the post
   thumbnail: /img/thumbnail.png
   tags: [tag1, tag2, tag3]
   ---"
  [md-content]
  (let [{:keys [frontmatter]} (parse-yaml-frontmatter md-content)]
    {:title       (:title frontmatter)
     :date        (:date frontmatter)
     :updated     (:updated frontmatter)
     :description (:description frontmatter)
     :thumbnail   (:thumbnail frontmatter)
     :tags        (:tags frontmatter)}))

(defn md->html
  "Converts markdown to HTML using pandoc.
   Requires pandoc installed locally."
  [md-file]
  (let [contents           (slurp md-file)
        {:keys [content]}  (parse-yaml-frontmatter contents)
        metadata           (extract-metadata contents)
        absolute-md-file   (full-path md-file)
        toc-template-path  (full-path "./src/org_blog/pandoc-template-toc.html")
        body-template-path (full-path "./src/org_blog/pandoc-template-body.html")
        ;; Use echo to pipe content without frontmatter to pandoc
        toc-cmd            (str "pandoc -f markdown -t html "
                                "--template=" toc-template-path " "
                                "--table-of-contents")
        body-cmd           (str "pandoc -f markdown -t html "
                                "--template=" body-template-path)
        toc-result         (shell/sh "sh" "-c" toc-cmd :in content)
        body-result        (shell/sh "sh" "-c" body-cmd :in content)]
    (if (and (zero? (:exit toc-result))
             (zero? (:exit body-result)))
      {:toc       (:out toc-result)
       :body      (:out body-result)
       :read-time (estimate-read-time contents)
       :metadata  metadata}
      (do (println (str "Error(s):" [(:err toc-result) (:err body-result)]))
          nil))))

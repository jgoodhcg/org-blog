(ns org-blog.common.org
(:require
 [clojure.java.shell :as shell]
 [clojure.string :refer [blank?]]
 [clojure.walk :as walk]
 [org-blog.common.files :refer [full-path]]
 [potpuri.core :as pot]))

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

(defn count-words [text]
  (count (re-seq #"\b\w+\b" text)))

(defn estimate-read-time [text]
  (let [word-count (count-words text)]
    (int (Math/ceil (/ word-count 250.0)))))

(defn org->html
  "Requires at least pandoc 3.1.2 installed locally"
  [org-file]

  (let [contents           (slurp org-file)
        absolute-org-file  (full-path org-file)
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
      {:toc       (:out toc-result)
       :body      (:out body-result)
       :read-time (estimate-read-time contents)}
      (do (println (str "Error(s):" [(:error toc-result) (:error body-result)]))
          nil))))

(defn extract-org-metadata [org-content]
  (let [title-pattern       #"\#\+title: (.+)"
        date-pattern        #"\#\+date:<(\d{4}-\d{2}-\d{2}).+>"
        description-pattern #"\#\+description: (.+)"
        thumbnail-pattern   #"\#\+thumbnail: (.+)"
        tags-pattern        #"\#\+tags: (.+)" ;; Pattern to match the tags line
        title               (second (re-find title-pattern org-content))
        date                (second (re-find date-pattern org-content))
        description         (second (re-find description-pattern org-content))
        thumbnail           (second (re-find thumbnail-pattern org-content))
        tags                (second (re-find tags-pattern org-content))
        tags-list           (when tags (clojure.string/split tags #",\s*"))] ;; Splitting tags by comma and optional whitespace

    (assoc (pot/map-of :title title :date date :description description :thumbnail thumbnail)
           :tags tags-list)))


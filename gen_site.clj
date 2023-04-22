#!/usr/bin/env bb

(require '[clojure.java.io :as io]
         '[clojure.string :as str]
         '[clojure.data.xml :as xml]
         '[babashka.process :as process])

(defn org-to-html [input-file output-file]
  (process/run ["emacs" "--batch"
                "--eval" "(require 'org)"
                "--eval" "(require 'ox-html)"
                "-f" "org-html-export-to-html"
                "--file" input-file
                "--output" output-file]))

(defn org-files [dir]
  (filter #(str/ends-with? (.getName %) ".org")
          (file-seq (io/file dir))))

(defn output-path [input-dir output-dir path]
  (-> path
      (str/replace input-dir output-dir)
      (str/replace ".org" ".html")))

(defn generate-site [input-dir output-dir]
  (doseq [org-file (org-files input-dir)]
    (let [output-file (output-path input-dir output-dir (str org-file))]
      (io/make-parents output-file)
      (org-to-html (str org-file) output-file))))

(defn generate-rss-item [path]
  {:tag :item
   :content [{:tag :title, :content [(subs (str path) 0 (count (str path)))]}
             {:tag :link, :content [(str "https://example.com/" (subs (str path) 0 (count (str path))))]}
             {:tag :guid, :content [(str "https://example.com/" (subs (str path) 0 (count (str path))))]}]})

(defn generate-rss [output-dir org-files]
  (let [rss {:tag :rss
             :attrs {:version "2.0"}
             :content [{:tag :channel
                        :content (concat [{:tag :title, :content ["Org files"]}
                                          {:tag :link, :content ["https://example.com"]}
                                          {:tag :description, :content ["Generated from org files"]}]
                                         (map generate-rss-item org-files))}]}
        rss-file (io/file output-dir "rss.xml")]
    (with-open [out (io/writer rss-file)]
      (xml/emit rss out))))

(defn main [input-dir output-dir]
  (let [org-files (org-files input-dir)]
    (generate-site input-dir output-dir)
    (generate-rss output-dir org-files)))

(if (= (count *command-line-args*) 2)
  (main (first *command-line-args*) (second *command-line-args*))
  (println "Usage: org2html_site.clj <input-directory> <output-directory>"))

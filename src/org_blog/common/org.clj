(ns org-blog.common.org
(:require
 [clojure.java.shell :as shell]
 [clojure.string :refer [blank?]]
 [clojure.walk :as walk]
 [org-blog.common.files :refer [full-path]]))

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

(defn org->html
  "Requires at least pandoc 3.1.2"
  [org-file]

  (let [absolute-org-file  (full-path org-file)
        toc-template-path  (full-path "./src/org_blog/pandoc-template-toc.html")
        body-template-path (full-path "./src/org_blog/pandoc-template-body.html")
        toc-cmd            (str "pandoc -f org -t html "
                                    "--template=" toc-template-path " "
                                    "--table-of-contents " absolute-org-file)
        body-cmd           (str "pandoc -f org -t html "
                                #_"--atx-headers <(printf \"#+OPTIONS: H:6\") "
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

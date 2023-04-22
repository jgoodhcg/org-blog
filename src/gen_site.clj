(ns gen-site
  (:require
   [clojure.java.shell :as shell]
   [clojure.term.colors :as c]
   [hiccup.core :refer [html]]
   [hickory.core :as hickory]))

(defn ^:chatgpt org-to-html [org-file]
  (let [absolute-org-file (-> (java.io.File. org-file) (.getCanonicalFile))
        cmd (str "pandoc -f org -t html5 " absolute-org-file)
        {:keys [exit out err]} (shell/sh "sh" "-c" cmd)]
    (if (zero? exit)
      out
      (do (println "Error:" err)
          nil))))

(defn wrap-in-hiccup [html-string]
  (let [parsed-html (hickory/parse-fragment html-string)
        hiccup-form (->> parsed-html (map hickory/as-hiccup))]
    (html hiccup-form)))

(defn -main []
  (-> "Parsing file.. " c/blue println)
  (->> "./posts/hello-world.org"
       org-to-html
       #_wrap-in-hiccup
       println)
  (-> "Done!" c/blue println)
  (System/exit 0))

(comment

  )

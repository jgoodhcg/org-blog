(ns gen-site
  (:require
   [babashka.process :as p]
   [clojure.term.colors :as c]))

(defn ^:chatgpt org-to-html [org-file]
  (let [absolute-org-file (-> (java.io.File. org-file) (.getCanonicalFile))
        cmd (str "pandoc -f org -t html5 " absolute-org-file)
        {:keys [exit out err]} (p/shell {:out :string} cmd)]
    (if (zero? exit)
      out
      (do (println "Error:" err)
          nil))))

(defn -main []
  (-> "Parsing file.. " c/blue println)
  (-> "./posts/hello-world.org" org-to-html println)
  (-> "Done!" c/blue println)
  )

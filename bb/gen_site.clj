(ns gen-site
  (:require [babashka.curl :as curl]
            [babashka.cli :as cli]
            [clojure.term.colors :as c]
            [puget.printer :as puget]
            [babashka.process :as p]
            [cljc.java-time.instant :as instant]))

(defn ^:chatgpt org-to-html [org-file]
  (let [cmd (str "emacs --batch --eval "
                 "\"(progn"
                 " (require 'ox-html)"
                 " (with-current-buffer (find-file-noselect \\\"" org-file "\\\")"
                 "   (org-html-export-as-html)"
                 "   (princ (buffer-string))))\"")

        {:keys [exit out err]} (p/shell {:out :string} cmd)]
    (if (zero? exit)
      out
      (do (println "Error:" err)
          nil))))


(defn -main []
  (-> "Parsing file.. " c/blue println)
  (-> "./../posts/hello-world.org" org-to-html println)
  (-> "Done!" c/blue println)
  )

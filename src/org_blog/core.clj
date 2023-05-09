(ns org-blog.core
  (:require
   [clojure.java.io :as io]
   [clojure.java.shell :as shell]
   [clojure.string :refer [blank?] :as string]
   [clojure.term.colors :as c]
   [clojure.tools.namespace.repl :as ns-repl]
   [clojure.walk :as walk]
   [hiccup.core :refer [html]]
   [hiccup.page :refer [include-css]]
   [hickory.core :as hickory]
   [org-blog.dev-server :as dev-server]
   [org-blog.pages.home :refer [gen-home]]
   [org-blog.posts :refer [gen-posts]]
   )
  )

(defn -main [& args]
  (println "I don't do anything yet")
  #_(System/exit 0))

(defn regenerate-site []
  (gen-home)
  (gen-posts))

;; Start dev server
(when (nil? @dev-server/server-atom)
  (-> "starting server" c/blue println)
  (regenerate-site)
  (reset! dev-server/server-atom (dev-server/start-server)))

;; Start file watcher
(when (nil? @dev-server/source-watchers)
  (reset! dev-server/source-watchers
          (dev-server/watch-source-files
           ["src"]
           (fn [ctx e]
             (when (= (:kind e) :modify)
               (println "File modified:" (:file e))
               ;; Calling `ns-repl/refresh` in another thread (hawk must run this handler in a another thread)
               ;; generates an error
               ;; By wrapping in future, by some magic, the function calls within are scheduled on the main thread
               (future
                 (println "Refreshing repl ...")
                 (ns-repl/refresh)
                 (regenerate-site)))))))

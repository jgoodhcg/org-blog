(ns org-blog.core
  (:require
   [clojure.term.colors :as c]
   [clojure.tools.namespace.repl :as ns-repl]
   [org-blog.dev-server :as dev-server]
   [org-blog.pages.archive :as archive]
   [org-blog.pages.home :as home]
   [org-blog.pages.resume :as resume]
   [org-blog.posts :as posts]))

(defn -main [& args]
  (println "I don't do anything yet")
  #_(System/exit 0))

(defn regenerate-site []
  (home/gen)
  (archive/gen)
  (resume/gen)
  (posts/gen))

;; Start dev server
(when (nil? @dev-server/server-atom)
  (-> "starting server" c/blue println)
  (regenerate-site)
  (reset! dev-server/server-atom (dev-server/start-server)))

;; Start file watcher
(when (nil? @dev-server/source-watchers)
  (reset! dev-server/source-watchers
          (dev-server/watch-source-files
           ["src" "posts" "pages"]
           (fn [ctx e]
             (when (= (:kind e) :modify)
               (println "File modified:" (:file e))
               ;; Calling `ns-repl/refresh` in another thread (hawk must run this handler in a another thread)
               ;; generates an error
               ;; By wrapping in future, by some magic, the function calls within are scheduled on the main thread I guess
               (future
                 (try
                   (println "Refreshing repl ...")
                   (ns-repl/refresh)
                   (println "Ahhhh, so refreshed!")
                   (regenerate-site)
                   (catch Exception e
                     (when-not (and (instance? IllegalStateException e)
                                    ;; Not sure why this error happens but the repl refreshes when it's thrown so I guess it doesn't matter
                                    (.contains (.getMessage e) "Can't change/establish root binding of: *ns* with set"))
                       (println "Error refreshing repl:" e))))))))))

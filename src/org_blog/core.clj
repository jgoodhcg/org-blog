(ns org-blog.core
  (:require
   [clojure.term.colors :as c]
   [clojure.tools.namespace.repl :as ns-repl]
   [org-blog.dev-server :as dev-server]
   [org-blog.pages.archive :as archive]
   [org-blog.pages.home :as home]
   [org-blog.pages.resume :as resume]
   [org-blog.pages.now :as now]
   [org-blog.pages.rss :as rss]
   [org-blog.posts :as posts]))

(defn -main [& args]
  (println "I don't do anything yet")
  #_(System/exit 0))

(defn regenerate-site []
  (time
   (let [home-future    (future (home/gen))
         archive-future (future (archive/gen))
         resume-future  (future (resume/gen))
         now-future     (future (now/gen))
         rss-future     (future (rss/gen))
         posts-future   (future (posts/gen))]
     ;; ensure all futures are completed before moving forward
     @home-future
     @archive-future
     @resume-future
     @now-future
     @rss-future
     @posts-future)))

;; Start and restart the server at the repl
(comment
  (do
    (dev-server/stop-server)
    (ns-repl/refresh)
    (regenerate-site)
    (dev-server/start-server))
  ;;
  )
;; Start file watcher
;; TODO implement a m series compatible file watcher
#_
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

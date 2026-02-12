(ns org-blog.core
  (:require
   [clojure.tools.namespace.repl :as ns-repl]
   [org-blog.dev-server :as dev-server]
   [org-blog.pages.archive :as archive]
   [org-blog.pages.home :as home]
   [org-blog.pages.now :as now]
   [org-blog.pages.resume :as resume]
   [org-blog.pages.rss :as rss]
   [org-blog.pages.snapshots :as snapshots]
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
         snapshots-future (future (snapshots/gen))
         posts-future   (future (posts/gen))]
     ;; ensure all futures are completed before moving forward
     @home-future
     @archive-future
     @resume-future
     @now-future
     @rss-future
     @snapshots-future
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

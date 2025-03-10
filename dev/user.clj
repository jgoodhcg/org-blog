(ns user
  (:require
   [clojure.tools.namespace.repl :as ns-repl]
   [org-blog.core :as core]
   [org-blog.dev-server :as dev-server]))

(defn regenerate
  "Regenerate all site content"
  []
  (core/regenerate-site))

(defn start
  "Start the development server"
  []
  (dev-server/start-server)
  :started)

(defn stop
  "Stop the development server"
  []
  (dev-server/stop-server)
  :stopped)

(defn start-and-regenerate
  "Helper function for restart to ensure proper sequencing"
  []
  (regenerate)
  (start))

(defn restart
  "Refresh namespaces, regenerate site content, and restart the server"
  []
  (stop)
  ; Use the :after hook to ensure server starts only after successful refresh
  (ns-repl/refresh :after 'user/start-and-regenerate)
  :restarted)

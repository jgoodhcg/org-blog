(ns org-blog.dev-server
  (:require
   [clojure.java.io :as io]
   [clojure.string :refer [split] :as string]
   [hawk.core :as hawk]
   [org.httpkit.server :as http-kit]))

(def content-types
  {"html" "text/html"
   "css"  "text/css"
   "js"   "application/javascript"
   "svg"  "image/svg+xml"
   ;; Add other content types as needed
   })

(defn content-type-for [filename]
  (let [ext (->> (split filename #"\.")
                 last
                 (get content-types))]
    (or ext "text/plain")))

(defn handler [req]
  (let [resource-path (str "static" (:uri req))
        file (io/file resource-path)]
    (if (.exists file)
      (if (.isDirectory file)
        {:status  200
         :headers {"Content-Type" "text/html"}
         :body    (slurp (io/file (str resource-path "/index.html")))}
        {:status  200
         :headers {"Content-Type" (content-type-for resource-path)}
         :body    (slurp file)})
      {:status  404
       :headers {"Content-Type" "text/plain"}
       :body    "Not Found"})))

(defn start-server []
  (http-kit/run-server handler {:port 8080}))

(defonce server-atom (atom nil))

(defn watch-source-files [dirs handler]
  (hawk/watch! [{:paths   dirs
                 :handler handler}]))

(defonce source-watchers (atom nil))

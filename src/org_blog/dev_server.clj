(ns org-blog.dev-server
  (:require
   [clojure.java.io :as io]
   [clojure.string :refer [split] :as string]
   [potpuri.core :as pot]
   [hawk.core :as hawk]
   [org.httpkit.server :as http-kit]))

(def content-types
  {"html" "text/html"
   "css"  "text/css"
   "js"   "application/javascript"
   "svg"  "image/svg+xml"
   "xml"  "application/xml"
   "jpg"  "image/jpeg"
   "png"  "image/png"
   "gif"  "image/gif"
   })

(defn content-type-for [filename]
  (let [ext (->> (split filename #"\.")
                 last
                 (get content-types))]
    (or ext "text/plain")))

(defn handler [req]
  (let [resource-path (str "static" (:uri req))
        file (io/file resource-path)
        ext  (->> (split resource-path #"\.")
                  last)]
    (if (.exists file)
      (if (.isDirectory file) ;; This is to support urls that don't end in .html
        {:status  200
         :headers {"Content-Type" "text/html"}
         :body    (slurp (io/file (str resource-path "/index.html")))}
        {:status  200
         :headers {"Content-Type" (content-type-for resource-path)}
         :body    (if (#{"jpg" "png" "gif"} ext)
                    (io/input-stream file)
                    (slurp file))})
      {:status  404
       :headers {"Content-Type" "text/plain"}
       :body    "Not Found"})))

(defn start-server []
  (http-kit/run-server handler {:port 8081}))

(defonce server-atom (atom nil))

(defn watch-source-files [dirs handler]
  (hawk/watch! [{:paths   dirs
                 :handler handler}]))

(defonce source-watchers (atom nil))

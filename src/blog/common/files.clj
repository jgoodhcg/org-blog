(ns blog.common.files
  (:require
   [clojure.java.io :as io]))

(def pages-dir "./pages")

(def posts-dir "./posts")

(def posts-out-dir "./static/posts")

(defn full-path [rel-path]
  (-> (java.io.File. rel-path) (.getCanonicalFile)))

(defn ensure-directories-exist [path]
  (let [parent (-> path io/file .getParentFile)]
    (when-not (.exists parent)
      (.mkdirs parent))))

(defn spit-with-path [path content]
  (ensure-directories-exist path)
  (spit path content))

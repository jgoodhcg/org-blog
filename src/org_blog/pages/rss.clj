(ns org-blog.pages.rss
  (:require
   [clojure.data.xml :as xml]
   [clojure.java.io :as io]
   [clojure.string :as str]
   [org-blog.common.files :refer [posts-org-dir spit-with-path]]
   [org-blog.common.org :refer [extract-org-metadata]]
   [org-blog.posts :as posts]
   [potpuri.core :as pot]
   [clojure.pprint :refer [pprint]]))

(import '[java.time LocalDate ZoneId ZonedDateTime]
        '[java.time.format DateTimeFormatter]
        '[java.util Locale])

(def rfc822-formatter
  (DateTimeFormatter/ofPattern "EEE, dd MMM yyyy HH:mm:ss Z" Locale/US))

(def base-url "http://jgood.online")

(defn format-pub-date [date-str]
  (let [ld   (LocalDate/parse date-str)
        zdt  (.atStartOfDay ld (ZoneId/of "UTC"))]
    (.format rfc822-formatter zdt)))

(defn extract-date-from-name [post-name]
  (second (re-find #"\d{4}-\d{2}-\d{2}" post-name)))

(defn create-rss-item [org-file]
  (let [post-name  (posts/get-org-file-name org-file)
        org-content (slurp org-file)
        metadata   (extract-org-metadata org-content)
        title      (or (:title metadata) post-name)
        description (:description metadata)
        link       (str base-url "/posts/" post-name)
        pub-date   (when-let [d (or (:date metadata)
                                    (extract-date-from-name post-name))]
                     (format-pub-date d))]
    (xml/element :item {}
                 (xml/element :title {} title)
                 (xml/element :link {} link)
                 (when description
                   (xml/element :description {} description))
                 (when pub-date
                   (xml/element :pubDate {} pub-date))
                 (xml/element :guid {} link))))

(defn create-rss-feed [items]
  (xml/element :rss {:version "2.0"}
               (xml/element :channel {}
                            (xml/element :title {} "Justin Good's Blog")
                            (xml/element :link {} "http://jgood.online")
                            (xml/element :description {} "Still trying to figure out what to write about")
                            items)))

;; Generate RSS feed for all posts with description, pubDate, guid, and XSLT styling
(defn gen []
  (println "Generating RSS feed")
  (let [items (->> posts-org-dir
                   io/file
                   file-seq
                   (filter #(re-matches #".*\.org" (.getName %)))
                   (sort)
                   (map #(.getCanonicalPath %))
                   (map create-rss-item))
        rss-feed (create-rss-feed items)
        xml-str  (xml/indent-str rss-feed)
        xml-str  (str/replace-first xml-str
                                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                                    (str "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                                         "<?xml-stylesheet type=\"text/xsl\" href=\"/rss.xsl\"?>"))]
    (spit-with-path "./static/rss.xml" xml-str)))

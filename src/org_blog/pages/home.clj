(ns org-blog.pages.home
  (:require
   [clojure.java.io :as io]
   [clojure.term.colors :as c]
   [hiccup.core :refer [html]]
   [org-blog.common.components :as comps]
   [org-blog.common.files :refer [posts-dir spit-with-path]]
   [org-blog.common.markdown :refer [estimate-read-time extract-metadata]]
   [org-blog.posts :as posts]))

(defn gen []
  (-> "Generating home (index) page" c/blue println)
  (-> [:html {:lang "en"}
       (comps/head)
       (comps/body
        [:main.max-w-2xl.mx-auto.px-4
         [:section.mb-12
          [:p.text-lg "Software engineer. Writing about code, tools, and whatever else comes to mind."]
          [:p.text-text-secondary.text-sm.mt-4
           "See what I'm up to " [:a {:href "/now"} "now"] ", "
           "browse the " [:a {:href "/archive"} "archive"] ", "
           "or check out my " [:a {:href "/resume"} "resume"] "."]]

         [:section
          [:h2.text-lg.font-semibold.mb-6 "Recent Posts"]
          [:div.space-y-8
           (->> posts-dir
                io/file
                file-seq
                (filter #(re-matches #".*\.md" (.getName %)))
                (sort)
                (reverse)
                (keep (fn [file]
                        (let [md-file    (str (.getCanonicalPath file))
                              md-content (slurp md-file)
                              metadata   (extract-metadata md-content)]
                          (when-not (some #{"snapshot"} (:tags metadata))
                            {:file md-file
                             :metadata metadata
                             :read-time (estimate-read-time md-content)
                             :post-name (posts/get-post-name md-file)}))))
                (take 8)
                (map (fn [{:keys [metadata read-time post-name]}]
                         [:article
                          [:a.no-underline.block.group {:href (str "/posts/" post-name)}
                           [:h3.text-base.font-medium.text-text.group-hover:text-accent.mb-1
                            (:title metadata)]
                           (when (:description metadata)
                             [:p.text-text-secondary.text-sm.mb-1 (:description metadata)])
                           [:p.text-text-secondary.text-xs
                            (:date metadata)
                            (when read-time (str " · " read-time " min read"))]]])))]]])]
      html
      (->> (spit-with-path "./static/index.html"))))

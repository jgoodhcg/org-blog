(ns org-blog.pages.resume
  (:require
   [clojure.java.io :as io]
   [clojure.term.colors :as c]
   [clojure.string :as str]
   [hiccup.core :refer [html]]
   [clojure.edn :as edn]
   [org-blog.common.components :as comps]
   [org-blog.common.files :refer [spit-with-path]]))

(defn read-resume-edn []
  (-> "./pages/resume.edn"
      io/file
      slurp
      edn/read-string))

(defn date-formatter [date-str]
  (if (= date-str "Present")
    "Present"
    (when date-str
      (let [[year month] (clojure.string/split date-str #"-")]
        (when (and year month)
          (str (case month
                 "01" "January"
                 "02" "February"
                 "03" "March"
                 "04" "April"
                 "05" "May"
                 "06" "June"
                 "07" "July"
                 "08" "August"
                 "09" "September"
                 "10" "October"
                 "11" "November"
                 "12" "December") " " year))))))

(defn format-date-range [start-date end-date]
  (str (date-formatter start-date) " – " (date-formatter end-date)))

(defn work-experience-section [work]
  [:section.my-10
   [:h2.text-xl.font-semibold.mb-6 "Experience"]
   (for [job work]
     [:div.mb-8
      [:div.flex.flex-col.sm:flex-row.sm:justify-between.sm:items-baseline.mb-2
       [:h3.text-base.font-medium.mb-0
        (:position job) " at " [:span.text-accent (:company job)]]
       [:span.text-sm.text-text-secondary (format-date-range (:startDate job) (:endDate job))]]
      (when (seq (:pastPositions job))
        [:div.text-xs.text-text-secondary
         [:span.font-medium "Previously: "]
         (str/join "; "
                   (for [position (:pastPositions job)]
                     (str (:position position)
                          " ("
                          (format-date-range (:startDate position) (:endDate position))
                          ")")))])
      [:ul.mt-2.text-sm
       (for [highlight (:highlights job)]
         [:li.mb-1 highlight])]])])

(defn projects-section [projects]
  [:section.my-10
   [:h2.text-xl.font-semibold.mb-6 "Projects"]
   (for [project projects]
     [:div.mb-6
      [:h3.text-base.font-medium.mb-1 (:name project)]
      [:p.text-sm.text-text-secondary.mb-2 (:description project)]
      [:p.text-xs.text-text-secondary
       [:span.font-medium "Technologies: "]
       (str/join ", " (:technologies project))]
      (when (or (:details project) (:achievements project))
        [:ul.mt-2.text-sm
         (for [detail (or (:details project) (:achievements project))]
           [:li.mb-1 detail])])])])

(defn education-section [education]
  [:section.my-10
   [:h2.text-xl.font-semibold.mb-4 "Education"]
   (for [edu education]
     [:div.mb-4
      [:h3.text-base.font-medium.mb-0
       (:studyType edu) " in " (:area edu)]
      [:p.text-sm.text-text-secondary
       (:institution edu) " · " (:startDate edu) "–" (:endDate edu)]])])

(defn resume-hiccup [resume]
  (let [{:keys [basics work projects education]} resume]
    [:html
     (comps/head {:title "Resume - Justin Good"})
     (comps/body
       [:main.max-w-2xl.mx-auto.px-4
        [:section.mb-10
         [:h1.text-2xl.font-semibold.mb-2 (:name basics)]
         [:p.text-lg.text-text-secondary.mb-4 (:label basics)]
         [:p.text-sm (:summary basics)]]

        (work-experience-section work)
        (projects-section projects)
        (education-section education)])]))

(defn gen []
  (-> "Generating resume page" c/blue println)
  (let [resume (read-resume-edn)]
    (->> resume
         resume-hiccup
         html
         (spit-with-path "./static/resume/index.html"))))

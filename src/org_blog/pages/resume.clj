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

(defn skills-section [skills]
  [:section.skills.my-8
   [:h2 "Skills"]
   [:div.grid.grid-cols-2.md:grid-cols-3.lg:grid-cols-4.gap-4
    (for [skill skills]
      [:div.rounded.p-3
       [:h3.text-white.mb-2.border-b.border-gray-700.pb-1 (:name skill)]
       [:div.flex.flex-wrap.gap-1
        (for [keyword (:keywords skill)]
          [:span.inline-block.text-white.text-sm.py-1 keyword
           (when-not (= keyword (last (:keywords skill)))
             [:span.mx-1.text-cyan "•"])])]])]])

(defn work-experience-section [work]
  [:section.experience.my-8
   [:h2 "Experience"]
   (for [job work]
     [:div.job.mb-8
      [:div.flex.flex-col.md:flex-row.justify-between.items-baseline
       [:h3.mb-0 (:position job)", " [:span.text-cyan (:company job)]]
       [:div.text-white-900.italic (format-date-range (:startDate job) (:endDate job))]]
      [:ul.mt-4
       (for [highlight (:highlights job)]
         [:li.mb-2 highlight])]])])

(defn projects-section [projects]
  [:section.projects.my-8
   [:h2 "Projects"]
   (for [project projects]
     [:div.project.mb-8
      [:h3.mb-0 (:name project)]
      [:p.mb-2 (:description project)]
      [:div.mb-3
       [:span.text-cyan.font-bold "Technologies: "]
       [:span.text-white (str/join ", " (:technologies project))]]
      (when (or (:details project) (:achievements project))
        [:ul.mt-2
         (for [detail (or (:details project) (:achievements project))]
           [:li.mb-2 detail])])])])

(defn education-section [education]
  [:section.education.my-8
   [:h2 "Education"]
   (for [edu education]
     [:div.education-item.mb-4
      [:h3.mb-0
       (:studyType edu) " in " (:area edu) ", "
       [:span.text-cyan  (:institution edu)]]
      [:div.text-white-900.italic (str (:startDate edu) " - " (:endDate edu))]])])

(defn resume-hiccup [resume]
  (let [{:keys [basics skills work projects education]} resume]
    [:html
     (comps/head {:title "Resume - Justin Good"})
     (comps/body
       [:div.container.mx-auto.px-4.py-8.max-w-4xl
        [:div.p-6
         [:section.header.mb-8
          [:h1 (:name basics)]
          [:p.text-xl.text-white (:label basics)]
          [:div.my-6.p-4
           [:p.text-white.italic (:summary basics)]]]

         (skills-section skills)
         (work-experience-section work)
         (projects-section projects)
         (education-section education)]])]))

(defn gen []
  (-> "Generating resume page" c/blue println)
  (let [resume (read-resume-edn)]
    (->> resume
         resume-hiccup
         html
         (spit-with-path "./static/resume/index.html"))))

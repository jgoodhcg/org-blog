(ns org-blog.common.components)

(defn nav []
  [:div.lcars-top-border.lcars-border-green.pl-8.md:pl-40
   [:div.p-4.rounded-bl-lg.bg-black
    [:h1.text-4xl.font-bold.mb-2.bg-clip-text.text-transparent.bg-gradient-to-b.from-green-100.to-cyan-100
     "JGood Blog"]
    [:nav
     [:a.text-xl.font-bold.m-2 {:href "/"} "Home"]
     [:a.text-xl.font-bold.m-2 {:href "/"} "Archive"]
     [:a.text-xl.font-bold.m-2 {:href "/"} "Resume"]]]])

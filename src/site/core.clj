(ns site.core
  (:require [hiccup.core :as h]
            [hiccup.page :as hp]))

(defn page [data]
  (hp/html5
   [:head
   [:title "Boo"]]
   [:body
   [:div {:style "max-width: 900px; margin: 40px auto;"}
   (-> data :entry :content)]]))

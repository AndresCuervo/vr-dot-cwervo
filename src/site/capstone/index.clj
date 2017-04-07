(ns site.capstone.index
  (:use [hiccup.core :only (html)]
        [hiccup.page :only (html5)]
        [clojure.string :as string]))

(defn render [{global-meta :meta posts :entries}]
  (html
    [:head
      [:title (:site-title global-meta)]
      [:meta {:charset "utf-8"}]
      [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0, user-scalable=no"}]
      [:link {:rel "stylesheet"
              :href "/css/main.css"}]
      [:script {:src "https://aframe.io/releases/0.5.0/aframe.min.js"}]]
    [:body
     [:div.page ;; needs to return a single element, so this wraps it !
     [:div#wrapper.center
      [:section
       [:span
        "Oh, hello there! :)"]
       [:p
        "Here are some links to other VR things I've done:"
        [:ul.vr_list
         [:li
          [:a {:href "/#/vr-capstone"}
           "My VR CRWR capstone"]]
         [:li
          [:a {:href "/#/about"}
           "about"]]]]]]
     [:a-scene.background-scene
      [:a-sky {:src "/images/panoramas/abandoned-room.jpg"}]]]]))

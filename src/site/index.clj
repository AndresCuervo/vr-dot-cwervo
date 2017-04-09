(ns site.index
  (:use [hiccup.core :only (html)]
        [hiccup.page :only (html5)]
        [clojure.string :as str]))

(defn render__defunct [{global-meta :meta posts :entries}]
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
           [:a {:href "/homepage.html"}
            "Homepage (update this to actually take over the homepage lol,
            (e.g. get rid of the index.md) and it won't be so bad to write a new task for each new page,
            that feels like over optimization @ this point, so just go for it!)"]]
          [:li
           [:a {:href "/vr-capstone.html"} ;; turn this into an index, inside a scenes directory! Gotta use sitemap generator probs?
            "My VR CRWR capstone"]]
          [:li
           [:a {:href "/about/index.html"}
            "about"]]]]]]
      [:a-scene.background-scene
       [:a-sky {:src "/images/panoramas/abandoned-room.jpg"}]]]]))

;; This might take over for the main index page, because that's effectively all its doing?
;; Collecting the previous page, I mean.
(defn render [{global-meta :meta entries :entries}]
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
          (for [entry entries]
        (when (and (contains? entry :title)
                   ;; Wow, I can add my own metadata keys, so fun!
                   (not (:draft? entry)))
          [:li
           [:a {:href (:permalink entry)}
           (:title entry)]]))]]]]
      [:a-scene.background-scene
       [:a-sky {:src "/images/panoramas/abandoned-room.jpg"}]]]]))



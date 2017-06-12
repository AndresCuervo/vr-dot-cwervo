(ns site.vive.base
  (:use [hiccup.core :only (html)]
        [hiccup.page :only (html5)]
        [clojure.string :as string]
        [site.capstone.index]))

;; Empty for now
(def vive-srcs ["//cdnjs.cloudflare.com/ajax/libs/annyang/2.5.0/annyang.min.js"
                "/js/utils/aframe-speech-command-component.min.js"])

(defn controls-test [{global-meta :meta
                      entries :entries}]
  (html
    [:head
     [:title "Vive Controls Test"]
     [:meta {:charset "utf-8"}]
     [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
     [:script {:src "https://aframe.io/releases/0.5.0/aframe.min.js"}]
     (for [src vive-srcs]
       [:script {:src src}])]
    [:body
     [:a-scene
      [:a-entity {:id "annyang"
                  :annyang-speech-recognition ""}]
      [:a-entity {:hand-controls "left"}]
      [:a-entity {:hand-controls "right"}]

      [:a-plane {:position "2 0 -4" :rotation "45 0 0" :width "4" :height "4" :color "#7BC8A4"
                 :speech-command__show "command: show menu; type: attribute; attribute: visible; value: true;",
                 :speech-command__hide "command: hide menu; type: attribute; attribute: visible; value: false;"}]

      ;; Normal hello world stuff:
      [:a-box#box {:position "-1 0.5 -3" :rotation "0 45 0" :color "#4CC3D9"
                   ;; Should totally turn this into a mixin that just takes the commend attribute as a param and passes it through!
                   :speech-command__show "command: show box; type: attribute; attribute: visible; value: true;",
                   :speech-command__hide "command: hide box; type: attribute; attribute: visible; value: false;"}]
      [:a-sphere {:position "0 1.25 -5" :radius "1.25" :color "#EF2D5E"}]
      [:a-cylinder {:position "1 0.75 -3" :radius "0.5" :height "1.5" :color "#FFC65D"}]
      [:a-plane {:position "0 0 -4" :rotation "-90 0 0" :width "4" :height "4" :color "#7BC8A4"}]
      [:a-sky {:color "#ECECEC"}]]]))

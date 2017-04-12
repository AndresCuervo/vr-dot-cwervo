(ns site.capstone.index
  (:use [hiccup.core :only (html)]
        [hiccup.page :only (html5)]
        [clojure.string :as string]))

(def srcs
  [;; Here be dragons.
   ;;
   ;; JK, here are all the extenal scripts, maybe they'll move to CLJSJS compilation
   ;; some day, if I can muster it.
   "https://rawgit.com/ngokevin/aframe-animation-component/master/dist/aframe-animation-component.min.js"
   ;; Custom scripts to be ported over:
   ;; [:script {:src"/a-frame-js/click-component.js"}]
   ;; [:script {:src"/a-frame-js/specifyPosition.js"}]
   ;; <!-- [:script {:src"/a-frame-js/tree-geom.js"}] -->
   ;; <!-- Working with .ply models -->
   "https://rawgit.com/donmccurdy/aframe-extras/v2.1.1/dist/aframe-extras.loaders.min.js"
   ;; <!-- Entity generator -->
   "https://unpkg.com/aframe-entity-generator-component@^3.0.0/dist/aframe-entity-generator-component.min.js"
   "https://unpkg.com/aframe-randomizer-components@^3.0.1/dist/aframe-randomizer-components.min.js"
   "https://unpkg.com/aframe-layout-component@4.0.1/dist/aframe-layout-component.min.js"
   "https://unpkg.com/aframe-template-component@^3.1.1/dist/aframe-template-component.min.js"
   ])

(defn make-box-row [n result]
  ;; Ohh, you can do trees like this, in raw html, maybe?
(let [size (* 0.1 n)
      pos (string/join
                  " "
                  [(+ -2 n 1) (+ 0.5 (Math/cos n)) -3])]
  (if (> n 0)
    (make-box-row (dec n) (list #_[:a-cone {:color "tomato"
                                            :radius-bottom "2"
                                            :radius-top size
                                            :position pos}]
                                [:a-box
                                 {:color "#4CC3D9"
                                  :depth size
                                  :height size
                                  :width size
                                  :rotation "0 45 0"
                                  :animation__rotate_lol (str
                                                           "property: rotation;
                                                           from: 0 45 0;
                                                           loop: true;
                                                           easing: linear;
                                                           to: 0 " (+ 360 (*  45 (Math/cos n))) " 0;")
                                  :position pos}] result))
    result)))

(defn render [{global-meta :meta
               entries :entries}]
  (html
    [:head
     [:title (:site-title global-meta)]
     [:meta {:charset "utf-8"}]
     [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
     [:script {:src "https://aframe.io/releases/0.5.0/aframe.min.js"}]
     (for [src srcs]
       [:script {:src src}])]
    [:body
     [:a-scene.whatever
      [:a-sky {:src "/images/panoramas/manic-night/archway.jpg" :rotation "0 -130 0"}]
      [:a-cone {:color "#2EAFAC"
                :position "-2 0 -4"

                :radius-bottom "2" :radius-top "0"
                :animation__pointy "property: radius-top;
                                   loop: true;
                                   easing: linear;
                                   from: 0; to: 3;
                                   dir: alternate;
                                   dur: 1000;"
                :data-radius-bottom "2"}]
      (make-box-row 20 '())

      ;; Sun???
      (for [rotation ["25 0 10" "25 15 15"]]
        [:a-circle.sun
         {:geometry "radius: 10; segments: 30"
          :material "color: #F79F24"
          :position "0  15 -12"
          :rotation rotation
          :animation__segs "property: geometry.segments;
                           loop: true;
                           easing: linear;
                           from: 30; to: 3;
                           dir: alternate;
                           dur: 3000;"
          }])

      (let [wireframe false]
        [:a-entity#egg-whites
         {:geometry "primitive: circle; radius: 1;"
          :material (str "color: white;"
                         "wireframe: " wireframe ";")
          :position "0  2 -3.5"}
         [:a-entity#egg-yolk
          {:geometry "primitive: sphere; radius: 0.5;"
          :material (str "color: yellow;"
                         "wireframe: " wireframe ";")
           :position "-0.3  0.2 -0.2"
           }]])

      #_[:a-entity#sun
       {:geometry "primitive: circle; radius: 10; segments: 30"
        :material "color: #F79F24"
        :position "0 15 -12"
        :rotation "25 0 10"
        :animation__segs "property: geometry.segments;
                         loop: true;
                         easing: linear;
                         from: 30; to: 3;
                         dir: alternate;
                         dur: 3000;"}]

      ;; Camera
      [:a-camera
       [:a-cursor {:scale "1.5 1.5 1.5"
                   :geometry "primitive: ring"
                   :material "color: #FFC0CB;
                             shader: flat"} ]]
      ]]))

(defn render-squiggles [{global-meta :meta
               entries :entries}]
  (html
    [:head
     [:title (:site-title global-meta)]
     [:meta {:charset "utf-8"}]
     [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0, user-scalable=no"}]
     [:script {:src "https://aframe.io/releases/0.5.0/aframe.min.js"}]
     ;; (for [src srcs]
     ;;   [:script {:src src}])
     ]
    [:body
     [:a-scene.whatever
      [:a-sky {:src "/images/panoramas/manic-night/archway.jpg" :rotation "0 -130 0"}]
      [:a-cone {:color "#2EAFAC"
                :position "-2 0 -4"

                :radius-bottom "2"
                :radius-top "0.5"
                :data-radius-bottom "2"}]
      (make-box-row 20 '())]]))

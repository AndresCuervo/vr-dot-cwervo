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
   "/js/a-frame-js/click-component.js"
   "/js/a-frame-js/specifyPosition.js"
   "/js/a-frame-js/randomDataPos.js"
   "/js/a-frame-js/changeOnLook.js"
   ;; <!-- Working with .ply models -->
   "https://rawgit.com/donmccurdy/aframe-extras/v2.1.1/dist/aframe-extras.loaders.min.js"
   ;; <!-- Entity generator -->
   "https://unpkg.com/aframe-entity-generator-component@^3.0.0/dist/aframe-entity-generator-component.min.js"
   "https://unpkg.com/aframe-randomizer-components@^3.0.1/dist/aframe-randomizer-components.min.js"
   "https://unpkg.com/aframe-layout-component@4.0.1/dist/aframe-layout-component.min.js"
   "https://unpkg.com/aframe-template-component@^3.1.1/dist/aframe-template-component.min.js"
   ])

(def head-el
  [:head
   [:title "VR Capstone"]
   [:meta {:charset "utf-8"}]
   [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
   [:script {:src "https://aframe.io/releases/0.5.0/aframe.min.js"}]
   (for [src srcs]
     [:script {:src src}])])

(def a-assets-el
  [:a-assets
   ;; [:img {:id "pine-needles-texture" :src "/images/textures/pine-needles.jpg"}]
   [:img {:id "pine-needles-texture" :src "/images/textures/pine-needles-conic.jpg"}]
   [:img {:id "bark-cylinder-texture" :src "/images/textures/bark.jpg"}]
   [:a-mixin {:id "tree-texture" :material "src:#pine-needles-texture"}]
   [:a-mixin {:id "bark-texture" :material "src:#bark-cylinder-texture"}]

   [:script {:id "treeTemplate"
             :type "html"}
    [:a-entity {:id "conicTree-${number}" :position "${pos}"}
     [:a-cone {:mixin "tree-texture" :radius-bottom "2" :radius-top "0" :height "2"
               :wireframe "${wireframe}"}
      [:a-cone {:mixin "tree-texture" :radius-bottom "2" :radius-top "0" :position "0 -1   0"
                :wireframe "${wireframe}"
                :animation__wow "property: position;
                                from: ${pos};
                                to: 0 -1 ;
                                dir: alternate;
                                dur: 2000;
                                loop: true;"
                }]
      [:a-cone {:mixin "tree-texture" :radius-bottom "2" :radius-top "0" :position "0 -1.5 0"
                :wireframe "${wireframe}"}]
      [:a-cylinder {:random-rotation "min: 0 0 0; max: 0 360 0" :mixin "bark-texture" :height "2" :radius "0.5" :position "0 -2.5 0"
                    :wireframe "${wireframe}"}]]]]])

(defn make-box-row [n result]
  ;; Ohh, you can do trees like this, in raw html, maybe?
  (let [size (* 0.1 n)
        pos (string/join
              " "
              [(+ -2 n 1) (+ 0.5 (Math/cos n)) -3])
        y-pos (Math/floor (+ 360 (*  45 (Math/cos n))))]
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
                                                             to: 0 " y-pos " 0;")
                                    :position pos}] result))
      result)))

(defn render [{global-meta :meta
               entries :entries}]
  (html
    head-el
    [:body
     [:a-scene
      a-assets-el

      [:a-sky {:src "/images/panoramas/test-arb1.jpg" :rotation "0 -130 0"
               :animation__color "property: color;
                                 from: #2EAFAC;
                                 to: #BBAAEE;
                                 easing: linear;
                                 dir: alternate;
                                 dur: 1500;
                                 loop: true;"}]

      (for [n (range 1 96)]
        [:a-entity {:rand-data-pos "" :template "src: #treeTemplate"
                    :data-number n
                    :data-wireframe "false"
                    }])

      ;; Funny expanding cone
      (for [n (range -50 150 20)]
        [:a-cone {:color "#2EAFAC"
                  :change-on-look ""
                  :change-color-on-click ""
                  :wireframe "true"
                  :position (string/join " "
                                         [(/ n 5)
                                          (Math/cos n)
                                          -3])
                  :radius-bottom "2" :radius-top "0"
                  :animation__pointy "startEvents: mouseenter;
                                     pauseEvents: mouseleave;
                                     property: radius-top;
                                     loop: true;
                                     easing: linear;
                                     from: 0; to: 3;
                                     dir: alternate;
                                     dur: 1000;"
                  :data-radius-bottom "2"}])

      (make-box-row 20 '())

      #_(for [n (range 0 600 60)]
          [:a-cone {:color "#2EAFAC"
                    :position (string/join " "
                                           [(Math/sin n)
                                            (Math/cos n)
                                            -3])
                    :radius-bottom "2" :radius-top "0"
                    :animation__pointy "property: radius-top;
                                       loop: true;
                                       easing: linear;
                                       from: 0; to: 3;
                                       dir: alternate;
                                       dur: 1000;"
                    :data-radius-bottom "2"}])

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

      (let [wireframe true]
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
      [:a-camera#camera {:look-controls "reverseMouseDrag: true;"}
       [:a-cursor {:scale "1.5 1.5 1.5"
                   :geometry "primitive: ring"
                   :material "color: #FFC0CB;
                             shader: flat"}]]]]))

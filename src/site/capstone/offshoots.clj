(ns site.capstone.offshoots
  (:use [hiccup.core :only (html)]
        [hiccup.page :only (html5)]
        [clojure.string :as string]
        [site.capstone.index]))

(defn render-leah [{global-meta :meta
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
                                     to: 3;
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
      [:a-camera {:look-controls "reverseMouseDrag: true;
                                 far: 100;" }
       [:a-cursor {:scale "1.5 1.5 1.5"
                   :geometry "primitive: ring"
                   :material "color: #FFC0CB;
                             shader: flat"}]]
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

(def itlt-srcs
  (concat srcs
          ["/js/utils/dat.gui.min.js"
           "/js/itlt.js"]))

(def itlt-assets
  (conj a-assets-el
        [:script {:id "treeTemplateWithSound"
                  :type "html"}
         [:a-entity {:id "conicTree-${number}"
                     :position "${pos}"
                     :sound "${sound}"
                     :enter-sound ""
                     }
          [:a-cone.clickable {:mixin "tree-texture" :radius-bottom "2" :radius-top "0" :height "2"
                              :animation__pointy "startEvents: mouseenter;
                                                 pauseEvents: mouseleave;
                                                 property: radius-top;
                                                 loop: true;
                                                 easing: linear;
                                                 from: 0; to: 10;
                                                 dir: alternate;
                                                 dur: 1000;"
                              :wireframe "${wireframe}"}
           [:a-cone.clickable {:mixin "tree-texture" :radius-bottom "2" :radius-top "0" :position "0 -1   0"
                               :wireframe "${wireframe}"
                               #_#_:animation__wow "property: position;
                                                   from: ${pos};
                                                   to: 0 -1 ;
                                                   dir: alternate;
                                                   dur: 2000;
                                                   loop: true;"
                               :animation__pointy "startEvents: mouseenter;
                                                  pauseEvents: mouseleave;
                                                  property: radius-top;
                                                  loop: true;
                                                  easing: linear;
                                                  from: 0; to: 13;
                                                  dir: alternate;
                                                  dur: 1000;"}]
           [:a-cone.clickable {:mixin "tree-texture" :radius-bottom "2" :radius-top "0" :position "0 -1.5 0"
                               :animation__pointy "startEvents: mouseenter;
                                                  pauseEvents: mouseleave;
                                                  property: radius-top;
                                                  loop: true;
                                                  easing: linear;
                                                  from: 0; to: 15;
                                                  dir: alternate;
                                                  dur: 1000;"
                               :wireframe "${wireframe}"}]
           [:a-cylinder {:random-rotation "min: 0 0 0; max: 0 360 0" :mixin "bark-texture" :height "2" :radius "0.5" :position "0 -2.5 0"
                         :wireframe "${wireframe}"}]]]]

           [:audio#ping_clang {:preload "auto" :src  "/assets/sound/effects/ping_clang.wav"}]
           [:audio#ping_edsward {:preload "auto" :src  "/assets/sound/effects/ping_edsward.wav"}]
           [:audio#ping_gem {:preload "auto" :src  "/assets/sound/effects/ping_gem.wav"}]
           [:audio#ping_thomas {:preload "auto" :src  "/assets/sound/effects/ping_thomas.wav"}]
           [:audio#forest_one {:preload "auto" :src  "/assets/sound/effects/forest_one.wav"}]
           [:audio#forest_two {:preload "auto" :src  "/assets/sound/effects/forest_two.wav"}]
           [:audio#forest_three {:preload "auto" :src  "/assets/sound/effects/forest_three.wav"}]
           ))

(def itlt-head-el
  [:head
   [:title "Imagine Trees Like These"]
   [:meta {:charset "utf-8"}]
   [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
   [:link {:rel "stylesheet"
           :href "/css/capstone.css"}]
   [:link {:rel "stylesheet"
           :href "/css/vr-styles.css"}]
   [:script {:src "https://aframe.io/releases/0.5.0/aframe.min.js"}]
   (for [src itlt-srcs]
     [:script {:src src}])])

(defn add-blank [href] {:href href :target "_blank"})

(defn itlt[{global-meta :meta
               entries :entries}]
  (html
    itlt-head-el
    [:body
     [:div#debug
      "state placeholder"]
     [:div#aboutInfo {:style "display: none;"}
      [:button#closeAbout {:onClick "toggleAboutInfo();"}
       "Close"]
      "This project was originally created as the capstone for my Creative Writing degree at Oberlin college. It has turned"
      " into an interactive web art piece that I hope will give you a small demonstration of the power of virtual"
      " spaces to create a sort of \"anti-reality\", an experience hopefully as playful, interesting, and \"real\" as any other."
      [:hr]
      [:h4 "Tools & links:"]
      [:ul
       [:li "Made using the wonderful" [:a (add-blank "https://aframe.io/") "A-Frame"] "WebVR framework, powered by"
        [:a (add-blank "https://threejs.org/") "Three.js"]]
       [:li [:a (add-blank "http://slides.cwervo.com/capstone.html") "Slides about the origins of this piece"]]
       [:li [:a (add-blank "https://www.youtube.com/watch?v=Ca6quGC_hUk") "Recorded talk about this project"]]
       [:li [:a (add-blank "http://cwervo.com") "Personal website"]]]
      ]
     [:a-scene {:loading-bar ""}
      itlt-assets

      [:a-sky {:src "/images/panoramas/test-arb1.jpg" :rotation "0 -130 0"
               :animation__color "property: color;
                                 from: #2EAFAC;
                                 to: #BBAAEE;
                                 easing: linear;
                                 dir: alternate;
                                 dur: 1500;
                                 loop: true;"}]
      [:a-sphere#intro_mask.clickable {:radius 3
                                       :position "0 1.6 0"
                                       ;; :material "side: back;"
                                       :material "side: double;
                                                 "
                                       ;; opacity: 0;
                                       ;; :color "#BAE"
                                       :color "black"
                                       ;; startEvents: removetitle, onremovetitle, onenterremovetitle;
                                       #_#_:animation__fade "property: material.opacity;
                                                            from: 1.0;
                                                            to: 0.0;
                                                            dir: alternate;
                                                            dur: 3000;"}
       (let [make-rotation #(str "0 " % " 0")
             distance-from-center 0.7
             rl-offset 1.7
             pos [(str "0 0 " (- 0 distance-from-center)) ;; front
                  (str (* rl-offset distance-from-center )" 0 0") ;; right
                  (str "0 0 " distance-from-center) ;; back
                  (str (* rl-offset (- 0 distance-from-center)) " 0 0") ;; left
                  ]
             rot ["0"
                  "-90"
                  "180"
                  "90"
                  ]]
         (for [n (range 4)]
           [:a-text.title_text {:value "Imagine Trees Like These
                                       \nBy Andres Cuervo
                                       \nLook around by dragging your mouse or moving your phone.
                                       \nIf you have a keyboard: WASD or arrow keys moves you around the scene.
                                       \nUse the button in the top right to see 'About' info and more options."
                                :font "sourcecodepro"
                                :align "center"
                                :width 1
                                :position (nth pos n)
                                :id (str "title-"n)
                                :rotation  (make-rotation (nth rot n))
                                :side "double"}
            [:a-plane.clickable.title_plane {:position "0 -1.5 -1"
                                             :color "white"
                                             :fsm-event-trigger "event: removetitle;"
                                             }
             [:a-text.title_plane_text {:color "black"
                                        :width 2
                                        :position "0 0 0"
                                        :align "center"
                                        :value "Stare here for 1 second
                                               \nto enter the scene."}]]]))]

      (for [n (range 10 200)
            ;; n == radius
            ;; 5 == steps
            :let [x-coord (+ (* n (Math/cos (* 2 Math/PI (/ n 50)))) (rand-int (/ n 10)))
                  z-coord (+ (* n (Math/sin (* 2 Math/PI (/ n 50)))) (rand-int (/ n 10)))
                  pos (string/join " "
                                   [x-coord
                                    2
                                    z-coord])
                  wireframe? false
                  forest-sounds ["one" "two" "three"]]]

        [:a-entity {:template "src: #treeTemplateWithSound"
                    ;; :data-sound "src: #ping_gem; poolSize: 1;"
                    :data-sound (str "src: #forest_" (nth forest-sounds (mod (/ n 10) (count forest-sounds))) "; poolSize: 1;")
                    :data-pos (string/join " "
                                           [x-coord
                                            2
                                            z-coord])
                    :data-number n
                    :data-wireframe "false"
                    }])

      ;; Funny expanding cone
      (let [pings [#_"thomas" "edsward" "gem" #_"clang"]]
        (for [n (range -50 150 20)]
        [:a-cone.clickable {:color "#2EAFAC"
                            :change-color-on-click ""
                            :sound (str "src: #ping_" (nth pings (mod (/ n 7) (count pings))) "; poolSize: 10;")
                            :enter-sound ""
                            ;; :mixin "tree-texture"
                            :wireframe "true"
                            :position #_(string/join " "
                                                  [(/ n 5)
                                                    (Math/cos n)
                                                    -3])
                            (let [scale 5]
                              (string/join " "
                                         [
                                          (+ (* (Math/sin n) scale) (rand-int (mod n 5)))
                                          (+ -0.5 (* (Math/sin n) 0.5))
                                          (+ (* (Math/cos n) scale) (rand-int (mod n 5)))
                                          ]))

                            :radius-top "0"
                            :radius-bottom "1"
                            :animation__pointy "startEvents: mouseenter;
                                               pauseEvents: mouseleave;
                                               property: radius-top;
                                               loop: true;
                                               easing: linear;
                                               from: 0; to: 2;
                                               dir: alternate;
                                               dur: 1000;"
                            :data-radius-bottom "2"}
         ]))

      #_(make-box-row 20 '())

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

      #_(let [wireframe false]
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


      ;; Camera
      [:a-camera#camera {
                         ;; :look-controls "reverseMouseDrag: true;"
                         }
       (let [cursor-size 0.025]
         [:a-entity {;;:cursor "fuse: true; fuseTimeout: 500;"
                   :cursor "fuse: true;"
                   :raycaster "objects: .clickable"
                   :fill "backwards"
                   :position "0 0 -1"
                   :scale (string/join " "
                                       (repeat 3 cursor-size))
                   :geometry "primitive: ring"
                   :material "color: #FFC0CB;
                             shader: flat"
                   :animation (str ""
                                       "property: scale;
                                       from: " cursor-size ";"
                                       "to: " (+ cursor-size 0.5) ";"
                                       "dir: alternate;")}])]]]))

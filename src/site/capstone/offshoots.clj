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

(ns site.utils
  (:use [site.core :only (root-head-element)]
        [hiccup.core :only (html)]
        [hiccup.page :only (html5)])
  (:require [clojure.pprint :refer [pprint]]
            [clojure.string :as str]))
(defn drag-and-drop [{global-meta :meta
                      entries :entries}]
  (html
    ;; Put all this into a panorama-head fn or def?
    (root-head-element "Drag & Drop!"
                       ["<script src='https://rawgit.com/donmccurdy/aframe-extras/v2.1.1/dist/aframe-extras.loaders.min.js'></script>"
                        "<script src='https://cdnjs.cloudflare.com/ajax/libs/dropzone/4.3.0/dropzone.js'></script>"
                        "<script src='https://unpkg.com/vue'></script>"
                        "<script src='/js/drag-and-drop.js'></script>"
                        "<link rel='stylesheet' href='/css/drag-and-drop.css'></link>"
                        ])
    [:body
     [:div#hud
      [:form#my-awesome-dropzone.dropzone {:action "#"}
       [:span {:class "dz-message"} "Click on or drop files in this pink rectangle to change the background."]]]
     [:a-scene {:embeded ""}
      [:a-sky#bgImage {:src "/images/panoramas/earth_equirectangular.jpg"
                       :rotation "0 -130 0"}]
      [:a-camera#camera {:v-bind:fov "zRotation"}]]]))

(defn root-6-head-element
  ([title]
   [:head
    [:title title]
    [:script {:src "https://cdnjs.cloudflare.com/ajax/libs/aframe/0.6.0/aframe-master.min.js"}]])
  ([title custom-scripts]
   (conj
     (into
       (root-6-head-element title)
       (concat custom-scripts)))))

(defn vr-dat-gui-test [{global-meta :meta entries :entries}]
  (html
    (root-6-head-element "VR-Dat-Gui"
                         ["<script src='https://andrescuervo.github.io/twentyfourseven/js/utils/Detector.js'></script>"
                          "<script src='https://andrescuervo.github.io/twentyfourseven/js/utils/stats.min.js'></script>"
                          "<script src='https://andrescuervo.github.io/twentyfourseven/js/loaders/PLYLoader.js'></script>"
                          "<script src='/js/vr/ViveController.js'></script>"
                          "<script src='/js/vr/datguivr.min.js'></script>"
                          "<script src='/js/a-frame-js/datguivr.js'></script>"
                          ;; Port the following script and reroute where the assets are coming from!
                          "<script src='/js/scenes/day3.js?v=12'></script>"])
    [:body
     [:div#container]
     [:a-scene {:make-point-cloud ""
                :dat-gui "query: [hand-controls]; objects: mySphere cube; enableMouse: true;"}
      (for [control ["left" "right"]]
        [:a-entity {:id (str control "Control")
                    :hand-controls control}])
      [:a-sphere#mySphere  {:material "color: pink"
                            :position "0 0 -4"}]
      [:a-box#cube {:material "color: blue" :position "-2 0 -4"}]
      [:a-sky {:material "color: black;"}]
      [:a-camera {:id "camera"}]]
     [:script {:src "https://andrescuervo.github.io/twentyfourseven/js/controls/TrackballControls.js"}]
     [:script {:src "https://andrescuervo.github.io/twentyfourseven/js/effects/AnaglyphEffect.js"}]
     [:script {:type "x-shader/x-vertex" :id "vertexshader"} "attribute float size;\n    attribute vec3 customColor;\n\n    varying vec3 vColor;\n\n    void main() {\n\n        vColor = customColor;\n\n        vec4 mvPosition = modelViewMatrix * vec4( position 1.0 );\n\n        gl_PointSize = size * ( 300.0 / -mvPosition.z );\n\n        gl_Position = projectionMatrix * mvPosition;\n\n    }"]
     [:script {:type "x-shader/x-fragment" :id "fragmentshader"} "uniform vec3 color;\n    uniform sampler2D texture;\n\n    varying vec3 vColor;\n\n    void main() {\n\n        gl_FragColor = vec4( color * vColor 1.0 );\n\n        gl_FragColor = gl_FragColor * texture2D( texture gl_PointCoord );\n\n    }"]
     [:script {:type "text/javascript"} "<!--\n    function toggle(id) {\n        var e = document.getElementById(id);\n        e.style.display == 'block' ? e.style.display = 'none' : e.style.display = 'block';\n    }\n    "]]))


(defn meme-bump-map-test [{global-meta :meta entries :entries}]
  (html
    (root-6-head-element "Bump Maps & Stuff"
                         ["<script src='https://rawgit.com/ngokevin/aframe-animation-component/master/dist/aframe-animation-component.min.js'></script>"
                          "<script src='/js/spec-map.js'></script>"

                          ])
    [:body
     [:div#container]
     (let [fname #_"/assets/textures/foot-glove/foot-glove-sandals_"
           "/assets/textures/concrete/concrete_"]
       [:a-scene
        [:a-assets
         (for [m ["COLOR" "NRM"]]
           [:img {:id (str "floor" m)
                  :src (str fname m ".png") }])
         ]
        (for [control ["left" "right"]]
          [:a-entity {:id (str control "Control")
                      :position "0 0.6 0"
                      :hand-controls control}])
        [:a-entity {:light "type: ambient; color: #DAD"}]
        (for [n (range -8 -4 0.5)
              :let [z (+ (Math/sin n) 0.5) ]]
          [:a-entity {:light "type: point; color: #2EAFAC; intensity: 0.1" :position (str "-4.5 1 " z)
                      :animation (str
                                   "property: position;
                                   from: -4.5 1 " z
                                   ";
                                   to: 4.5 1 " z
                                   ";
                                   easing: linear;
                                   dir: alternate;
                                   loop: true;
                                   dur: 3000;")}]
          )

        [:a-sphere {:light-shader ""
                    :position "0 3.2 -2"
                    :material "color: white"
                    :radius "2"}]
        [:a-plane#floor
         {:scale "100 100 100"
          :position "0 0 0"
          :rotation "-90 0 0"
          :roughness 0.4
          ;; :shininess 30
          :material "color: white;
                    src: #floorCOLOR;
                    normalMap: #floorNRM;
                    "
          :spec-map (str "src: " fname "SPEC.png")
          }]
        [:a-sky {:material "color: #2EAFAC;"}]
        [:a-camera {:id "camera"}]])
     ]))

(defn apainter6 [{global-meta :meta entries :entries}]
  (html
    (root-6-head-element "A-Painter 0.6.0"
                         ["<script src='https://rawgit.com/ngokevin/aframe-animation-component/master/dist/aframe-animation-component.min.js'></script>"
                          "<script src='https://cdn.rawgit.com/dmarcos/a-painter-loader-component/master/dist/a-painter-loader-component.min.js'></script>"
                          "<script src='/js/spec-map.js'></script>"
                          ])
    [:body
     [:div#container]
     (let [_ "nada"]
       [:a-scene
        [:a-assets]
        [:a-entity#ucareloader {:a-painter-loader "src: https://ucarecdn.com/f5653bf5-0f4f-4b80-8899-5a0a1227c8e8/"}]
        [:a-sky {:material "color: #2EAFAC;"}]
        [:a-camera {:id "camera"}]])
     ]))

(defn simple-vr-dat-gui-example [{global-meta :meta entries :entries}]
  (html
    (root-6-head-element "VR-Dat-Gui"
                         ["<script src='/js/vr/datguivr.min.js'></script>"
                          "<script src='/js/a-frame-js/datguivr.js'></script>"])
    [:body
     [:div#container]
     [:a-scene {:dat-gui "query: [hand-controls]; objects: box plane; enableMouse: true;"}
      (for [control ["left" "right"]]
        [:a-entity {:id (str control "Control")
                    :hand-controls control}])
      [:a-box#box {:position "-1 0.5 -3" :rotation "0 45 0" :color "#4CC3D9"}]
      [:a-sphere#sphere {:position "0 1.25 -5" :radius "1.25" :color "#EF2D5E"}]
      [:a-cylinder#cylinder {:position "1 0.75 -3" :radius "0.5" :height "1.5" :color "#FFC65D"}]
      [:a-plane#plane {:position "0 0 -4" :rotation "-90 0 0" :width "4" :height "4" :color "#7BC8A4"}]
      [:a-sky {:color "#ECECEC"}]
      [:a-camera {:id "camera"}]
      ]]))

(defn meme-test [{global-meta :meta entries :entries}]
  (html
    (root-6-head-element "Meeeemee"
                         ["<script src='https://rawgit.com/ngokevin/aframe-animation-component/master/dist/aframe-animation-component.min.js'></script>"])
    [:body
     [:a-scene
      [:a-assets
       ;; Asset-cache the images they're so fucking big
       ;;
       ;; also move the let up around the scene!
       [:a-asset-item {:id "poss" :src "https://andrescuervo.github.io/assets/poss-text/178_td.gltf"}]]
      (let [count 13
            xy-pos "0 1.6 "
            scale-size 80]
        [:a-entity#memeContainer {:position "0 0 -10"
                                  :scale "10 10 10"}
         (for [n (range (inc count))]
           [:a-plane {:id (str "plane-" count)
                      :material (str "src: url(/images/meme-test/final_layers_"n".png); alphaTest: 0.5;")
                      :position (str xy-pos  (- -10 (* (+ count n) 1.6666)))
                      :depth count
                      :scale (clojure.string/join " " (map #_(+ % (* n 10)) identity
                                                            [(* 1.6 (* 1.3 scale-size))
                                                             scale-size
                                                             0]))
                      }])
         [:a-entity {:id "poss-text" :gltf-model "#poss"
                     :position "-10 100 13"
                     :animation "property: rotation; from: 0 0 0; to: 0 1 0; dir: alternate; loop: true; easing: linear;"
                     :scale "2400 2400 2400"}]])
      [:a-sky {:material "color: black;"}]]]))


(defn gltf-tests [{global-meta :meta entries :entries}]
  (html
    (root-6-head-element "GLTF Test!"
                         ["<script src='//cdn.rawgit.com/donmccurdy/aframe-extras/v3.8.6/dist/aframe-extras.min.js'></script>"
                          "<script src='https://rawgit.com/ngokevin/aframe-animation-component/master/dist/aframe-animation-component.min.js'></script>"
                          "<script src='/js/model-texture.js'></script>"
                          "<script src='https://rawgit.com/fernandojsg/aframe-teleport-controls/master/dist/aframe-teleport-controls.min.js'></script>"])
    [:body
     [:a-scene
      [:a-assets
       [:a-asset-item#waterCloth {:src "/assets/models/water_cloth/water_cloth.gltf"}]
       [:a-mixin#infoText {:color "white"
                           :position "-1 -1 0"
                           :height "0.5"} ]
       ]
      [:a-camera {:id "camera"}]

      [:a-entity#models {:position "0 0 0"}
       [:a-entity {:gltf-model "#waterCloth"
                   :position "0 1 -6"
                   :scale "3 3 3"}
       [:a-text {:value "Water cloth model \n Generated with: Blender OBJ -> Meshlab to Collada -> collada2gltf \n Loaded with: a-gltf core component"
                 :mixin "infoText"}]]

      [:a-entity#buster {:gltf-model-next "src: url(/assets/models/buster/busterDrone.gltf)"
                         :position "-4 1 -2"
                         :animation-mixer "busterDrone"
                         :scale "0.5 0.5 0.5"
                         :rotation "0 45 0"}
       [:a-entity#busterTextWrapper {:position "2 2 0"}
        [:a-text {:value "Buster Drone model \n Generated with: Unity 5.6.1f1 \n Loaded with: gltf-model-next a-frame-extras component"
                  :scale "2 2 2"
                  :mixin "infoText"}]]]

      [:a-entity#duck {:gltf-model-next "src: url(/assets/models/duck/duck.gltf)"
                         :position "4 1 0"
                         ;; :animation-mixer "duckDrone"
                         :scale "1 1 1"
                         :rotation "0 -90 0"}
       [:a-text {:value "Duck model \n Generated with: Khronos Blender glTF 2.0 exporter \n Loaded with: gltf-model-next a-frame-extras component"
                 :mixin "infoText"}]]]

      [:a-sky {:color "black"}]
      [:a-entity {:teleport-controls ""
                  :vive-controls "hand: left"}]
      [:a-entity {:teleport-controls "type: line"
                  :vive-controls "hand: right"}]]]))

(defn generative-train [{global-meta :meta entries :entries}]
  (html
    (root-6-head-element "Making a train????"
                         ["<script src='//cdn.rawgit.com/donmccurdy/aframe-extras/v3.8.6/dist/aframe-extras.min.js'></script>"
                          "<script src='https://rawgit.com/ngokevin/aframe-animation-component/master/dist/aframe-animation-component.min.js'></script>"
                          "<script src='https://cdn.rawgit.com/spite/THREE.MeshLine/master/src/THREE.MeshLine.js'></script>"
                          "<script src='/js/model-texture.js'></script>"
                          "<script src='https://rawgit.com/fernandojsg/aframe-teleport-controls/master/dist/aframe-teleport-controls.min.js'></script>"
                          "<script src='https://rawgit.com/protyze/aframe-curve-component/master/dist/aframe-curve-component.min.js'></script>"
                          "<script src='https://rawgit.com/protyze/aframe-alongpath-component/master/dist/aframe-alongpath-component.min.js'></script>"
                          "<script src='/js/fix-train.js'></script>"
                          ])
    [:body
     [:a-scene
      [:a-assets
       [:a-mixin#red {:material "color: red;"}]
       [:a-mixin#box {:geometry "primitive: box;"}]
       [:a-mixin#smallBox {:geometry "height:0.1; width:0.1; depth:0.1"}]]
      ;; [:a-curve {:id "track1"}
      ;;  [:a-curve-point {:position "-2 1 -3" :mixin "red box smallBox"}]
      ;;  [:a-curve-point {:position "0 1 -5" :mixin "red box smallBox" :color "blue"}]
      ;;  [:a-curve-point {:position "2 1 -3" :mixin "red box smallBox"}]]
      ;; [:a-box {:color "#ff0000" :width "0.1" :height "0.3" :depth "0.1" :alongpath "curve: #track1; loop:true; dur:6000; rotate:true;"} ]
      #_(let [duration 6000]
          [:a-entity#one
           [:a-curve#track1 {:rotation "0 0 0"
                             :type "QuadraticBezier"
                             ;; :type "Line"
                             :curve "closed: false;"}
            ;; [:a-curve-point {:position "-2 0 -2"}]
            ;; [:a-curve-point {:position "0.5 1 -2"}]
            ;; [:a-curve-point {:position "2 0 -3"}]
            (for [n (range 10)]
                (cond
                  (odd? n)  [:a-curve-point {:position (str "2 0 " (- 0 n))}]
                  (even? n) [:a-curve-point {:position (str "-2 0 " (- 0 n))}]
                  ))
            #_(for [n (range 7)
                    :let [scale 10
                          x (* (Math/cos n) scale)
                          z (- (* (Math/sin n) scale) 15)]]
                [:a-curve-point {:id (str "curve-point-"n)
                                 :position (str/join " " [x 0 z])}])]
           [:a-entity#train {:gltf-model-next "src: url(/assets/models/myFirstTrain/myFristTrain_from_blender.gltf)"
                             :alongpath (str "curve: #track1;
                                             loop: true;
                                             rotate: true;
                                             dur:" duration ";")
                             :material "color: blue;
                                       src: url(/assets/textures/metal.jpg)"
                             :rotation "0 180 0"
                             :scale "1 1 1"
                             :fix-train-rotation ""
                             }]]
          [:a-draw-curve {:curveref "#track1" :material "shader: line; color: red;"}]
          )
      [:a-entity {:position "0 0 -4"
               :make-spiral ""
               :make-spiral__2 ""
               :make-spiral__3 ""
               :make-spiral__4 ""
               }]
      #_[:a-entity {:clone-along-curve "curve: #track1; spacing: 0.2; scale: 1 1 1; rotation: 90 0 0;"
                  :geometry "primitive: box; height:0.1; width:0.8; depth:0.2"
                  :material "color: brown"}]
      [:a-sky {:color "black"}]]]))

(defn sofia3D [{global-meta :meta entries :entries}]
  (html
    (root-6-head-element "¿?¿?¿"
                         ["<script src='//cdn.rawgit.com/donmccurdy/aframe-extras/v3.8.6/dist/aframe-extras.min.js'></script>"
                          "<script src='https://rawgit.com/ngokevin/aframe-animation-component/master/dist/aframe-animation-component.min.js'></script>"
                          "<script src='https://rawgit.com/fernandojsg/aframe-teleport-controls/master/dist/aframe-teleport-controls.min.js'></script>"])
    [:body
     (let [animate? false]
       [:a-scene
      [:a-assets
       [:a-mixin#meme3D {:ply-model "src: /assets/models/180/180.ply"}]
       ]
      [:a-camera {:id "camera"}]

      (for [attrs [
                   {:pos "0 -1 -10"
                    :rot "90 0 0"}
                   {:pos "0 -1 10"
                    :rot "90 180 0"}
                   {:pos "10 -1 0"
                    :rot (str "90 " (* 3 90) " 0")}
                   {:pos "-10 -1 0"
                    :rot (str (* -3 90) " " -90 " " 180)}
                   {:pos "0 10 0"
                    :rot "180 0 0"}
                   {:pos "0 -10 0"
                    :rot (str (* -3 90) " 0 0")}
                   ]]
        [:a-entity#oneeighty {:mixin "meme3D"
                              :position (:pos attrs)
                              :scale "0.5 0.5 0.5"
                              :rotation (:rot attrs)
                              :animation (when animate?
                                           "property: rotation;
                                           from: 0 0 0;
                                           to: 360 0 0;
                                           loop: true;
                                           easing: easeInOutSine;
                                           dur: 12000;")}])

      [:a-sky {:color "black"}]
      [:a-entity {:teleport-controls ""
                  :vive-controls "hand: left"}]
      [:a-entity {:teleport-controls "type: line"
                  :vive-controls "hand: right"}]])]))

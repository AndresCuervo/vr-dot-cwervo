(ns site.utils
  (:use [site.core :only (root-head-element)]
        [hiccup.core :only (html)]
        [hiccup.page :only (html5)])
  (:require [clojure.pprint :refer [pprint]]
            [clojure.string :as str]
            [clojure.java.io :as io]))
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
    [:meta {:http-equiv "Content-Type"
            :content "text/html; charset=UTF-8"}]
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
                      :animation_ (str
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
                     :animation_ "property: rotation; from: 0 0 0; to: 0 1 0; dir: alternate; loop: true; easing: linear;"
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
                          :animation_-mixer "busterDrone"
                          :scale "0.5 0.5 0.5"
                          :rotation "0 45 0"}
        [:a-entity#busterTextWrapper {:position "2 2 0"}
         [:a-text {:value "Buster Drone model \n Generated with: Unity 5.6.1f1 \n Loaded with: gltf-model-next a-frame-extras component"
                   :scale "2 2 2"
                   :mixin "infoText"}]]]

       [:a-entity#duck {:gltf-model-next "src: url(/assets/models/duck/duck.gltf)"
                        :position "4 1 0"
                        ;; :animation_-mixer "duckDrone"
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
                          ;; "<script src='https://cdn.rawgit.com/dataarts/dat.gui/master/build/dat.gui.min.js'></script>"
                          "<script src='https://cdn.rawgit.com/dataarts/dat.guiVR/master/build/datguivr.min.js'></script>"
                          "<script src='/js/model-texture.js'></script>"
                          "<script src='https://rawgit.com/fernandojsg/aframe-teleport-controls/master/dist/aframe-teleport-controls.min.js'></script>"
                          "<script src='https://rawgit.com/protyze/aframe-curve-component/master/dist/aframe-curve-component.min.js'></script>"
                          "<script src='https://rawgit.com/protyze/aframe-alongpath-component/master/dist/aframe-alongpath-component.min.js'></script>"
                          "<script src='/js/grid-glitch-material.js'></script>"
                          "<script src='/js/fix-train.js'></script>"
                          "<script src='https://rawgit.com/mayognaise/aframe-draw-shader/master/dist/aframe-draw-shader.min.js'></script>"
                          "<script src='/js/draw-shader-noise-test.js'></script>"
                          "<script src='https://cdn.rawgit.com/wizgrav/aframe-effects/master/dist/aframe-effects.min.js'></script>"
                          ])
    [:body
     [:a-scene {:antialias "true"
                :effects "noise-effect-2"
                :noise-effect-2 ""
                ;; :effects "outline"
                ;; :outline ""
                ;; :effects="bloom"
                ;; :bloom ""
                }
      [:a-assets
       [:a-mixin#red {:material "color: red;"}]
       [:a-mixin#box {:geometry "primitive: box;"}]
       [:a-mixin#craneShader {:crane-shader ""}]
       [:a-mixin#smallBox {:geometry "height:0.1; width:0.1; depth:0.1"}]]
      #_[:a-curve {:id "track1"}
         [:a-curve-point {:position "-2 1 -3" :mixin "red box smallBox"}]
         [:a-curve-point {:position "0 1 -5" :mixin "red box smallBox" :color "blue"}]
         [:a-curve-point {:position "2 1 -3" :mixin "red box smallBox"}]]
      [:a-box {:crane-shader ""
               ;; :material "shader:draw;"
               ;; :noise ""
               :position "0 0 -2"
               ;; :animation "property: position; from: 0 0 -2; to: -3 0 -2; loop: true; easing: linear; dur: 4000;"} ]
               :animation "property: rotation; from: 0 0 0; to: 360 0 0; loop: true; easing: linear; dur: 4000;"} ]
      [:a-plane#floor {:rotation "-90 0 0"
                       :color "#010101"
                       :scale "40 40 40"}]
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
          #_[:a-draw-curve {:curveref "#track1" :material "shader: line; color: red;"}]
          )
      #_[:a-datgui {:name "settings" :position "0 2 -2"}
         [:a-gui-slider {:id "colorControl" :name "color" :step "0.01" :min "0.01" :max "4"}]]
      (let [scale {:x 5 :z 5}]
        (println (:x scale))
        [:a-entity {:position "0 0 -4"
                    ;; TODO Adjust rotation with an override of each
                    :make-crane (str "n: 40; position: " (* -3 (:x scale)) " " 0 " " (* -4 (:z scale)))
                    :make-crane__2 (str "n: 40; position: " (* -2.5 (:x scale)) " " 0 " " (* -3 (:z scale)))
                    :make-crane__3 (str "n: 45; position: " (* -1.5 (:x scale)) " " 0 " " (* -3.75 (:z scale)))
                    :make-crane__4 (str "n: 24; position: " (* -0.5 (:x scale)) " " 0 " " (* -3 (:z scale)))
                    }])
      #_[:a-entity {:clone-along-curve "curve: #track1; spacing: 0.2; scale: 1 1 1; rotation: 90 0 0;"
                    :geometry "primitive: box; height:0.1; width:0.8; depth:0.2"
                    :mixin "craneShader"
                    :material "color: brown"}]
      [:a-camera#camera]
      [:a-sky {:color "#101010"}]]]))

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
        [:a-camera {:id "camera"
                    :fov "10"}]

        (for [attrs [
                     {:pos "0 -1 -10"
                      :rot "90 0 0"}
                     {:pos "0 -1 10"
                      :rot "90 180 0"}
                     {:pos "10 -1 0"
                      :rot (str "90 " (* 3 90) " 0")}
                     {:pos "-10 -1 0"
                      :rot (str (* -3 90) " " -90 " " 180)}
                     ;; {:pos "0 10 0"
                     ;;  :rot "180 0 0"}
                     ;; {:pos "0 -10 0"
                     ;;  :rot (str (* -3 90) " 0 0")}
                     ]]
          [:a-entity#oneeighty {:mixin "meme3D"
                                :position (:pos attrs)
                                :scale "0.5 0.5 0.5"
                                :rotation (:rot attrs)
                                :animation_ (when animate?
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

(defn maxprentisvisual-1 [{global-meta :meta entries :entries}]
  (html
    (root-6-head-element "Illustrated planes"
                         ["<script src='//cdn.rawgit.com/donmccurdy/aframe-extras/v3.8.6/dist/aframe-extras.min.js'></script>"
                          "<script src='https://rawgit.com/ngokevin/aframe-animation-component/master/dist/aframe-animation-component.min.js'></script>"
                          "<script src='/js/move-camera-max-present.js'></script>"
                          "<script src='https://rawgit.com/fernandojsg/aframe-teleport-controls/master/dist/aframe-teleport-controls.min.js'></script>"])
    [:body
     (let []
       [:a-scene
        [:a-assets
         ]
        [:a-camera {:id "camera"
                    :move-camera ""}]

        (let [web-path-prefix "images/3Dmemes/maxprentisvisual_1"
              relative-path (str "resources/public/" web-path-prefix)
              file-list (.list (io/file relative-path))
              file-count (count file-list)
              x-pos 0
              y-pos 1
              displace-first-pic -0.05
              scale-size 80]
          [:a-entity#memeContainer {:position "0 -0.5 -10"
                                    :scale "10 10 10"}
           (for [n (range (dec file-count))
                 :let [file-path (str "/" web-path-prefix "/layer_"n".png")
                       position (clojure.string/join " " [#_(if (= n 0) displace-first-pic x-pos )
                                                          x-pos
                                                          y-pos
                                                          (if (= n 0) 0 (* -0.25 n))
                                                          ])]]
             [:a-plane {:id (str "plane-" n)
                        :material (str "src: url(" file-path "); alphaTest: 0.5;")
                        :position position
                        :depth n
                        :scale "2.5 2.5 2.5"
                        :offset-plane (str "n: "n)
                        #_#_:animation__offset (str
                                                 "property: material.offset.x;
                                                 from: 10;
                                                 to: 0;
                                                 loop: true;")
                        :animation__hover_first_x (when (= n 0)
                                                    (str "property: position;"
                                                         "easing: linear;"
                                                         "dur: 3000;"
                                                         "loop: true;"
                                                         "from: "  displace-first-pic " " y-pos " 0;"
                                                         "to: " (* -1 displace-first-pic) " " y-pos " 0;"
                                                         "dir: alternate;"
                                                         "loop: true;"
                                                         ))
                        ;; :animation__hover_first_y (when (= n 0)
                        ;;                           (str "property: rotation;"
                        ;;                                "easing: linear;"
                        ;;                                "dur: 5000;"
                        ;;                                "loop: true;"
                        ;;                                "from: 0 -5 0;"
                        ;;                                "to: 0 5 0;"
                        ;;                                "dir: alternate;"
                        ;;                                "loop: true;"
                        ;;                                ))
                        #_#_:animation__move_first (when (= n 0)
                                                     (str "property: position;"
                                                          "easing: linear;"
                                                          "dur: 100000;"
                                                          "loop: true;"
                                                          "from: " position ";"
                                                          "to: " (clojure.string/join " " [(* -1 displace-first-pic)
                                                                                           y-pos
                                                                                           0]) ";"
                                                          ))
                        }])
           #_[:a-entity {:id "poss-text" :gltf-model "#poss"
                         :position "-10 100 13"
                         :animation_ "property: rotation; from: 0 0 0; to: 0 1 0; dir: alternate; loop: true; easing: linear;"
                         :scale "2400 2400 2400"}]])

        [:a-sky {:color "#000"}]
        [:a-entity {:teleport-controls ""
                    :vive-controls "hand: left"}]
        [:a-entity {:teleport-controls ""
                    :vive-controls "hand: right"}]])]))

(defn note-styled [container]
  [:div.note {:style "position: absolute;
                     top: 1%;
                     left: 1%;
                     color: black;
                     text-shadow:
                     -1px -1px 0 #BBB,
                     1px -1px 0 #BBB,
                     -1px 1px 0 #BBB,
                     1px 1px 0 #BBB;
                     "} container])

(defn hiro-message [& args]
  (note-styled [:span "You'll need to have " [:a {:href "https://jeromeetienne.github.io/AR.js/data/images/HIRO.jpg"} "this image"] " printed or just displaying on your phone! " [:br] args]))

(defn ar-refraction [{global-meta :meta entries :entries}]
  (html
    (root-6-head-element "Some simple boxes as a first AR test!"
                         ["<script src='//cdn.rawgit.com/donmccurdy/aframe-extras/v3.8.6/dist/aframe-extras.min.js'></script>"
                          "<script src='https://rawgit.com/jeromeetienne/ar.js/master/aframe/build/aframe-ar.js'></script>"
                          "<script src='https://rawgit.com/ngokevin/aframe-animation-component/master/dist/aframe-animation-component.min.js'></script>"
                          "<script src='https://cdn.rawgit.com/spite/THREE.MeshLine/master/src/THREE.MeshLine.js'></script>"
                          "<script src='https://cdn.rawgit.com/dataarts/dat.gui/master/build/dat.gui.min.js'></script>"
                          "<script src='https://rawgit.com/protyze/aframe-curve-component/master/dist/aframe-curve-component.min.js'></script>"
                          "<script src='https://rawgit.com/protyze/aframe-alongpath-component/master/dist/aframe-alongpath-component.min.js'></script>"
                          "<script src='/js/refraction-shader.js'></script>"
                          "<script src='/js/ar-refraction-misc.js'></script>"
                          ])
    [:body
     (hiro-message "Thanks to " [:a {:href "https://twitter.com/jerome_etienne"} "Jerome Etienne"]
                   " for " [:a {:href "https://github.com/jeromeetienne/AR.js"} "AR.js and this refraction shader!"])
     [:a-scene {:artoolkit "sourceType: webcam;"
                :style "pointer-events: none;"}
      ;; [:a-scene {:artoolkit "debug: true; sourceType: image; sourceUrl: /images/hiro-placeholder.png;"}
      [:a-torus#clearTorus {:position "0 0 0"
                            :refraction-shader ""
                            :scale "1 1 1"
                            ;; :animation "property: position; from: 0 0 0; to: -3 0 0; loop: true; easing: linear; dur: 4000;"
                            :animation__rot "property: rotation; from: 0 0 0; to: 360 0 0; loop: true; easing: linear; dur: 10000;"
                            }]
      ;; TODO Duck doesn't get remapped with current comopnent, idk why, check what the material on document.querySelector("#duck") is???
      ;; [:a-entity#duck {:gltf-model-next "src: url(/assets/models/duck/duck.gltf)"
      ;;                  :position "2 1 0"
      ;;                  :scale "1 1 1"
      ;;                  :rotation "0 -90 0"}]
      ;; [:a-entity {:obj-model (str "obj: url(/assets/models/pak_1/sphere.obj);")
      ;;             :refraction-shader ""}]
      [:a-marker-camera {:preset "hiro"}]]]))

(defn liquid-marker-weird [{global-meta :meta entries :entries}]
  (html
    (root-6-head-element "Some simple boxes as a first AR test!"
                         ["<script src='//cdn.rawgit.com/donmccurdy/aframe-extras/v3.8.6/dist/aframe-extras.min.js'></script>"
                          "<script src='https://rawgit.com/jeromeetienne/ar.js/master/aframe/build/aframe-ar.js'></script>"
                          "<script src='/js/threex-arliquidmarker.js'></script>"
                          "<script src='/js/refraction-shader.js'></script>"
                          "<script src='/js/liquidmarker_weird.js'></script>"
                          ])
    [:body

     (hiro-message)
     [:a-scene {:artoolkit "sourceType: webcam;"
                :style "pointer-events: none;"
                }
      [:a-marker-camera {:preset "hiro"
                         :liquid-marker ""}]]]))

(defn liquid-marker [{global-meta :meta entries :entries}]
  (html
    (root-6-head-element "Some simple boxes as a first AR test!"
                         ["<script src='//cdn.rawgit.com/donmccurdy/aframe-extras/v3.8.6/dist/aframe-extras.min.js'></script>"
                          "<script src='https://rawgit.com/jeromeetienne/ar.js/master/aframe/build/aframe-ar.js'></script>"
                          "<script src='https://rawgit.com/ngokevin/aframe-animation-component/master/dist/aframe-animation-component.min.js'></script>"
                          "<script src='https://cdn.rawgit.com/spite/THREE.MeshLine/master/src/THREE.MeshLine.js'></script>"
                          "<script src='https://cdn.rawgit.com/dataarts/dat.gui/master/build/dat.gui.min.js'></script>"
                          "<script src='https://rawgit.com/protyze/aframe-curve-component/master/dist/aframe-curve-component.min.js'></script>"
                          "<script src='https://rawgit.com/protyze/aframe-alongpath-component/master/dist/aframe-alongpath-component.min.js'></script>"
                          "<script src='/js/threex-arliquidmarker.js'></script>"
                          "<script src='/js/refraction-shader.js'></script>"
                          "<script src='/js/liquidmarker.js'></script>"
                          ])
    [:body

     (hiro-message)
     [:a-scene {:artoolkit "sourceType: webcam;"
                :style "pointer-events: none;"
                }
      ;; [:a-entity {}]
      ;; [:a-scene {:artoolkit "debug: true; sourceType: image; sourceUrl: /images/hiro-placeholder.png;"}
      #_[:a-torus {:position "0 0 0"
                   :refraction-shader ""
                   :scale "1 1 1"
                   ;; :animation "property: position; from: 0 0 0; to: -3 0 0; loop: true; easing: linear; dur: 4000;"
                   :animation__rot "property: rotation; from: 0 0 0; to: 360 0 0; loop: true; easing: linear; dur: 4000;"
                   }]
      [:a-marker-camera {:preset "hiro"
                         :liquid-marker ""}]]]))


(defn pak [{global-meta :meta entries :entries}]
  (html
    (root-6-head-element "B&W test :)"
                         ["<script src='//cdn.rawgit.com/donmccurdy/aframe-extras/v3.8.6/dist/aframe-extras.min.js'></script>"
                          "<script src='https://rawgit.com/jeromeetienne/ar.js/master/aframe/build/aframe-ar.js'></script>"
                          "<script src='https://rawgit.com/ngokevin/aframe-animation-component/master/dist/aframe-animation-component.min.js'></script>"
                          ;; "<script src='/js/threex-arliquidmarker.js'></script>"
                          ;; "<script src='/js/refraction-shader.js'></script>"
                          ;; "<script src='/js/liquidmarker.js'></script>"
                          ])
    [:body
     (hiro-message)
     (let [nameList ["cube" "plane" "sphere"]
           basePath "/assets/models/pak_1/"]
       [:a-scene {:artoolkit "sourceType: webcam;"
                  :style "pointer-events: none;"}

        [:a-assets
         ;; Add OBJs:
         (for [baseName nameList]
           [:a-asset-item {:id (str baseName "-obj")
                           :src (str basePath baseName ".obj")}])
         ;; Add MTLs:
         (for [baseName nameList]
           [:a-asset-item {:id (str baseName "-mtl")
                           :src (str basePath baseName ".mtl")}])]
        ;;;;;;;;;
        ;; Scene actually starts:
        ;;;;;;;;;
        (for [baseName nameList]
          [:a-entity {:obj-model (str  "obj: #" baseName "-obj; mtl: #" baseName "-mtl")}])
        [:a-marker-camera {:preset "hiro"
                           :liquid-marker ""}]])]))

(defn ar-bubbles [{global-meta :meta entries :entries}]
  (html
    (root-6-head-element "Some simple boxes as a first AR test!"
                         ["<script src='//cdn.rawgit.com/donmccurdy/aframe-extras/v3.8.6/dist/aframe-extras.min.js'></script>"
                          "<script src='https://rawgit.com/jeromeetienne/ar.js/master/aframe/build/aframe-ar.js'></script>"
                          "<script src='https://rawgit.com/ngokevin/aframe-animation-component/master/dist/aframe-animation-component.min.js'></script>"
                          "<script src='https://cdn.rawgit.com/spite/THREE.MeshLine/master/src/THREE.MeshLine.js'></script>"
                          "<script src='//cdn.rawgit.com/donmccurdy/aframe-physics-system/v1.4.3/dist/aframe-physics-system.min.js'></script>"
                          "<script src='https://cdn.rawgit.com/dataarts/dat.gui/master/build/dat.gui.min.js'></script>"
                          "<script src='https://rawgit.com/protyze/aframe-curve-component/master/dist/aframe-curve-component.min.js'></script>"
                          "<script src='https://rawgit.com/protyze/aframe-alongpath-component/master/dist/aframe-alongpath-component.min.js'></script>"
                          "<script src='/js/refraction-shader.js'></script>"
                          "<script src='/js/ar-bubbles.js'></script>"])
    [:body
     (hiro-message)
     [:a-scene {:artoolkit "sourceType: webcam;"
                :style "pointer-events: none;"
                :physics "debug: false; gravity: 0.4;"}
      [:a-plane#bubbleFeedback {:height "4"
                                ;; :refraction-shader ""
                                :width "2"
                                :color "#2EAFAC"
                                :static-body ""
                                :rotation "-90 0 0"}]
      #_[:a-sphere {:color "#2EAFAC"
                    :refraction-shader ""
                    :dynamic-body ""}]
      [:a-entity {:make-bubbles ""}]
      [:a-marker-camera {:preset "hiro"
                         }]]]))

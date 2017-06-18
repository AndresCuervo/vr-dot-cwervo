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
     [:a-scene {:embedded ""}
      [:a-sky#bgImage {:src "/images/panoramas/earth_equirectangular.jpg"
                       :rotation "0 -130 0"}]
      [:a-camera#camera {:v-bind:fov "zRotation"}]]]))

(defn root-6-head-element
  ([title]
   [:head
    [:title title]
    [:script {:src "https://rawgit.com/aframevr/aframe/149586a/dist/aframe-master.min.js"}]])
  ([title custom-scripts]
   (conj
     (into
       (root-6-head-element title)
       (concat custom-scripts)))))

(defn vr-dat-gui-test [{gloabl-meta :meta entries :entries}]
  (html
    (root-6-head-element "VR-Dat-Gui"
                       ["<script src='https://andrescuervo.github.io/twentyfourseven/js/utils/Detector.js'></script>"
                       "<script src='https://andrescuervo.github.io/twentyfourseven/js/utils/stats.min.js'></script>"
                       "<script src='https://andrescuervo.github.io/twentyfourseven/js/loaders/PLYLoader.js'></script>"
                       "<script src='https://andrescuervo.github.io/twentyfourseven/js/utils/dat.gui.min.js'></script>"
                       "<script src='/js/vr/datguivr.min.js'></script>"
                       "<script src='/js/vr/ViveController.js'></script>"
                       ;; Port the following script and reroute where the assets are coming from!
                       "<script src='/js/scenes/day3.js'></script>"])
    [:body
     [:div#container]
     [:a-scene {:make-point-cloud ""}
      (for [hand ["left" "right"]]
      [:a-entity {:id (str hand "Control") :hand-controls hand}])
      [:a-sky {:material "color: blue;"}]
      [:a-camera {:id "camera"}]]
     [:script {:src "https://andrescuervo.github.io/twentyfourseven/js/controls/TrackballControls.js"}]
     [:script {:src "https://andrescuervo.github.io/twentyfourseven/js/effects/AnaglyphEffect.js"}]
     [:script {:type "x-shader/x-vertex", :id "vertexshader"} "attribute float size;\n    attribute vec3 customColor;\n\n    varying vec3 vColor;\n\n    void main() {\n\n        vColor = customColor;\n\n        vec4 mvPosition = modelViewMatrix * vec4( position, 1.0 );\n\n        gl_PointSize = size * ( 300.0 / -mvPosition.z );\n\n        gl_Position = projectionMatrix * mvPosition;\n\n    }"]
     [:script {:type "x-shader/x-fragment", :id "fragmentshader"} "uniform vec3 color;\n    uniform sampler2D texture;\n\n    varying vec3 vColor;\n\n    void main() {\n\n        gl_FragColor = vec4( color * vColor, 1.0 );\n\n        gl_FragColor = gl_FragColor * texture2D( texture, gl_PointCoord );\n\n    }"]
     [:script {:type "text/javascript"} "<!--\n    function toggle(id) {\n        var e = document.getElementById(id);\n        e.style.display == 'block' ? e.style.display = 'none' : e.style.display = 'block';\n    }\n    "]]))


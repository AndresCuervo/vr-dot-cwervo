(ns site.core
  (:use [hiccup.core :only (html)]
        [hiccup.page :only (html5)]
        )
  (:require [clojure.pprint :refer [pprint]]
            [clojure.string :as str]))

(defn root-head-element
  ([title]
   [:head
    [:title title]
    [:script {:src "https://aframe.io/releases/0.5.0/aframe.min.js"}]])
  ([title custom-scripts]
   (conj
     (into
       (root-head-element title)
       (concat custom-scripts)))))

(defn root-head-element-no-aframe
  [title custom-scripts]
  (conj
    (into
      [:head
       [:title title]
       [:script {:src "/js/three.js"}]]
      (concat custom-scripts))))

(defn build-panorama [title content]
  (html5
    ;; Put all this into a panorama-head fn or def?
    (root-head-element title)
    [:body
     content]))

(defn page [data]
  (let [parent-path (-> data
                        :entry
                        :parent-path)
        ;; It's wild that this isn't equivalent to following form @ runtime????
        ;; title (-> data
        ;;           (:entry)
        ;;           (:tite))
        title (:title (:entry data))
        in-there (not (nil? (re-find (re-pattern "^public/panoramas") parent-path)))
        content (-> data :entry :content)
        panorama? (->> parent-path
                       (re-find (re-pattern "^public/panoramas") ,,,)
                       nil?
                       not)]
    ;; (println "\n ----- \n")
    ;; (println "from the let binding: "title)
    ;; (println ":title, :entry " (:title (:entry data)))
    ;; (println "using threading: " (-> data
    ;;                                  :entry
    ;;                                  :tite))
    ;; (println (str title
    ;;               " in panoramas dir? : "
    ;;               panorama?
    ;;               #_(not (nil? (re-find (re-pattern "^public/panoramas") parent-path)))))
    ;; (println "\n -----")
    ;; If it's in the "panoramas" folder, apply a panoramas specific render-fn???
    (if panorama?
      (build-panorama title content)
      (html5
        [:head
         [:title "Boo"]]
        [:body
         [:div {:style "max-width: 900px; margin: 40px auto;"}
          content]]))))

(defn ply-test [{global-meta :meta
                 entries :entries}]
  (html
    ;; Put all this into a panorama-head fn or def?
    (root-head-element "Ply Test"
                       ["<script src='https://rawgit.com/donmccurdy/aframe-extras/v2.1.1/dist/aframe-extras.loaders.min.js'></script>"])
    [:body
     [:a-scene
      [:a-assets
       [:a-asset-item#treePly {:src "/assets/tree-shelled.bake.ply"}]
       [:a-asset-item#sagePly {:src "/assets/00420.ply"}]
       [:a-asset-item#sageObj {:src "/assets/00420_blue.stl.obj"}]
       [:img {:id "pine-needles-texture" :src "/images/textures/pine-needles-conic.jpg"}]
       [:a-mixin#sageModel {:ply-model "src: #sagePly"
                           :scale "0.05 0.05 0.05"}]
       [:a-mixin#sageObjModel {:obj-model "src: #sageObj"
                           :scale "0.05 0.05 0.05"}]
       [:a-mixin#baseTree {:ply-model "src: #treePly"
                           :rotation "-90 0 0"
                           :specify-position ""
                           :scale "0.05 0.05 0.05"}]]

      [:a-entity#treee {:mixin "baseTree"
                        :position "-3 2 -3"}]

      #_[:a-entity#sage {:mixin "sageModel"
                       :material "src: #pine-needles-texture;
                                 color: blue;"
                       :position "0 2 -3"}]
      [:a-entity#sage-o{:mixin "sageModel"
                        :material "src: #pine-needles-texture;
                                  color: blue;"
                        :position "0 2 -3"}]
      [:a-sky {:material "color: #BEE;"}]
      ]]))

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

(defn point-cloud [{global-meta :meta
                      entries :entries}]
  (html
    ;; Put all this into a panorama-head fn or def?
    (root-head-element "Point Cloudz"
                       ["<script src='https://rawgit.com/donmccurdy/aframe-extras/v2.1.1/dist/aframe-extras.loaders.min.js'></script>"
                       "<script src='https://rawgit.com/ngokevin/aframe-animation-component/master/dist/aframe-animation-component.min.js'></script>"
                        "<script src='/js/Detector.js'></script>"
                        "<script src='/js/stats.min.js'></script>"
                        "<script src='/js/loaders/PLYLoader.js'></script>"
                        "<script src='/js/pointCloud.js'></script>"
                        "<link rel='stylesheet' href='/css/vr-styles.css'></link>"
                        ])
    [:body
     [:div.a-frame-note
      [:span
       "Made with 3D point cloud by"]
      [:a {:href "http://sagejenson.com/"} " Sage Jenson, "]
      [:span
       "for "]
      [:a {:href "http://andrescuervo.github.io/twentyfourseven/"} "couchlet"]
      [:span
        "."]]
     [:a-scene {:make-point-cloud ""}
      #_[:a-assets
       ;; [:a-asset-item#treePly {:src "/assets/tree-shelled.bake.ply"}]
       [:a-asset-item#cloudPly {:src "/assets/cloud_no_binary.ply"}]
       [:a-mixin#cloud {:ply-model "src: #cloudPly"
                           :rotation "-90 0 0"
                           :specify-position ""
                           :scale "0.05 0.05 0.05"}]]
      [:a-sky#bgImage {:src "/images/panoramas/earth_equirectangular.jpg"
                       :material "color: #2FACAC;"
                       :animation__fade "property: color;
                                        from: #2EAFAC;
                                        to: #000000;
                                        dir: alternate;
                                        dur: 20000;
                                        loop: true;"
                       :rotation "0 -130 0"}]
      #_[:a-entity#cloudEntity {:mixin "cloud"
                              :material "color: blue;"}]
      #_[:a-entity#pointLight {:pointlight ""}]
      [:a-camera#cam {}]]]))

(defn three-test [{global-meta :meta
                      entries :entries}]
  (html
    (root-head-element-no-aframe "Three BG Test"
                       ["<script src=''></script>"
                        "<script src='/js/Detector.js'></script>"
                        "<script src='/js/stats.min.js'></script>"
                        "<script src='/js/loaders/PLYLoader.js'></script>"
                        "<script src='/js/threeTest.js'></script>"
                        "<link rel='stylesheet' href='/css/vr-styles.css'></link>"
                        ])
    [:body
     [:div#container]
     [:script#vertexShader {:type "x-shader/x-vertex"}
      "uniform float time;
      uniform vec2 resolution;
      void main() {
      gl_Position = vec4( position, 1.0 );
      }"]
     [:script#fragmentShader {:type "x-shader/x-fragment"}
      "uniform float time;
      uniform vec2 resolution;
      void main() {
      float x = mod(time + gl_FragCoord.x, 20.) < 10. ? 1. : 0.;
      float y = mod(time + gl_FragCoord.y, 20.) < 10. ? 1. : 0.;
      gl_FragColor = vec4(vec3(min(x, y)), 1.);
      }"]
     [:section
      (for [n (range 0 100)]
        [:div "hello"])
      ]]))

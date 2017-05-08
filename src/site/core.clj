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
    (root-head-element "Ply Test"
                       ["<script src='https://rawgit.com/donmccurdy/aframe-extras/v2.1.1/dist/aframe-extras.loaders.min.js'></script>"
                        "<script src='https://cdnjs.cloudflare.com/ajax/libs/dropzone/4.3.0/dropzone.js'></script>"
                        "<script src='https://unpkg.com/vue'></script>"
                        "<script src='/js/drag-and-drop.js'></script>"
                        "<link rel='stylesheet' href='/css/drag-and-drop.css'></link>"
                        ])
    [:body
     [:div#hud
      [:form#my-awesome-dropzone.dropzone {:action "#"}
       [:span {:class "dz-message"} "Click on or drop files in this pink rectangle to change the background."]]
      #_[:div
       [:div "FOV:"]
       ;; Set to initial FOV number!
       ;; Don't forget to change if initial changes, or wire that up ... see, this is why using Vue.js would have been so much easier
       [:p#fovNum "80"]
       ;; Ugh, putting a-scene into the Vue element duplicates it or something, causing everything to break :(
       [:input {:type "range"
                :value 80
                :min 1
                :max 160
                :oninput "changeFOV(this.value)"
                ;; :v-model "zRotation"
                }]]

      ;; common params for a function to automate making this shit:
      ;; param 1: base id: #hud
      ;; param 2 : title
      ;; param 3: initialValue (got to both title with its own class and as value of input)
      ;; param 4: inputType (usually range, but you can experiment!)
      ;; param 5: range (an array with 'min' and 'max' attributes! e.g. {'min' : -360, 'max' : 360})
      ;; param 6: oninput (text value of function to call to maniupulate stuff!)
      #_[:div
       [:div "rotation Y"]
       ;; Set to initial FOV number!
       ;; Don't forget to change if initial changes, or wire that up ... see, this is why using Vue.js would have been so much easier
       [:p#xNum "-130"]
       ;; Ugh, putting a-scene into the Vue element duplicates it or something, causing everything to break :(
       [:input {:type "range"
                :value -130
                :min -360
                :max 360
                :oninput "changeRotation('y', this.value)"
                ;; :v-model "zRotation"
                }]]
      ]
     [:a-scene {:embedded ""}
      [:a-sky#bgImage {:src "/images/panoramas/earth_equirectangular.jpg"
                       :rotation "0 -130 0"}]
      [:a-camera#camera {:v-bind:fov "zRotation"}]]]))

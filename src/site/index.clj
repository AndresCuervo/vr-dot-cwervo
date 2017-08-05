(ns site.index
  (:use [hiccup.core :only (html)]
        [hiccup.page :only (html5)]
        [clojure.string :as s-t-r]))

(defn make-social-list-item [href icon-string display-name]
        [:li [:a {:href href :target "_blank"} [:i {:class (str "fa fa-" icon-string) :aria-hidden "true"}] (str " " display-name)]])

(defn render [{global-meta :meta
               entries :entries}]
  (html
    [:head
     [:title (:site-title global-meta)]
     [:meta {:charset "utf-8"}]
     [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0, user-scalable=no"}]
     [:link {:rel "stylesheet"
             :href "/css/main.css"}]
     [:link {:rel "stylesheet"
             :href "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css"}]
     [:script {:src "https://aframe.io/releases/0.5.0/aframe.min.js"}]]
    [:body
     [:div#wrapper.center
      [:section
       [:span
        "👋🏼 I'm Andres"
        [:a {:href "https://cwervo.com/"} " Cwervo"]]

       [:p
        "Here are some links to other VR things I've done:"
        [:ul.vr_list
         [:li.details-wrapper
          [:details
          [:summary "Some WebVR scenes"]
          [:li
           [:a {:href "/scenes/meme-test/"}
            "A collage meme in VR!"]]
          [:li
           [:a {:href "/scenes/itlt/"}
            "Imagine Trees Like These"]]
          [:li
           [:a {:href "/scenes/bump-test/"}
            "Meme bump-map test!"]]
          [:li
           [:a {:href "/scenes/vr-capstone.html"}
            "VR Capstone"]]
          [:li
           [:a {:href "/scenes/squiggles-vr-capstone.html"}
            "Squiggly VR Capstone"]]
          [:li
           [:a {:href "/scenes/anotherWorld.html"}
            "[AnotherWorld]"]]
          [:li
           [:a {:href "/scenes/point-cloud/"}
            "Baby's first point cloud test"]]]]

         [:li.details-wrapper
          [:details
           [:summary "Web-based AR experiences"]
           [:li
            [:a {:href "/scenes/ar-medusa-refraction/"}
             "A bust of Medusa made refractive in AR 💡🔮"]]
           [:li
            [:a {:href "/scenes/ar-bubbles/"}
             "AR bubbles demo (tap or click on your screen!)"]]
           [:li
            [:a {:href "/scenes/ar-refraction/"}
             "My first AR refraction test"]
            ", based off "
            [:a {:href "https://twitter.com/jerome_etienne"} "Jerome Etienne's work."]]
           [:li
            [:a {:href "/scenes/ar-refraction/"}
             "(WIP) AR Reflection box"]]
           [:li
            [:a {:href "https://ar-aframe-shader-test.glitch.me/"}
             "A-Frame + AR.js with a custom displacement shader"]
            " written by  "
            [:a {:href "http://sagejenson.com"} "Sage Jenson."]]]]

         [:li.details-wrapper
          [:details
           [:summary "Panoramas and such"]
           (for [entry entries]
             (when (and (contains? entry :title)
                        ;; Wow, I can add my own metadata keys, so fun!
                        (not (:draft? entry)))
               [:li
                [:a {:href (:permalink entry)}
                 (:title entry)]]))]]

         [:li.details-wrapper
          [:details
           [:summary "Some utils for WebVR I've been cooking up!"]
           [:li
            [:a {:href "/utils/drag-and-drop/"}
             "Drag & Drop a VR photo"]]
           [:li
            [:a {:href "/utils/gltf-tests/"}
             "Testing a few gltf models in A-Frame !"]]
           [:li
            [:a {:href "/utils/simple-vr-dat-gui-example/"}
             "My VR Dat.gui Component Basic Example"]]
           [:li
            [:a {:href "/utils/vr-dat-gui/"}
             "VR Dat.gui test"]]]]
         ]]
       [:span#contact "Contact:"]
       [:ul.vr_list
        (make-social-list-item "https://instagram.com/cwervo.gif/" "instagram" "Instagram")
        (make-social-list-item "https://twitter.com/acwervo/" "twitter" "Twitter")
        (make-social-list-item "mailto:acwervo+vr.cwervo.com@gmail.com" "envelope-o" "Email")
        ]]]

     [:a-scene.background-scene
      [:a-sky {:src "/images/panoramas/abandoned-room.jpg"}]]]))

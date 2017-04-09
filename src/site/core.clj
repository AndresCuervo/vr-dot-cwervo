(ns site.core
  (:require [hiccup.core :as h]
            [hiccup.page :as hp]
            [clojure.pprint :refer [pprint]]
            [clojure.string :as str]
            ))

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
  (hp/html5
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
    (println (str title
                  " in panoramas dir? : "
                  panorama?
                  #_(not (nil? (re-find (re-pattern "^public/panoramas") parent-path)))))
    (println "\n -----")
    ;; If it's in the "panoramas" folder, apply a panoramas specific render-fn???
    (if panorama?
      (build-panorama title content)
      (hp/html5
        [:head
         [:title "Boo"]]
        [:body
         [:div {:style "max-width: 900px; margin: 40px auto;"}
          content]]))))

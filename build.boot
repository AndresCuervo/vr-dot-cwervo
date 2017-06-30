(set-env!
  :source-paths #{"src" "content" "resources"}
  :dependencies '[[perun "0.4.2-SNAPSHOT" :scope "test"]
                  [hiccup "1.0.5"]
                  [deraen/boot-sass "0.2.1"]
                  [deraen/boot-livereload "0.1.2"]
                  ;; When this merges:

                  ;; https://github.com/pandeiro/boot-http/pull/61
                  ;; upgrade to 0.7.6 or 0.7.7 or w/e
                  [pandeiro/boot-http "0.7.0"]
                  [deraen/boot-livereload "0.2.0"]])


(require '[io.perun :refer :all]
         '[deraen.boot-sass :refer [sass]]
         '[pandeiro.boot-http :refer [serve]]
         '[site.index :as index]
         '[deraen.boot-livereload :refer [livereload]])

(deftask base-build
  "Generic build tasks, with no custom option, gets build every time"
  []
  (comp
    ;; (collection :renderer 'site.core/page :page "index.html")
    (markdown)
    (render :renderer 'site.core/page)
    ;; (collection :renderer 'site.index/collect-pages :page "not-index-for-now.html")
    ;; (collection :renderer 'site.index/render :page "vr-capstone.html")

    ;; It JUST works with arbitrary paths <3 FUCK YEAH
    ;; (static :renderer 'site.index/render :page "homepage.html")
    (collection :renderer 'site.index/render :page "index.html")

    (static :renderer 'site.capstone.index/render :page "scenes/vr-capstone.html")
    (static :renderer 'site.capstone.offshoots/render-squiggles :page "scenes/squiggles-vr-capstone.html")
    (static :renderer 'site.capstone.offshoots/render-leah :page "scenes/anotherWorld.html")
    (static :renderer 'site.core/ply-test :page "scenes/ply-test.html")
    (static :renderer 'site.utils/drag-and-drop :page "utils/drag-and-drop/index.html")
    (static :renderer 'site.core/point-cloud :page "scenes/point-cloud/index.html")
    (static :renderer 'site.core/three-test :page "scenes/three-test/index.html")
    (static :renderer 'site.capstone.offshoots/itlt :page "scenes/itlt/index.html")
    (static :renderer 'site.vive.base/controls-test :page "vive/controls-test/index.html")
    (static :renderer 'site.utils/vr-dat-gui-test :page "utils/vr-dat-gui/index.html")
    (static :renderer 'site.utils/meme-bump-map-test :page "scenes/bump-test/index.html")
    ;; (images-dimensions) ;; Just print the meta data for images
    (target)
    ))

(deftask dev-build
  "Dev build task, doesn't compress SCSS to make debugging easier."
  []
  (comp
    (sass)
    (base-build)))

(deftask build
  "Production build task, does compress SCSS."
  []
  (comp
    (sass :output-style :compressed)
    (base-build)))

(deftask dev
  []
  (comp (repl :server true)
        (watch)
        (dev-build)
        (livereload :asset-path "public" :filter #"\.(css|html|js)$")
        (serve :port 8888 :resource-root "public")))

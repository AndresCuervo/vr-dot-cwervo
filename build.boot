(set-env!
  :source-paths #{"src" "content" "resources"}
  :dependencies '[[perun "0.4.2-SNAPSHOT" :scope "test"]
                  [hiccup "1.0.5"]
                  [deraen/boot-sass "0.2.1"]
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

(deftask build
  "Build test blog. This task is just for testing different plugins together."
  []
  (comp
    (sass)
    ;; (collection :renderer 'site.core/page :page "index.html")
    (markdown)
    (render :renderer 'site.core/page)
    ;; (collection :renderer 'site.index/render :page "vr-capstone.html")
    (static :renderer 'site.index/render :page "vr-capstone.html")
    ;; this is confusing b/c it overrides the index.markdown file, fix the semantics
    (static :renderer 'site.capstone.index/render);; :page "index.html")
    (images-dimensions) ;; Just print the meta data for images
    (target)
    ))

(deftask dev
  []
  (comp (watch)
        (build)
        (serve :resource-root "public")))

(defproject pushit-clj "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring-server "0.4.0"]
                 [reagent "0.7.0"]
                 [reagent-utils "0.2.1"]
                 [ring "1.6.1"]
                 [ring/ring-defaults "0.3.0"]
                 [ring/ring-json "0.4.0"]
                 [compojure "1.6.0"]
                 [org.clojure/data.json "0.2.6"]
                 [http-kit "2.2.0"]
                 [hiccup "1.0.5"]
                 [yogthos/config "0.8"]
                 [org.clojure/clojurescript "1.9.671"
                  :scope "provided"]
                 [secretary "1.2.3"]
                 [cljs-http "0.1.43"]
                 [cljsjs/kjua "0.1.1-1"]
                 [venantius/accountant "0.2.0"
                  :exclusions [org.clojure/tools.reader]]
                 [ring-webjars "0.2.0"]
                 [org.webjars/bootstrap "3.3.7"]
                 ]

  :plugins [[lein-environ "1.0.2"]
            [lein-cljsbuild "1.1.5"]
            [lein-asset-minifier "0.4.6"
             :exclusions [org.clojure/clojure]]]

  :ring {:handler pushit-clj.handler/app
         :uberwar-name "pushit-clj.war"}

  :min-lein-version "2.5.0"

  :uberjar-name "pushit-clj.jar"

  :main pushit-clj.server

  :clean-targets ^{:protect false}
  [:target-path
   [:cljsbuild :builds :app :compiler :output-dir]
   [:cljsbuild :builds :app :compiler :output-to]]

  :source-paths ["src/clj" "src/cljc"]
  :resource-paths ["resources" "target/cljsbuild"]

  :minify-assets
  [[:css {:source "resources/public/css/site.css"
          :target "resources/public/css/site.min.css"}]]

  :cljsbuild
  {:builds {:min
            {:source-paths ["src/cljs" "src/cljc" "env/prod/cljs"]
             :compiler
             {:output-to "target/cljsbuild/public/js/app.js"
              :output-dir "target/uberjar"
              :optimizations :whitespace
              :pretty-print  true}}
            :app
            {:source-paths ["src/cljs" "src/cljc" "env/dev/cljs"]
             :figwheel {:on-jsload "pushit-clj.core/mount-root"}
             :compiler
             {:main "pushit-clj.dev"
              :asset-path "/js/out"
              :output-to "target/cljsbuild/public/js/app.js"
              :output-dir "target/cljsbuild/public/js/out"
              :source-map true
              :optimizations :none
              :pretty-print  true}}
            :test
            {:source-paths ["src/cljs" "src/cljc" "test/cljs"]
             :compiler {:main pushit-clj.doo-runner
                        :asset-path "/js/out"
                        :output-to "target/test.js"
                        :output-dir "target/cljstest/public/js/out"
                        :optimizations :whitespace
                        :pretty-print true}}

            :devcards
            {:source-paths ["src/cljs" "src/cljc" "env/dev/cljs"]
             :figwheel {:devcards true}
             :compiler {:main "pushit-clj.cards"
                        :asset-path "js/devcards_out"
                        :output-to "target/cljsbuild/public/js/app_devcards.js"
                        :output-dir "target/cljsbuild/public/js/devcards_out"
                        :source-map-timestamp true
                        :optimizations :none
                        :pretty-print true}}
            }
   }


  :figwheel
  {:http-server-root "public"
   :server-port 3449
   :nrepl-port 7002
   :nrepl-middleware ["cemerick.piggieback/wrap-cljs-repl"
                      ]
   :css-dirs ["resources/public/css"]
   :ring-handler pushit-clj.handler/app}

  :less {:source-paths ["src/less"]
         :target-path "resources/public/css"}


  :profiles {:dev {:repl-options {:init-ns pushit-clj.repl
                                  :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

                   :dependencies [[binaryage/devtools "0.9.4"]
                                  [ring/ring-mock "0.3.1"]
                                  [ring/ring-devel "1.6.1"]
                                  [prone "1.1.4"]
                                  [figwheel-sidecar "0.5.11"]
                                  [org.clojure/tools.nrepl "0.2.13"]
                                  [com.cemerick/piggieback "0.2.2"]
                                  [devcards "0.2.3" :exclusions [cljsjs/react]]
                                  [pjstadig/humane-test-output "0.8.2"]
                                  ]

                   :source-paths ["env/dev/clj"]
                   :resource-paths ["env/dev"]
                   
                   :plugins [[lein-figwheel "0.5.11"]
                             [lein-doo "0.1.6"]
                             
                             [lein-less "1.7.5"]]

                   :injections [(require 'pjstadig.humane-test-output)
                                (pjstadig.humane-test-output/activate!)]

                   :env {:dev true}}

             :uberjar {:hooks [leiningen.less,minify-assets.plugin/hooks]
                       :source-paths ["env/prod/clj"]
                       :resource-paths ["env/prod"]
                       :prep-tasks ["compile" ["cljsbuild" "once" "min"]]
                       :env {:production true}
                       :aot :all
                       :omit-source true}})

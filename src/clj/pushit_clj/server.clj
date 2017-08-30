(ns pushit-clj.server
  (:use org.httpkit.server)
  (:require [pushit-clj.handler :refer [app]]
            [config.core :refer [env]]
            ;;[ring.adapter.jetty :refer [run-jetty]]
            )
  (:gen-class))

 (defn -main [& args]
   (let [port (Integer/parseInt (or (env :port) "3000"))]
     (run-server app {:port port :join? false})))

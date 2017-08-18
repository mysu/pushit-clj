(ns pushit-clj.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [not-found resources]]
            [pushit-clj.middleware :refer [wrap-middleware]]
            [config.core :refer [env]]
            [pushit-clj.pages :refer [loading-page cards-page]]
            ))


(defroutes routes
  (GET "/" [] (loading-page))
  (GET "/about" [] (loading-page))
  (GET "/cards" [] (cards-page))
  (resources "/")
  (not-found "Not Found"))


(def app (wrap-middleware #'routes))

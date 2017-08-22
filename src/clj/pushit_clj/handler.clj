(ns pushit-clj.handler
  (:require [compojure.core :refer [GET ANY defroutes routes context]]
            [compojure.route :refer [not-found resources]]
            [compojure.handler :as handler]
            [config.core :refer [env]]
            [ring.middleware.json :as jsonware]
            [pushit-clj.middleware :refer [wrap-middleware]]
            [pushit-clj.pages :refer [loading-page cards-page]]
            [pushit-clj.rest.push :refer [new-push-id]]
            [pushit-clj.json-wrapper :refer [json-rsp]]
            ))


(defroutes site-routes
  (GET "/" [] (loading-page))
  (GET "/about" [] (loading-page))
  (GET "/cards" [] (cards-page))
  (resources "/")
  (not-found "Not Found"))

(def site (handler/site site-routes))

(defroutes rest-routes
  (context "/rest" []
    (GET "/push" [] (json-rsp (new-push-id)))
    (ANY "*"  [] (json-rsp "Not Found" 404))
  )
)
(def rest-api (handler/site rest-routes))

(def app (routes rest-api site))


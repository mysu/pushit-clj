(ns pushit-clj.handler
  (:require [compojure.core :refer [GET ANY POST defroutes routes context]]
            [compojure.route :refer [not-found resources]]
            [compojure.handler :as handler]
            [config.core :refer [env]]
            [ring.middleware.json :as jsonware]
            [ring.middleware.webjars :refer [wrap-webjars]]
            [pushit-clj.middleware :refer [wrap-middleware]]
            [pushit-clj.pages :refer [loading-page cards-page]]
            [pushit-clj.rest.push :refer [new-push-id connect push-msg]]
            [pushit-clj.json-wrapper :refer [json-rsp]]
            ))


(defroutes site-routes
  (GET "/" [] (loading-page))
  (GET "/about" [] (loading-page))
  (GET "/cards" [] (cards-page))
  (GET "/ws/:push-id" {{push-id :push-id} :params :as request} (connect request push-id))
  (resources "/")
  (not-found "Not Found"))

(def site (wrap-webjars (handler/site site-routes)))

(defroutes rest-routes
  (context "/rest" []
    (GET "/push" [] (json-rsp (new-push-id)))
    (POST "/push" {{push-id :pushId msg :msg} :params} (json-rsp (push-msg push-id msg))) 
    (ANY "*"  [] (json-rsp "Not Found" 404))
  )
)
(def rest-api (handler/site rest-routes))

(def app (routes rest-api site))


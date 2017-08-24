(ns pushit-clj.core
    (:require [reagent.core :as reagent :refer [atom]]
              [secretary.core :as secretary :include-macros true]
              [cljs.core.async :refer [<!]]
              [cljs-http.client :as http]
              [accountant.core :as accountant]
              [cljsjs.kjua]
    )
  (:require-macros [cljs.core.async.macros :refer [go]])
  )

(def pushid (atom "Loading"))
;; -------------------------
;; Views

(defn pushid-view []
  [:div#push-id
   [:h3 @pushid]
   ]
  )

(defn home-page []
  [:div [:h2 "Welcome to pushit-clj"]
   [:div [:a {:href "/about"} "go to about page"]]
   (pushid-view)
   ])

(defn about-page []
  [:div [:h2 "About pushit-clj"]
   [:div [:a {:href "/"} "go to the home page"]]])

;; -------------------------
;; Routes

(def page (atom #'home-page))

(defn current-page []
  [:div [@page]])

(secretary/defroute "/" []
  (reset! page #'home-page))

(secretary/defroute "/about" []
  (reset! page #'about-page))

;; -------------------------
;; Initialize app

(defn post-init []
  (go (let [rsp (<! (http/get "rest/push"))]
        (reset! pushid (get-in rsp [:body :pushId]))
        (.appendChild (.getElementById js/document "push-id") (js/kjua "{text: 'pushid', mode: 'plain', label: 'PushIt}"))
        )))

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root)
  (post-init)
  )

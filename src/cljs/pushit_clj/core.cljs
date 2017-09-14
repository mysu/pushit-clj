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

(def pushid (atom nil))
(def messages (atom () ))
(def log-msgs (atom () ))

;; -------------------------
;; PushIt

(defn connectws []


  (def connection (js/WebSocket. (str "ws://localhost:3449/ws/" @pushid)))

  (set! (.-onopen connection)
        (fn [e]
          (swap! log-msgs conj "Connection established" )))

  (set! (.-onerror connection)
        (fn [e]
          (swap! log-msgs conj "Connection error")))

  (set! (.-onmessage connection)
        (fn [e]
          (let [received-msg (.parse js/JSON (.-data e))]
            (swap! messages conj received-msg.msg))))
  )

;; -------------------------
;; Views

(defn message-item [msg]
  [:a {:href msg :target "_blank"} msg])

(defn item-list [title msg-list]
  [:div
   [:h3 title]
   [:ul
    (for [msg msg-list]
      ^{:key msg}[:li (message-item msg)])
    ]]
  )

(defn pushid-view []
  [:div#push-id
   [:h3 @pushid]
   ]
  )

(defn home-page []
  [:div [:h2 "Welcome to pushit-clj"]
   [:div [:a {:href "/about"} "go to about page"]]
   (pushid-view)
   (item-list "Message list" @messages)
   (item-list "Log" @log-msgs)
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
        (let [newid (get-in rsp [:body :pushId])]
          (reset! pushid newid)
          (.appendChild (.getElementById js/document "push-id") (js/kjua (clj->js {:text (str newid) })))
          (connectws)
          ))))

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


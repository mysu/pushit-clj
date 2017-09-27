(ns pushit-clj.rest.push
  (:use org.httpkit.server
        [clojure.data.json :only [json-str]]
        )
  (:require [config.core :refer [env]] ))

(def sessions (atom {}))

(defn uuid [] (str (java.util.UUID/randomUUID)))

(defn msg-received [msg]
  (str "")
  )

(defn new-push-id []
  {:pushId (uuid)
   :host (:basehost env) 
  })

(defn connect [req push-id]
    (with-channel req channel
      (swap! sessions assoc push-id channel)
      (on-receive channel #'msg-received)
      (on-close channel (fn [status]
                          (swap! sessions dissoc push-id)))
      (send! channel (json-str {:msg "connected"}))
      ))

(defn push-msg [push-id msg]
  (let [channel (get @sessions push-id)]
    (if (not (nil? channel))
      (do
        (send! channel (json-str {:msg msg}))
        {:status "200"}
        )
      {:status "404"
       :msg "push-id does not exist"
       :sessions (str  @sessions)
       })))


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
   :port (or (:port env) "3000") 
  })

(defn connect [req push-id]
    (with-channel req channel
      (swap! sessions assoc push-id channel)
      (on-receive channel #'msg-received)
      (on-close channel (fn [status]
                          (swap! sessions dissoc push-id)))
      ))

(defn push-msg [push-id msg ]
  (let [channel (get @sessions push-id)]
    (if (not (nil? channel))
      (do
        (send! channel (json-str {:msg msg}))
        {:status "200"}
        )
      {:status "400"
       :msg (str "push-id does not exist: " push-id ", msg: "  msg)
       })))


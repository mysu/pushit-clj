(ns pushit-clj.rest.push
  (:use org.httpkit.server))

(def sessions (atom {}))

(defn uuid [] (str (java.util.UUID/randomUUID)))

(defn msg-received [msg]
  (str "")
  )

(defn new-push-id []
  {:pushId (uuid)})

(defn connect [push-id]
  (with-channel push-id channel
    (swap! sessions assoc push-id channel)
    (on-receive channel #'msg-received)
    (on-close channel (fn [status]
                        (swap! sessions dissoc push-id)))
    ))

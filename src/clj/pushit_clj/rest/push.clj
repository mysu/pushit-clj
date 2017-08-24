(ns pushit-clj.rest.push)

(defn uuid [] (str (java.util.UUID/randomUUID)))

(defn new-push-id []
  {:pushId (uuid)})

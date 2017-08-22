(ns pushit-clj.json-wrapper
  (:require [clojure.data.json :as json]))

(defn json-rsp [data & status] 
  {:status (or status 200)
   :headers {"Content-Type" "application/json"}
   :body (json/write-str data)
   })


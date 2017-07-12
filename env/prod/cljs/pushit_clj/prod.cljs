(ns pushit-clj.prod
  (:require [pushit-clj.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)

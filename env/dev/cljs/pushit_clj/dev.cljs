(ns ^:figwheel-no-load pushit-clj.dev
  (:require
    [pushit-clj.core :as core]
    [devtools.core :as devtools]))

(devtools/install!)

(enable-console-print!)

(core/init!)

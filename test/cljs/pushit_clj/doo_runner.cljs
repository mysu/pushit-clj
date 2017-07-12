(ns pushit-clj.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [pushit-clj.core-test]))

(doo-tests 'pushit-clj.core-test)

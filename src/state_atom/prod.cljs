(ns state-atom.prod
  (:require [state-atom.core :as app]))

(defn main []
  ;; Make production adjustments here
  (app/main (atom {}) (js/document.getElementById "app")))

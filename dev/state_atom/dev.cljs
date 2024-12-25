(ns state-atom.dev
  (:require [dataspex.core :as dataspex]
            [nexus.action-log :as action-log]
            [state-atom.core :as app]))

(defonce store (atom {}))
(defonce el (js/document.getElementById "app"))
(dataspex/inspect "App state" store)
(action-log/inspect)

(defn ^:dev/after-load main []
  ;; Add additional dev-time tooling here
  (app/main store el))

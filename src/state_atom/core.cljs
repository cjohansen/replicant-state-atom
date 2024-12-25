(ns state-atom.core
  (:require [clojure.walk :as walk]
            [replicant.dom :as r]
            [state-atom.ui :as ui]))

(defn interpolate-actions [event actions]
  (walk/postwalk
   (fn [x]
     (case x
       :event/target.value (.. event -target -value)
       ;; Add more cases as needed
       x))
   actions))

(defn execute-actions [store actions]
  (doseq [[action & args] actions]
    (case action
      :store/assoc-in (apply swap! store assoc-in args)
      (println "Unknown action" action "with arguments" args))))

(defn main [store el]
  (add-watch
   store ::render
   (fn [_ _ _ state]
     (r/render el (ui/render-page state))))

  (r/set-dispatch!
   (fn [event-data actions]
     (->> actions
          (interpolate-actions
           (:replicant/dom-event event-data))
          (execute-actions store))))

  ;; Trigger the initial render
  (swap! store assoc :app/started-at (js/Date.)))

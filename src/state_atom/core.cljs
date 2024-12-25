(ns state-atom.core
  (:require [nexus.registry :as nxr]
            [replicant.dom :as r]
            [state-atom.ui :as ui]))

(nxr/register-system->state! deref)

(defn dissoc-in [m path]
  (if (= 1 (count path))
    (dissoc m (first path))
    (update-in m (butlast path) dissoc (last path))))

(defn conj-in [m path v]
  (update-in m path conj v))

(defn update-state [state [op & args]]
  (case op
    :assoc-in (apply assoc-in state args)
    :dissoc-in (apply dissoc-in state args)
    :conj-in (apply conj-in state args)))

(nxr/register-effect! :store/save
  ^:nexus/batch
  (fn [_ store ops]
    (swap! store
           (fn [state]
             (reduce update-state state ops)))))

(nxr/register-action! :store/assoc-in
  (fn [_ path value]
    [[:store/save :assoc-in path value]]))

(nxr/register-action! :counter/inc
  (fn [state path]
    [[:store/assoc-in path (inc (get-in state path))]]))

(defn main [store el]
  (add-watch
   store ::render
   (fn [_ _ _ state]
     (r/render el (ui/render-page state))))

  (r/set-dispatch!
   (fn [dispatch-data actions]
     (nxr/dispatch store dispatch-data actions)))

  ;; Trigger the initial render
  (swap! store assoc :app/started-at (js/Date.)))

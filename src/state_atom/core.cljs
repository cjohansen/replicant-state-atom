(ns state-atom.core
  (:require [nexus.registry :as nxr]
            [replicant.alias :as alias]
            [replicant.dom :as r]
            [state-atom.router :as router]
            [state-atom.ui :as ui]))

(defn routing-anchor [attrs children]
  (let [routes (-> attrs :replicant/alias-data :routes)]
    (into [:a (cond-> attrs
                (:ui/location attrs)
                (assoc :href (router/location->url routes
                                                   (:ui/location attrs))))]
          children)))

(alias/register! :ui/a routing-anchor)

(defn find-target-href [e]
  (some-> e .-target
          (.closest "a")
          (.getAttribute "href")))

(defn get-current-location []
  (->> js/location.href
       (router/url->location router/routes)))

(nxr/register-effect! :effects/update-url
  (fn [_ {:keys [routes]} new-location old-location]
    (if (router/essentially-same? new-location old-location)
      (.replaceState js/history nil "" (router/location->url routes new-location))
      (.pushState js/history nil "" (router/location->url routes new-location)))))

(defn route-click [e system]
  (let [href (find-target-href e)]
    (when-let [location (router/url->location (:routes system) href)]
      (.preventDefault e)
      (nxr/dispatch system nil [[:actions/navigate location]]))))

(nxr/register-system->state! (comp deref :store))

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
  (fn [_ {:keys [store]} ops]
    (swap! store
           (fn [state]
             (reduce update-state state ops)))))

(nxr/register-action! :store/assoc-in
  (fn [_ path value]
    [[:store/save :assoc-in path value]]))

(nxr/register-action! :counter/inc
  (fn [state path]
    [[:store/assoc-in path (inc (get-in state path))]]))

(nxr/register-action! :actions/navigate
  (fn [state location]
    [[:effects/update-url location (:location state)]
     [:store/assoc-in [:location] location]]))

;; Bootstrap

(defn main [store el]
  (let [system {:store store
                :routes router/routes}]
    (add-watch store ::render
     (fn [_ _ _ state]
       (r/render el (ui/render-page state) {:alias-data {:routes router/routes}})))

    (r/set-dispatch!
     (fn [dispatch-data actions]
       (nxr/dispatch system dispatch-data actions)))

    (js/document.body.addEventListener "click" #(route-click % system))

    (js/window.addEventListener
     "popstate"
     (fn [_]
       (nxr/dispatch system nil
        [[:store/assoc-in [:location] (get-current-location)]])))

    ;; Trigger the initial render
    (swap! store assoc
           :app/started-at (js/Date.)
           :location (get-current-location))))

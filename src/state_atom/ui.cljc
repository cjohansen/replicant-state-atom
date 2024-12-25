(ns state-atom.ui)

(defn render-page [state]
  (let [clicks (:clicks state 0)]
    [:div
     [:h1 "Hello world"]
     [:p "Started at " (:app/started-at state)]
     [:button
      {:on {:click [[:store/assoc-in [:clicks] (inc clicks)]]}}
      "Click me"]
     (when (< 0 clicks)
       [:p
        "Button was clicked "
        clicks
        (if (= 1 clicks) " time" " times")])]))

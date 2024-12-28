(ns state-atom.ui)

(defn render-frontpage [state]
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
        (if (= 1 clicks) " time" " times")])
     [:p [:ui/a {:ui/location {:location/page-id :pages/episode
                               :location/params {:episode/id "s2e1"}}}
          "Episode 1"]]]))

(defn render-episode [{:keys [location]}]
  [:main
   [:h1 "Episode " (-> location :location/params :episode/id)]
   (if (-> location :location/hash-params :description)
     (list
      [:p "It's an episode of Parens of the dead"]
      [:ui/a {:ui/location (update location :location/hash-params dissoc :description)}
       "Hide description"])
     [:ui/a {:ui/location (assoc-in location [:location/hash-params :description] "1")}
      "Show description"])
   [:p
    [:ui/a {:ui/location {:location/page-id :pages/frontpage}}
     "Back to frontpage"]]])

(defn render-not-found [_]
  [:h1 "Not found"])

(defn render-page [state]
  (let [f (case (:location/page-id (:location state))
            :pages/frontpage render-frontpage
            :pages/episode render-episode
            render-not-found)]
    (f state)))

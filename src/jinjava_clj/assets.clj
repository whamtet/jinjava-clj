(ns jinjava-clj.assets
  (:require
    [hiccup.core :refer [html]]))

(def css-to-print (atom nil))
(def js-to-print (atom nil))
(defn reset-stack! []
  (reset! css-to-print [])
  (reset! js-to-print []))

(defn- break-after [x] (list x "\n"))
(defn- condense [header items]
  (if (-> items first string?)
    (list [header (map break-after items)])
    items))

(defn- render [items header]
  (when-let [items (->> items (filter identity) not-empty)]
    (->> items
         (partition-by string?)
         (mapcat #(condense header %))
         (map break-after)
         html)))

(defn total-css [] (render @css-to-print :style))
(defn total-js [] (render @js-to-print :script))

(defn append-css [css]
  (swap! css-to-print conj css))
(defn append-js [js]
  (swap! js-to-print conj js))
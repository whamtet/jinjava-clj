(ns jinjava-clj.assets
  (:require
    [clojure.string :as string]
    [hiccup.core :refer [html]]))

(def css-to-print (atom nil))
(def js-to-print (atom nil))
(defn reset-stack! []
  (reset! css-to-print [])
  (reset! js-to-print []))

(def ^:private empty-str? #(-> % .trim empty?))
(defn total-css [] #_
  (let [css (remove empty-str? css-to-print)]
    (when (not-empty css)
      (html [:style "\n" (string/join "\n" css) "\n"] "\n"))))
(defn total-js [] #_
  (let [js (remove empty-str? js-to-print)]
    (when (not-empty js)
      (html [:script "\n" (string/join "\n" js) "\n"] "\n"))))

(defn append-css [css]
  (swap! css-to-print conj css))
(defn append-js [js]
  (swap! js-to-print conj js))
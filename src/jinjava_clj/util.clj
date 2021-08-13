(ns jinjava-clj.util
 (:require
   [clojure.walk :as walk]))

(defn dot-m [m]
  (if (map? m)
    (reduce
     (fn [m [k v]]
      (assoc-in m (vec (.split k "\\.")) v)) {} m)
    m))

(defn dot-map [& args]
  (walk/postwalk dot-m (apply array-map args)))
(defn dotize [m]
  (walk/postwalk dot-m m))
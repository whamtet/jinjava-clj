(ns jinjava-clj.data.names
  (:require
    [clojure.data.json :as json]))

(defn slurp-fields [name]
  (->> name
       (format "resources/korumsandbox/modules/%s.module/fields.json")
       slurp
       json/read-str))

(defn names-map [m]
  (cond
    (map? m)
    (let [children (mapcat names-map (vals m))
          {:strs [name label]} m]
      (if (and name label)
        (conj children [label name])
        children))
    (coll? m) (mapcat names-map m)))

(-> "korum-slider" slurp-fields names-map prn)
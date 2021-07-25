(ns jinjava-clj.data
  (:require
    [clojure.string :as string]
    [clojure.walk :as walk]))

(defn slurp-lines [f]
  (as-> f $
        (slurp $)
        (.split $ "\n")
        (filter #(-> % .trim not-empty) $)))

(defn parse-attrs [^String attrs]
  (let [attrs (.substring attrs 13 (dec (count attrs)))]
    (into {}
          (for [item (.split attrs ", ")
                :let [[k v] (string/split item #"=" 2)]
                :when (and
                       (not (contains? #{"null" nil} v))
                       (not (.startsWith k "<")))]
            [(-> k .trim keyword) (.trim v)]))))

(defn process-attrs [^String s]
  (-> s
      parse-attrs
      (dissoc :children)
      (update :level #(Integer/parseInt %))))

(defn split-true [f s]
  (reduce (fn [v x]
            (if (f x)
              (conj v [x])
              (update v (dec (count v)) conj x)))
          []
          s))

(defn re-group [[item & descendants]]
  (let [descendants (map #(update % :level dec) descendants)]
    (vec
     (list*
      (dissoc item :level)
      (->> descendants (split-true #(-> % :level zero?)) (map re-group))))))

(defn recompress
  ([items] (recompress 0 items))
  ([level [item & children]]
   (if children
     (assoc item :level level :children (map #(recompress (inc level) %) children))
     (assoc item :level level))))

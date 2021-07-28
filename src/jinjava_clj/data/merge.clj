(ns jinjava-clj.data.merge
  (:require
    [jinjava-clj.data.merge-str :as merge-str]
    [pl.danieljanus.tagsoup :refer [parse]])
  (:import
    java.io.File))

(def template (parse (File. "tmp/template.html")))
(def data (parse (File. "tmp/data.html")))
(def actual-data (drop 2 (get data 2)))

(def actual-template (get-in template [2 2]))
(def datum (get-in data [2 2]))

(defn flatten-together [a b]
  (cond
    (string? a) (merge-str/merge-str a b)
    (map? a) (mapcat flatten-together (vals a) (vals b))
    (coll? a) (mapcat flatten-together a b)))

(defn merge-data [template datum]
  (reduce
   (fn [m [k v]] (assoc-in m (-> k .trim (.split "\\.") vec) v))
   {}
   (flatten-together template datum)))

(spit "resources/blog_recent.edn"
      (pr-str (map #(merge-data actual-template %) actual-data)))
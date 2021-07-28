(ns jinjava-clj.data.merge-str
 (:require
  [clojure.string :as string]))

(def template "a{{t1}}b{{t2}}c{{t3}}e")
(def data "ad1bd2cd3e")

(defn replaces [s m]
 (reduce (fn [s [a b]] (.replace s a b)) s m))
(defn- re-pattern-str [s]
 (replaces s {"(" "\\(" ")" "\\)"}))

(defn- split1 [s split]
 (string/split s (-> split re-pattern-str re-pattern) 2))
(defn- drop-str [^String s ^String suffix]
 (.substring s 0 (- (count s) (count suffix))))
;; assume every template has a space between it

(defn merge-str [template data]
 (when (.contains template "{{")
  (let [[_ prefix t1 suffix t-rest] (re-find #"(.*?)\{\{(.*?)\}\}([^\{]*)(.*)" template)
        head (.substring data (count prefix))]
   (if (empty? t-rest)
    [[t1 (drop-str head suffix)]]
    (let [[d1 d2] (split1 head suffix)]
     (cons [t1 d1] (merge-str t-rest d2)))))))
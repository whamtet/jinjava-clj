(ns jinjava-clj.snippets
  (:require
    [clojure.java.io :as io]))

(defmacro defresource [s]
  `(def ~s (-> ~(str s ".html") io/resource slurp)))

(defresource logo)
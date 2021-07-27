(ns jinjava-clj.snippets
  (:require
    [clojure.java.io :as io]))

(defmacro defresource [s]
  `(def ~s (-> ~(str s ".html") io/resource slurp)))
(defresource logo)
(defresource form-home)
(defresource jquery)

(defmacro defedn [s]
  `(defn ~s [] (-> ~(str s ".edn") io/resource slurp read-string)))
(defedn menu)
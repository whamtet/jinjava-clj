(ns jinjava-clj.snippets
  (:require
    [clojure.java.io :as io]
    [jinjava-clj.stack :as stack]))

(defmacro defresources [& ss]
  `(do ~@(for [s ss] `(def ~s (-> ~(str s ".html") io/resource slurp)))))
(defresources logo)
(defresources form-home form-home-cta)
(defresources jquery)
(defresources header-prefix)
(defresources header-suffix)

(defmacro defedn [s]
  `(defn ~s [] (-> ~(str s ".edn") io/resource slurp read-string)))
(defedn menu)

(def small-data
  {"homepage"
   {"textarea" "Subscribe for our latest news stories"
    "form" form-home
    "ctasignup"
    {"form" form-home-cta}}})

(defn get-snippet [k]
  (get-in small-data (conj (stack/get-stack) k)))
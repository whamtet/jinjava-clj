(ns jinjava-clj.snippets
  (:require
    [clojure.java.io :as io]
    [jinjava-clj.stack :as stack]))

(defmacro defresource [s]
  `(def ~s (-> ~(str s ".html") io/resource slurp)))
(defresource logo)
(defresource form-home)
(defresource jquery)
(defresource header-prefix)
(defresource header-suffix)

(defmacro defedn [s]
  `(defn ~s [] (-> ~(str s ".edn") io/resource slurp read-string)))
(defedn menu)

(def small-data
  {"homepage"
   {"textarea" "Subscribe for our latest news stories"
    "form" form-home
    "ctasignup"
    {"form" "fffuck"}}})

(defn get-snippet [k]
  (get-in small-data (conj (stack/get-stack) k)))
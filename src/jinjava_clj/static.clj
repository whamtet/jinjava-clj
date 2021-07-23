(ns jinjava-clj.static
  (:gen-class
    :name org.jinjava.CLJStatic
    :methods [^:static [requireCss [String] String]
              ^:static [getAssetUrl [String] String]
              ^:static [requireJs [String] String]]))

(defn -requireCss [path] "")
(defn -getAssetUrl [path] "")
(defn -requireJs [path] "")
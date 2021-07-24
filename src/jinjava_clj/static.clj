(ns jinjava-clj.static
  (:gen-class
    :name org.jinjava.CLJStatic
    :methods [^:static [requireCss [String] String]
              ^:static [getAssetUrl [String] String]
              ^:static [requireJs [String] String]
              ^:static [blogPopularPosts [String Long] java.util.List]
              ^:static [blogTags [String Long] java.util.List]]))

(defn -requireCss [path] "")
(defn -getAssetUrl [path] "")
(defn -requireJs [path] "")
(defn -blogPopularPosts [path limit] [])
(defn -blogTags [blog-name limit] [])
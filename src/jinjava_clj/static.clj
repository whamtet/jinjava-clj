(ns jinjava-clj.static
  (:require
    [clojure.walk :as walk]
    [jinjava-clj.snippets :as snippets])
  (:gen-class
    :name org.jinjava.CLJStatic
    :methods [^:static [requireCss [String] String]
              ^:static [getAssetUrl [String] String]
              ^:static [requireJs [String] String]
              ^:static [blogPopularPosts [String Long] java.util.List]
              ^:static [blogTags [String Long] java.util.List]
              ^:static [menu [String] Object]
              ^:static [blogRecentTagPosts [String String Long] java.util.List]]))

(defn -requireCss [path] "")
(defn -getAssetUrl [path] "")
(defn -requireJs [path] "")
(defn -blogPopularPosts [path limit] [])
(defn -blogTags [blog-name limit] [])
(defn -menu [_] (walk/stringify-keys snippets/menu))
(defn -blogRecentTagPosts [blog tags limit] [])
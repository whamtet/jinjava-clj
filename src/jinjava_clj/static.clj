(ns jinjava-clj.static
  (:require
    [clojure.java.io :as io]
    [clojure.walk :as walk]
    [hiccup.core :refer [html]]
    [jinjava-clj.snippets :as snippets])
  (:import
    java.io.File)
  (:gen-class
    :name org.jinjava.CLJStatic
    :methods [^:static [requireCss [String] String]
              ^:static [getAssetUrl [String] String]
              ^:static [requireJs [String] String]
              ^:static [blogPopularPosts [String Long] java.util.List]
              ^:static [blogTags [String Long] java.util.List]
              ^:static [menu [String] Object]
              ^:static [blogRecentTagPosts [String String Long] java.util.List]
              ^:static [inspect [Object] Object]]))

(defn -requireCss [href] (html [:link {:type "text/css" :rel "stylesheet" :href href}] "\n"))
(defn -getAssetUrl [path]
  (let [path (.replace path "../" "")
        src (-> (str "korumsandbox/" path) io/resource slurp)
        target (File. (str "out/" path))]
    (-> target .getParentFile .mkdirs)
    (io/copy src target)
    path))

(defn -requireJs [path] (html [:script {:src path}] "\n"))
(defn -blogPopularPosts [path limit] [])
(defn -blogTags [blog-name limit] [])
(defn -menu [_] (walk/stringify-keys (snippets/menu)))
(defn -blogRecentTagPosts [blog tags limit] [])
(defn -inspect [x] (prn 'x (class x) x) "")
(ns jinjava-clj.module
  (:require
    [clojure.java.io :as io]
    [jinjava-clj.snippets :as snippets])
  (:import
    com.hubspot.jinjava.lib.tag.Tag
    (com.hubspot.jinjava.util
     HelperStringTokenizer)))

(defn- clean-str [^String s]
  (reduce #(.replace %1 %2 "") s ["\"" "'" ","]))
(defn- helper-map [i ^String s]
  (let [[a b] (.split (clean-str s) "=")]
    [(cond b (keyword a) (zero? i) :name :else i)
     (or b a)]))
(defn parse-helpers [node]
  (->> node
       .getHelpers
       HelperStringTokenizer.
       iterator-seq
       (map-indexed helper-map)
       (into {})))

(defn resolve-path [path] ;; TODO make better
  (if (.startsWith path "/korumsandbox")
    (.substring path 1)
    (as-> path $
          (.split $ "modules")
          (second $)
          (str "korumsandbox/modules" $))))

(defn slurp-module [path]
  (-> path
      resolve-path
      (str ".module/module.html")
      io/resource
      slurp))

(def module-tag
  (reify Tag
         (getName [this] "module")
         (getEndTagName [this])
         (interpret [this node interpreter]
                    (let [{:keys [name path] :as info} (parse-helpers node)]
                      (prn 'info info)
                      (case name
                        "site_logo" snippets/logo
                        (let [raw (slurp-module path)
                              node (.parse interpreter raw)]
                          (.render interpreter node false)))))))
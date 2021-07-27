(ns jinjava-clj.module
  (:require
    [clojure.java.io :as io]
    [jinjava-clj.assets :as assets]
    [jinjava-clj.snippets :as snippets]
    [jinjava-clj.tag :as tag])
  (:import
    com.hubspot.jinjava.lib.tag.Tag))

(defn resolve-path [path] ;; TODO make better
  (if (.startsWith path "/korumsandbox")
    (.substring path 1)
    (as-> path $
          (.split $ "modules")
          (second $)
          (str "korumsandbox/modules" $))))

(defn slurp-module [path]
  (let [parent-folder (-> path resolve-path (str ".module"))]
    (for [suffix [".html" ".css" ".js"]]
      (-> (format "%s/module%s" parent-folder suffix)
          io/resource
          slurp))))

(def module-tag
  (reify Tag
         (getName [this] "module")
         (getEndTagName [this])
         (interpret [this node interpreter]
                    (let [{:keys [name path] :as info} (tag/parse-helpers node)]
                      (prn 'info info)
                      (case name
                        "site_logo" snippets/logo
                        (let [[html css js] (slurp-module path)
                              node (.parse interpreter html)]
                          (assets/append-css css)
                          (assets/append-js js)
                          (.render interpreter node false)))))))
(ns jinjava-clj.core
  (:require
    [camel-snake-kebab.core :refer [->camelCase]])
  (:import
    com.hubspot.jinjava.loader.ResourceLocator
    com.hubspot.jinjava.lib.tag.Tag
    com.hubspot.jinjava.lib.tag.IncludeTag
    com.hubspot.jinjava.lib.fn.ELFunctionDefinition
    com.hubspot.jinjava.Jinjava
    org.jinjava.CLJStatic))

(def jinjava (Jinjava.))
(def context (.getGlobalContext jinjava))

(defn reify-tag [s]
  (reify Tag
         (getName [this] s)
         (getEndTagName [this])
         (interpret [this node interpreter] "")))

(defn el-def [name args]
  (ELFunctionDefinition.
    ""
    name
    CLJStatic
    (->camelCase name)
    (if (empty? args) (make-array Class 0) (into-array args))))

(def global-partial
  (proxy [IncludeTag] []
         (getName [] "global_partial")))

(def resource-locator
  (reify ResourceLocator
         (getString [this full-name encoding interpreter]
                    (prn 'full-name full-name)
                    (as-> full-name $
                          (.replace $ "path=\"../" "")
                          (.replace $ "\"" "")
                          (str "resources/korumsandbox/templates/" $)
                          (slurp $)))))

(doseq [t ["module" "form" "textarea" "require_css" "image_src" "menu"]]
  (.registerTag context (reify-tag t)))
(.registerTag context global-partial)

(def functions {"require_css" [String]
                "get_asset_url" [String]
                "require_js" [String]})
(doseq [[name args] functions]
  (.registerFunction context (el-def name args)))

(.setResourceLocator jinjava resource-locator)

(println
   "hi"
   (.render jinjava
            (slurp "resources/korumsandbox/templates/homepage.html")
            {}))

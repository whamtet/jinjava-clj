(ns jinjava-clj.core
  (:require
    [camel-snake-kebab.core :refer [->camelCase]]
    [clojure.java.io :as io]
    [clojure.java.shell :refer [sh]]
    [jinjava-clj.assets :as assets]
    [jinjava-clj.module :as module]
    [jinjava-clj.snippets :as snippets]
    [jinjava-clj.stack :as stack]
    [jinjava-clj.static :as static]
    [jinjava-clj.tag :as tag])
  (:import
    com.hubspot.jinjava.loader.ResourceLocator
    com.hubspot.jinjava.lib.tag.Tag
    com.hubspot.jinjava.lib.tag.IncludeTag
    com.hubspot.jinjava.lib.fn.ELFunctionDefinition
    com.hubspot.jinjava.Jinjava
    com.hubspot.jinjava.JinjavaConfig
    com.hubspot.jinjava.util.HelperStringTokenizer
    java.io.File
    org.jinjava.CLJStatic))

(def config (-> (JinjavaConfig/newBuilder)
                (.withEnableRecursiveMacroCalls true)
                .build))
(def jinjava (Jinjava. config))
(def context (.getGlobalContext jinjava))

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
                    (as-> full-name $
                          (.replace $ "path=\"../" "")
                          (.replace $ "\"" "")
                          (str "korumsandbox/templates/" $)
                          (io/resource $)
                          (slurp $)))))

(tag/add-to-context context)

(doseq [t [global-partial module/module-tag]]
  (.registerTag context t))

(def functions {"require_css" [String]
                "get_asset_url" [String]
                "require_js" [String]
                "blog_popular_posts" [String Long]
                "blog_tags" [String Long]
                "menu" [String]
                "blog_recent_tag_posts" [String String Long]
                "inspect" [Object]})
(doseq [[name args] functions]
  (.registerFunction context (el-def name args)))

(.setResourceLocator jinjava resource-locator)

(defn- before [s tag & rest] ;;TODO
  (let [[a b] (.split s tag)]
    (apply str (concat [a] rest [tag b]))))

(def ^:private base-info
  {"standard_header_includes" "standard_header_includes"
   "standard_footer_includes" "standard_footer_includes"})

(defn- header []
  (str snippets/jquery "\n"
       snippets/header-prefix "\n"
       (assets/total-css)
       snippets/header-suffix "\n"))
(def ^:private footer assets/total-js)

(defn render-template [f stack-base m]
  (assets/reset-stack!)
  (as-> f s
        (io/resource s)
        (slurp s)
        (stack/with-stack stack-base (.render jinjava s (merge base-info m)))
        (.replace s "standard_header_includes" (header))
        (.replace s "standard_footer_includes" (footer))))
(defn spit-template [template stack-base out m]
  (binding [static/*out-dir* out]
    (spit (str out "/index.html") (render-template template stack-base m))))

(spit-template "korumsandbox/templates/insights.html" "insights" "out/insights" {})
(spit-template "korumsandbox/templates/insights2.html" "insights" "out/insights2" {})
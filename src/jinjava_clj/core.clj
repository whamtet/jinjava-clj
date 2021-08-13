(ns jinjava-clj.core
  (:require
    [camel-snake-kebab.core :refer [->camelCase]]
    [clojure.java.io :as io]
    [clojure.java.shell :refer [sh]]
    [jinjava-clj.assets :as assets]
    [jinjava-clj.module :as module]
    [jinjava-clj.snippets :as snippets]
    [jinjava-clj.stack :as stack]
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

(def templates
  '("korumsandbox/templates/blog-archive.html" "korumsandbox/templates/case-studies2.html" "korumsandbox/templates/team.html" "korumsandbox/templates/search_results.html" "korumsandbox/templates/homepage.html" "korumsandbox/templates/people.html" "korumsandbox/templates/axceller.html" "korumsandbox/templates/contact.html" "korumsandbox/templates/mls.html" "korumsandbox/templates/howwework.html" "korumsandbox/templates/our consultants customer.html" "korumsandbox/templates/case-studies.html" "korumsandbox/templates/404.html" "korumsandbox/templates/consultants copy.html" "korumsandbox/templates/our consultants.html" "korumsandbox/templates/landing-page.html" "korumsandbox/templates/consultants.html" "korumsandbox/templates/our-story.html" "korumsandbox/templates/pandt.html" "korumsandbox/templates/clients-home.html" "korumsandbox/templates/single-post.html" "korumsandbox/templates/locations.html"))
(def template "korumsandbox/templates/insights.html")

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

(defn render-template [f m]
  (let [[_ stack-base] (re-find #".*/(.+)\.html" template)]
    (assets/reset-stack!)
    (as-> f s
          (io/resource s)
          (slurp s)
          (stack/with-stack stack-base (.render jinjava s (merge base-info m)))
          (.replace s "standard_header_includes" (header))
          (.replace s "standard_footer_includes" (footer)))))

;(sh "./pull.sh")
(spit "out/index.html" (render-template template {}))
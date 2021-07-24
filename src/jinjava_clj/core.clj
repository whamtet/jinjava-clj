(ns jinjava-clj.core
  (:require
    [camel-snake-kebab.core :refer [->camelCase]]
    [clojure.java.io :as io]
    ;[hiccup.core :refer [html]]
    [jinjava-clj.module :as module])
  (:import
    com.hubspot.jinjava.loader.ResourceLocator
    com.hubspot.jinjava.lib.tag.Tag
    com.hubspot.jinjava.lib.tag.IncludeTag
    com.hubspot.jinjava.lib.fn.ELFunctionDefinition
    com.hubspot.jinjava.Jinjava
    (com.hubspot.jinjava.util
     HelperStringTokenizer
     #_WhitespaceUtils)
    java.io.File
    org.jinjava.CLJStatic))

(def jinjava (Jinjava.))
(def context (.getGlobalContext jinjava))

(defn reify-tag [[name end-name]]
  (reify Tag
         (getName [this] name)
         (getEndTagName [this] end-name)
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
                    (as-> full-name $
                          (.replace $ "path=\"../" "")
                          (.replace $ "\"" "")
                          (str "korumsandbox/templates/" $)
                          (io/resource $)
                          (slurp $)))))

(doseq [t [["form"]
           ["textarea"]
           ["image_src"]
           ["menu"]
           ["icon"]
           ["text"]
           ["rich_text"]
           ["dnd_area" "end_dnd_area"]
           ["related_blog_posts"]]]
  (.registerTag context (reify-tag t)))

(doseq [t [global-partial module/module-tag]]
  (.registerTag context t))

(def functions {"require_css" [String]
                "get_asset_url" [String]
                "require_js" [String]
                "blog_popular_posts" [String Long]
                "blog_tags" [String Long]
                "menu" [String]
                "blog_recent_tag_posts" [String String Long]})
(doseq [[name args] functions]
  (.registerFunction context (el-def name args)))

(.setResourceLocator jinjava resource-locator)

(def templates
  '("korumsandbox/templates/blog-archive.html" "korumsandbox/templates/case-studies2.html" "korumsandbox/templates/team.html" "korumsandbox/templates/search_results.html" "korumsandbox/templates/homepage.html" "korumsandbox/templates/people.html" "korumsandbox/templates/axceller.html" "korumsandbox/templates/contact.html" "korumsandbox/templates/mls.html" "korumsandbox/templates/howwework.html" "korumsandbox/templates/our consultants customer.html" "korumsandbox/templates/case-studies.html" "korumsandbox/templates/404.html" "korumsandbox/templates/consultants copy.html" "korumsandbox/templates/our consultants.html" "korumsandbox/templates/landing-page.html" "korumsandbox/templates/consultants.html" "korumsandbox/templates/our-story.html" "korumsandbox/templates/pandt.html" "korumsandbox/templates/clients-home.html" "korumsandbox/templates/single-post.html" "korumsandbox/templates/locations.html"))
(def template "korumsandbox/templates/homepage.html")

(defn render-template [f]
  (.render jinjava (-> f io/resource slurp) {}))

(spit "index.html" (render-template template))
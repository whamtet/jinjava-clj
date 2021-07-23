(ns jinjava-clj.core
  (:import
    com.hubspot.jinjava.loader.ResourceLocator
    com.hubspot.jinjava.lib.tag.Tag
    com.hubspot.jinjava.lib.tag.IncludeTag
    com.hubspot.jinjava.Jinjava
    org.jinjava.CLJStatic))

(def jinjava (Jinjava.))
(def context (.getGlobalContext jinjava))

(defn reify-tag [s]
  (reify Tag
         (getName [this] s)
         (getEndTagName [this])
         (interpret [this node interpreter] "")))

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
                          (str "src/main/resources/korumsandbox/templates/" $)
                          (slurp $)))))

(doseq [t ["module" "form" "textarea" "require_css"]]
  (.registerTag context (reify-tag t)))
(.registerTag context global-partial)
#_(.registerFunction context
                   (ELFunctionDefinition2. "ff" (fn [])))

(.setResourceLocator jinjava resource-locator)
#_
(println
 "hi"
 (.render jinjava
          (slurp "src/main/resources/korumsandbox/templates/homepage.html")
          {}))

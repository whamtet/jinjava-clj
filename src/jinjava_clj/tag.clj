(ns jinjava-clj.tag
  (:require
    [jinjava-clj.snippets :as snippets]
    [jinjava-clj.stack :as stack])
  (:import
    com.hubspot.jinjava.lib.tag.Tag
    com.hubspot.jinjava.util.HelperStringTokenizer))

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

(defn reify-tag [[name end-name]]
  (case name
    (reify Tag
           (getName [this] name)
           (getEndTagName [this] end-name)
           (interpret [this node interpreter]
                      (let [info (parse-helpers node)]
                        (or (-> info :name snippets/get-snippet)
                            (snippets/get-snippet name)
                            (do
                              (println "interpreting" name (stack/get-stack) (parse-helpers node))
                              "")))))))

(defn add-to-context [context]
  (doseq [t [["form"]
             ["textarea"]
             ["image_src"]
             ["menu"]
             ["icon"]
             ["text"]
             ["rich_text"]
             ["dnd_area" "end_dnd_area"]
             ["related_blog_posts"]]]
    (.registerTag context (reify-tag t))))
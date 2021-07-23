(ns jinjava-clj.static
  (:gen-class
    :name org.jinjava.CLJStatic
    :methods [^:static [name [] java.lang.String]]))

(defn -name [] "My name is Foo")
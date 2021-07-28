(ns jinjava-clj.stack)

(def stack (atom []))

(defmacro with-stack [label & body]
  `(let [_# (swap! stack conj ~label)
         res# (do ~@body)]
    (swap! stack pop)
    res#))
(defn get-stack [] @stack)
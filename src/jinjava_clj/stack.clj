(ns jinjava-clj.stack)

(def stack (atom []))

(defmacro with-stack [label & body]
  `(do
    (swap! stack conj ~label)
    (try ~@body (finally (swap! stack pop)))))

(defn get-stack [] @stack)
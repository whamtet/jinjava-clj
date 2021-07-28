(ns jinjava-clj.snippets
  (:require
    [clojure.java.io :as io]
    [jinjava-clj.stack :as stack]))

(defmacro defresources [& ss]
  `(do ~@(for [s ss] `(def ~s (-> ~(str s ".html") io/resource slurp)))))
(defresources logo)
(defresources form-home form-home-cta)
(defresources jquery)
(defresources header-prefix)
(defresources header-suffix)
(defresources footer-menu1 footer-menu2 footer-menu3)

(defmacro defedn [s]
  `(defn ~s [] (-> ~(str s ".edn") io/resource slurp read-string)))
(defedn menu)
(defedn blog-recent)

(def small-data
  {"homepage"
   {"footer_cp" "Copyright © Korum Consulting Limited (t/a KorumLegal) 2020. All Rights Reserved."
    "textarea" "Subscribe for our latest news stories"
    "form" form-home
    "ctasignup" {"form" form-home-cta}
    "image_src" "https://www.korumlegal.com/hubfs/Logo@2x.png"
    "footer_menu1" footer-menu1
    "footer_menu2" footer-menu2
    "footer_menu3" footer-menu3}})

(defn get-snippet [k]
  (get-in small-data (conj (stack/get-stack) k)))
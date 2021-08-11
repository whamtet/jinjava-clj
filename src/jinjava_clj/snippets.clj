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
    "footer_menu3" footer-menu3}
   "insights"
   {"mls_head_img" "https://f.hubspotusercontent00.net/hubfs/2155006/New%20Home%20Page%20Banner%201.png"
    "mls_head_title" "Insights"
    "footer_image_src" "https://www.korumlegal.com/hubfs/Logo@2x.png"
    "text_footer_mid" "The KorumLegal Group is a legal solutions company, providing flexible, quality, value-driven legal solutions to meet our customers’ needs. The KorumLegal entities do not provide legal advice. See our Terms of Use for further details."
    "footer_menu1" footer-menu1
    "footer_menu2" footer-menu2
    "footer_menu3" footer-menu3
    "footer_cp" "Copyright © Korum Consulting Limited (t/a KorumLegal) 2020. All Rights Reserved."}})

(defn get-snippet [k]
  (get-in small-data (conj (stack/get-stack) k)))

(defn dot-map [& args]
  (reduce
   (fn [m [k v]]
     (assoc-in m (vec (.split k "\\.")) v))
   {}
   (partition 2 args)))

(def home-data
  (dot-map
   "module.ctasignup_form.form_id" 123
   "module.korum_slide_group"
   [{"image_field" ;; this itself supports multiple images
     [{"src" "https://www.korumlegal.com/hubfs/New%20Home%20Page%20Banner%201%20%282%29.jpg"}]
     "text_field1" "We partner with you to support your legal needs </br>throughout Asia and beyond"
     "text_fieldcta" "Submit a query"
     "title" "Your gateway </br>to Asia."
     "banner_fieldcta" {"url" {"href" "https://www.korumlegal.com/contact-us"}}}
    #_{"image_field" ;; this itself supports multiple images
       [{"src" "https://f.hubspotusercontent00.net/hubfs/2155006/sunny%20analytics.jpg"}]
       "text_field1" "We partner with you to support your legal needs </br>throughout Asia and beyond"
       "text_fieldcta" "Submit a query"
       "title" "Your gateway </br>to Asia."
       "banner_fieldcta" {"url" {"href" "https://www.korumlegal.com/contact-us"}}}]))


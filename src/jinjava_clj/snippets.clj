(ns jinjava-clj.snippets
  (:require
    [clojure.java.io :as io]
    [clojure.string :as string]
    [jinjava-clj.stack :as stack]
    [jinjava-clj.util :as util]))

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

(def home-data
  (util/dot-map
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

(def blog-posts
  (util/dotize
   [{"absolute_url" "https://blog.korumlegal.com/gc-spotlight-tilly-mcadden-senior-legal-counsel-hackerone"
     "featured_image" "https://blog.korumlegal.com/hubfs/Newsletter%20pics-5.png"
     "blog_author.display_name" "KorumLegal"
     "name" "GC Spotlight: Tilly McAdden, Senior Legal Counsel, HackerOne"
     "post_summary" "Tilly McAdden is the Senior Legal Counsel of HackerOne, a vulnerability coordination and bug bounty platform. She gives us the inside story in this interesting GC Spotlight. Let's go right in!"}
    {"absolute_url" "https://blog.korumlegal.com/why-you-need-to-value-inclusion-before-you-can-achieve-diversity"
     "featured_image" "https://blog.korumlegal.com/hubfs/2-Jul-23-2021-12-12-55-49-PM.png"
     "blog_author.display_name" "Natasha Norton"
     "name" "Why You Need to Value Inclusion Before You Can Achieve Diversity"
     "post_summary" "With the growing focus from boards and organisations on Environmental, Social and Governance [ESG] and within this the subset of Diversity, Equity & Inclusion, it is with some degree of scepticism blaaa blaa blaa"}]))

(def case-studies
  (util/dotize
    [{"absolute_url" "https://todo"
      "featured_image" "https://www.korumlegal.com/hubfs/Untitled%20design%20(10).jpg"
      "name" "Asia Retail Conglomerate"
      "post_summary" "After conducting a review of various technology solutions for contract lifecycle management, the customer was dissatisfied with their own findings. They approached KorumLegal to expedite the process to identify a relevant tech solution, and then provide ongoing support for the implementation."}
     {"absolute_url" "https://todo"
      "featured_image" "https://www.korumlegal.com/hubfs/Untitled%20design%20(11).jpg"
      "name" "Swiss Private Wealth and Asset Management Bank"
      "post_summary" "Customer had an extremely lean legal team who were dealing with complex M&A, counselling and business-as-usual document review without any technology tools beyond their email inbox. Customer wanted to implement technology to improve velocity and visibility, and to adapt and implement automated workflows within their processes."}]))
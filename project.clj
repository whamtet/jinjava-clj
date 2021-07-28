(defproject jinjava-clj "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [com.hubspot.jinjava/jinjava "2.5.9"]
                 [camel-snake-kebab "0.4.2"]
                 [hiccup "1.0.5"]
                 [clj-tagsoup/clj-tagsoup "0.3.0" :exclusions [org.clojure/clojure]]
                 [org.clojure/data.xml "0.0.8"]]
  :aot [jinjava-clj.static]
  :repl-options {:init-ns jinjava-clj.core})

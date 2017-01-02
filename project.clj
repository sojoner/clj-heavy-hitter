(defproject org.clojars.sojoner/clj-heavy-hitter "0.1.0-SNAPSHOT"
  :description "A Clojure library designed to sample the top-N data items with a probabilistic data structure called Count Min Sketch."
  :url "https://github.com/sojoner/clj-heavy-hitter"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha14"]
                 [org.clojure/data.priority-map "0.0.7"]
                 [com.google.guava/guava "17.0"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [org.clojure/tools.logging "0.2.6"]
                 ]
  :plugins [[lein-cloverage "1.0.9"]]
  :scm {:name "git"
        :url "git@github.com:sojoner/clj-heavy-hitter.git"})

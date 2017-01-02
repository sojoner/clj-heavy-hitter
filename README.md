# clj-heavy-hitter

A Clojure library designed to sample the top-N data items with a probabilistic data structure called [Count Min Sketch](https://en.wikipedia.org/wiki/Count%E2%80%93min_sketch).

## Get it

[![Clojars Project](https://img.shields.io/clojars/v/org.clojars.sojoner/clj-heavy-hitter.svg)](https://clojars.org/org.clojars.sojoner/clj-heavy-hitter)

## Build Status

[![Build Status](https://travis-ci.org/sojoner/clj-heavy-hitter.svg?branch=master)](https://travis-ci.org/sojoner/clj-heavy-hitter)


## Usage

Read about it for example here [heavy hitters](http://www.slideshare.net/mikiobraun/realtime-data-analysis-patterns) 


```Clojure
    (:require [clj-heavy-hitter.core :refer :all])
    
    ; init the scatcher structure
    (reset! hitter (priority-map))
    (reset! min-sketch (make-array Integer/TYPE 10N 1000N))
    (swap! state assoc
           :top-n 5
           :number-of-hashfn 10N
           :bucket-size 1000N)
    
    ; start scatching
    (sketch-value a_string)
    (add-to-hitter a_string)
    
    ; collect your results
    (let [result (reduce (fn [map [k _]]
                               (assoc map k (get-sketched-value k)))
                             {}
                             @hitter)
              sorted (sort-by val > result)]
          (println result)
          (println sorted)    
```

For some use cases it makes sense to reset the structure data structures.

```Clojure
(reset! hitter (priority-map))
(reset! min-sketch (make-array Integer/TYPE 10N 1000N))
```


## License

Copyright © 2017 Hagen Tönnies

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

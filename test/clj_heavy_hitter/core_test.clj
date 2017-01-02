(ns clj-heavy-hitter.core-test
  (:use [clojure.data.priority-map])
  (:require [clojure.test :refer :all]
            [clj-heavy-hitter.core :refer :all]))

(deftest a-test
  (testing "Can we sketch a strings"
    ; init the scatcher structure
    (reset! hitter (priority-map))
    (reset! min-sketch (make-array Integer/TYPE 10N 1000N))
    (swap! state assoc
           :top-n 5
           :number-of-hashfn 10N
           :bucket-size 1000N)

    ; scatch the sample data
    (let [sample-data (reduce #(conj %1 (str "whats the sample " (rand-int %2))) (list) (range 100))]
      (doseq [a_string sample-data]
        (do
          (sketch-value a_string)
          (add-to-hitter a_string))))

    ; collect scatches
    (let [result (reduce (fn [map [k _]]
                           (assoc map k (get-sketched-value k)))
                         {}
                         @hitter)
          sorted (sort-by val > result)]
      (println result)
      (println sorted)
      ; asserts
      (is (= 5 (count result))))))

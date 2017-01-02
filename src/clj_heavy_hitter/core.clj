(ns clj-heavy-hitter.core
  (:use [clojure.data.priority-map])
  (:require [clojure.tools.logging :as log]
            [clojure.math.numeric-tower :as math])
  (:import (com.google.common.hash Hashing)
           (com.google.common.base Charsets))
  (:gen-class))

(def hitter (atom (priority-map)))
(def state (atom
             {:top-n 5
              :bucket-size 1000N
              :number-of-hashfn 10N}))
(def min-sketch (atom (make-array Integer/TYPE 10M 1000N)))

(def h1
  (let [m (Hashing/murmur3_128)]
    (fn ^Long [^String s]
      (-> (doto (.newHasher m)
            (.putString s Charsets/UTF_8))
          (.hash)
          (.asLong)))))

(def h2
  (let [m (Hashing/murmur3_128 5N)]
    (fn ^Long [^String s]
      (-> (doto (.newHasher m)
            (.putString s Charsets/UTF_8))
          (.hash)
          (.asLong)))))

(defn- get-hash-function [hash-index]
  (fn [^String string] (+ (h1 ^String string) (* hash-index (h2 ^String string)))))

(defn- h [hash-index]
  (fn [^String string]
    (mod (math/abs ((get-hash-function hash-index) ^String string)) (:bucket-size @state))))

(defn sketch-value [^String a-token]
  (doseq [hash-index (range (:number-of-hashfn @state))]
    (let [bucket-index ((h (bigint hash-index)) a-token)
          temp-sketch-value (aget @min-sketch hash-index bucket-index)]
      (aset-int @min-sketch hash-index
                bucket-index
                (inc temp-sketch-value)))))

(defn add-to-hitter [^String a-token]
  (let [length (count @hitter)
        result (get @hitter a-token)]
    (if (not (nil? result))
      (let [new-value (inc result)]
        (swap! hitter assoc a-token new-value))
      (if (< length (:top-n @state))
        (swap! hitter assoc a-token 1)
        (let [last-element-count (peek @hitter)]
          (swap! hitter pop)
          (swap! hitter assoc a-token (inc (second last-element-count))))))))

(defn get-sketched-value [^String a-token]
  (let [bucket-values (atom '())]
    (doseq [hash-index (range (:number-of-hashfn @state))]
      (let [bucket-index ((h (bigint hash-index)) a-token)
            current-values @bucket-values
            value (aget @min-sketch hash-index bucket-index)
            ]
        (reset! bucket-values (conj current-values value))))
    (apply min @bucket-values)))

(ns jack-compiler.core
  (:gen-class)
  (:use [clojure.string :as str]))

(def keyword-regex #"class|constructor|function|method|field|static|var|int|char|boolean|void|true|false|null|this|let|do|if|else|while|return")

(def symbol-regex #"\{|\}|\(|\)|\[|\]|\.|,|;|\+|-|\*|/|&|\||<|>|=|~")

; recognizes integers between 0 and 32726
(def integer-constant-regex #"[0-9]|[0-9][0-9]|[0-9][0-9][0-9]|[0-9][0-9][0-9][0-9]|[0-3][0-2][0-7][0-2][0-6]")

(def string-constant-regex #"\"[^\"\n]*\"")

(def identifier-regex #"[a-zA-Z_]+[a-zA-Z0-9_]*")

(defn union-re-patterns [& patterns] 
    (re-pattern (apply str (interpose "|" (map #(str %) patterns)))))

(def tokenizer-regex (union-re-patterns keyword-regex symbol-regex integer-constant-regex string-constant-regex identifier-regex))

(defn tokenizer
  "Converts a jack file to a list of tokens"
  [jack-text]
  ; Remove line comments
  (def stripped-jack-text (replace jack-text #"//.*" (re-quote-replacement "")))
  ; Remove block comments
  (def stripped-jack-text (replace stripped-jack-text #"/\*\*.*\*/" ""))
  ; Trim whitespace
  ; (def stripped-jack-text (replace stripped-jack-text #"\s*" ""))
  
  ; (println tokenizer-regex)
  
  (def stripped-jack-text (re-seq tokenizer-regex stripped-jack-text))
  (apply println stripped-jack-text))

(defn -main
  "I don't do a whole lot ... yet."
  [jack-file & args]
  (tokenizer (slurp jack-file)))
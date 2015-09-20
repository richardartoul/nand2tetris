(ns jack-compiler.core
  (:gen-class))

(def keyword-regex #"class|constructor|function|method|field|static|var|int|char|boolean|void|true|false|null|this|let|do|if|else|while|return")

(def symbol-regex #"\{|\}|\(|\)|\[|\]|\.|,|;|\+|-|\*|/|&|\||<|>|=|~")

; recognizes integers between 0 and 32726
(def integer-constant-regex #"[0-9]|[0-9][0-9]|[0-9][0-9][0-9]|[0-9][0-9][0-9][0-9]|[0-3][0-2][0-7][0-2][0-6]")

(def string-constant-regex #"[^\"\n]*")

(def identifier-regex #"[a-zA-Z_]+[a-zA-Z0-9_]*")

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
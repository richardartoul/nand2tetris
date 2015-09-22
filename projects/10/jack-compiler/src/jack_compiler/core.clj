(ns jack-compiler.core
  (:gen-class)
  (:use [clojure.string :as str]))

(declare compilation-engine)
(declare compile-class)
(declare compilation-functions)

(def keyword-regex #"class|constructor|function|method|field|static|var|int|char|boolean|void|true|false|null|this|let|do|if|else|while|return")

(def symbol-regex #"\{|\}|\(|\)|\[|\]|\.|,|;|\+|-|\*|/|&|\||<|>|=|~")

; recognizes integers between 0 and 32726
(def integer-constant-regex #"[0-9]|[0-9][0-9]|[0-9][0-9][0-9]|[0-9][0-9][0-9][0-9]|[0-3][0-2][0-7][0-2][0-6]")

(def string-constant-regex #"\"[^\"\n]*\"")

(def identifier-regex #"[a-zA-Z_]+[a-zA-Z0-9_]*")

; Combines multiple regex-literals together
(defn union-re-patterns [& patterns] 
    (re-pattern (apply str (interpose "|" (map str patterns)))))

; Create a master token regex from all the regex types
(def tokenizer-regex (union-re-patterns keyword-regex symbol-regex integer-constant-regex string-constant-regex identifier-regex))

(defn tokenizer
  "Converts a jack file to a list of tokens"
  [jack-text]
  ; Remove line comments
  (def stripped-jack-text (replace jack-text #"//.*" (re-quote-replacement "")))
  ; Remove block comments
  (def stripped-jack-text (replace stripped-jack-text #"/\*\*.*\*/" ""))
  ; Convert string to lazy sequence of tokens using regex
  (re-seq tokenizer-regex stripped-jack-text))
  
(defn compile-x
  "Generic compilation function for known keywords"
  [remaining xml-output xml-head xml-tail]
   (if (not (= xml-tail nil))
     ; If xml-tail is provided
     (do (into (into (into xml-output xml-head) 
                     (compilation-engine remaining xml-output)) 
               xml-tail))
     ; If xml-tail is not provided
     (do (into 
           (into xml-output xml-head)
           (compilation-engine remaining xml-output)))))

(defn compile-custom
  "Compiles identifiers, integer constants, and strings"
  [remaining xml-output custom]
  (if (re-matches integer-constant-regex custom)
    (compile-x remaining xml-output 
               ["<term>" "<integerConstant>" custom "</integerConstant>" "</term>"] [])
    (if (re-matches string-constant-regex custom)
      (compile-x remaining xml-output 
                 ["<term>" "<stringConstant>" custom "</stringConstant>" "</term>"] [])
      (if (re-matches identifier-regex custom)
        (compile-x remaining xml-output 
                   ["<term>" "<identifier>" custom "</identifier>" "</term>"] [])))))

(defn compilation-engine
  "Converts a list of jack tokens to VM code by looping through it recursively
  and employing a recursive descent tree strategy"
  [[current & remaining] xml-output]
  ; (println current)
  ; (println remaining)
  (if (not (= current nil))
    ; If its an identifier, integer constant, or string
    (if (= (compilation-functions current) nil)
      (compile-custom remaining xml-output current)
    (compile-x remaining xml-output 
               (get-in compilation-functions [current :head]) 
               (get-in compilation-functions [current :tail])))
    xml-output))

(def compilation-functions {"class" {:head ["<class>" "<keywords> class </keywords>"]
                                     :tail ["</class>"]}
                            "{" {:head ["<symbol> { </symbol>"]}})

(defn -main
  "I don't do a whole lot ... yet."
  [jack-file & args]
  (println (compilation-engine (tokenizer (slurp jack-file)) [])))
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

(declare compile-class-var-dec)

(declare compile-subroutine-dec)

(defn compile-class-var-dec
  [[current & remaining] xml-output]
  (if (= current "static")
    (into (into xml-output ["<classVarDec>" "<keyword>" current "</keyword>"])
          (into xml-output (compile-class-var-dec remaining xml-output)))
  (if (= current "field")
    (into (into xml-output ["<classVarDec>" "<keyword>" current "</keyword>"])
          (into xml-output (compile-class-var-dec remaining xml-output)))
  (if (= current "int")
    (into (into xml-output ["<keyword>" current "</keyword>"])
        (into xml-output (compile-class-var-dec remaining xml-output)))
  (if (= current "char")
    (into (into xml-output ["<keyword>" current "</keyword>"])
        (into xml-output (compile-class-var-dec remaining xml-output)))
  (if (= current "boolean")
    (into (into xml-output ["<keyword>" current "</keyword>"])
        (into xml-output (compile-class-var-dec remaining xml-output)))
  (if (re-matches identifier-regex current)
    (into (into xml-output ["<identifier>" current "</identifier>"])
        (into xml-output (compile-class-var-dec remaining xml-output)))
  (if (= current ";")
    (into (into xml-output ["<symbol>" current "</symbol>" "</classVarDec>"])
        (into xml-output (compile-class remaining xml-output)))))))))))

(defn compile-parameter-list
  [[current & remaining :as full] xml-output]
  (if (= current "char")
    (into (into xml-output ["<keyword>" current "</keyword>"])
          (compile-parameter-list remaining xml-output))
  (if (= current "boolean")
    (into (into xml-output ["<keyword>" current "</keyword>"])
          (compile-parameter-list remaining xml-output))
  (if (= current "int")
    (into (into xml-output ["<keyword>" current "</keyword>"])
          (compile-parameter-list remaining xml-output))
  (if (= current ",")
    (into (into xml-output ["<symbol>" current "</symbol>"])
          (compile-parameter-list remaining xml-output))
  (if (= current ")")
    (into xml-output (compile-subroutine-dec full xml-output))
  (if (re-matches identifier-regex current)
    (into (into xml-output ["<identifier>" current "</identifier>"])
          (compile-parameter-list remaining xml-output)))))))))

(defn compile-statements
  [[current & remaining :as full] xml-output])

(defn compile-subroutine-dec
  [[current & remaining] xml-output]
  (if (= current "constructor")
    (into (into xml-output ["<subroutineDec" "<keyword>" "constructor" "</keyword>"])
          (compile-subroutine-dec remaining xml-output))
  (if (= current "function")
    (into (into xml-output ["<subroutineDec>" "<keyword>" "function" "</keyword>"])
          (compile-subroutine-dec remaining xml-output))
  (if (= current "method")
    (into (into xml-output ["<subroutineDec" "<keyword>" "method" "</keyword>"])
          (compile-subroutine-dec remaining xml-output))
  (if (= current "int")
    (into (into xml-output ["<keyword>" current "</keyword>"])
        (into xml-output (compile-subroutine-dec remaining xml-output)))
  (if (= current "char")
    (into (into xml-output ["<keyword>" current "</keyword>"])
        (into xml-output (compile-subroutine-dec remaining xml-output)))
  (if (= current "boolean")
    (into (into xml-output ["<keyword>" current "</keyword>"])
        (into xml-output (compile-subroutine-dec remaining xml-output)))
  (if (= current "void")
    (into (into xml-output ["<keyword>" current "</keyword>"])
        (into xml-output (compile-subroutine-dec remaining xml-output)))
  (if (= current "(")
    (into (into xml-output ["<symbol>" current "</symbol>" "<parameterList>"])
        (into xml-output (compile-parameter-list remaining xml-output)))
  (if (= current ")")
    (into (into xml-output ["</parameterList>" "<symbol>" current "</symbol>"])
        (into xml-output (compile-class remaining xml-output)))
  (if (= current "{")
    (into (into xml-output ["<symbol>" current "</symbol>"])
        (into xml-output (compile-statements remaining xml-output)))
  (if (re-matches identifier-regex current)
    (into (into xml-output ["<identifier>" current "</identifier>"])
        (into xml-output (compile-subroutine-dec remaining xml-output)))))))))))))))

(defn compile-class
  [[current & remaining :as full] xml-output]
  (let [var-dec-regex #"static|field"
        subroutine-dec-regex #"constructor|function|method"]
    (if (= current "class")
      (into (into xml-output ["<class>" "<keyword>" "class" "</keyword>"])
            (compile-class remaining xml-output))
    (if (= current "{")
      (into (into xml-output ["<symbol>" "{" "</symbol>"])
            (compile-class remaining xml-output))
    (if (re-matches var-dec-regex current)
      (into xml-output (compile-class-var-dec full xml-output))
    (if (re-matches subroutine-dec-regex current)
      (into xml-output (compile-subroutine-dec full xml-output))
    (if (= current "}")
      (into xml-output ["<symbol>" "}" "</symbol>" "</class>"])
    (if (re-matches identifier-regex current)
      (into (into xml-output ["<identifier>" current "</identifier>"])
            (compile-class remaining xml-output))))))))))
  
; (defn compile-statement
;   [current remaining xml-output]
;   (if (= current "if")
;     (compile-if-statement remaining xml-output))
;   (if (= current "while")
;     (compile-while-statement remaining xml-output))
;   (if (= current "do")
;     (compile-do-statement remaining xml-output))
;   (if (= current "let")
;     (compile-while-statement remaining xml-output))
;   (if (= current "return")
;     (compile-return-statement remaining xml-output)))

; (defn compile-while-statement
;   [remaining xml-output])

; (defn compile-let-statement
;   [remaining xml-output])

; (defn compile-do-statement
;   [remaining xml-output])

; (defn compile-return-statement
;   [remaining xml-output])

; (defn compile-if-statement
;   [remaining xml-output])

; (defn compile-sequence-statement)

; (defn compile-expression)

(def compilation-functions {"class" {:head ["<class>" "<keyword>" "class" "</keyword>"]
                                     :tail ["</class>"]}
                            "function" {:head ["<subroutineDec>" "<keyword>" "function" "</keyword>"]
                                     :tail ["</subroutineDec>"]}
                            "void" {:head ["<keyword>" "void" "</keyword>"]}
                            "{" {:head ["<symbol>" "{" "</symbol>"]}
                            "}" {:head ["<symbol>" "}" "</symbol>"]}
                            "(" {:head ["<symbol>" "(" "</symbol>"]}
                            ")" {:head ["<symbol>" ")" "</symbol>"]}
                            "[" {:head ["<symbol>" "[" "</symbol>"]}
                            "]" {:head ["<symbol>" "]" "</symbol>"]}
                            "." {:head ["<symbol>" "." "</symbol>"]}
                            "," {:head ["<symbol>" "," "</symbol>"]}
                            ";" {:head ["<symbol>" ";" "</symbol>"]}
                            "+" {:head ["<symbol>" "+" "</symbol>"]}
                            "-" {:head ["<symbol>" "-" "</symbol>"]}
                            "*" {:head ["<symbol>" "*" "</symbol>"]}
                            "/" {:head ["<symbol>" "/" "</symbol>"]}
                            "&" {:head ["<symbol>" "&" "</symbol>"]}
                            "|" {:head ["<symbol>" "|" "</symbol>"]}
                            ">" {:head ["<symbol>" ">" "</symbol>"]}
                            "<" {:head ["<symbol>" ">" "</symbol>"]}
                            "=" {:head ["<symbol>" "=" "</symbol>"]}
                            "~" {:head ["<symbol>" "~" "</symbol>"]}})

(defn -main
  "I don't do a whole lot ... yet."
  [jack-file & args]
  (println (compile-class (tokenizer (slurp jack-file)) [])))
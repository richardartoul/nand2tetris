(ns jack-compiler.core
  (:gen-class)
  (:use [clojure.string :as str]))

(declare compilation-engine)
(declare compile-class)
(declare compilation-functions)
(declare compile-term)

(def keyword-regex #"class|constructor|function|method|field|static|var|int|char|boolean|void|true|false|null|this|let|do|if|else|while|return")

(def keyword-constant-regex #"true|false|null|this")

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
  ; Remove block comments - (?s) enables dotall mode so . matches \n aswell
  (def stripped-jack-text (replace stripped-jack-text #"(?s)\/\*\*.*\*\/" ""))
  (println stripped-jack-text)
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
(declare compile-subroutine-body)
(declare compile-statements)
(declare compile-expression)
(declare compile-subroutine-call)
(declare compile-if-statement)
(declare compile-while-statement)
(declare compile-do-statement)
(declare compile-return-statement)

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
  (if (= current ",")
    (into (into xml-output ["<symbol>" current "</symbol>"])
        (into xml-output (compile-class-var-dec remaining xml-output)))
  (if (= current ";")
    (into (into xml-output ["<symbol>" current "</symbol>" "</classVarDec>"])
        (into xml-output (compile-class remaining xml-output))))))))))))

(defn compile-var-dec
  [[current & remaining] xml-output]
  (if (= current "var")
    (into (into xml-output ["<VarDec>" "<keyword>" current "</keyword>"])
          (into xml-output (compile-var-dec remaining xml-output)))
  (if (= current "int")
    (into (into xml-output ["<keyword>" current "</keyword>"])
        (into xml-output (compile-var-dec remaining xml-output)))
  (if (= current "char")
    (into (into xml-output ["<keyword>" current "</keyword>"])
        (into xml-output (compile-var-dec remaining xml-output)))
  (if (= current "boolean")
    (into (into xml-output ["<keyword>" current "</keyword>"])
        (into xml-output (compile-var-dec remaining xml-output)))
  (if (re-matches identifier-regex current)
    (into (into xml-output ["<identifier>" current "</identifier>"])
        (into xml-output (compile-var-dec remaining xml-output)))
  (if (= current ",")
    (into (into xml-output ["<symbol>" current "</symbol>"])
        (into xml-output (compile-var-dec remaining xml-output)))
  (if (= current ";")
    (into (into xml-output ["<symbol>" current "</symbol>" "</VarDec>"])
        (into xml-output (compile-subroutine-body remaining xml-output)))))))))))

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

(defn compile-expression-list
  ; compile-expression-list always calls back to compile subroutine but it has a callback
  ; parameter so it knows which callback to send back to compile subroutine
  [[current & remaining :as full] xml-output callback]
  (println "In compile expression list, callback is: " callback)
  (if (or (re-matches integer-constant-regex current) 
          (re-matches string-constant-regex current) 
          (re-matches keyword-constant-regex current)
          (re-matches identifier-regex current))
    (into xml-output (compile-expression full xml-output compile-expression-list callback))
  (if (= current "(")
    (into xml-output (compile-expression remaining xml-output compile-expression-list callback))
  (if (= current ")")
    (into xml-output (compile-subroutine-call full xml-output callback))))))

(defn compile-expression
  [[current & remaining :as full] xml-output callback & callbackTunnel]
  (println "in compile expression, callbacktunnel is: ", callbackTunnel)
  (if (or (re-matches integer-constant-regex current) 
          (re-matches string-constant-regex current) 
          (re-matches keyword-constant-regex current)
          (re-matches identifier-regex current))
    (into (into xml-output ["<expression>"])
      (compile-term full xml-output callback (first callbackTunnel)))
  (if (= current "+")
    (into xml-output ["<symbol>" current "</symbol>"])
  (if (= current "-")
    (into xml-output ["<symbol>" current "</symbol>"])
  (if (= current "*")
    (into xml-output ["<symbol>" current "</symbol>"])
  (if (= current "/")
    (into xml-output ["<symbol>" current "</symbol>"])
  (if (= current "&")
    (into xml-output ["<symbol>" current "</symbol>"])
  (if (= current "|")
    (into xml-output ["<symbol>" current "</symbol>"])
  (if (= current "<")
    (into xml-output ["<symbol>" current "</symbol>"])
  (if (= current ">")
    (into xml-output ["<symbol>" current "</symbol>"])
  (if (= current "=")
    (into xml-output ["<symbol>" current "</symbol>"])
    ; else 
    (into (into xml-output ["</expression>"])
      (callback full xml-output (first callbackTunnel))))))))))))))

(defn compile-term
  [[current & remaining :as full] xml-output callback & callbackTunnel]
  (if (re-matches integer-constant-regex current)
    (into (into xml-output ["<expression>" "<term>" "<integerConstant>" current "</integerConstant>" "</term>"])
      (compile-expression remaining xml-output callback (first callbackTunnel)))
  (if (re-matches string-constant-regex current)
    (into (into xml-output ["<term>" "<stringConstant>" current "</stringConstant>" "</term>"])
      (compile-expression remaining xml-output callback (first callbackTunnel)))
  (if (re-matches keyword-constant-regex current)
    (into (into xml-output ["<term>" "<keywordConstant>" current "</keywordConstant>" "</term>"])
      (compile-expression remaining xml-output callback (first callbackTunnel)))
  (if (re-matches identifier-regex current)
    (into (into xml-output ["<term>" "<identifier>" current "</identifier>"])
      (compile-expression remaining xml-output callback (first callbackTunnel))))))))
  ;   (let [first-ahead (head remaining) second-ahead (second remaining)]
  ;     (if (= look-ahead ".")
  ;       (into xml-output ["term" "<identifier>" current "</identifier" "<symbol>" "." "</symbol>"
  ;         "<identifier>" second-ahead "</identifier>" "("]))))))))

; Needs to handle expressions
(defn compile-let-statement
  [[current & remaining :as full] xml-output & extra]
  (if (= current "let")
    (into (into xml-output ["<letStatement>" "<keyword>" "let" "</keyword>"])
      (compile-let-statement remaining xml-output))
  (if (re-matches identifier-regex current)
    (into (into xml-output ["<identifier>" current "</identifier>"])
      (compile-let-statement remaining xml-output))
  (if (= current "=")
    (into (into xml-output ["<symbol>" "=" "</symbol>"])
      (compile-expression remaining xml-output compile-let-statement))
  (if (= current ";")
    (into (into xml-output ["<symbol>" current "</symbol>" "</letStatement>"])
      (compile-statements remaining xml-output)))))))

(defn compile-do-statement
  [[current & remaining :as full] xml-output & extra]
  (if (= current "do")
    (into (into xml-output ["<doStatement>" "<keyword>" "do" "</keyword>"])
      (compile-do-statement remaining xml-output))
  (if (= current ";")
    (into (into xml-output ["<symbol>" ";" "</symbol>" "</doStatement>"])
      (compile-statements remaining xml-output))
  (if (re-matches identifier-regex current) 
    (into xml-output (compile-subroutine-call full xml-output compile-do-statement))))))

(defn compile-subroutine-call
  [[current & remaining :as full] xml-output callback]
  (println "callback: " callback)
  (println "first full: " (first full))
  (if (re-matches identifier-regex current)
    (into (into xml-output ["<identifier>" current "</identifier>"])
      (compile-subroutine-call remaining xml-output callback))
  (if (= current "(")
    (into (into xml-output ["<symbol>" current "</symbol>"])
      (compile-expression-list remaining xml-output callback))  
  (if (= current ")")
    (into (into xml-output ["<symbol>" current "</symbol>"])
      (compile-subroutine-call remaining xml-output callback))
  (if (= current ".")
    (into (into xml-output ["<symbol>" current "</symbol>"])
      (compile-subroutine-call remaining xml-output callback))
  (if (= current ";")
    (do
    ; (println callback)
    ; (println (first full))
    (into xml-output (callback full xml-output)))))))))

(defn compile-return-statement
  [[current & remaining :as full] xml-output & extra]
  (if (= current "return")
    (into (into xml-output ["<returnStatement>" "<keyword>" "return" "</keyword>"])
      (compile-return-statement remaining xml-output))
  (if (or (re-matches integer-constant-regex current) 
          (re-matches string-constant-regex current) 
          (re-matches keyword-constant-regex current)
          (re-matches identifier-regex current))
    (into xml-output (compile-expression full xml-output compile-return-statement))
  (if (= current ";")
    (into (into xml-output ["</returnStatement>"])
      (compile-statements remaining xml-output))))))

(defn compile-statements
  [[current & remaining :as full] xml-output]
  (if (= current "let")
    (into xml-output (compile-let-statement full xml-output))
  (if (= current "do")
    (into xml-output (compile-do-statement full xml-output))
  (if (= current "if")
    (into xml-output (compile-if-statement full xml-output))
  (if (= current "while")
    (into xml-output (compile-while-statement full xml-output))
  (if (= current "return")
    (into xml-output (compile-return-statement full xml-output))
  (if (= current "}")
    (into xml-output (compile-subroutine-body full xml-output)))))))))

(defn compile-subroutine-body
  [[current & remaining :as full] xml-output]
  (if (= current "var")
    (into xml-output (compile-var-dec full xml-output))
  (if (or (= current "let") (= current "if") (= current "while") (= current "do") (= current "do"))
    (into xml-output (compile-statements full xml-output))
  (if (= current "}")
    (into xml-output (compile-subroutine-dec remaining xml-output))))))
  ; (if (= current "function")
  ;   (into (into xml-output ["<subroutineDec>" "<keyword>" "function" "</keyword>"])
  ;         (compile-subroutine-dec remaining xml-output)))))

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
        (into xml-output (compile-subroutine-dec remaining xml-output)))
  (if (= current "{")
    (into (into xml-output ["<symbol>" current "</symbol>"])
        (compile-subroutine-body remaining xml-output))
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

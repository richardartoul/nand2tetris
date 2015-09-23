(ns jack-compiler.core-test
  (:require [clojure.test :refer :all]
            [jack-compiler.core :refer :all]))

(deftest keyword-regex-test
  (testing "keyword-regex identifies keywords properly"
    (is (re-matches keyword-regex "class"))
    (is (re-matches keyword-regex "constructor"))
    (is (re-matches keyword-regex "function"))
    (is (re-matches keyword-regex "method"))
    (is (re-matches keyword-regex "field"))
    (is (re-matches keyword-regex "static"))
    (is (re-matches keyword-regex "var"))
    (is (re-matches keyword-regex "int"))
    (is (re-matches keyword-regex "char"))
    (is (re-matches keyword-regex "boolean"))
    (is (re-matches keyword-regex "void"))
    (is (re-matches keyword-regex "true"))
    (is (re-matches keyword-regex "false"))
    (is (re-matches keyword-regex "null"))
    (is (re-matches keyword-regex "this"))
    (is (re-matches keyword-regex "let"))
    (is (re-matches keyword-regex "do"))
    (is (re-matches keyword-regex "if"))
    (is (re-matches keyword-regex "else"))
    (is (re-matches keyword-regex "while"))
    (is (re-matches keyword-regex "return"))))

(deftest symbol-regex-test
  (testing "symbol-regex identifies symbols properly"
    (is (re-matches symbol-regex "{"))
    (is (re-matches symbol-regex "}"))
    (is (re-matches symbol-regex "["))
    (is (re-matches symbol-regex "]"))
    (is (re-matches symbol-regex "("))
    (is (re-matches symbol-regex ")"))
    (is (re-matches symbol-regex "."))
    (is (re-matches symbol-regex ","))
    (is (re-matches symbol-regex ";"))
    (is (re-matches symbol-regex "+"))
    (is (re-matches symbol-regex "-"))
    (is (re-matches symbol-regex "*"))
    (is (re-matches symbol-regex "/"))
    (is (re-matches symbol-regex "&"))
    (is (re-matches symbol-regex "|"))
    (is (re-matches symbol-regex "<"))
    (is (re-matches symbol-regex ">"))
    (is (re-matches symbol-regex "="))
    (is (re-matches symbol-regex "~"))))

(deftest integer-constant-regex-test
  (testing "integer-constant-regex identifies integer constants properly"
    (is (re-matches integer-constant-regex "0"))
    (is (not (re-matches integer-constant-regex "-1")))
    (is (re-matches integer-constant-regex "5000"))
    (is (re-matches integer-constant-regex "32726"))
    (is (not (re-matches integer-constant-regex "32727")))))

(deftest string-constant-regex-test
  (testing "string-constant-regex identifies string constants properly"
    (is (re-matches string-constant-regex "\"fasdfsdfasd\""))
    (is (re-matches string-constant-regex "\"_asfdasdfasd\""))
    (is (re-matches string-constant-regex "\"5000asdfasd\""))
    (is (re-matches string-constant-regex "\"asfasdkj&@&A*SD(*ASjd__\""))
    (is (not (re-matches string-constant-regex "\n\n")))))

(deftest compile-class-test
  (testing "compile-class properly compiles class"
    (println (compile-class ["class" "main" "{" "field" "Square" "square" ";" "field" "int" "direction" ";" "}"] []))))


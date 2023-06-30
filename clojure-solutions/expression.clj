(defn abstractBinaryFunc [operation] (fn [f1 f2]  (fn [v] (operation (f1 v) (f2 v)))))

(defn abstractUnaryFunc [operation] (fn [f]  (fn [v] (operation (f v)))))

(defn constant [a] (fn [v] a))

(def negate (abstractUnaryFunc -))

(defn variable [a] (fn [v] (get v a)))

(def add (abstractBinaryFunc +))

(def subtract (abstractBinaryFunc -))

(def multiply (abstractBinaryFunc *))

(defn divide [a b] (fn [v] (/ (a v) (double (b v)))))

(def sinh (abstractUnaryFunc (fn [x] (Math/sinh x))))

(def cosh (abstractUnaryFunc (fn [x] (Math/cosh x))))

(def operation {'+ add '- subtract '* multiply '/ divide 'negate negate 'sinh sinh 'cosh cosh})

(defn parseFunction [x]
  ((fn [string parse] (if (list? (read-string x))
                        (cond
                          (= (count parse) 2) ((get operation (nth parse 0)) (parseFunction (str (nth parse 1))))
                          (= (count parse) 3) ((get operation (nth parse 0)) (parseFunction (str (nth parse 1))) (parseFunction (str (nth parse 2))))
                          (number? (nth parse 0)) (constant (nth parse 0))
                          :else (variable (str (nth parse 0))))
                        (parseFunction (clojure.string/join ["(" string ")"])))) x (read-string x)))

(defn pget [obj key]
  (cond
    (contains? obj key) (obj key)
    (contains? obj :proto) (pget (obj :proto) key)
    :else nil
    )
  )

(defn pcall [obj key & args] (apply (pget obj key) obj args))

(defn constructor [ctor proto]
  (fn [& args] (apply ctor {:proto proto} args)))

(defn evaluate [expr v] (pcall expr :evaluate v))
(defn toString [expr] (pcall expr :toString))
(defn diff [expr var] (pcall expr :diff var))

(declare Constant)
(declare Negate)
(declare Add)
(declare Subtract)
(declare Multiply)
(declare Divide)
(declare Sinh)
(declare Cosh)
(declare ZERO)
(declare ONE)

(defn Unary [this var operation operand] (assoc this :var var :operation operation :operand operand))

(def AbstractUnaryFuncConstructor
  {
   :evaluate (fn [this v] ((:operation this) (evaluate (:var this) v)))
   :toString (fn [this] (str "(" (pget this :operand) " " (toString (:var this)) ")"))
   })

(def ConstantPrototype
  {
   :evaluate (fn [this v] (double (:var this)))
   :toString (fn [this] (format "%.1f" (double (:var this))))
   :diff (fn [this var] ZERO)
   })

(def Constant (constructor (fn [this var] (Unary this var "" "")) ConstantPrototype))

(def ZERO (Constant 0))
(def ONE (Constant 1))

(def VariableConstructor
  {
   :evaluate (fn [this v] (get v (:var this)))
   :toString (fn [this] (str (:var this)))
   :diff (fn [this var] (if (= (:var this) var) ONE ZERO))
   })

(def Variable (constructor (fn [this var] (Unary this var "" "")) VariableConstructor))

(def Negate (constructor (fn [this var] (Unary this var - "negate")) (assoc AbstractUnaryFuncConstructor :diff (fn [this var] (Negate (diff (:var this) var))))))

(def Sinh (constructor (fn [this var] (Unary this var (fn [x] (Math/sinh x)) "sinh")) (assoc AbstractUnaryFuncConstructor :diff (fn [this var] (Multiply (Cosh (:var this)) (diff (:var this) var))))))

(def Cosh(constructor (fn [this var] (Unary this var (fn [x] (Math/cosh x)) "cosh")) (assoc AbstractUnaryFuncConstructor :diff (fn [this var] (Multiply (Sinh (:var this)) (diff (:var this) var))))))

(defn Binary [this f1 f2 operation operand]
  (assoc this :f1 f1 :f2 f2 :operation operation :operand operand))

(def AbstractBinaryFuncConstructor
  {
   :evaluate (fn [this v] ((:operation this) (evaluate (:f1 this) v) (evaluate (:f2 this) v)))
   :toString (fn [this] (str "(" (pget this :operand) " " (toString (:f1 this)) " " (toString (:f2 this)) ")"))
   })

(def Add (constructor (fn [this f1 f2] (Binary this f1 f2 + "+")) (assoc AbstractBinaryFuncConstructor
                                                                    :diff (fn [this var] (Add (diff (:f1 this) var) (diff (:f2 this) var))))))

(def Subtract (constructor (fn [this f1 f2] (Binary this f1 f2 - "-" )) (assoc AbstractBinaryFuncConstructor
                                                                          :diff (fn [this var] (Subtract (diff (:f1 this) var) (diff (:f2 this) var))))))

(def Multiply (constructor (fn [this f1 f2] (Binary this f1 f2 * "*" )) (assoc AbstractBinaryFuncConstructor
                                                                          :diff (fn [this var] (Add (Multiply (diff (:f1 this) var) (:f2 this)) (Multiply (:f1 this) (diff (:f2 this) var)))))))

(def Divide (constructor (fn [this f1 f2] (Binary this f1 f2 '/ "/"))
                         (assoc AbstractBinaryFuncConstructor :evaluate (fn [this v] (/ (double (evaluate (:f1 this) v)) (double (evaluate (:f2 this) v))))
                                                              :diff (fn [this var] (Divide (Subtract (Multiply (diff (:f1 this) var) (:f2 this))
                                                                                                     (Multiply (:f1 this) (diff (:f2 this) var))) (Multiply (:f2 this) (:f2 this)))))))

(def operationObj {'+ Add '- Subtract '* Multiply '/ Divide 'negate Negate 'sinh Sinh 'cosh Cosh})

(defn parseObject [x]
  ((fn [string parse] (if (list? (read-string x))
    (cond
      (= (count parse) 2) ((get operationObj (nth parse 0)) (parseObject (str (nth parse 1))))
      (= (count parse) 3) ((get operationObj (nth parse 0)) (parseObject (str (nth parse 1))) (parseObject (str (nth parse 2))))
      (number? (nth parse 0)) (Constant (nth parse 0))
      :else (Variable (str (nth parse 0))))
    (parseObject (clojure.string/join ["(" string ")"])))) x (read-string x)))
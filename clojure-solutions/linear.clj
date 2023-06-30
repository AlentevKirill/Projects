(defn binaryFunc [operation] (fn [a b] (mapv operation a b)))

(def v+ (binaryFunc +))

(def v- (binaryFunc -))

(def v* (binaryFunc *))

(def vd (binaryFunc /))

(defn scalar [a b] (apply + (v* a b)))

(defn vect [a b]
  [(- (* (nth a 1) (nth b 2)) (* (nth a 2) (nth b 1)))
   (- (- (* (nth a 0) (nth b 2)) (* (nth a 2) (nth b 0))))
   (- (* (nth a 0) (nth b 1)) (* (nth a 1) (nth b 0)))]
  )

(defn tensorFunc [operation] (fn func [a b] (if (not (vector? a)) (operation a b) (mapv func a b))))

(def t+ (tensorFunc +))

(def t- (tensorFunc -))

(def t* (tensorFunc *))

(def td (tensorFunc /))

(def m+ t+)

(def m- t-)

(def m* t*)

(def md td)

(defn binaryFunc2 [operation] (fn [a b] (mapv (fn [v] (operation v b)) a)))

(def v*s (binaryFunc2 *))

(def m*s (binaryFunc2 v*s))

(def m*v (binaryFunc2 scalar))

(defn transpose [m] (apply mapv vector m))

(defn m*m [a b] (mapv (fn [v] (mapv (fn [t] (scalar v t)) (transpose b))) a))
(ns cssoj.css3
  (:use [cssoj.core]))

(defn gen-css-func 
  ;TODO: write doc
  [fname unit & args]
  (str fname "(" (sep-to-str "," (map (partial add-unit unit) args)) ")"))

(defn cssbunch
  ;TODO: write doc
  [prop-name prop-value & prefixes]
  (interleave 
    (concat [prop-name]
            (map #(str "-" (value-to-str %) "-" (value-to-str prop-name)) 
                 prefixes))
    (repeat (inc (count prefixes)) prop-value)))

(defn border 
  ;TODO: write the doc 
  [& {:keys [attrs image radius shadow]}]
  ;TODO: test me  
  (style
    (if attrs
      (style [:border attrs]))
    (if image
      (style (cssbunch :border-image image :o :webkit :moz)))
    (if radius
      (style (cssbunch :border-radius radius :o :webkit :moz)))
    (if shadow
      (style [:box-shadow shadow]))))

(defn background
  ;TODO: write the doc 
  [& {:keys [attrs clip origin size]}]
  ;TODO: test me  
  (style (if attrs (style [:background attrs]))
         (if clip (style [:background-clip clip]))
         (if origin (style [:background-origin origin]))
         (if size (cssbunch :background-size size :moz))))

(defn text
  [& {:keys []}]
  ;TODO: complete this
  )

(defn custom-font
  [& {:keys []}]
  ;TODO: complete this
  )

(defn gen-css-matrix 
  ;TODO: write the doc 
  [coll]
  ;TODO: test me
  (str (if (= 6 (count coll))
         "matrix(" "matrix3d(") 
       (sep-to-str "," coll) 
       ")"))

(defn transform
  ;TODO: write the doc 
  [& {:keys [translate rotate scale skew origin child3d persp persp-origin backface matrix]
      :or {backface true child3d true}}]
  (style (cssbunch :backface-visibility (if (backface) :visible :hidden) 
                   :webkit :moz :ms)
         (if persp-origin 
           (cssbunch :perspective-origin persp-origin :webkit)) 
         (if persp 
           (cssbunch :perspective persp :webkit)) 
         (cssbunch :transform-style (if child3d :preserve-3d :flat) :webkit)
         (if origin 
           (cssbunch :transform-origin origin :ms :webkit :moz :o))
         (if (or matrix translate rotate scale skew)
           (cssbunch :transform (if matrix
                                  (gen-css-matrix matrix)
                                  [(if translate 
                                     (let [[x y z] translate] 
                                       (if z 
                                         (gen-css-func "translate3d" "px" x y z) 
                                         (gen-css-func "translate" "px" x y))))
                                   (if rotate
                                     (if (coll? rotate)
                                       (let [[x y z] rotate]
                                         (str (gen-css-func "rotateX" "deg" x) " " 
                                              (gen-css-func "rotateY" "deg" y) " " 
                                              (gen-css-func "deg" "rotateZ" z)))
                                       (gen-css-func "rotate" "deg" rotate)))
                                   (if scale
                                     (let [[x y z] scale] 
                                       (if z 
                                         (gen-css-func "scale3d" "%" x y z) 
                                         (gen-css-func "scale" "%" x y))))
                                   (if skew
                                     (let [[x y] skew] 
                                       (gen-css-func "skew" "deg" x y)))])
                     :ms :moz :webkit :o))))

(defn transition
  ;TODO: write the doc 
  [& transitions] 
  ;TODO: test me
  (let 
    [v [(for [t (value-to-str transitions)]
          (let [[prop duration timing start] t]
            [prop 
             (str (value-to-str duration) 
                  (if (number? duration) "s")) 
             timing 
             (str (value-to-str start) 
                  (if (number? start) "s"))]))]]
    (style (cssbunch :transition v :moz :o :webkit))))

(defn apply-transition
  ;TODO: write the doc 
  [ele transitions & {:keys [on to]}]
  ;TODO: test me
  (let [[ele transitions] (value-to-str [ele transitions])] 
    (if on
      (let [[on to] (value-to-str [on to])] 
        (onto [ele [(transition transitions)] [(str "&" on) [to]]]))
      (onto [ele [(transition transitions)]]))))

(defn keyframe
  ;TODO: write the doc 
  [kfname & key-points]
  ;TODO: test me
  (let [flow (str " " (value-to-str kfname) " {"
                  (apply apply-to 
                         (map #(list (add-unit "%" (first %))
                                     (second %)) key-points))
                  "}\n")]
    (str "@keyframes" flow
         "@-moz-keyframes" flow
         "@-webkit-keyframes" flow
         "@-o-keyframes" flow)))

(defn animation
  ;TODO: write the doc 
  [aname duration & {:keys [timing start-delay iteration direction state]}]
  ;TODO: test me
  (let [v [aname (add-unit "s" duration)
           timing (add-unit start-delay)
           iteration direction state]]
    (style (cssbunch :animation v :moz :webkit :o))))

(defn column
  ;TODO: write the doc 
  [colcount & {:keys [width fill gap rule-color rule-style rule-width colspan]}]
  ;TODO: test me
  (style (cssbunch :colunm-count colcount :moz :webkit)
         (if width 
           (cssbunch :column-width (add-unit "px" width) :moz :webkit))
         (if fill 
           (cssbunch :column-fill fill :moz :webkit))
         (if gap 
           (cssbunch :column-gap (add-unit "px" gap) :moz :webkit))
         (if rule-color 
           (cssbunch :column-rule-color rule-color :moz :webkit))
         (if rule-style 
           (cssbunch :column-rule-style rule-style :moz :webkit))
         (if rule-width 
           (cssbunch :column-rule-width (add-unit "px" rule-width) :moz :webkit))
         (if colspan
           (cssbunch column-span colspan :moz :webkit))))

(ns cssoj.css3
  (:use [cssoj.core]))

(defn cssfunc 
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
  (style
    (if attrs
      (style [:border attrs]))
    (if image
      (style (cssbunch :border-image image :o :webkit :moz)))
    (if radius
      (style (cssbunch :border-radius (add-unit "px" radius) :o :webkit :moz)))
    (if shadow
      (style [:box-shadow shadow]))))

(defn background
  ;TODO: write the doc 
  [& {:keys [attrs clip origin size]}]
  (style (if attrs (style [:background attrs]))
         (if clip (style [:background-clip clip]))
         (if origin (style [:background-origin origin]))
         (if size (cssbunch :background-size size :moz))))

(defn text
  ;TODO: write the doc 
  [& {:keys [justify overflow shadow word-break word-wrap]}]
  (style
    (if justify
      [:text-align justify]
      (cssbunch :text-justify justify)) 
    (if overflow
      (cssbunch :text-overflow overflow)) 
    (if shadow
      (cssbunch :text-shadow shadow)) 
    (if word-break
      (cssbunch :word-break word-break)) 
    (if word-wrap
      (cssbunch :word-wrap word-wrap))))

(defn font
  ;TODO: write the doc 
  [family src & {:keys [fstretch fstyle fweight unicode-range]}]
  (onto ["@fontface" [[:font-family family :src src]
                      (if fstretch
                        [:font-stretch fstretch])
                      (if fstyle
                        [:font-style fstyle])
                      (if fweight
                        [:font-weight fweight])
                      (if unicode-range
                        [:unicode-range unicode-range])]]))

(defn gen-css-matrix 
  ;TODO: write the doc 
  [coll]
  (str (if (= 6 (count coll))
         "matrix(" "matrix3d(") 
       (sep-to-str "," coll) 
       ")"))

(defn transform
  ;TODO: write the doc 
  [& {:keys [translate rotate scale skew origin child3d persp persp-origin backface matrix]
      :or {backface true child3d true}}]
  (style (cssbunch :backface-visibility (if backface :visible :hidden) 
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
                                         (cssfunc "translate3d" "px" x y z) 
                                         (cssfunc "translate" "px" x y))))
                                   (if rotate
                                     (if (coll? rotate)
                                       (let [[x y z] rotate]
                                         (str (cssfunc "rotateX" "deg" x) " " 
                                              (cssfunc "rotateY" "deg" y) " " 
                                              (cssfunc "deg" "rotateZ" z)))
                                       (cssfunc "rotate" "deg" rotate)))
                                   (if scale
                                     (let [[x y z] scale] 
                                       (if z 
                                         (cssfunc "scale3d" "%" x y z) 
                                         (cssfunc "scale" "%" x y))))
                                   (if skew
                                     (let [[x y] skew] 
                                       (cssfunc "skew" "deg" x y)))])
                     :ms :moz :webkit :o))))

(defn transition
  ;TODO: write the doc 
  [& transitions] 
  (style 
    (cssbunch :transition (for [t transitions]
                             (let [[prop duration timing start] t]
                               [prop 
                                (add-unit "s" duration) 
                                timing 
                                (add-unit "s" start)])) :moz :o :webkit)))

(defn apply-transition
  ;TODO: write the doc 
  [ele transitions & {:keys [on to]}]
  (let [ele (value-to-str ele)] 
    (if on
      (let [[on to] (value-to-str [on to])] 
        (onto [ele [(apply transition transitions)] [(str "&" on) to]]))
      (onto [ele [(apply transition transitions)]]))))

(defn keyframe
  ;TODO: write the doc 
  [kfname & key-points]
  (let [flow (str " " (value-to-str kfname) " {\n"
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
  (style
    (cssbunch :animation [aname (add-unit "s" duration)
                          timing (add-unit "s" start-delay)
                          iteration direction state]
              :moz :webkit :o)))

(defn column
  ;TODO: write the doc 
  [colcount & {:keys [width fill gap rule-color rule-style rule-width colspan]}]
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
           (cssbunch :column-span colspan :moz :webkit))))

(defn appearance 
  ;TODO: write the doc 
  [x]
  (style (cssbunch :appearance x :moz :webkit)))

(defn box-sizing 
  ;TODO: write the doc 
  [x]
  (style (cssbunch :box-sizing x :moz :webkit)))

(defn offline-offset 
  ;TODO: write the doc 
  [x]
  (style (cssbunch :appearance (add-unit "px" x))))

(defn resize
  ;TODO: write the doc 
  [x]
  (style (cssbunch :resize x)))

(defn nav
  ;TODO: write the doc 
  [{:keys [up down left right index]}]
  (style
    (if up (cssbunch :nav-up up)) 
    (if down (cssbunch :nav-down down)) 
    (if left (cssbunch :nav-left left)) 
    (if right (cssbunch :nav-right right)) 
    (if index (cssbunch :nav-index index))))

(ns cssoj.css3
  (:use [cssoj.core]))

(defn border 
  ;TODO: write the doc 
  [& {:keys [attrs image radius shadow]}]
  ;TODO: test me  
  (style
    (if attrs
      (style [:border attrs]))
    (if image
      (style [:border-image image]
             [:-o-border-image image]
             [:-webkit-border-image image]))
    (if radius
      (style [:border-radius radius]
             [:-moz-border-radius radius]))
    (if shadow
      (style [:box-shadow shadow]))))

(defn background
  ;TODO: write the doc 
  [& {:keys [attrs clip origin size]}]
  ;TODO: test me  
  (style (if attrs (style [:background attrs]))
         (if clip (style [:background-clip clip]))
         (if origin (style [:background-origin origin]))
         (if size (style [:background-size size
                          :-moz-background-size size]))))

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
  [& {:keys [translate
             rotate 
             scale 
             skew 
             origin
             child3d
             persp
             persp-origin
             backface
             matrix]
      :or {backface true
           child3d true}}]
  (style (if backface
           [:backface-visibility :visible
            :-webkit-backface-visibility :visible 
            :-moz-backface-visibility :visible 
            :-ms-backface-visibility :visible]
           [:backface-visibility :hidden
            :-webkit-backface-visibility :hidden 
            :-moz-backface-visibility :hidden 
            :-ms-backface-visibility :hidden]) 
         (if persp-origin [:perspective-origin persp-origin
                           :-webkit-perspective-origin persp-origin]) 
         (if persp [:perspective persp
                    :-webkit-perspective persp]) 
         (if child3d
           [:transform-style :preserve-3d
            :-webkit-transform-style :preserve-3d]
           [:transform-style :flat
            :-webkit-transform-style :flat])
         (if origin [:transform-origin origin 
                     :-ms-transform-origin origin  
                     :-webkit-transform-origin origin  
                     :-moz-transform-origin origin 
                     :-o-transform-origin origin])
         (if (or matrix translate rotate scale skew)
           (let [t (if matrix
                     (gen-css-matrix matrix)
                     [(if translate 
                        (let [[x y z] translate] 
                          (if z (str "translate3d(" x "," y "," z ")") (str "translate(" x "," y ")"))))
                      (if rotate
                        (if (coll? rotate)
                          (let [[x y z] (map (partial add-unit "deg") rotate)]
                            (str "rotateX(" x ") " "rotateY(" y ") " "rotateZ(" z ")"))
                          (str "rotate(" (add-unit "deg" rotate) ")")))
                      (if scale
                        (let [[x y z] scale] 
                          (if z (str "scale3d(" x "," y "," z ")") (str "scale(" x "," y ")"))))
                      (if skew
                        (let [[x y] skew] (str "skew(" x "," y ")")))])]
             [:transform t
              :-ms-transform t 
              :-moz-transform t 
              :-webkit-transform t 
              :-o-transform t]))))

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
    (style [:transition v
            :-moz-transition v
            :-o-transition v
            :-webkit-transition v])))

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
    (style [:animation v
            :-moz-animation v
            :-webkit-animation v
            :-o-animation v])))

(defn column
  ;TODO: write the doc 
  [colcount & {:keys [width fill gap rule-color rule-style rule-width colspan]}]
  ;TODO: test me
  (style [:colunm-count colcount
          :-moz-colunm-count colcount
          :-webkit-colunm-count colcount]
         (if width 
           (let [width (add-unit "px" width)] 
             [:column-width width
              :-moz-column-width width
              :-webkit-column-width width]))
         (if fill [:column-fill fill
                   :-moz-column-fill fill
                   :-webkit-column-fill fill])
         (if gap 
           (let [gap (add-unit "px" gap)] 
             [:column-gap gap
              :-moz-column-gap gap
              :-webkit-column-gap gap]))
         (if rule-color [:column-rule-color rule-color
                         :-moz-column-rule-color rule-color
                         :-webkit-column-rule-color rule-color])
         (if rule-style [:column-rule-style rule-style
                         :-moz-column-rule-style rule-style
                         :-webkit-column-rule-style rule-style])
         (if rule-width 
           (let [rule-width (add-unit "px" rule-width)] 
             [:column-rule-width rule-width
              :-moz-column-rule-width rule-width
              :-webkit-column-rule-width rule-width]))
         (if colspan
           [:column-span colspan
            :-moz-column-span colspan
            :-webkit-column-span colspan])))

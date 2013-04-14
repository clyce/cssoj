(ns cssoj.css3
  (:use [cssoj.core]))

(defn border 
  [& {:keys [attrs image radius shadow]}]
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
  [& {:keys [attrs clip origin size]}]
  (style
    (if attrs
      (style [:background attrs]))
    (if clip
      (style [:background-clip clip]))
    (if origin
      (style [:background-origin origin]))
    (if size
      (style [:background-size size
              :-moz-background-size size]))))

(defn text
  [& {:keys []}]
  ;TODO: complete this
  )

(defn custom-font
  [& {:keys []}]
  ;TODO: complete this
  )

(defn gen-css-matrix [coll]
  (if (= 6 (count coll))
    (str "matrix(" (apply str (interleave coll (repeat (dec (count coll)) ","))) ")")
    (str "matrix3d(" (apply str (interleave coll (repeat (dec (count coll)) ","))) ")")))

(defn transform
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
  (style 
    (if backface
       [:backface-visibility :visible
        :-webkit-backface-visibility :visible 
        :-moz-backface-visibility :visible 
        :-ms-backface-visibility :visible]
       [:backface-visibility :hidden
        :-webkit-backface-visibility :hidden 
        :-moz-backface-visibility :hidden 
        :-ms-backface-visibility :hidden]) 

     (if persp-origin
       [:perspective-origin persp-origin
        :-webkit-perspective-origin persp-origin]) 

     (if persp
       [:perspective persp
        :-webkit-perspective persp]) 

     (if child3d
       [:transform-style :preserve-3d
        :-webkit-transform-style :preserve-3d]
       [:transform-style :flat
        :-webkit-transform-style :flat])

     (if origin
       [:transform-origin origin 
        :-ms-transform-origin origin  
        :-webkit-transform-origin origin  
        :-moz-transform-origin origin 
        :-o-transform-origin origin])

     (if (or matrix translate rotate scale skew)
       (let 
         [t (if matrix
              (gen-css-matrix matrix)
              [(if translate 
                 (let [[x y z] translate] 
                   (if z
                     (str "translate3d(" x "," y "," z ")")
                     (str "translate(" x "," y ")"))))
               (if rotate
                 (if (coll? rotate)
                   (let [[x y z] (map #(if (number? %) (str % "deg") %) rotate)]
                     (str "rotateX(" x ") "
                          "rotateY(" y ") "
                          "rotateZ(" z ")"))
                   (str "rotate(" (if (number? rotate) (str rotate "deg") rotate) ")")))
               (if scale
                 (let [[x y z] scale] 
                   (if z
                     (str "scale3d(" x "," y "," z ")")
                     (str "scale(" x "," y ")"))))
               (if skew
                 (let [[x y] skew]
                   (str "skew(" x "," y ")")))])]
         [:transform t
          :-ms-transform t 
          :-moz-transform t 
          :-webkit-transform t 
          :-o-transform t]))))

(defn transition
  [& transitions] 
  ;TODO: test me
  (let 
    [v ((partial sep-to-str ", ")
          (for [t (value-to-str transitions)]
            (let [[prop duration timing start] t]
              (str prop " " 
                   (str duration 
                        (if (number? duration) "s")) " "
                   timing " "
                   (str start
                        (if (number? start) "s"))))))]
    (style [:transition v
            :-moz-transition v
            :-o-transition v
            :-webkit-transition v])))

(defn apply-transition
  [ele transitions & {:keys [on to]}]
  ;TODO: test me
  (let [[ele transitions] (value-to-str [ele transitions])] 
    (if on
      (let [[on to] (value-to-str [on to])] 
        (onto [ele [(transition transitions)] [(str "&" on) [to]]]))
      (onto [ele [(transition transitions)]]))))

(defn keyframe
  [kfname & key-points]
  ;TODO: test me
  (let [flow 
        (str " " kfname " {"
             (apply apply-to 
                    (map #(list (str (value-to-str (first %)) 
                                     (if (number? (first %)) "%"))
                                (second %)) key-points))
             "}\n"
             )]
    (str "@keyframes" flow
         "@-moz-keyframes" flow
         "@-webkit-keyframes" flow
         "@-o-keyframes" flow)))

(ns cssoj.css3
  (:using cssoj.core))

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
    (if bax-shadow
      (style [:box-shadow box-shadow]))))

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
    [(if backface
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
       [:transform-style preserve-3d
        :-webkit-transform-style preserve-3d]
       [:transform-style flat
        :-webkit-transform-style flat])

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
          :-o-transform t]))]))

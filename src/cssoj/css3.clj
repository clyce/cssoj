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
  [& {:keys []}]
  ;TODO: complete this
  )

(ns cssoj.core)

(defn sep-to-str 
  ;TODO: write the doc 
  [sep coll]
  (apply str
         (butlast 
           (interleave coll
             (repeat (count coll) sep)))))

(defn value-to-str 
  ;TODO: write the doc 
  [coll]
  (if (coll? coll)
    ((if (map? coll) 
       mapcat map) value-to-str coll)  
    (if (keyword? coll)
      (apply str (rest (str coll)))
      (if (string? coll)
        coll
        (str coll)))))

(defn- process-attr 
  ;TODO: write the doc 
  [attr]
  (if (string? attr) 
    attr
    (apply str 
      (if (some coll? attr) 
        (reduce #(str % ", " %2) (map process-attr attr)) 
        (reduce #(str % " " %2) attr)))))

(defn- process-pair 
  ;TODO: write the doc 
  [pair]
  (let [pair (value-to-str pair)]
    (if (coll? pair)
      (apply str
        (map #(let [[k v] %] (str k ": " (process-attr v) "; ")) 
          (partition 2 (if (map? pair) (apply concat pair) pair)))) 
      pair)))

(defn process-nest 
  ;TODO: write the doc 
  [p c]
  (let [c (value-to-str c)]
    (condp = (first c)
      \& (str p c)
      (str p " " c))))

(defn prettify 
  ;TODO: write the doc 
  [css-string]
  (apply str 
    (map #(condp = %
            \{ "{\n  "
            \} "\n}\n"
            \; ";\n  "
            %)
      css-string)))

(defn style 
  ;TODO: write the doc 
  [& style-list]
  (apply str
    (map process-pair
      style-list)))

(defn onto 
  ;TODO: write the doc 
  [[ele stl & children]] 
  (let [ele (value-to-str ele)]
    (apply str
      (str ele " {" 
        (apply style stl) 
        "}\n")
      (map 
        #(onto (concat [(process-nest ele  (first %))] 
                 (rest %)))
        children))))

(defn apply-to 
  ;TODO: write the doc 
  [& args]
  (apply str
    (map 
      #(if (string? %) % (onto %))
      args)))

(defn add-unit 
  ;TODO: write the doc 
  [unit v]
  (str (value-to-str v) (if (number? v) (value-to-str unit))))

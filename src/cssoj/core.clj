(ns cssoj.core)

(defn- keyword2str [coll]
  (if (coll? coll)
    (if (map? coll)
      (apply concat (map keyword2str coll))  
      (map keyword2str coll)) 
    (if (keyword? coll)
      (apply str (rest (str coll)))
      coll)))

(defn- process-attr [attr]
  (if (string? attr) 
    attr
    (if (some coll? attr) 
      (apply str (reduce #(str % ", " %2) (map process-attr attr))) 
      (apply str (reduce #(str % " " %2) attr)))))

(defn- process-pair [pair]
  (let [pair (keyword2str pair)]
    (if (coll? pair)
      (apply str
      (map #(let [[k v] %]
             (str k ": " 
                 (process-attr v) "; ")) 
           (if (map? pair)
             pair
             (apply hash-map pair)))) 
      pair)))

(defn prettify [css-string]
  (apply str 
         (map
           #(condp = %
              \{ " {\n  "
              \} "\n}\n"
              \; ";\n  "
              %)
           css-string)))

(defn style [& style-list]
  (apply str
         (map process-pair
              style-list)))

(defn onto [[ele stl & children]] 
  (let [ele (keyword2str ele)]
    (apply str
           (str ele "{" 
                (style stl) 
                "}\n")
           (map 
             #(apply-to (concat [(str ele " " (keyword2str (first %)))] 
                                (rest %)))
             children))))

(defn apply-to [& args]
  (apply str
         (map 
           #(if (string? %)
                 %
              (onto %))
           args)))

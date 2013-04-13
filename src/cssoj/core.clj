(ns cssoj.core)

(defn- value-to-str [coll]
  (if (coll? coll)
    ((if (map? coll) 
       mapcat map) value-to-str coll)  
    (if (keyword? coll)
      (apply str (rest (str coll)))
      (if (string? coll)
        coll
        (str coll)))))

(defn- process-attr [attr]
  (if (string? attr) 
    attr
    (apply str 
           (if (some coll? attr) 
             (reduce #(str % ", " %2) (map process-attr attr)) 
             (reduce #(str % " " %2) attr)))))

(defn- process-pair [pair]
  (let [pair (value-to-str pair)]
    (if (coll? pair)
      (apply str
             (map #(let [[k v] %] (str k ": " (process-attr v) "; ")) 
                  (partition 2 (if (map? pair) (apply concat pair) pair)))) 
      pair)))

(defn process-nest [p c]
  (let [c (value-to-str c)]
    (condp = (first c)
      \& (str p c)
      (str p " " c))))

(defn prettify [css-string]
  (apply str 
         (map #(condp = %
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
  (let [ele (value-to-str ele)]
    (apply str
           (str ele "{" 
                (apply style stl) 
                "}\n")
           (map 
             #(onto (concat [(process-nest ele  (first %))] 
                                (rest %)))
             children))))

(defn apply-to [& args]
  (apply str
         (map 
           #(if (string? %) % (onto %))
           args)))

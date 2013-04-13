# cssoj

A Clojure library designed to generate css using clojure code, under construction

Now supports:

 - generate raw styles using (style ...) function, e.g:

```clojure
(style {:background :#FFF :color :#333}
       [:padding :10px :margin :5px]
	   "float: left; text-align: center;")
```

	generates

```css
background:#FFF; color:#333; padding:10px; margin:5px; float:left; text-align:center;
```

 - build mixin styles using (style ...) function, e.g:
	
```clojure
(defn test-style [float]
 (style [:float float :text-align center]))

(style 
  (style {:background :#FFF :color :#333}
         [:padding :10px])
  [:margin :5px]
  (test-style :left))
```
	generates

```css
background:#FFF; color:#333; padding:10px; margin:5px; float:left; text-align:center;
```

README NOT FINISHED... THERE ARE MORE....

## Usage



## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License, the same as Clojure.

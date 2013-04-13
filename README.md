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

 - apply style on certain selector(s), with nested rules, e.g:

```clojure
(apply-to 
 [:#test-div
  [{:background :#FFF :color :#333}
   [:padding :10px :margin :5px]]]
 [:#test-div2
  [[:background :#DDD :color :#000]]
  [:ul [[:margin :10px]]
   [:li [(test-style :left)]]]])
```
	generates

```css
#test-div{background:#FFF; color:#333; padding:10px; margin:5px;}
#test-div2{background:#DDD; color:#000}
#test-div2 ul{margin:10px;}
#test-div2 ul li{float:left; text-align:center;}
```

 - useful css3 helpers(under construction) e.g:

```clojure
```
	generates

```css
```

README NOT FINISHED... THERE ARE MORE....

## Usage

TODO: WRITE THIS

## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License, the same as Clojure.

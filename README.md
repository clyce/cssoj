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

 - prtettify the output e.g

```clojure
(println
  (prettify
    (apply-to 
     [:#test-div
      [{:background :#FFF :color :#333}
       [:padding :10px :margin :5px]]]
     [:#test-div2
      [[:background :#DDD :color :#000]]
      [:ul [[:margin :10px]]
       [:li [(test-style :left)]]]])))
```
	prints

```css
#test-div{
  background:#FFF; 
  color:#333; 
  padding:10px; 
  margin:5px;

}

#test-div2{
  background:#DDD; 
  color:#000

}

#test-div2 ul{
  margin:10px;

}

#test-div2 ul li{
  float:left; 
  text-align:center;

}
```

 - useful css3 helpers(under construction) e.g:

```clojure
(println 
  (prettify
    (apply-to [:div#testcss3
               (css3/transform :translate ["1px" "2px"] 
                               :rotate [30 60 30] 
                               :scale ["50%" "70%" "90%"]
                               :persp 50
                               :backface true)])))
```
outputs

```css
div#testcss3 {
  -ms-backface-visibility: visible;
   -moz-backface-visibility: visible;
   backface-visibility: visible;
   -webkit-backface-visibility: visible;
   perspective: 50;
   -webkit-perspective: 50;
   transform-style: preserve-3d;
   -webkit-transform-style: preserve-3d;
   -o-transform: translate(1px,2px) rotateX(30deg) rotateY(60deg) rotateZ(30deg) scale3d(50%,70%,90%) ;
   -moz-transform: translate(1px,2px) rotateX(30deg) rotateY(60deg) rotateZ(30deg) scale3d(50%,70%,90%) ;
   transform: translate(1px,2px) rotateX(30deg) rotateY(60deg) rotateZ(30deg) scale3d(50%,70%,90%) ;
   -webkit-transform: translate(1px,2px) rotateX(30deg) rotateY(60deg) rotateZ(30deg) scale3d(50%,70%,90%) ;
   -ms-transform: translate(1px,2px) rotateX(30deg) rotateY(60deg) rotateZ(30deg) scale3d(50%,70%,90%) ;
   
}
```

README NOT FINISHED... THERE ARE MORE....

## Usage

TODO: WRITE THIS

## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License, the same as Clojure.

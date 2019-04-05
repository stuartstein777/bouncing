(ns bouncing.collision-detection)

(defn- deltax
  [circle-x, rect-x]
  (- circle-x
     (apply max [rect-x
                 (apply min [circle-x,
                             (+ rect-x 20)])])))

(defn- deltay
  [circle-y, rect-y]
  (- circle-y
     (apply max [rect-y
                 (apply min [circle-y,
                             (+ rect-y 100)])])))

; hitting-left-wall:: x -> -> width -> bool
(defn hitting-left-wall
  [x width]
  (if (<= x (/ width 2))
    true
    false))

; hitting-right-wall:: x -> width -> bool
(defn hitting-right-wall
  [ball-x ball-width]
  (if (>= ball-x (+ (- 800 ball-width) (/ ball-width 2)))
    true
    false))

; collided-with-paddle:: state -> bool
; (deltax * deltax) + (deltay * deltay) < (radius * radius)
(defn collided-with-paddle
  [state width]
  (let [deltax (deltax (:x state) 0)
        deltay (deltay (:y state) (:paddley state))
        radius (/ width 2)]
    (< (+ (* deltax deltax) (* deltay deltay)) (* radius radius))))

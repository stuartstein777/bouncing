(ns bouncing.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [clojure.math.numeric-tower :as math]))

(def speed 10)
(def width 40)
(defn setup []
  (q/frame-rate 30)
  (q/color-mode :hsb)
  {:x 0
   :y 250
   :delta speed
   :paddley 120})

(defn get-paddley
  [paddley change]
  (let [new-y (+ paddley change)]
    (if (< new-y 0)
      0
      (if (> new-y (- (q/height) 100))
        (- (q/height) 100)
        new-y))))

(defn collided-with-paddle
  [state]
  (if (<= (:x state) width)
    (if (<= (math/abs (- (:paddley state) (:y state))) width)
        (assoc state :x (+ (:x state) speed) :delta (+ 0 speed))
        (assoc state :x (+ (:x state) (:delta state))))
    (assoc state :x (+ (:x state) (:delta state))))

(defn key-pressed
  [{ :keys [velocity] :as state} { :keys [key key-code] }]
  (case key
    (:w :up) (if (not= [0 1] velocity) (assoc state :paddley (get-paddley (:paddley state) -20)))
    (:s :down) (if (not= [0 -1] velocity) (assoc state :paddley (get-paddley (:paddley state) 20)))
    state))

(defn update-state [state]
  (if (>= (:x state) (+ (- 800 width) (/ width 2)))
    (assoc state :x (- (:x state) speed) :delta (- 0 speed))
    (if (<= (:x state) (/ width 2))
      (if (= (:delta state) (- 0 speed))
        (assoc state :x (+ (:x state) speed) :delta (+ 0 speed))
        (assoc state :x (+ (:x state) (:delta state)))))
      (assoc state :x (+ (:x state) (:delta state))))))

(defn draw-state [state]
  (q/background 120)
  (q/stroke 0)
  (q/fill 120 120 120)
  (q/ellipse (:x state) (:y state) width width)
  (q/stroke 2)
  (q/fill 0 255 255)
  (q/rect 0 (:paddley state) 25 100))

(q/defsketch bouncing
             :title "Bouncing was the limit of their cognitive range."
             :size [800 800]
             :setup setup
             :update update-state
             :key-pressed key-pressed
             :draw draw-state
             :features [:keep-on-top]
             :middleware [m/fun-mode])
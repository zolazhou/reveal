(ns vlaaad.reveal.keymap
  (:require [vlaaad.reveal.prefs :as prefs])
  (:import [javafx.scene.input KeyEvent KeyCode]))


(def keymap
  {:default {:left  KeyCode/LEFT
             :right KeyCode/RIGHT
             :down  KeyCode/DOWN
             :up    KeyCode/UP}
   :vim     {:left  KeyCode/H
             :right KeyCode/L
             :down  KeyCode/J
             :up    KeyCode/K}
   :emacs   {:left  [KeyCode/CONTROL KeyCode/B]
             :right [KeyCode/CONTROL KeyCode/F]
             :down  [KeyCode/CONTROL KeyCode/N]
             :up    [KeyCode/CONTROL KeyCode/P]}})

(defn key-match?
  [mapping k ^KeyEvent event]
  (let [key (get-in keymap [mapping k])]
    (if (vector? key)
      (let [[modifier code] key]
        (and (= code (.getCode event))
             (condp = modifier
               KeyCode/CONTROL (.isControlDown event)
               KeyCode/ALT (.isAltDown event)
               KeyCode/SHIFT (.isShiftDown event)
               KeyCode/META (.isMetaDown event)
               false)))
      (= key (.getCode event)))))

(def left? (partial key-match? (prefs/keymap) :left))
(def right? (partial key-match? (prefs/keymap) :right))
(def down? (partial key-match? (prefs/keymap) :down))
(def up? (partial key-match? (prefs/keymap) :up))

(defn input-down?
  [event]
  (or (key-match? :default :down event)
      (key-match? :emacs :down event)))

(defn input-up?
  [event]
  (or (key-match? :default :up event)
      (key-match? :emacs :up event)))

(defn copy?
  [^KeyEvent event]
  (and (.isShortcutDown event) (= KeyCode/C (.getCode event))))

(defn select-all?
  [^KeyEvent event]
  (and (.isShortcutDown event) (= KeyCode/A (.getCode event))))

(defn search?
  [^KeyEvent event]
  (and (.isShortcutDown event) (= KeyCode/F (.getCode event))))

(defn clear?
  [^KeyEvent event]
  (and (.isShortcutDown event) (= KeyCode/L (.getCode event))))

(defn direction?
  [^KeyEvent event]
  (#{KeyCode/UP KeyCode/DOWN KeyCode/LEFT KeyCode/RIGHT} (.getCode event)))

(defn escape?
  [^KeyEvent event]
  (= KeyCode/ESCAPE (.getCode event)))

(defn enter?
  [^KeyEvent event]
  (= KeyCode/ENTER (.getCode event)))

(defn page-up?
  [^KeyEvent event]
  (= KeyCode/PAGE_UP (.getCode event)))

(defn page-down?
  [^KeyEvent event]
  (= KeyCode/PAGE_DOWN (.getCode event)))

(defn home?
  [^KeyEvent event]
  (= KeyCode/HOME (.getCode event)))

(defn end?
  [^KeyEvent event]
  (= KeyCode/END (.getCode event)))

(defn space?
  [^KeyEvent event]
  (= KeyCode/SPACE (.getCode event)))

(defn slash?
  [^KeyEvent event]
  (= KeyCode/SLASH (.getCode event)))

(defn tab?
  [^KeyEvent event]
  (= KeyCode/TAB (.getCode event)))

(defn backspace?
  [^KeyEvent event]
  (= KeyCode/BACK_SPACE (.getCode event)))

(defn delete?
  [^KeyEvent event]
  (= KeyCode/DELETE (.getCode event)))

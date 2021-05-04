(ns vlaaad.reveal.prefs
  (:require [clojure.spec.alpha :as s]
            [clojure.edn :as edn]
            [clojure.main :as m]
            [clojure.java.io :as io])
  (:import [java.net URL MalformedURLException]
           [javafx.scene.text Font]))

(s/def ::font-size
  (s/and number? pos?))

(defn- valid-url? [s]
  (try
    (URL. s) true
    (catch MalformedURLException _ false)))

(def ^:private system-font-families
  (delay (set (Font/getFamilies))))

(defn- system-font? [s]
  (contains? @system-font-families s))

(s/def ::font-family
  (s/or :url-string (s/and string? valid-url?)
        :system-font system-font?))

(s/def ::theme
  #{:dark :light})

(s/def ::keymap
  #{:default :vim :emacs})

(s/def ::x float?)
(s/def ::y float?)
(s/def ::width float?)
(s/def ::height float?)
(s/def ::window (s/keys :opt-un [::x ::y ::width ::height]))

(s/def ::prefs
  (s/keys :opt-un [::font-family ::font-size ::theme ::keymap]))

(defn- config-file
  [t]
  (io/file (System/getProperty "user.home") ".config" "reveal" (str (name t) ".edn")))

(def prefs
  (delay
    (try
      (let [raw   (edn/read-string (slurp (config-file :config)))
            prefs (s/conform ::prefs raw)]
        (when (s/invalid? prefs)
          (throw (ex-info "Invalid prefs" (s/explain-data ::prefs raw))))
        prefs)
      (catch Exception e
        (println "Failed to read reveal prefs")
        (println (-> e Throwable->map m/ex-triage m/ex-str))))))

(defn update!
  [f]
  (let [value (f @prefs)]
    (when (not= value @prefs)
      (spit (config-file :config) (pr-str value)))))

(defn keymap [] (:keymap @prefs))

(defn window
  []
  (try
    (let [raw (edn/read-string (slurp (config-file :window)))
          win (s/conform ::window raw)]
      (when (s/invalid? win)
        (throw (ex-info "Invalid window settings" (s/explain-data ::window raw))))
      win)
    (catch Exception _e
      (println "Failed to read reveal window settings")
      {:x 100.0 :y 100.0 :width 400.0 :height 500.0})))

(defn persistent-window!
  [window]
  (spit (config-file :window) (pr-str window)))

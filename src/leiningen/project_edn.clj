;   Copyright (c) Shantanu Kumar. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file LICENSE at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.


(ns leiningen.project-edn
  (:refer-clojure :exclude [select-keys])
  (:require
    [clojure.edn     :as edn]
    [clojure.java.io :as io]
    [clojure.pprint  :as pp]
    [robert.hooke    :as hooke]
    [leiningen.core.main :as lmain]
    [leiningen.compile   :as lcompile])
  (:import
    [java.util.regex Pattern]))


(defn normalize
  "Given a potential EDN value, recursively normalize it as a parseable EDN."
  [x]
  (cond
    (map? x)     (let [ks (keys x)
                       vs (vals x)]
                   (->> (normalize vs)
                     (interleave (normalize ks))
                     (apply array-map)))
    (vector? x)  (mapv normalize x)
    (set? x)     (set (map normalize x))
    (list? x)    (list* (map normalize x))
    (coll? x)    (list* (map normalize x))
    (string? x)  x
    (number? x)  x
    (keyword? x) x
    (symbol? x)  (let [fx (first (str x))]
                   (cond
                     (Character/isLetter fx) x
                     (#{\$ \%} fx)           x
                     ;; lossy coercion because we have EDN-incompatible symbol
                     :otherwise              (str x)))
    (true? x)    x
    (false? x)   x
    (nil? x)     x
    ;; coerce as over-simplified (lossy) EDN values
    (var? x)     (str x)
    (instance?
      Pattern x) (pr-str x)
    ;; fallback error
    :otherwise   (lmain/abort (format "[lein-project-edn] Encountered unknown EDN value: (%s) %s"
                                (class x) (pr-str x)))))


(defn emit-project-edn
  "Emit project map as EDN."
  [{:keys [project-edn]
    :as project}]
  (let [{:keys [output-prefix
                output-suffix
                output-file
                output-mkdirs?
                select-keys
                remove-keys
                verify-edn?]
         :or {remove-keys [:injections  ; Leiningen's monkey-patching thunk when running tests
                           :uberjar-merge-with]
              output-mkdirs? true
              verify-edn? true}} project-edn
        nor-proj (as-> project <>
                   (clojure.core/select-keys <> (or select-keys (keys project)))
                   (apply dissoc <> remove-keys)
                   (normalize <>))
        edn-text (->> (keys nor-proj)
                   sort
                   (mapcat (fn [k] [k (get nor-proj k)]))
                   (apply array-map)
                   pp/pprint
                   with-out-str)
        out-text (str output-prefix edn-text output-suffix)]
    (when verify-edn?
      (try
        (edn/read-string edn-text)
        (catch Exception e
          (.printStackTrace e)
          (lmain/abort (str "[lein-project-edn] Error parsing generated EDN:\n" edn-text)))))
    (if output-file
      (do
        (when output-mkdirs?
          (io/make-parents output-file))
        (spit output-file out-text))
      (println out-text))))


(defn project-edn
  "Emit project details as EDN"
  [project]
  (emit-project-edn project))


(defn project-edn-hook
  "Emit EDN ahead of the hooked task."
  [task project & args]
  (project-edn project)
  (apply task project args))


(defn activate
  "Activate the hook on Leiningen compile task."
  []
  (hooke/add-hook #'lcompile/compile #'project-edn-hook))

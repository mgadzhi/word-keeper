(ns word-keeper.db
  (:require [korma.db :refer [defdb postgres]]
            [korma.core :refer :all]))

(defdb dev-db (postgres {:db "wordkeeper"
                         :user "wordkeeper"}))

(defentity users)
(defentity languages)
(defentity translations)

(defn get-users []
  (select users))

(defn get-languages []
  (select languages))

(defn get-vocabularies [uid]
  (select translations
          (fields :from_lang :to_lang)
          (where {:user_id uid})
          (group :from_lang :to_lang)))

(defn get-vocabulary [uid from to]
  (select translations
          (where {:user_id uid
                  :from_lang from
                  :to_lang to})))

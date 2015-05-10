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

(defn get-translations [uid from to]
  (select translations
          (where {:user_id uid
                  :from_lang from
                  :to_lang to})))

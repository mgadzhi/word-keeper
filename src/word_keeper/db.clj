(ns word-keeper.db
  (:require [korma.db :refer [defdb postgres]]
            [korma.core :refer :all]))

(defdb dev-db (postgres {:db "wordkeeper"
                         :user "wordkeeper"}))

(defentity users
  (has-many translations {:fk :user_id}))
(defentity languages)
(defentity translations)

(defn get-users []
  (select users))

(defn get-languages []
  (select languages))

(defn get-translations []
  (select users (with translations)))

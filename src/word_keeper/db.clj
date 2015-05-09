(ns word-keeper.db
  (:require [korma.db :refer [defdb postgres]]
            [korma.core :refer [defentity
                                entity-fields
                                select]]))

(defdb dev-db (postgres {:db "wordkeeper"
                         :user "wordkeeper"}))

(defentity users)
(defentity languages)
(defentity translations)

(defn get-users []
  (select users))

(defn get-languages []
  (select languages))

(defn get-translations []
  (select translations))

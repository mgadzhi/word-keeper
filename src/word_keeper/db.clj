(ns word-keeper.db
  (:require [korma.db :refer [defdb postgres]]
            [korma.core :refer [defentity
                                entity-fields
                                select]]))

(defdb dev-db (postgres {:db "wordkeeper"
                         :user "wordkeeper"}))

(defentity language)

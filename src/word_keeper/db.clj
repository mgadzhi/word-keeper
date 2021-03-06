(ns word-keeper.db
  (:require [korma.db :refer [defdb postgres]]
            [korma.core :refer :all]))

(defdb dev-db (postgres {:host (get (System/getenv) "OPENSHIFT_POSTGRESQL_DB_HOST" "localhost")
                         :port (get (System/getenv) "OPENSHIFT_POSTGRESQL_DB_PORT" "5432")
                         :db   (get (System/getenv) "OPENSHIFT_APP_NAME" "wordkeeper")
                         :user (get (System/getenv) "OPENSHIFT_POSTGRESQL_DB_USERNAME" "wordkeeper")
                         :password (get (System/getenv) "OPENSHIFT_POSTGRESQL_DB_PASSWORD" "")}))

(defentity users)
(defentity languages)
(defentity translations)

(defn get-users []
  (select users))

(defn select-user [login]
  (first
   (select users
           (where {:login login}))))

(defn insert-user [login password]
  (insert users
          (values {:login login
                   :password password})))

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

(defn insert-translation [uid from to word trans]
  (insert translations
          (values {:user_id uid
                   :from_lang from
                   :to_lang to
                   :word word
                   :trans trans})))

(defn delete-translation [uid from to word trans]
  (delete translations
          (where {:user_id uid
                  :from_lang from
                  :to_lang to
                  :word word
                  :trans trans})
          (limit 1)))

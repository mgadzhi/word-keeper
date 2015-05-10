(defn local-repo-path []
  (get (System/getenv) "M2_REPO" (str (get (System/getenv) "HOME") "/.m2/repository")))

(defn db-url []
  (str
    "jdbc:postgresql://"
    (get (System/getenv) "OPENSHIFT_POSTGRESQL_DB_HOST" "localhost")
    ":"
    (get (System/getenv) "OPENSHIFT_POSTGRESQL_DB_PORT" "5432")
    "/"
    (get (System/getenv) "OPENSHIFT_APP_NAME" "wordkeeper")
    "?user="
    (get (System/getenv) "OPENSHIFT_POSTGRESQL_DB_USERNAME" "wordkeeper")
    "&password="
    (get (System/getenv) "OPENSHIFT_POSTGRESQL_DB_PASSWORD" "")))

(defproject word-keeper "0.1.0-SNAPSHOT"
  :description "JSON API for word keeper"
  :url "https://github.com/mgadzhi/word-keeper"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [ring "1.4.0-beta1"]
                 [http-kit "2.1.18"]
                 [compojure "1.3.3"]
                 [cheshire "5.4.0"]
                 [korma "0.4.0"]
                 [org.postgresql/postgresql "9.2-1002-jdbc4"]
                 [ragtime "0.3.8"]]
  :local-repo ~(local-repo-path)
  :plugins [[ragtime/ragtime.lein "0.3.8"]]
  :ragtime {:migrations ragtime.sql.files/migrations
            :database ~(db-url)}
  :main word-keeper.core)

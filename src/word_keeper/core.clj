(ns word-keeper.core
  (:require [org.httpkit.server :refer [run-server]]
            [ring.middleware.reload :as reload]
            [compojure.handler :refer [site]]
            [compojure.core :refer [defroutes GET POST DELETE]]
            [compojure.route :refer [not-found]]
            [cheshire.core :refer [generate-string]]
            [word-keeper.db :as db]
            [buddy.hashers :as hashers]))

(defn in-dev? [] true)

(def uid 1)

(defn hello [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str "<h1>Hello, " (-> req :params :user) "</h1>")})

(defn logged-handler [func]
  (fn [req]
    (do
      (println req)
      (func req))))

(defn json-response [body & status]
  (let [status (or status 200)]
    {:status status
     :headers {"Content-Type" "application/json; charset=utf-8"}
     :body (generate-string body)}))

(defn validate-password [password]
  (>= (count password) 6))

(defn users [req]
  (json-response (db/get-users)))

(defn create-user [req]
  (let [login (-> req :params :login)
        password (-> req :params :password)
        maybe-existed-user (db/select-user login)
        valid-password? (validate-password password)]
    (if maybe-existed-user
      (json-response
       {:message "Login is already used"}
       409)
      (if-not valid-password?
        (json-response
         {:message "Password must be at least 6 characters long"}
         400)
        (do
          (db/insert-user login (hashers/encrypt password))
          (json-response {:message "User successfully created"}))))))

(defn languages [req]
  (json-response (db/get-languages)))

(defn vocabularies [uid]
  (fn [req]
    (json-response
     (db/get-vocabularies uid))))

(defn vocabulary [uid from to]
  (fn [req]
    (let [vocab (db/get-vocabulary uid from to)]
      (json-response vocab))))

(defn create-translation [uid from to]
  (fn [req]
    (let [word (-> req :params :word)
          trans (-> req :params :trans)]
      (db/insert-translation uid from to word trans)
      (json-response {:message "Translation is created"}))))

(defn delete-translation [uid from to]
  (fn [req]
    (let [word (-> req :params :word)
          trans (-> req :params :trans)]
      (db/delete-translation uid from to word trans)
      (json-response {:message "Translation is deleted"}))))

(defroutes routes
  (GET "/" [] (logged-handler hello))
  (GET "/users" [] users)
  (POST "/users" [] create-user)
  (GET "/languages" [] languages)
  (GET "/vocabularies" [] (vocabularies uid))
  (GET "/vocabulary/:from/:to" [from to] (vocabulary uid from to))
  (POST "/vocabulary/:from/:to" [from to] (create-translation uid from to))
  (DELETE "/vocabulary/:from/:to" [from to] (delete-translation uid from to))
  (not-found "Page not found"))

(defn -main [& args]
  (let [handler (if (in-dev?)
                  (reload/wrap-reload (site #'routes))
                  (site routes))
        port (Integer/parseInt (get (System/getenv) "OPENSHIFT_CLOJURE_HTTP_PORT" "8080"))
        ip (get (System/getenv) "OPENSHIFT_CLOJURE_HTTP_IP" "0.0.0.0")]
    (run-server handler {:ip ip :port port})
    (println "Application run " ip ":" port)))

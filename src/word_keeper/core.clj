(ns word-keeper.core
  (:require [org.httpkit.server :refer [run-server]]
            [ring.middleware.reload :as reload]
            [compojure.handler :refer [site]]
            [compojure.core :refer [defroutes GET POST]]
            [compojure.route :refer [not-found]]
            [cheshire.core :refer [generate-string]]
            [word-keeper.db :refer [get-users
                                    get-languages
                                    get-vocabularies
                                    get-vocabulary]]))

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

(defn users-view [req]
  (json-response (get-users)))

(defn languages-view [req]
  (json-response (get-languages)))

(defn vocabularies-view [uid]
  (fn [req]
    (json-response
     (get-vocabularies uid))))

(defn vocabulary-view [uid from to]
  (fn [req]
    (let [vocab (get-vocabulary uid from to)]
      (json-response vocab))))

(defroutes routes
  (GET "/" [] (logged-handler hello))
  (GET "/users" [] users-view)
  (GET "/languages" [] languages-view)
  (GET "/vocabularies" [] (vocabularies-view uid))
  (GET "/vocabulary/:from/:to" [from to] (vocabulary-view uid from to))
  (not-found "Page not found"))

(defn -main [& args]
  (let [handler (if (in-dev?)
                  (reload/wrap-reload (site #'routes))
                  (site routes))
        port (Integer/parseInt (get (System/getenv) "OPENSHIFT_CLOJURE_HTTP_PORT" "8080"))
        ip (get (System/getenv) "OPENSHIFT_CLOJURE_HTTP_IP" "0.0.0.0")]
    (run-server handler {:ip ip :port port})
    (println "Application run " ip ":" port)))

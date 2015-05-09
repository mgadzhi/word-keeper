(ns word-keeper.core
  (:require [org.httpkit.server :refer [run-server]]
            [ring.middleware.reload :as reload]
            [compojure.handler :refer [site]]
            [compojure.core :refer [defroutes GET POST]]
            [compojure.route :refer [not-found]]
            [cheshire.core :refer [generate-string]]
            [word-keeper.db :refer [get-users get-languages get-translations]]))

(defn in-dev? [] true)

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
     :headers {"Content-Type" "application/json"}
     :body (generate-string body)}))

(defn users-view [req]
  (json-response (get-users)))

(defn languages-view [req]
  (json-response (get-languages)))

(defroutes routes
  (GET "/" [] (logged-handler hello))
  (GET "/users" [] users-view)
  (GET "/languages" [] languages-view)
  (not-found "Page not found"))

(defn -main [& args]
  (let [handler (if (in-dev?)
                  (reload/wrap-reload (site #'routes))
                  (site routes))]
    (run-server handler {:port 8080})
    (println "Head to localhost:8080")))

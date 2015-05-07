(ns word-keeper.core
  (:require [org.httpkit.server :refer [run-server]]
            [ring.middleware.reload :as reload]
            [compojure.handler :refer [site]]
            [compojure.core :refer [defroutes GET POST]]
            [compojure.route :refer [not-found]]))

(defn in-dev? [] true)

(defroutes routes
  (GET "/" [] "Hello, world!")
  (not-found "Page not found"))

(defn -main [& args]
  (let [handler (if (in-dev?)
                  (reload/wrap-reload (site #'routes))
                  (site routes))]
    (run-server handler {:port 8080})))

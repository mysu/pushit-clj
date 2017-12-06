PushIt - Server - Clojure version
---------------------------------

This project was configured using the Reagent template: https://github.com/reagent-project/reagent-template

## Commands:

* Run: ```lein figwheel```
* Access a browser: http://localhost:3449

* Generate jar: ```lein uberjar```
* Run jar (with customConfig): java -jar -Dconfig=<PATH_TO_CUSTOM_CONFIG.EDN> target/pushit-clj.jar

## Config

    {
      :basehost "localhost"
      :port 3449
    }

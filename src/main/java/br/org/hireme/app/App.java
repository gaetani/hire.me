package br.org.hireme.app;

import static spark.Spark.*;

import br.org.hireme.controller.ShortenerController;
import br.org.hireme.service.ShortenerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Session;

public class App {

    private static Logger logger = LoggerFactory.getLogger(App.class);
    // Enables CORS on requests. This method is an initialization method and should be called once.
    private static void enableCORS(final String origin, final String methods, final String headers) {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            // Note: this may or may not be necessary in your particular application
            response.type("application/json");
        });
    }


    private void configureRoutes(){
        //ensure user is logged in to have access to protected routes
        before((req, res) -> {
            req.attribute("begin", System.currentTimeMillis());

        });

        after((req, res) -> {
            long time = req.attribute("begin");
            res.body(res.body() + "\netere:"+(time - System.currentTimeMillis()));

        });

        ShortenerController shortenerService = new ShortenerController();
        put( "/create/:url/:alias", "application/json", shortenerService::shortIt);

        //Gson gson = new Gson();
        get("/:alias", "application/json", shortenerService::getAlias);
    }


    private App() {
        port(getHerokuAssignedPort());
        configureRoutes();
    }



    private static int getHerokuAssignedPort() {
//         this will get the heroku assigned port in production
//         or return 8080 for use in local dev
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 8080; //return 8080 on localhost
    }


    public static void main(String[] args) {
        new App();
    }
}
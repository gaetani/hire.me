package br.org.hireme.app;

import static spark.Spark.*;

import br.org.hireme.controller.IShortenerController;
import br.org.hireme.controller.ShortenerController;
import br.org.hireme.routes.IRoutesDefinition;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;

public class App {


    private App() {

        port(getHerokuAssignedPort());
        Injector injector = Guice.createInjector(new Module() );
        IRoutesDefinition routesDefinition = injector.getInstance( IRoutesDefinition.class );

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
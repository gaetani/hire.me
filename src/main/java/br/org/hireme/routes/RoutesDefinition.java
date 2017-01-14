package br.org.hireme.routes;

import br.org.hireme.controller.IShortenerController;
import br.org.hireme.exception.BusinessException;
import br.org.hireme.handler.RequestLogHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;

import javax.annotation.PostConstruct;

import static spark.Spark.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;

import static spark.Spark.*;

public class RoutesDefinition implements IRoutesDefinition {


    private IShortenerController shortenerController;

    @Inject
    public RoutesDefinition(IShortenerController shortenerController){
        this.shortenerController = shortenerController;
    }

    private void beforeRoutes(){
        //ensure user is logged in to have access to protected routes
        before((req, res) -> RequestLogHandler.logRequest(req, res));
    }


    private void configureRoutes(){

        get( "/create", "application/json", shortenerController::shortIt);

        //Gson gson = new Gson();
        get("/:alias", "application/json", shortenerController::getAlias);

    }

    private void configureExceptions(){
        exception(BusinessException.class, (exception, request, response) -> {
            // Handle the exception hereJsonObject timeTaken = new JsonObject();


            BusinessException businessException = (BusinessException) exception;
            JsonObject exceptionObject = new JsonObject();
            exceptionObject.addProperty("alias", businessException.getAlias());
            exceptionObject.addProperty("err_code", businessException.getCodeError().getCode());
            exceptionObject.addProperty("description", businessException.getCodeError().getDescription());
            response.status(businessException.getCodeError().getHttpCode());
            response.body(new Gson().toJson(exceptionObject));
        });
    }


    private void afterRoutes(){
        after((req, res) -> {
            //Nothing yet
        });
    }

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



    @PostConstruct
    public void init(){
        beforeRoutes();
        configureRoutes();
        afterRoutes();
        configureExceptions();
    }
}

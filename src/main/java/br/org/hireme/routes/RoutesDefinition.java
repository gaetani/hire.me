package br.org.hireme.routes;

import br.org.hireme.controller.IShortenerController;
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

    private RestClient restClient = RestClient.builder(
            new HttpHost("localhost", 9200, "http")).build();


    private void beforeRoutes(){
        //ensure user is logged in to have access to protected routes
        before((req, res) -> {
            Instant begin = Instant.now();
            req.attribute("begin", begin);

            Gson gson = new Gson();

            JsonObject request = new JsonObject();
            JsonObject header = new JsonObject();
            JsonObject param = new JsonObject();

            (req.headers().stream()).forEach(name -> header.addProperty(name, req.headers(name)));
            (req.queryParams().stream()).forEach(name -> param.addProperty(name, req.queryParams(name)));

            DateTimeFormatter formatador =
                    DateTimeFormatter.ISO_INSTANT;
            request.addProperty("user-agent", req.userAgent());
            request.addProperty("content-length", req.contentLength());
            request.addProperty("content-type", req.contentType());
            request.addProperty("body", req.body());
            request.addProperty("context-path", req.contextPath());
            request.addProperty("ip", req.ip());
            request.addProperty("request-method", req.requestMethod());
            request.addProperty("scheme", req.scheme());
            request.addProperty("url", req.url());
            request.addProperty("host", req.host());
            request.addProperty("uri", req.uri());
            request.addProperty("path-info", req.pathInfo());
            request.addProperty("protocol", req.protocol());
            request.addProperty("query-string", req.queryString());
            request.addProperty("timestamp", formatador.format(begin));

            request.add("params", param);
            request.add("headers", header);

            //index a document
            try {
                HttpEntity entity = new NStringEntity(gson.toJson(request), ContentType.APPLICATION_JSON);
                restClient.performRequestAsync(
                        "POST",
                        "/hiremerequest1/log",
                        Collections.<String, String>emptyMap(),
                        entity,
                        new ResponseListener() {
                            @Override
                            public void onSuccess(Response response) {
                                System.out.println(response);
                            }

                            @Override
                            public void onFailure(Exception exception) {
                                exception.printStackTrace();
                            }
                        });


            } catch (Exception e){
                e.printStackTrace();
            }


        });

    }


    private void configureRoutes(){

        get( "/create", "application/json", shortenerController::shortIt);

        //Gson gson = new Gson();
        get("/:alias", "application/json", shortenerController::getAlias);

    }


    private void afterRoutes(){
        after((req, res) -> {
            Instant begin = req.attribute("begin");
            Duration duration = Duration.between(begin, Instant.now());

            DateTimeFormatter formatador =
                    DateTimeFormatter.ISO_INSTANT;

            long seconds = duration.getSeconds();
            long absSeconds = Math.abs(seconds);
            String positive = String.format(
                    "%d:%02d:%02d",
                    absSeconds / 3600,
                    (absSeconds % 3600) / 60,
                    absSeconds % 60);

            res.body(res.body() + "\netere:"+positive);

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
    }
}

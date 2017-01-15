package br.org.hireme.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.*;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by gaetani on 14/01/17.
 */
public class RequestLogHandler {


    private static ExecutorService EXECUTORS = Executors.newFixedThreadPool(3);
    private static RestClient REST_CLIENT = RestClient.builder(
            new HttpHost("localhost", 9200, "http")).build();


    public static void logRequest(Request req, Response res){
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



        EXECUTORS.execute(() -> {

            HttpEntity entity = new NStringEntity(gson.toJson(request), ContentType.APPLICATION_JSON);
            try {
                REST_CLIENT.performRequest(
                        "POST",
                        "/hiremerequest1/log",
                        Collections.<String, String>emptyMap(),
                        entity,
                        new Header() {
                            @Override
                            public String getName() {
                                return "teste";
                            }

                            @Override
                            public String getValue() {
                                return "tes";
                            }

                            @Override
                            public HeaderElement[] getElements() throws ParseException {
                                return new HeaderElement[0];
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }

        });


    }
}

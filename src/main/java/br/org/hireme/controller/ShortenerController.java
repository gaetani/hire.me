package br.org.hireme.controller;


import br.org.hireme.domain.Shortener;
import br.org.hireme.service.IShortenerService;
import br.org.hireme.service.ShortenerService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import spark.Request;
import spark.Response;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Singleton
public class ShortenerController implements IShortenerController {


    private Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    private IShortenerService shortenerService;

    @Inject
    public ShortenerController(IShortenerService shortenerService) {
        this.shortenerService = shortenerService;
    }

    @Override
    public String shortIt(Request request, Response response) throws Exception {

        Optional<String> alias = Optional.ofNullable(request.queryParams("CUSTOM_ALIAS"));
        String url = request.queryParams("url");

        Shortener shortener = shortenerService.shortIt(url, alias);

        JsonElement jsonElement = GSON.toJsonTree(shortener);
        JsonObject jsonShort = jsonElement.getAsJsonObject();
        Instant begin = request.attribute("begin");
        Duration duration = Duration.between(begin, Instant.now());
        long seconds = duration.toMillis();

        String positive = String.format(
                "%02dms",
                seconds);
        JsonObject timeTaken = new JsonObject();
        timeTaken.addProperty("time_taken", positive);
        jsonShort.add("statistics", timeTaken);

        response.status(200);

        return jsonShort.toString();
    }



    @Override
    public String getAlias(Request request, Response response) throws Exception {
        String alias = request.params("alias");
        String url = shortenerService.getUrl(alias).getUrl();

        response.redirect(url);

        return url;
    }
}

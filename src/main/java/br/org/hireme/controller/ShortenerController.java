package br.org.hireme.controller;


import br.org.hireme.domain.Shortener;
import br.org.hireme.service.ShortenerService;
import spark.Request;
import spark.Response;

import java.util.Optional;

public class ShortenerController {


    private ShortenerService shortenerService;

    public Shortener shortIt(Request request, Response response) throws Exception {

        Optional<String> alias = Optional.ofNullable(request.attribute("CUSTOM_ALIAS"));
        String url = request.attribute("url");

        Shortener shortener = shortenerService.shortIt(url, alias);

        return shortener;
    }



    public String getAlias(Request request, Response response) throws Exception {
        String alias = request.attribute("CUSTOM_ALIAS");
        String url = shortenerService.getUrl(alias).url;

        response.redirect(url, 200);

        return url;
    }
}

package br.org.hireme.controller;


import br.org.hireme.domain.Shortener;
import br.org.hireme.service.IShortenerService;
import br.org.hireme.service.ShortenerService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import spark.Request;
import spark.Response;

import java.util.Optional;

@Singleton
public class ShortenerController implements IShortenerController {


    private IShortenerService shortenerService;

    @Inject
    public ShortenerController(IShortenerService shortenerService) {
        this.shortenerService = shortenerService;
    }

    @Override
    public Shortener shortIt(Request request, Response response) throws Exception {

        Optional<String> alias = Optional.ofNullable(request.queryParams("CUSTOM_ALIAS"));
        String url = request.queryParams("url");

        Shortener shortener = shortenerService.shortIt(url, alias);

        return shortener;
    }



    @Override
    public String getAlias(Request request, Response response) throws Exception {
        String alias = request.attribute("CUSTOM_ALIAS");
        String url = shortenerService.getUrl(alias).getUrl();

        response.redirect(url, 200);

        return url;
    }
}

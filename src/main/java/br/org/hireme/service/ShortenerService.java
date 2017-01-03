package br.org.hireme.service;

import br.org.hireme.domain.Shortener;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Optional;

public class ShortenerService {

    public Shortener shortIt(String url, Optional<String> alias){
        alias.orElseGet(() ->{
            return null;
        });
        return null;
    }

    public Shortener getUrl(String alias) {
        return null;
    }
}

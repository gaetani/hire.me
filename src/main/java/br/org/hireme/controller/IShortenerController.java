package br.org.hireme.controller;

import br.org.hireme.domain.Shortener;
import spark.Request;
import spark.Response;

/**
 * Created by gaetani on 11/01/17.
 */
public interface IShortenerController {
    Shortener shortIt(Request request, Response response) throws Exception;

    String getAlias(Request request, Response response) throws Exception;
}

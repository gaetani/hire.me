package br.org.hireme.hello;

import static spark.Spark.*;

/**
 * Created by gaetani on 01/01/17.
 */
public class HelloWorld {
    public static void main(String[] args) {
        get("/hello/:url/:CUSTOM_ALIAS", (req, res) -> "Maricon" +req.params("url") +req.params("CUSTOM_ALIAS"));
    }
}
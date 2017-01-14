package br.org.hireme.exception.domain;

public enum CodeError {

    CUSTOM_ALIAS_ALREADY_EXISTS(401, "001", "CUSTOM ALIAS ALREADY EXISTS"),
    SHORTENED_URL_NOT_FOUND(404, "002", "SHORTENED URL NOT FOUND");

    private int httpCode;
    private String code;
    private String description;

    CodeError(int httpCode, String code, String description){
        this.httpCode = httpCode;
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public int getHttpCode() {
        return httpCode;
    }
}

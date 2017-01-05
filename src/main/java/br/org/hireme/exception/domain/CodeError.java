package br.org.hireme.exception.domain;

public enum CodeError {

    CUSTOM_ALIAS_ALREADY_EXISTS("001", "CUSTOM ALIAS ALREADY EXISTS"),
    SHORTENED_ALIAS_ALREADY_EXISTS("002", "SHORTENED URL NOT FOUND");

    private String code;
    private String description;

    CodeError(String code, String description){
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

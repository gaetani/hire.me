package br.org.hireme.exception;


import br.org.hireme.exception.domain.CodeError;

public class BusinessException extends RuntimeException {

    private CodeError codeError;
    private String alias;

    public String getAlias() {
        return alias;
    }

    public BusinessException(CodeError codeError, String alias) {
        this.alias = alias;
        this.codeError = codeError;
    }


    public CodeError getCodeError() {
        return codeError;
    }
}

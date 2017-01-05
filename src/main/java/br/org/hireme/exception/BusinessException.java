package br.org.hireme.exception;


import br.org.hireme.exception.domain.CodeError;

public class BusinessException extends RuntimeException {

    private CodeError codeError;

    public BusinessException(CodeError codeError) {
        this.codeError = codeError;
    }


    public CodeError getCodeError() {
        return codeError;
    }
}

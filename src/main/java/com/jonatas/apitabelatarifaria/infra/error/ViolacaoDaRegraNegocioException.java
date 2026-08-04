package com.jonatas.apitabelatarifaria.infra.error;

public class ViolacaoDaRegraNegocioException extends RuntimeException {

    private int statusCode;

    public ViolacaoDaRegraNegocioException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

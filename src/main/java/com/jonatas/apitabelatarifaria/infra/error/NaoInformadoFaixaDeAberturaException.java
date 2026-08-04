package com.jonatas.apitabelatarifaria.infra.error;

import org.springframework.http.HttpStatus;

public class NaoInformadoFaixaDeAberturaException extends ViolacaoDaRegraNegocioException {

    private static final String MESSAGEM = "Não foi informado uma faixa de consumo de abertura começando por zero.";

    public NaoInformadoFaixaDeAberturaException() {
        super(MESSAGEM, HttpStatus.UNPROCESSABLE_CONTENT.value());
    }

}

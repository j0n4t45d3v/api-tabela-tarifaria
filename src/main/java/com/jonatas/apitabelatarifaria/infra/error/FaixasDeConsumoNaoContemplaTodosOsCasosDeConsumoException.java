package com.jonatas.apitabelatarifaria.infra.error;

import org.springframework.http.HttpStatus;

public class FaixasDeConsumoNaoContemplaTodosOsCasosDeConsumoException extends ViolacaoDaRegraNegocioException {

    private static final String MESSAGEM = "É esperado que exista uma faixa de consumo que termine com 99999.";

    public FaixasDeConsumoNaoContemplaTodosOsCasosDeConsumoException() {
        super(MESSAGEM, HttpStatus.UNPROCESSABLE_CONTENT.value());
    }

}

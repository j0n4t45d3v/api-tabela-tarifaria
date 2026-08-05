package com.jonatas.apitabelatarifaria.infra.error;

import org.springframework.http.HttpStatus;

public class NaoEhPermitidoQueTenhaSobreposicaoEntreAsFaixasDeConsumoException extends ViolacaoDaRegraNegocioException {

    private static final String MESSAGEM = "Não é permitido que exista faixas de consumo que sobreponha outra.";

    public NaoEhPermitidoQueTenhaSobreposicaoEntreAsFaixasDeConsumoException() {
        super(MESSAGEM, HttpStatus.UNPROCESSABLE_CONTENT.value());
    }

}

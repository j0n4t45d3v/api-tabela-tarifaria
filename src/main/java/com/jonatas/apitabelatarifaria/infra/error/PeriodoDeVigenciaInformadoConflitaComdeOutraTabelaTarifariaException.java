package com.jonatas.apitabelatarifaria.infra.error;

import org.springframework.http.HttpStatus;

public class PeriodoDeVigenciaInformadoConflitaComdeOutraTabelaTarifariaException extends ViolacaoDaRegraNegocioException {

    private static final String MENSAGEM = "A data de vigência informada conflita com o período de outra tabela tarifária existente.";

    public PeriodoDeVigenciaInformadoConflitaComdeOutraTabelaTarifariaException() {
        super(MENSAGEM, HttpStatus.CONFLICT.value());
    }
}

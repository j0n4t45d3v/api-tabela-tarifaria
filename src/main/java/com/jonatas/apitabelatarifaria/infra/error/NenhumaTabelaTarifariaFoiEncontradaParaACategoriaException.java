package com.jonatas.apitabelatarifaria.infra.error;

import org.springframework.http.HttpStatus;

public class NenhumaTabelaTarifariaFoiEncontradaParaACategoriaException extends ViolacaoDaRegraNegocioException {

    private static final String MENSAGEM = "Nenhuma Tabela Tarifária foi encontrada para a categoria informada.";

    public NenhumaTabelaTarifariaFoiEncontradaParaACategoriaException() {
        super(MENSAGEM, HttpStatus.NOT_FOUND.value());
    }

}

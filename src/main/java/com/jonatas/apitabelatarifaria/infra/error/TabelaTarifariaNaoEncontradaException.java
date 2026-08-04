package com.jonatas.apitabelatarifaria.infra.error;

import org.springframework.http.HttpStatus;

public class TabelaTarifariaNaoEncontradaException extends ViolacaoDaRegraNegocioException{

    private static final String MENSAGEM = "Tabela tarifaria não encontrada";

    public TabelaTarifariaNaoEncontradaException() {
        super(MENSAGEM, HttpStatus.NOT_FOUND.value());
    }

}

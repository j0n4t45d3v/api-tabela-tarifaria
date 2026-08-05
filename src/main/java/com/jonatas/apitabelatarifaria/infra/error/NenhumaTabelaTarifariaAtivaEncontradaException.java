package com.jonatas.apitabelatarifaria.infra.error;

import org.springframework.http.HttpStatus;

public class NenhumaTabelaTarifariaAtivaEncontradaException extends ViolacaoDaRegraNegocioException{

    private static final String MENSAGEM = "Nenhum tabela tarifaria foi encontrada em vigência de hoje.";

    public NenhumaTabelaTarifariaAtivaEncontradaException() {
        super(MENSAGEM, HttpStatus.NOT_FOUND.value());
    }

}

package com.jonatas.apitabelatarifaria.infra.error;

import org.springframework.http.HttpStatus;

public class DataVigenciaInicialEhMaiorQueAFinalException
    extends ViolacaoDaRegraNegocioException{

        private static final String MENSAGEM = "A Data de vigência inicial não pode ser maior que a final";

    public DataVigenciaInicialEhMaiorQueAFinalException() {
        super(MENSAGEM, HttpStatus.UNPROCESSABLE_CONTENT.value());
    }

}

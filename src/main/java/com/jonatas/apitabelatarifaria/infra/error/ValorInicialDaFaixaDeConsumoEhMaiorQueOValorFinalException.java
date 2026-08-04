package com.jonatas.apitabelatarifaria.infra.error;

import org.springframework.http.HttpStatus;

public class ValorInicialDaFaixaDeConsumoEhMaiorQueOValorFinalException
    extends ViolacaoDaRegraNegocioException{
    
        private static final String MENSAGEM = "Valor inicial da faixa de consumo deve ser menor que o valor final.";

    public ValorInicialDaFaixaDeConsumoEhMaiorQueOValorFinalException() {
        super(MENSAGEM, HttpStatus.UNPROCESSABLE_CONTENT.value());
    }

}

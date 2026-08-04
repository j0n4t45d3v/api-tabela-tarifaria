package com.jonatas.apitabelatarifaria.dto;

import java.math.BigDecimal;
import java.util.List;

import com.jonatas.apitabelatarifaria.dvo.DetalhamentoConsumoVO;

public record ValorAPagarResponse(
    String categoria,
    Integer consumoTotal,
    BigDecimal valorTotal,
    List<Detalhamento> detalhamento
) {
    public record Detalhamento(
        Faixa faixa,
        Integer m3Cobrados,
        BigDecimal valorUnitario,
        BigDecimal subtotal
    ) {

        public static Detalhamento of(DetalhamentoConsumoVO consumoVO) {
            return new Detalhamento(
                new Faixa(consumoVO.faixaInicial(), consumoVO.faixaFinal()),
                consumoVO.cobradoPorMetroCubico(),
                consumoVO.valorUnitario(),
                consumoVO.subtotal()
            );
        }

    }

        public record Faixa(Integer inicio, Integer fim) {
        }

}

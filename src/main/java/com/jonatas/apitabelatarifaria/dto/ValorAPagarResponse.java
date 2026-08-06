package com.jonatas.apitabelatarifaria.dto;

import com.jonatas.apitabelatarifaria.dvo.DetalhamentoConsumoVO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

public record ValorAPagarResponse(
    String categoria,
    Integer consumoTotal,
    BigDecimal valorTotal,
    List<Detalhamento> detalhamento
) {

    public ValorAPagarResponse {
        valorTotal = valorTotal.setScale(2, RoundingMode.HALF_UP);
    }

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
                consumoVO.valorUnitario().setScale(2, RoundingMode.HALF_UP),
                consumoVO.subtotal().setScale(2, RoundingMode.HALF_UP)
            );
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Detalhamento that)) return false;
            return Objects.equals(faixa, that.faixa) &&
                   Objects.equals(m3Cobrados, that.m3Cobrados) &&
                   Objects.equals(subtotal, that.subtotal) &&
                   Objects.equals(valorUnitario, that.valorUnitario);
        }

        @Override
        public int hashCode() {
            return Objects.hash(faixa, m3Cobrados, valorUnitario, subtotal);
        }
    }

    public record Faixa(Integer inicio, Integer fim) {
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Faixa faixa)) return false;
            return Objects.equals(fim, faixa.fim) && Objects.equals(inicio, faixa.inicio);
        }

        @Override
        public int hashCode() {
            return Objects.hash(inicio, fim);
        }
    }

}

package com.jonatas.apitabelatarifaria.dvo;

import java.math.BigDecimal;

public record DetalhamentoConsumoVO(
    Integer faixaInicial,
    Integer faixaFinal,
    Integer cobradoPorMetroCubico,
    BigDecimal valorUnitario,
    BigDecimal subtotal
) {
}

package com.jonatas.apitabelatarifaria.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record FaixaConsumoRequest(

    @NotNull(message = "Informe 'faixas[].de' da faixa de consumo.")
    Integer de,

    @NotNull(message = "Informe 'faixas[].ate' da faixa de consumo.")
    Integer ate,

    @NotNull(message = "Informe 'faixas[].valorUnitario' da faixa de consumo.")
    BigDecimal valorUnitario

) {

}

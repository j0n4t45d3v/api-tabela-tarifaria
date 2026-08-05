package com.jonatas.apitabelatarifaria.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.*;

public record FaixaConsumoRequest(

    @NotNull(message = "Informe 'faixas[].de' da faixa de consumo.")
    @Min(value = 0, message = "Informe somente valores positivos e zero")
    @Max(value = 99999, message = "Valor máximo permitido é 99999")
    Integer de,

    @NotNull(message = "Informe 'faixas[].ate' da faixa de consumo.")
    @Positive(message = "Informe somente valores positivos")
    @Max(value = 99999, message = "Valor máximo permitido é 99999")
    Integer ate,

    @NotNull(message = "Informe 'faixas[].valorUnitario' da faixa de consumo.")
    BigDecimal valorUnitario

) {

}

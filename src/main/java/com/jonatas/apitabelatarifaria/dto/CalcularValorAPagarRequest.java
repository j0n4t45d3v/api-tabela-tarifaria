package com.jonatas.apitabelatarifaria.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CalcularValorAPagarRequest(
    @NotBlank(message = "Informa uma categoria")
    String categoria,

    @NotNull(message = "Informe o campo 'consumo'")
    @Min(value = 1, message = "Informe um valor maior que zero")
    Integer consumo
) {
}

package com.jonatas.apitabelatarifaria.dto;

import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record TarifaCategoriaConsumoRequest(

    @NotBlank(message = "Informe a 'tarifas[].categoria' do consumidor dessa tarifa.")
    @Size(max = 40, message = "Categoria não pode ter mais de 40 caracteres")
    String categoria, 

    @Valid
    @NotEmpty(message = "Informe pelo menos uma faixa de consumo em 'tarifas[].faixas'")
    Set<FaixaConsumoRequest> faixas

) {
}

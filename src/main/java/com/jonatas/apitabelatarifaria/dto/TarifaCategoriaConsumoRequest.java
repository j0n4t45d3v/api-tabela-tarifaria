package com.jonatas.apitabelatarifaria.dto;

import java.util.Set;

public record TarifaCategoriaConsumoRequest(String categoria, Set<FaixaConsumoRequest> faixas) {
}

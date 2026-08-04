package com.jonatas.apitabelatarifaria.dto;

import java.time.LocalDate;
import java.util.Set;

public record CriarTabelaTarifariaRequest(
    String nome,
    Vigencia vigente,
    Set<TarifaCategoriaConsumoRequest> tarifas
) {
    record Vigencia(LocalDate de, LocalDate ate) {}

    public LocalDate vigenteDe() {
        return this.vigente.de();
    }

    public LocalDate vigenteAte() {
        return this.vigente.ate();
    }
}

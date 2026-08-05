package com.jonatas.apitabelatarifaria.dto;

import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CriarTabelaTarifariaRequest(

    @NotBlank(message = "Informe o 'nome' para a tabela tarifaria.")
    @Size(max = 40, message = "Nome da tabela tarifaria não pode ter mais de 40 caracteres")
    String nome,

    @Valid
    @NotNull(message = "Informe o periodo de vigência da tabela tarifaria no campo 'vigente'")
    Vigencia vigente,

    @Valid
    @NotEmpty(message = "Informe as tarifas para essa tabela")
    Set<TarifaCategoriaConsumoRequest> tarifas

) {
    public record Vigencia(

        @NotNull(message = "Informe 'vigente.de' para dizer quando irá entrar em rigor a tabela tarifaria.")
        LocalDate de,

        @NotNull(message = "Informe 'vigente.ate' para dizer quando essa tabela irá parar de ser usada.")
        LocalDate ate

    ) {}

    public LocalDate vigenteDe() {
        return this.vigente.de();
    }

    public LocalDate vigenteAte() {
        return this.vigente.ate();
    }
}

package com.jonatas.apitabelatarifaria.entity;

import com.jonatas.apitabelatarifaria.infra.error.DataVigenciaInicialEhMaiorQueAFinalException;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "tabelas_tarifarias")
public class TabelaTarifaria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private LocalDate dataVigenciaInicial;

    private LocalDate dataVigenciaFinal;

    @OneToMany(mappedBy = "tabelaTarifaria")
    private Set<FaixaConsumo> faixasConsumo;

    public TabelaTarifaria() {
        this(null, null, null, null);
    }

    public TabelaTarifaria(Long id, String nome, LocalDate dataVigenciaInicial, LocalDate dataVigenciaFinal) {
        validarPeriodoDeVigencia(dataVigenciaInicial, dataVigenciaFinal);
        this.id = id;
        this.nome = nome;
        this.dataVigenciaInicial = dataVigenciaInicial;
        this.dataVigenciaFinal = dataVigenciaFinal;
    }

    private void validarPeriodoDeVigencia(LocalDate dataInicial, LocalDate dataFinal) {
        if (dataInicial != null && dataFinal != null && dataInicial.isAfter(dataFinal)) {
            throw new DataVigenciaInicialEhMaiorQueAFinalException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getDataVigenciaInicial() {
        return dataVigenciaInicial;
    }

    public LocalDate getDataVigenciaFinal() {
        return dataVigenciaFinal;
    }

    public Set<FaixaConsumo> getFaixasConsumo() {
        return faixasConsumo;
    }
}

package com.jonatas.apitabelatarifaria.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "tabelas_tarifarias")
public class TabelaTarifaria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private LocalDate dataVigenciaInicial;

    private LocalDate dataVigenciaFinal;

    public TabelaTarifaria() {
        this(null, null, null, null);
    }

    public TabelaTarifaria(Long id, String nome, LocalDate dataVigenciaInicial, LocalDate dataVigenciaFinal) {
        this.id = id;
        this.nome = nome;
        this.dataVigenciaInicial = dataVigenciaInicial;
        this.dataVigenciaFinal = dataVigenciaFinal;
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
}

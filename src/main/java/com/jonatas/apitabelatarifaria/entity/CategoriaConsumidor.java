package com.jonatas.apitabelatarifaria.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "categorias_consumidores")
public class CategoriaConsumidor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    protected CategoriaConsumidor() {
        this(null, null);
    }

    public CategoriaConsumidor(String nome) {
        this(null, nome);
    }

    public CategoriaConsumidor(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CategoriaConsumidor categoria)
            return Objects.equals(nome, categoria.nome);
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nome);
    }
}

package com.jonatas.apitabelatarifaria.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "faixas_consumo")
public class FaixaConsumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer de;

    private Integer ate;

    @Column(name = "valor_unitario", precision = 10, scale = 2)
    private BigDecimal valorUnitario;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private CategoriaConsumidor categoriaConsumidor;

    @ManyToOne
    @JoinColumn(name = "id_tabela_tarifaria")
    private TabelaTarifaria tabelaTarifaria;

    public FaixaConsumo(
            Long id,
            Integer de,
            Integer ate,
            BigDecimal valorUnitario,
            CategoriaConsumidor categoriaConsumidor,
            TabelaTarifaria tabelaTarifaria
    ) {
        this.id = id;
        this.de = de;
        this.ate = ate;
        this.valorUnitario = valorUnitario;
        this.categoriaConsumidor = categoriaConsumidor;
        this.tabelaTarifaria = tabelaTarifaria;
    }

    public Long getId() {
        return id;
    }

    public Integer getDe() {
        return de;
    }

    public Integer getAte() {
        return ate;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public CategoriaConsumidor getCategoriaConsumidor() {
        return categoriaConsumidor;
    }

    public TabelaTarifaria getTabelaTarifaria() {
        return tabelaTarifaria;
    }
}


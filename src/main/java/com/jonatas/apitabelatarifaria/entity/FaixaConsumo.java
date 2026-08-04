package com.jonatas.apitabelatarifaria.entity;

import java.math.BigDecimal;
import java.util.Objects;

import com.jonatas.apitabelatarifaria.infra.error.ValorInicialDaFaixaDeConsumoEhMaiorQueOValorFinalException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "faixas_consumo")
public class FaixaConsumo implements Comparable<FaixaConsumo>{

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

    public FaixaConsumo() {}

    private FaixaConsumo(
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

    public static FaixaConsumo of(
            Integer de,
            Integer ate,
            BigDecimal valorUnitario,
            CategoriaConsumidor categoriaConsumidor,
            TabelaTarifaria tabelaTarifaria
    ) {
        if (!ehUmIntervaloValido(de, ate)) {
            throw new ValorInicialDaFaixaDeConsumoEhMaiorQueOValorFinalException();
        }
        return new FaixaConsumo(null, de, ate, valorUnitario, categoriaConsumidor, tabelaTarifaria);
    }

    private static boolean ehUmIntervaloValido(Integer inicio, Integer fim) {
        return inicio < fim;
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

    @Override
    public int hashCode() {
        return Objects.hashCode(id) + 
               Objects.hashCode(de) + 
               Objects.hashCode(ate) + 
               Objects.hashCode(valorUnitario);

    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FaixaConsumo faixaConsumo) {
            return Objects.equals(faixaConsumo.id, this.id) && 
                   Objects.equals(faixaConsumo.de, this.de) &&
                   Objects.equals(faixaConsumo.ate, this.ate) &&
                   Objects.equals(faixaConsumo.valorUnitario, this.valorUnitario) &&
                   Objects.equals(faixaConsumo.categoriaConsumidor, this.categoriaConsumidor) &&
                   Objects.equals(faixaConsumo.tabelaTarifaria, this.tabelaTarifaria);
        } 
        return false;
    }

    @Override
    public int compareTo(FaixaConsumo o) {
        return 0;
    }

}


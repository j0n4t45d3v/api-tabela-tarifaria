package com.jonatas.apitabelatarifaria.entity;

import com.jonatas.apitabelatarifaria.infra.error.ValorInicialDaFaixaDeConsumoEhMaiorQueOValorFinalException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FaixaConsumoTest {

    @Test
    void deveCriarUmaFaixaDeConsumoComSucesso() {
        var tabelaTarifaria = new TabelaTarifaria(null, "TARIFA - 2026", LocalDate.MIN, LocalDate.MAX);
        var categoria = new CategoriaConsumidor("INDUSTRIAL");
        var faixaConsumo = FaixaConsumo.of(0, 10, BigDecimal.ONE, categoria, tabelaTarifaria);

        assertEquals(0, faixaConsumo.getDe());
        assertEquals(10, faixaConsumo.getAte());
        assertEquals(BigDecimal.ONE, faixaConsumo.getValorUnitario());
        assertEquals(categoria, faixaConsumo.getCategoriaConsumidor());
        assertEquals(tabelaTarifaria, faixaConsumo.getTabelaTarifaria());
    }

    @Test
    void naoDeveCriarUmaFaixaDeConsumoQuandoOIntervaloInicialSejaMaiorQueOFinal() {
        var tabelaTarifaria = new TabelaTarifaria(null, "TARIFA - 2026", LocalDate.MIN, LocalDate.MAX);
        var categoria = new CategoriaConsumidor("INDUSTRIAL");
        assertThrows(
                ValorInicialDaFaixaDeConsumoEhMaiorQueOValorFinalException.class,
                () -> FaixaConsumo.of(10,0,  BigDecimal.ONE, categoria, tabelaTarifaria)
        );
    }
}
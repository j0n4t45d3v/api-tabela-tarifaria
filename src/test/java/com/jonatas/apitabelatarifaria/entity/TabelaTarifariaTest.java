package com.jonatas.apitabelatarifaria.entity;

import com.jonatas.apitabelatarifaria.infra.error.DataVigenciaInicialEhMaiorQueAFinalException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TabelaTarifariaTest {

    @Test
    void deveCriarUmaTabelaTarifariaComSucesso() {
        var tabelaTarifaria = new TabelaTarifaria(null, "TARIFA - 2026", LocalDate.MIN, LocalDate.MAX);

        assertEquals("TARIFA - 2026", tabelaTarifaria.getNome());
        assertEquals(LocalDate.MIN, tabelaTarifaria.getDataVigenciaInicial());
        assertEquals(LocalDate.MAX, tabelaTarifaria.getDataVigenciaFinal());
    }

    @Test
    void naoDeveCriarUmaTabelaTarifariaQuandoADataInicialEhMaiorQueADataFinal() {
        assertThrows(
                DataVigenciaInicialEhMaiorQueAFinalException.class,
                () -> new TabelaTarifaria(null, "TARIFA - 2026", LocalDate.MAX, LocalDate.MIN)
        );
    }

}
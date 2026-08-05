package com.jonatas.apitabelatarifaria.service;

import com.jonatas.apitabelatarifaria.entity.TabelaTarifaria;
import com.jonatas.apitabelatarifaria.infra.error.PeriodoDeVigenciaInformadoConflitaComdeOutraTabelaTarifariaException;
import com.jonatas.apitabelatarifaria.infra.error.TabelaTarifariaNaoEncontradaException;
import com.jonatas.apitabelatarifaria.repository.TabelaTarifariaRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class TabelaTarifariaServiceTest {

    @Mock
    private TabelaTarifariaRepository tabelaTarifariaRepository;

    @InjectMocks
    private TabelaTarifariaService tabelaTarifariaService;

    static TabelaTarifaria montarTabelaTarifaria(String nome) {
        return new TabelaTarifaria(null, nome, LocalDate.MIN, LocalDate.MAX);
    }

    @Nested
    class Criar {

        @Test
        void deveCriarUmaTabelaTributarioComSucesso() {
            var novaTabela = montarTabelaTarifaria("TARIFAS - JULHO/2026");

            mockExisteConflitoDePeriodoDeVigencia(false);
            when(tabelaTarifariaRepository.save(novaTabela)).thenReturn(novaTabela);

            var tabelaCriada = tabelaTarifariaService.criar(novaTabela);

            assertNotNull(tabelaCriada);

            verify(tabelaTarifariaRepository, times(1))
                    .save(any(TabelaTarifaria.class));
        }

        @Test
        void deveLançarExcessaoQuandoOPeriodoDeVigenciaConflitarComODeOutraTabelaExistente() {
            var tabelaTarifaria = montarTabelaTarifaria("TARIFAS - JULHO/2026");
            mockExisteConflitoDePeriodoDeVigencia(true);
            assertThrows(
                    PeriodoDeVigenciaInformadoConflitaComdeOutraTabelaTarifariaException.class,
                    () -> tabelaTarifariaService.criar(tabelaTarifaria)
            );
        }

        private void mockExisteConflitoDePeriodoDeVigencia(boolean valor) {
            when(tabelaTarifariaRepository.existsByDataVigenciaInicialLessThanEqualAndDataVigenciaFinalGreaterThanEqual(
                    ArgumentMatchers.any(LocalDate.class),
                    ArgumentMatchers.any(LocalDate.class)
            )).thenReturn(valor);
        }

    }

    @Nested
    class DeletarPeloId {

        @Test
        void deveDeletarUmTabelaTarifarioComSucesso() {
            mockExisteTabelaTarifaria(true);
            assertDoesNotThrow(() -> tabelaTarifariaService.deletarPeloId(1L));
        }

        @Test
        void deveLancarExcessaoQuandoNaoExistirATabelaTarifariaInformada() {
            mockExisteTabelaTarifaria(false);
            assertThrows(
                    TabelaTarifariaNaoEncontradaException.class,
                    () -> tabelaTarifariaService.deletarPeloId(1L)
            );
        }

        private void mockExisteTabelaTarifaria(boolean valorRetornado) {
            when(tabelaTarifariaRepository.existsById(anyLong()))
                    .thenReturn(valorRetornado);
        }

    }

}
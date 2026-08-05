package com.jonatas.apitabelatarifaria.service;

import com.jonatas.apitabelatarifaria.dto.CriarTabelaTarifariaRequest;
import com.jonatas.apitabelatarifaria.dto.FaixaConsumoRequest;
import com.jonatas.apitabelatarifaria.dto.TarifaCategoriaConsumoRequest;
import com.jonatas.apitabelatarifaria.entity.CategoriaConsumidor;
import com.jonatas.apitabelatarifaria.entity.TabelaTarifaria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class CriarTabelaTarifariaFachadaServiceTest {

    @Mock
    private TabelaTarifariaService tabelaTarifariaService;

    @Mock
    private CategoriaConsumidorService categoriaConsumidorService;

    @Mock
    private FaixaConsumoService faixaConsumoService;

    @InjectMocks
    private CriarTabelaTarifariaFachadaService criarTabelaTarifariaFachadaService;

    @Test
    void deveCriarUmTabelaTarifariaCompletaComSucesso() {
        var categoriaConsumidorComercial= new CategoriaConsumidor("COMERCIAL");
        var categoriaConsumidorIndustrial= new CategoriaConsumidor("INDUSTRIAL");

        when(categoriaConsumidorService.buscarPorNome(Mockito.anyString()))
                .thenReturn(Optional.of(categoriaConsumidorComercial))
                .thenReturn(Optional.of(categoriaConsumidorIndustrial));

        when(tabelaTarifariaService.criar(Mockito.any(TabelaTarifaria.class)))
                .thenReturn(mock(TabelaTarifaria.class));

        var request = montarCriarTabelaTarifariaRequest();
        var tabelaTarifaria = criarTabelaTarifariaFachadaService.executar(request);

        assertNotNull(tabelaTarifaria);

        verify(this.tabelaTarifariaService, times(1))
                .criar(any(TabelaTarifaria.class));
        verify(this.categoriaConsumidorService, times(2))
                .buscarPorNome(Mockito.anyString());
        verify(this.categoriaConsumidorService, never())
                .criar(Mockito.anyString());
        verify(this.faixaConsumoService, times(2))
                .criar(Mockito.anySet(), any(CategoriaConsumidor.class), any(TabelaTarifaria.class));
    }

    @Test
    void deveCriarUmTabelaTarifariaCompletaComSucessoECriarUmCategoriaDeConsumidorQuandoNaoExistir() {
        var categoriaConsumidorComercial= new CategoriaConsumidor("COMERCIAL");
        var categoriaConsumidorIndustrial= new CategoriaConsumidor("INDUSTRIAL");

        when(categoriaConsumidorService.buscarPorNome(Mockito.anyString()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.empty());

        when(categoriaConsumidorService.criar(Mockito.anyString()))
                .thenReturn(categoriaConsumidorComercial)
                .thenReturn(categoriaConsumidorIndustrial);

        when(tabelaTarifariaService.criar(Mockito.any(TabelaTarifaria.class)))
                .thenReturn(mock(TabelaTarifaria.class));

        var request = montarCriarTabelaTarifariaRequest();
        var tabelaTarifaria = criarTabelaTarifariaFachadaService.executar(request);

        assertNotNull(tabelaTarifaria);

        verify(this.tabelaTarifariaService, times(1))
                .criar(any(TabelaTarifaria.class));
        verify(this.categoriaConsumidorService, times(2))
                .buscarPorNome(Mockito.anyString());
        verify(this.categoriaConsumidorService, times(2))
                .criar(Mockito.anyString());
        verify(this.faixaConsumoService, times(2))
                .criar(Mockito.anySet(), any(CategoriaConsumidor.class), any(TabelaTarifaria.class));
    }

    CriarTabelaTarifariaRequest montarCriarTabelaTarifariaRequest() {
        var tarifasComerciais = new TarifaCategoriaConsumoRequest(
                "COMERCIAL",
                new HashSet<>(List.of(new FaixaConsumoRequest(0, 99999, BigDecimal.ONE)))
        );
        var tarifasIndustriais = new TarifaCategoriaConsumoRequest(
                "INDUSTRIAL",
                new HashSet<>(List.of(new FaixaConsumoRequest(0, 99999, BigDecimal.ONE)))
        );
        return new CriarTabelaTarifariaRequest(
                "TARIFA - JULHO/2026",
                new CriarTabelaTarifariaRequest.Vigencia(LocalDate.MIN, LocalDate.MAX),
                new HashSet<>(List.of(tarifasComerciais, tarifasIndustriais))
        );
    }


}
package com.jonatas.apitabelatarifaria.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jonatas.apitabelatarifaria.dto.CriarTabelaTarifariaRequest;
import com.jonatas.apitabelatarifaria.dto.TarifaCategoriaConsumoRequest;
import com.jonatas.apitabelatarifaria.entity.TabelaTarifaria;

@Service
public class CriarTabelaTarifariaFachadaService {

    private final TabelaTarifariaService tabelaTarifariaService;
    private final CategoriaConsumidorService categoriaConsumidorService;
    private final FaixaConsumoService faixaConsumoService;

    public CriarTabelaTarifariaFachadaService(
        TabelaTarifariaService tabelaTarifariaService,
        CategoriaConsumidorService categoriaConsumidorService,
        FaixaConsumoService faixaConsumoService
    ) {
        this.tabelaTarifariaService = tabelaTarifariaService;
        this.categoriaConsumidorService = categoriaConsumidorService;
        this.faixaConsumoService = faixaConsumoService;
    }

    @Transactional(rollbackFor = Exception.class)
    public TabelaTarifaria executar(CriarTabelaTarifariaRequest request) {
        var novaTabelaTarifaria = new TabelaTarifaria(null, request.nome(), request.vigenteDe(), request.vigenteAte());
        var tabelaTarifariaCriada = this.tabelaTarifariaService.criar(novaTabelaTarifaria);

        for (TarifaCategoriaConsumoRequest tarifa : request.tarifas()) {
            cadastrarTarifa(tabelaTarifariaCriada, tarifa);
        }

        return tabelaTarifariaCriada;
    }

    public void cadastrarTarifa(
        TabelaTarifaria tabelaTarifaria,
        TarifaCategoriaConsumoRequest tarifa
    ) {
        var categoriaConsumidor = this.categoriaConsumidorService
            .buscarPorNome(tarifa.categoria())
            .orElseGet(() -> this.categoriaConsumidorService.criar(tarifa.categoria()));

        faixaConsumoService.criar(tarifa.faixas(), categoriaConsumidor, tabelaTarifaria);
    }

}

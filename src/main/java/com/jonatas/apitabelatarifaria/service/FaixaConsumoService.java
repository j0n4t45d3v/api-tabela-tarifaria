package com.jonatas.apitabelatarifaria.service;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jonatas.apitabelatarifaria.dto.FaixaConsumoRequest;
import com.jonatas.apitabelatarifaria.entity.CategoriaConsumidor;
import com.jonatas.apitabelatarifaria.entity.FaixaConsumo;
import com.jonatas.apitabelatarifaria.entity.TabelaTarifaria;
import com.jonatas.apitabelatarifaria.repository.FaixaConsumoRepository;

@Service
public class FaixaConsumoService {

    private final FaixaConsumoRepository faixaConsumoRepository;

    public FaixaConsumoService(FaixaConsumoRepository faixaConsumoRepository) {
        this.faixaConsumoRepository = faixaConsumoRepository;
    }

    @Transactional
    public void criar(
        Set<FaixaConsumoRequest> faixasConsumo,
        CategoriaConsumidor categoriaConsumidor,
        TabelaTarifaria tabelaTarifaria
    ) {
        var faixas = faixasConsumo
        .stream()
        .map(f -> new FaixaConsumo(
            null, 
            f.de(),
            f.ate(),
            f.valorUnitario(),
            categoriaConsumidor,
            tabelaTarifaria
        ))
        .toList();
        this.faixaConsumoRepository.saveAll(faixas);
    }

}

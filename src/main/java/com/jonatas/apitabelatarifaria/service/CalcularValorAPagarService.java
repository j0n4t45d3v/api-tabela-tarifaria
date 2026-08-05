package com.jonatas.apitabelatarifaria.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.jonatas.apitabelatarifaria.dto.CalcularValorAPagarRequest;
import com.jonatas.apitabelatarifaria.dto.ValorAPagarResponse;
import com.jonatas.apitabelatarifaria.dvo.DetalhamentoConsumoVO;
import com.jonatas.apitabelatarifaria.infra.error.NenhumaTabelaTarifariaAtivaEncontradaException;
import com.jonatas.apitabelatarifaria.infra.error.NenhumaTabelaTarifariaFoiEncontradaParaACategoriaException;
import com.jonatas.apitabelatarifaria.repository.FaixaConsumoRepository;
import com.jonatas.apitabelatarifaria.repository.TabelaTarifariaRepository;

@Service
public class CalcularValorAPagarService {

    private final TabelaTarifariaRepository tabelaTarifariaRepository;
    private final FaixaConsumoRepository faixaConsumoRepository;

    public CalcularValorAPagarService(
        FaixaConsumoRepository faixaConsumoService,
        TabelaTarifariaRepository tabelaTarifariaRepository
    ) {
        this.tabelaTarifariaRepository = tabelaTarifariaRepository;
        this.faixaConsumoRepository = faixaConsumoService;
    }

    public ValorAPagarResponse executar(CalcularValorAPagarRequest request) {
        if (!existeTabelaTarifariaAtiva()) {
            throw new NenhumaTabelaTarifariaAtivaEncontradaException();  
        }
        var faixasDeConsumo = this.faixaConsumoRepository
            .findFaixaDeConsumoDaCategoriaNaTarifaVigente(
                request.categoria(),
                LocalDate.now(),
                request.consumo()
            );

        if (faixasDeConsumo.isEmpty()) {
            throw new NenhumaTabelaTarifariaFoiEncontradaParaACategoriaException();
        }

        return new ValorAPagarResponse(
            request.categoria(), 
            faixasDeConsumo.stream().mapToInt(DetalhamentoConsumoVO::cobradoPorMetroCubico).sum(), 
            faixasDeConsumo.stream().map(DetalhamentoConsumoVO::subtotal).reduce(BigDecimal::add).orElse(BigDecimal.ZERO),
            faixasDeConsumo.stream().map(ValorAPagarResponse.Detalhamento::of).toList()
        );
    }

    private boolean existeTabelaTarifariaAtiva() {
        LocalDate hoje = LocalDate.now();
        return this.tabelaTarifariaRepository
        .existsByDataVigenciaInicialLessThanEqualAndDataVigenciaFinalGreaterThanEqual(hoje, hoje);
    }

}

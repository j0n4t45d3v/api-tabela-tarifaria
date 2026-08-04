package com.jonatas.apitabelatarifaria.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.jonatas.apitabelatarifaria.dto.CalcularValorAPagarRequest;
import com.jonatas.apitabelatarifaria.dto.ValorAPagarResponse;
import com.jonatas.apitabelatarifaria.dvo.DetalhamentoConsumoVO;
import com.jonatas.apitabelatarifaria.repository.FaixaConsumoRepository;

@Service
public class CalcularValorAPagarService {

    private final FaixaConsumoRepository faixaConsumoRepository;

    public CalcularValorAPagarService(FaixaConsumoRepository faixaConsumoService) {
        this.faixaConsumoRepository = faixaConsumoService;
    }

    public ValorAPagarResponse executar(CalcularValorAPagarRequest request) {
        var faixasDeConsumo = this.faixaConsumoRepository
            .findFaixaDeConsumoDaCategoriaNaTarifaVigente(
                request.categoria(),
                LocalDate.now(),
                request.consumo()
            );

        return new ValorAPagarResponse(
            request.categoria(), 
            faixasDeConsumo.stream().mapToInt(DetalhamentoConsumoVO::cobradoPorMetroCubico).sum(), 
            faixasDeConsumo.stream().map(DetalhamentoConsumoVO::subtotal).reduce(BigDecimal::add).get(), 
            faixasDeConsumo.stream().map(ValorAPagarResponse.Detalhamento::of).toList()
        );
    }

}

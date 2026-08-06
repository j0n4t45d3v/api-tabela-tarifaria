package com.jonatas.apitabelatarifaria.service;

import com.jonatas.apitabelatarifaria.dto.CalcularValorAPagarRequest;
import com.jonatas.apitabelatarifaria.dto.ValorAPagarResponse;
import com.jonatas.apitabelatarifaria.dvo.DetalhamentoConsumoVO;
import com.jonatas.apitabelatarifaria.infra.error.NenhumaTabelaTarifariaAtivaEncontradaException;
import com.jonatas.apitabelatarifaria.infra.error.NenhumaTabelaTarifariaFoiEncontradaParaACategoriaException;
import com.jonatas.apitabelatarifaria.repository.FaixaConsumoRepository;
import com.jonatas.apitabelatarifaria.repository.TabelaTarifariaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
        var categoriaConsumidor = request.categoria();
        var faixasDeConsumo = this.faixaConsumoRepository
            .findFaixaDeConsumoDaCategoriaNaTarifaVigente(
                categoriaConsumidor,
                LocalDate.now(),
                request.consumo()
            );

        if (faixasDeConsumo.isEmpty()) {
            throw new NenhumaTabelaTarifariaFoiEncontradaParaACategoriaException();
        }

        var consumoTotal = calcularConsumoTotal(faixasDeConsumo);
        var valorTotal = calcularValorTotalAPagar(faixasDeConsumo);
        var detalhamentoMapeadoParaDTO = mapearDetalhamentoParaDTO(faixasDeConsumo);
        return new ValorAPagarResponse(categoriaConsumidor, consumoTotal, valorTotal, detalhamentoMapeadoParaDTO);
    }

    private boolean existeTabelaTarifariaAtiva() {
        LocalDate hoje = LocalDate.now();
        return this.tabelaTarifariaRepository
        .existsByDataVigenciaInicialLessThanEqualAndDataVigenciaFinalGreaterThanEqual(hoje, hoje);
    }

    private int calcularConsumoTotal(List<DetalhamentoConsumoVO> detalhamentos) {
        return  detalhamentos
                .stream()
                .mapToInt(DetalhamentoConsumoVO::cobradoPorMetroCubico)
                .sum();
    }

    private BigDecimal calcularValorTotalAPagar(List<DetalhamentoConsumoVO> detalhamentos) {
        return detalhamentos
                .stream()
                .map(DetalhamentoConsumoVO::subtotal)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private List<ValorAPagarResponse.Detalhamento> mapearDetalhamentoParaDTO(List<DetalhamentoConsumoVO> detalhamentos) {
        return detalhamentos
                .stream()
                .map(ValorAPagarResponse.Detalhamento::of)
                .toList();
    }

}

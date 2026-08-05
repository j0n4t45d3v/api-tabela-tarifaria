package com.jonatas.apitabelatarifaria.service;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.jonatas.apitabelatarifaria.infra.error.FaixasDeConsumoNaoContemplaTodosOsCasosDeConsumoException;
import com.jonatas.apitabelatarifaria.infra.error.NaoEhPermitidoQueTenhaSobreposicaoEntreAsFaixasDeConsumoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jonatas.apitabelatarifaria.dto.FaixaConsumoRequest;
import com.jonatas.apitabelatarifaria.entity.CategoriaConsumidor;
import com.jonatas.apitabelatarifaria.entity.FaixaConsumo;
import com.jonatas.apitabelatarifaria.entity.TabelaTarifaria;
import com.jonatas.apitabelatarifaria.infra.error.NaoInformadoFaixaDeAberturaException;
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
        validarIntervalosDasFaixas(faixasConsumo);

        var faixas = faixasConsumo
        .stream()
        .map(f ->
            FaixaConsumo.of(
                f.de(), 
                f.ate(), 
                f.valorUnitario(),
                categoriaConsumidor,
                tabelaTarifaria
            ))
        .toList();
        this.faixaConsumoRepository.saveAll(faixas);
    }

    private void validarIntervalosDasFaixas(Set<FaixaConsumoRequest> faixas) {
        Set<FaixaConsumoRequest> faixasOrdenadas = faixas
                .stream()
                .sorted(Comparator.comparing(FaixaConsumoRequest::de))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        var aberturaDosIntervalos = faixasOrdenadas.stream().mapToInt(FaixaConsumoRequest::de).toArray();
        var fechamentoDosIntervalos = faixasOrdenadas.stream().mapToInt(FaixaConsumoRequest::ate).toArray();

        if (existeSobreposicaoEntreAsFaixasDeConsumo(aberturaDosIntervalos, fechamentoDosIntervalos)) {
            throw new NaoEhPermitidoQueTenhaSobreposicaoEntreAsFaixasDeConsumoException();
        }
        if (!existeFaixaDeAbertura(faixas)) {
            throw new NaoInformadoFaixaDeAberturaException();
        }
        if (!existeFaixaDeFechamento(faixas) || existeLacunasEntreOsIntervalos(aberturaDosIntervalos, fechamentoDosIntervalos)) {
            throw new FaixasDeConsumoNaoContemplaTodosOsCasosDeConsumoException();
        }
    }

    private boolean existeLacunasEntreOsIntervalos(int[] aberturaDeIntervalos, int[] fechamentoDeIntervalos) {
        int cursor = 0;
        int intervalorFechamentoAnterior = -1;
        boolean existeLacunaEntreIntervalos = false;
        while(cursor < aberturaDeIntervalos.length && !existeLacunaEntreIntervalos) {
            int intervaloAbertura = aberturaDeIntervalos[cursor];
            int intervaloFechamento = fechamentoDeIntervalos[cursor];
            existeLacunaEntreIntervalos = (intervaloAbertura - intervalorFechamentoAnterior) > 1;
            intervalorFechamentoAnterior = intervaloFechamento;
            cursor++;
        }
        return existeLacunaEntreIntervalos;
    }

    private boolean existeSobreposicaoEntreAsFaixasDeConsumo(int[] aberturaDeIntervalos, int[] fechamentoDeIntervalos) {
        int cursor = 0;
        int intervalorFechamentoAnterior = -1;
        boolean existeSobreposicaoDeFaixa = false;
        while(cursor < aberturaDeIntervalos.length && !existeSobreposicaoDeFaixa) {
            int intervaloAbertura = aberturaDeIntervalos[cursor];
            int intervaloFechamento = fechamentoDeIntervalos[cursor];
            existeSobreposicaoDeFaixa = !(intervaloAbertura > intervalorFechamentoAnterior);
            intervalorFechamentoAnterior = intervaloFechamento;
            cursor++;
        }
        return existeSobreposicaoDeFaixa;
    }

    private boolean existeFaixaDeAbertura(Set<FaixaConsumoRequest> faixas) {
        return faixas.stream().anyMatch(fr -> fr.de() == 0);
    }

    private boolean existeFaixaDeFechamento(Set<FaixaConsumoRequest> faixas) {
        return faixas.stream().anyMatch(fr -> fr.ate() == 99999);
    }

}

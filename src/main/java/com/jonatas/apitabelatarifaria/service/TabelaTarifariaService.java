package com.jonatas.apitabelatarifaria.service;

import com.jonatas.apitabelatarifaria.entity.TabelaTarifaria;
import com.jonatas.apitabelatarifaria.infra.error.PeriodoDeVigenciaInformadoConflitaComdeOutraTabelaTarifariaException;
import com.jonatas.apitabelatarifaria.infra.error.TabelaTarifariaNaoEncontradaException;
import com.jonatas.apitabelatarifaria.repository.TabelaTarifariaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TabelaTarifariaService {

    private final TabelaTarifariaRepository tabelaTarifariaRepository;

    public TabelaTarifariaService(TabelaTarifariaRepository tabelaTarifariaRepository) {
        this.tabelaTarifariaRepository = tabelaTarifariaRepository;
    }

    public TabelaTarifaria criar(TabelaTarifaria tabelaTarifaria) {
        if (existeTabelaTributarioQueConflitaComOPeriodo(tabelaTarifaria.getDataVigenciaInicial(), tabelaTarifaria.getDataVigenciaFinal())) {
            throw new PeriodoDeVigenciaInformadoConflitaComdeOutraTabelaTarifariaException();
        }
        return this.tabelaTarifariaRepository.save(tabelaTarifaria);
    }

    private boolean existeTabelaTributarioQueConflitaComOPeriodo(LocalDate dataInicial, LocalDate dateFinal) {
        return this.tabelaTarifariaRepository
                .existsByDataVigenciaInicialLessThanEqualAndDataVigenciaFinalGreaterThanEqual(dateFinal, dataInicial);
    }

    public List<TabelaTarifaria> listarTabelasTarifarias() {
        return  this.tabelaTarifariaRepository.findTodasTabelasTarifarias();
    }

    public Optional<TabelaTarifaria> buscarTabelaTarifaria(Long id) {
        return this.tabelaTarifariaRepository.findById(id);
    }

    public void deletarPeloId(Long id) {
        if(!this.tabelaTarifariaRepository.existsById(id)) {
            throw new TabelaTarifariaNaoEncontradaException();
        }
        this.tabelaTarifariaRepository.deleteById(id);
    }

}

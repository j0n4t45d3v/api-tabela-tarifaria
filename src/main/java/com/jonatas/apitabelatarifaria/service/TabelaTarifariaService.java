package com.jonatas.apitabelatarifaria.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jonatas.apitabelatarifaria.entity.TabelaTarifaria;
import com.jonatas.apitabelatarifaria.infra.error.TabelaTarifariaNaoEncontradaException;
import com.jonatas.apitabelatarifaria.repository.TabelaTarifariaRepository;

@Service
public class TabelaTarifariaService {

    private final TabelaTarifariaRepository tabelaTarifariaRepository;

    public TabelaTarifariaService(TabelaTarifariaRepository tabelaTarifariaRepository) {
        this.tabelaTarifariaRepository = tabelaTarifariaRepository;
    }

    public TabelaTarifaria criar(TabelaTarifaria tabelaTarifaria) {
        return this.tabelaTarifariaRepository.save(tabelaTarifaria);
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

package com.jonatas.apitabelatarifaria.service;

import com.jonatas.apitabelatarifaria.entity.TabelaTarifaria;
import com.jonatas.apitabelatarifaria.repository.TabelaTarifariaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

}

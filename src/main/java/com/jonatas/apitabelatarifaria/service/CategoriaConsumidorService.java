package com.jonatas.apitabelatarifaria.service;

import com.jonatas.apitabelatarifaria.entity.CategoriaConsumidor;
import com.jonatas.apitabelatarifaria.repository.CategoriaConsumidorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoriaConsumidorService {

    private final CategoriaConsumidorRepository categoriaConsumidorRepository;


    public CategoriaConsumidorService(CategoriaConsumidorRepository categoriaConsumidorRepository) {
        this.categoriaConsumidorRepository = categoriaConsumidorRepository;
    }

    public CategoriaConsumidor criar(String nome) {
        var novaCategoria = new CategoriaConsumidor(nome);
        return this.categoriaConsumidorRepository.save(novaCategoria);
    }

    public Optional<CategoriaConsumidor> buscarPorNome(String nome){
        return this.categoriaConsumidorRepository.findByNome(nome);
    }

}

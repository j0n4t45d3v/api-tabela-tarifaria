package com.jonatas.apitabelatarifaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jonatas.apitabelatarifaria.entity.FaixaConsumo;

public interface FaixaConsumoRepository extends JpaRepository<FaixaConsumo, Long> {
}

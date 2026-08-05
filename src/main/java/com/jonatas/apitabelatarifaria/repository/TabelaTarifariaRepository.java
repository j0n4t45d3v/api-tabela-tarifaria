package com.jonatas.apitabelatarifaria.repository;

import com.jonatas.apitabelatarifaria.entity.TabelaTarifaria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface TabelaTarifariaRepository extends JpaRepository<TabelaTarifaria, Long> {
    boolean existsByDataVigenciaInicialLessThanEqualAndDataVigenciaFinalLessThanEqual(LocalDate dataInicial, LocalDate dataFinal);
}

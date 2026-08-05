package com.jonatas.apitabelatarifaria.repository;

import com.jonatas.apitabelatarifaria.entity.TabelaTarifaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TabelaTarifariaRepository extends JpaRepository<TabelaTarifaria, Long> {
    boolean existsByDataVigenciaInicialLessThanEqualAndDataVigenciaFinalGreaterThanEqual(LocalDate dataFinal, LocalDate dataInicial);

    @Query("""
        SELECT t
        FROM TabelaTarifaria t
        LEFT JOIN FETCH t.faixasConsumo
        LEFT JOIN FETCH t.faixasConsumo.categoriaConsumidor
    """)
    List<TabelaTarifaria> findTodasTabelasTarifarias();
}

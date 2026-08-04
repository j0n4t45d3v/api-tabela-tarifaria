package com.jonatas.apitabelatarifaria.repository;

import com.jonatas.apitabelatarifaria.entity.TabelaTarifaria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TabelaTarifariaRepository extends JpaRepository<TabelaTarifaria, Long> {
}

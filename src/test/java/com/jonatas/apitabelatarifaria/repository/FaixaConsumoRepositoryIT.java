package com.jonatas.apitabelatarifaria.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.jonatas.apitabelatarifaria.config.ContainersConfig;

import jakarta.transaction.Transactional;

@DataJpaTest
@Transactional
@ActiveProfiles("test")
@Import(ContainersConfig.class)
public class FaixaConsumoRepositoryIT {

    @Autowired
    private FaixaConsumoRepository faixaConsumoRepository;

    void deveTrazerODetalhamentoDoValorAPagarPorConsumo() {

    }

}

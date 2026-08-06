package com.jonatas.apitabelatarifaria.repository;

import com.jonatas.apitabelatarifaria.config.ContainersConfig;
import com.jonatas.apitabelatarifaria.dvo.DetalhamentoConsumoVO;
import com.jonatas.apitabelatarifaria.entity.CategoriaConsumidor;
import com.jonatas.apitabelatarifaria.entity.FaixaConsumo;
import com.jonatas.apitabelatarifaria.entity.TabelaTarifaria;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Transactional
@ActiveProfiles("test")
@Import(ContainersConfig.class)
public class FaixaConsumoRepositoryIT {

    @Autowired
    private TabelaTarifariaRepository tabelaTarifariaRepository;

    @Autowired
    private CategoriaConsumidorRepository categoriaConsumidorRepository;

    @Autowired
    private FaixaConsumoRepository faixaConsumoRepository;

    @BeforeEach
    void setup() {
        var tabelaSalvada = this.tabelaTarifariaRepository.save(new TabelaTarifaria(
                null,
                "TABELA - Julho/2026",
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1)
        ));

        var categoriaConsumidor = this.categoriaConsumidorRepository.save(new CategoriaConsumidor(null, "INDUSTRIAL"));

        this.faixaConsumoRepository.saveAll(List.of(
           FaixaConsumo.of(0, 10, BigDecimal.ONE, categoriaConsumidor, tabelaSalvada),
           FaixaConsumo.of(11, 20, BigDecimal.TWO, categoriaConsumidor, tabelaSalvada),
           FaixaConsumo.of(21, 30, BigDecimal.valueOf(3), categoriaConsumidor, tabelaSalvada),
           FaixaConsumo.of(31, 99999, BigDecimal.valueOf(4), categoriaConsumidor, tabelaSalvada)
        ));
    }

    @Test
    void deveTrazerODetalhamentoDoValorAPagarPorConsumo() {
        var detalhamento = this.faixaConsumoRepository
                .findFaixaDeConsumoDaCategoriaNaTarifaVigente("INDUSTRIAL", LocalDate.now(), 18);

        var detalhamentoEsperado = List.of(
                detalhamentoConsumoVO(0, 10, 10, BigDecimal.ONE, BigDecimal.valueOf(10)),
                detalhamentoConsumoVO(11, 20, 8, BigDecimal.TWO, BigDecimal.valueOf(16))
        );
        assertIterableEquals(detalhamentoEsperado, detalhamento);
    }

    @Test
    void deveCalcularCorretamenteQuandoOConsumoForZero() {
        var detalhamento = this.faixaConsumoRepository
                .findFaixaDeConsumoDaCategoriaNaTarifaVigente("INDUSTRIAL", LocalDate.now(), 0);

        var detalhamentoEsperado = List.of(
                detalhamentoConsumoVO(0, 10, 0, BigDecimal.ONE, BigDecimal.ZERO)
        );
        assertIterableEquals(detalhamentoEsperado, detalhamento);
    }

    @Test
    void deveCalcularCorretamenteQuandoOConsumoBaterNoLimiteExatoDaPrimeiraFaixa() {
        var detalhamento = this.faixaConsumoRepository
                .findFaixaDeConsumoDaCategoriaNaTarifaVigente("INDUSTRIAL", LocalDate.now(), 10);

        var detalhamentoEsperado = List.of(
                detalhamentoConsumoVO(0, 10, 10, BigDecimal.ONE, BigDecimal.valueOf(10.00))
        );
        assertIterableEquals(detalhamentoEsperado, detalhamento);
    }

    @Test
    void deveTransbordarCorretamenteApenasUmaUnidadeParaAProximaFaixa() {
        var detalhamento = this.faixaConsumoRepository
                .findFaixaDeConsumoDaCategoriaNaTarifaVigente("INDUSTRIAL", LocalDate.now(), 11);

        var detalhamentoEsperado = List.of(
                detalhamentoConsumoVO(0, 10, 10, BigDecimal.ONE, BigDecimal.valueOf(10)),
                detalhamentoConsumoVO(11, 20, 1, BigDecimal.TWO, BigDecimal.valueOf(2))
        );
        assertIterableEquals(detalhamentoEsperado, detalhamento);
    }

    @Test
    void deveCalcularProgressaoPercorrendoTodasAsFaixasAteAUltima() {
        var detalhamento = this.faixaConsumoRepository
                .findFaixaDeConsumoDaCategoriaNaTarifaVigente("INDUSTRIAL", LocalDate.now(), 35);

        var detalhamentoEsperado = List.of(
                detalhamentoConsumoVO(0, 10, 10, BigDecimal.ONE, BigDecimal.valueOf(10)),
                detalhamentoConsumoVO(11, 20, 10, BigDecimal.TWO, BigDecimal.valueOf(20)),
                detalhamentoConsumoVO(21, 30, 10, BigDecimal.valueOf(3), BigDecimal.valueOf(30)),
                detalhamentoConsumoVO(31, 99999, 5, BigDecimal.valueOf(4), BigDecimal.valueOf(20))
        );
        assertIterableEquals(detalhamentoEsperado, detalhamento);
    }

    @Test
    void naoDeveRetornarDetalhamentoSeDataDoConsumoForForaDaVigenciaDaTarifa() {
        LocalDate dataAnteriorAVigencia = LocalDate.now().minusDays(5);

        var detalhamento = this.faixaConsumoRepository
                .findFaixaDeConsumoDaCategoriaNaTarifaVigente("INDUSTRIAL", dataAnteriorAVigencia, 15);

        assertTrue(detalhamento.isEmpty(), "Não deveria calcular consumo para tarifas fora da vigência");
    }

    @Test
    void naoDeveRetornarDetalhamentoQuandoNaoExistirTabelaTarifariaParaACategoria() {
        LocalDate dataAnteriorAVigencia = LocalDate.now();

        var detalhamento = this.faixaConsumoRepository
                .findFaixaDeConsumoDaCategoriaNaTarifaVigente("COMERCIAL", dataAnteriorAVigencia, 15);

        assertTrue(detalhamento.isEmpty(), "Não deveria calcular consumo para categorias não cadastradas na tabela tarifária vigênte");
    }

    private DetalhamentoConsumoVO detalhamentoConsumoVO(int faixaInicio, int faixaFim, int cobradoPorMetroCubico, BigDecimal valorUnitario, BigDecimal subtotal) {
        return new DetalhamentoConsumoVO(
                faixaInicio,
                faixaFim,
                cobradoPorMetroCubico,
                valorUnitario.setScale(2, RoundingMode.HALF_UP),
                subtotal.setScale(2, RoundingMode.HALF_UP)
        );
    }
}

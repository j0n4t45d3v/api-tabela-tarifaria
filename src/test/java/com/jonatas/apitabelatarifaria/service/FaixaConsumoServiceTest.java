package com.jonatas.apitabelatarifaria.service;

import com.jonatas.apitabelatarifaria.dto.FaixaConsumoRequest;
import com.jonatas.apitabelatarifaria.entity.CategoriaConsumidor;
import com.jonatas.apitabelatarifaria.entity.TabelaTarifaria;
import com.jonatas.apitabelatarifaria.infra.error.FaixasDeConsumoNaoContemplaTodosOsCasosDeConsumoException;
import com.jonatas.apitabelatarifaria.infra.error.NaoEhPermitidoQueTenhaSobreposicaoEntreAsFaixasDeConsumoException;
import com.jonatas.apitabelatarifaria.infra.error.NaoInformadoFaixaDeAberturaException;
import com.jonatas.apitabelatarifaria.infra.error.ValorInicialDaFaixaDeConsumoEhMaiorQueOValorFinalException;
import com.jonatas.apitabelatarifaria.repository.FaixaConsumoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith({MockitoExtension.class})
class FaixaConsumoServiceTest {

    @Mock
    private FaixaConsumoRepository faixaConsumoRepository;

    @InjectMocks
    private FaixaConsumoService faixaConsumoService;

    private static final TabelaTarifaria TABELA_TARIFARIA = new TabelaTarifaria(null, "TARIFA - 2026", LocalDate.MIN, LocalDate.MAX);
    private static final CategoriaConsumidor CATEGORIA_CONSUMIDOR = new CategoriaConsumidor("INDUSTRIAL");

    @Test
    void deveCriarVariasFaixasComSucesso() {
        Set<FaixaConsumoRequest> faixas = new HashSet<>(List.of(
                new FaixaConsumoRequest(0, 10, BigDecimal.ONE),
                new FaixaConsumoRequest(11, 20, BigDecimal.ONE),
                new FaixaConsumoRequest(21, 99999, BigDecimal.ONE)
        ));

        faixaConsumoService.criar(faixas, CATEGORIA_CONSUMIDOR, TABELA_TARIFARIA);

        Mockito.verify(this.faixaConsumoRepository, Mockito.times(1))
                .saveAll(Mockito.anyCollection());

    }

    @ParameterizedTest
    @MethodSource("faixasComSobreposicaoDeIntervalos")
    void deveLancarExcecaoQuandoHouverSobreposicaoEntreAsFaixas(Set<FaixaConsumoRequest> faixas) {
        assertThrows(
                NaoEhPermitidoQueTenhaSobreposicaoEntreAsFaixasDeConsumoException.class,
                () -> faixaConsumoService.criar(faixas, CATEGORIA_CONSUMIDOR, TABELA_TARIFARIA)
        );
    }

    static Stream<Arguments> faixasComSobreposicaoDeIntervalos() {
        return Stream.of(
                Arguments.of(new HashSet<>(
                        List.of(
                                new FaixaConsumoRequest(0, 10, BigDecimal.ONE),
                                new FaixaConsumoRequest(5, 99999, BigDecimal.ONE)
                        )
                )),
                Arguments.of(new HashSet<>(
                        List.of(
                                new FaixaConsumoRequest(0, 10, BigDecimal.ONE),
                                new FaixaConsumoRequest(11, 20, BigDecimal.ONE),
                                new FaixaConsumoRequest(10, 30, BigDecimal.ONE),
                                new FaixaConsumoRequest(21, 99999, BigDecimal.ONE)
                        )
                )),
                Arguments.of(new HashSet<>(
                        List.of(
                                new FaixaConsumoRequest(0, 99999, BigDecimal.ONE),
                                new FaixaConsumoRequest(10, 50, BigDecimal.ONE)
                        )
                )),

                // CASO 2: Sobreposição exata de bordas/limites
                Arguments.of(new HashSet<>(
                        List.of(
                                new FaixaConsumoRequest(0, 50, BigDecimal.ONE),
                                new FaixaConsumoRequest(50, 99999, BigDecimal.ONE)
                        )
                )),

                // CASO 3: Múltiplas faixas com uma "intruza" que quebra duas ao mesmo tempo
                Arguments.of(new HashSet<>(
                        List.of(
                                new FaixaConsumoRequest(0, 15, BigDecimal.ONE),
                                new FaixaConsumoRequest(16, 30, BigDecimal.ONE),
                                new FaixaConsumoRequest(12, 25, BigDecimal.ONE),
                                new FaixaConsumoRequest(31, 99999, BigDecimal.ONE)
                        )
                )),

                // CASO 4: Duas faixas idênticas no início da escala
                Arguments.of(new HashSet<>(
                        List.of(
                                new FaixaConsumoRequest(0, 20, BigDecimal.ONE),
                                new FaixaConsumoRequest(0, 20, BigDecimal.TEN),
                                new FaixaConsumoRequest(21, 99999, BigDecimal.ONE)
                        )
                )),

                // CASO 5: Três faixas idênticas em cadeia se sobrepondo gradualmente
                Arguments.of(new HashSet<>(
                        List.of(
                                new FaixaConsumoRequest(0, 50, BigDecimal.ONE),
                                new FaixaConsumoRequest(40, 80, BigDecimal.ONE),
                                new FaixaConsumoRequest(70, 99999, BigDecimal.ONE)
                        )
                )),

                // CASO 6: Sobreposição dupla no limite final da escala
                Arguments.of(new HashSet<>(
                        List.of(
                                new FaixaConsumoRequest(0, 100, BigDecimal.ONE),
                                new FaixaConsumoRequest(90, 99999, BigDecimal.ONE),
                                new FaixaConsumoRequest(95, 99999, BigDecimal.TEN)
                        )
                ))
        );
    }

    @Test
    void deveLancarExcecaoQuandoNaoExistirUmaFaixaComecandoPorZero() {
        Set<FaixaConsumoRequest> faixas = new HashSet<>();
        faixas.add(new FaixaConsumoRequest(11, 20, BigDecimal.ONE));
        faixas.add(new FaixaConsumoRequest(21, 30, BigDecimal.ONE));
        faixas.add(new FaixaConsumoRequest(31, 99999, BigDecimal.ONE));

        assertThrows(
                NaoInformadoFaixaDeAberturaException.class,
                () -> faixaConsumoService.criar(faixas, CATEGORIA_CONSUMIDOR, TABELA_TARIFARIA)
        );
    }

    @ParameterizedTest
    @MethodSource("faixasComLacunaEntreOsIntervalos")
    void deveLancarExcecaoQuandoAsFaixasInformadaNaoContemplaTodosOsCasosDeConsumo(Set<FaixaConsumoRequest> faixas) {
        assertThrows(
                FaixasDeConsumoNaoContemplaTodosOsCasosDeConsumoException.class,
                () -> faixaConsumoService.criar(faixas, CATEGORIA_CONSUMIDOR, TABELA_TARIFARIA)
        );
    }

    static Stream<Arguments> faixasComLacunaEntreOsIntervalos() {
        return Stream.of(
                Arguments.of(new HashSet<>(List.of(
                    new FaixaConsumoRequest(0, 10, BigDecimal.ONE),
                    new FaixaConsumoRequest(11, 20, BigDecimal.ONE),
                    new FaixaConsumoRequest(21, 30, BigDecimal.ONE))
                )),
                Arguments.of(new HashSet<>(List.of(
                    new FaixaConsumoRequest(0, 10, BigDecimal.ONE),
                    new FaixaConsumoRequest(12, 20, BigDecimal.ONE),
                    new FaixaConsumoRequest(21, 99999, BigDecimal.ONE))
                )),
                Arguments.of(new HashSet<>(List.of(new FaixaConsumoRequest(0, 10, BigDecimal.ONE)))),
                Arguments.of(new HashSet<>(List.of(
                    new FaixaConsumoRequest(0, 10, BigDecimal.ONE))
                )),
                // CASO 1: Lacuna de apenas 1 número isolado (O clássico erro de "+2" em vez de "+1")
                // Detalhe: Falta exatamente o número 11 na sequência.
                Arguments.of(new HashSet<>(List.of(
                        new FaixaConsumoRequest(0, 10, BigDecimal.ONE),
                        new FaixaConsumoRequest(12, 50, BigDecimal.ONE), // Pulou o 11
                        new FaixaConsumoRequest(51, 99999, BigDecimal.ONE)
                ))),

                // CASO 2: Grande buraco no meio do caminho
                // Deixa um abismo sem cobertura entre o consumo 20 e o 500.
                Arguments.of(new HashSet<>(List.of(
                        new FaixaConsumoRequest(0, 20, BigDecimal.ONE),
                        new FaixaConsumoRequest(500, 99999, BigDecimal.ONE) // Lacuna gigante de 21 a 499
                ))),

                // CASO 3: Lacuna dupla (Múltiplos buracos na mesma coleção)
                // Avalia se o algoritmo para no primeiro erro ou consegue mapear que existem várias falhas.
                Arguments.of(new HashSet<>(List.of(
                        new FaixaConsumoRequest(0, 10, BigDecimal.ONE),
                        // Buraco de 11 a 14
                        new FaixaConsumoRequest(15, 30, BigDecimal.ONE),
                        // Buraco de 31 a 39
                        new FaixaConsumoRequest(40, 99999, BigDecimal.ONE)
                ))),

                // CASO 4: Lacuna causada por sub-faixa deslocada
                // Como o Set não garante ordem, as faixas entram misturadas.
                // O intervalo 16 a 20 fica completamente sem cobertura aqui.
                Arguments.of(new HashSet<>(List.of(
                        new FaixaConsumoRequest(0, 15, BigDecimal.ONE),
                        new FaixaConsumoRequest(21, 50, BigDecimal.ONE),
                        new FaixaConsumoRequest(51, 99999, BigDecimal.ONE)
                ))),

                // CASO 5: Borda decimal / Fracionada (Caso seu sistema use inteiros, mas vale o teste)
                // Se o sistema aceitar inputs inesperados, uma lacuna "invisível" pode aparecer se faltar o próximo inteiro.
                Arguments.of(new HashSet<>(List.of(
                        new FaixaConsumoRequest(0, 100, BigDecimal.ONE),
                        new FaixaConsumoRequest(102, 99999, BigDecimal.ONE) // O número 101 ficou no limbo
                )))
        );
    }

}
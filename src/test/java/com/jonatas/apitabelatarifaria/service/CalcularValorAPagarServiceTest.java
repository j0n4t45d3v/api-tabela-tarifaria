package com.jonatas.apitabelatarifaria.service;

import com.jonatas.apitabelatarifaria.dto.CalcularValorAPagarRequest;
import com.jonatas.apitabelatarifaria.dto.ValorAPagarResponse.Detalhamento;
import com.jonatas.apitabelatarifaria.dto.ValorAPagarResponse.Faixa;
import com.jonatas.apitabelatarifaria.dvo.DetalhamentoConsumoVO;
import com.jonatas.apitabelatarifaria.infra.error.NenhumaTabelaTarifariaAtivaEncontradaException;
import com.jonatas.apitabelatarifaria.infra.error.NenhumaTabelaTarifariaFoiEncontradaParaACategoriaException;
import com.jonatas.apitabelatarifaria.repository.FaixaConsumoRepository;
import com.jonatas.apitabelatarifaria.repository.TabelaTarifariaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class CalcularValorAPagarServiceTest {

    @Mock
    private TabelaTarifariaRepository tabelaTarifariaRepository;

    @Mock
    private FaixaConsumoRepository faixaConsumoRepository;

    @InjectMocks
    private CalcularValorAPagarService calcularValorAPagarService;

    @Test
    void deveCacularCorretamenteOValorAPagarSobreOConsumoBaseadoNaTabelaDaCategoria() {
        var consumo = 18;
        var categoria = "INDUSTRIAL";

        mockExisteTabelaTarifariaEmAtividadeNoDia(true);
        mockDetalhamentoDeConsumoPorFaixaCategoria(categoria, consumo, List.of(
                detalhamentoConsumoVO(0, 10, 10, BigDecimal.ONE, BigDecimal.TEN),
                detalhamentoConsumoVO(11, 20, 8, BigDecimal.TWO, BigDecimal.valueOf(16))
        ));

        var request = new CalcularValorAPagarRequest(categoria, consumo);
        var resultado = calcularValorAPagarService.executar(request);

        assertEquals(consumo, resultado.consumoTotal());
        assertEquals(categoria, resultado.categoria());
        assertEquals(BigDecimal.valueOf(26).setScale(2, RoundingMode.HALF_UP), resultado.valorTotal());
        var detalhamentoEsperado = List.of(
                faixaConsumoDetalhamento(0, 10, 10, BigDecimal.ONE, BigDecimal.TEN),
                faixaConsumoDetalhamento(11, 20, 8, BigDecimal.TWO, BigDecimal.valueOf(16))
        );
        assertIterableEquals(detalhamentoEsperado, resultado.detalhamento());
    }

    @Test
    void deveLancarExcessaoQuandoNaoExistirUmaTabelaTarifariaEmAtividadeNoDia() {
        var consumo = 18;
        var categoria = "INDUSTRIAL";

        mockExisteTabelaTarifariaEmAtividadeNoDia(false);

        var request = new CalcularValorAPagarRequest(categoria, consumo);
        assertThrows(
                NenhumaTabelaTarifariaAtivaEncontradaException.class,
                () -> calcularValorAPagarService.executar(request)
        );
    }

    @Test
    void deveLancarExcessaoQuandoNaoExistirUmaTabelaTarifariaParaACategoriaDeConsumidor() {
        var consumo = 18;
        var categoria = "INDUSTRIAL";

        mockExisteTabelaTarifariaEmAtividadeNoDia(true);
        mockDetalhamentoDeConsumoPorFaixaCategoria(categoria, consumo, List.of());

        var request = new CalcularValorAPagarRequest(categoria, consumo);
        assertThrows(
                NenhumaTabelaTarifariaFoiEncontradaParaACategoriaException.class,
                () -> calcularValorAPagarService.executar(request)
        );
    }

    private void mockExisteTabelaTarifariaEmAtividadeNoDia(boolean valor) {
        when(tabelaTarifariaRepository
                .existsByDataVigenciaInicialLessThanEqualAndDataVigenciaFinalGreaterThanEqual(any(LocalDate.class), any(LocalDate.class))
        ).thenReturn(valor);
    }

    private void mockDetalhamentoDeConsumoPorFaixaCategoria(
            String categoria,
            int consumo,
            List<DetalhamentoConsumoVO> detalhamentos
    ) {
        when(faixaConsumoRepository
                .findFaixaDeConsumoDaCategoriaNaTarifaVigente(eq(categoria), any(LocalDate.class), eq(consumo))
        ).thenReturn(detalhamentos);
    }

    private Detalhamento faixaConsumoDetalhamento(int faixaInicio, int faixaFim, int cobradoPorMetroCubico, BigDecimal valorUnitario, BigDecimal subtotal) {
        return new Detalhamento(
                new Faixa(faixaInicio, faixaFim),
                cobradoPorMetroCubico,
                valorUnitario.setScale(2, RoundingMode.HALF_UP),
                subtotal.setScale(2, RoundingMode.HALF_UP)
        );
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
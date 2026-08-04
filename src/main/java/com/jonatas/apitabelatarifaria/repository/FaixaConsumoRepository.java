package com.jonatas.apitabelatarifaria.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import com.jonatas.apitabelatarifaria.dvo.DetalhamentoConsumoVO;
import com.jonatas.apitabelatarifaria.entity.FaixaConsumo;

public interface FaixaConsumoRepository extends JpaRepository<FaixaConsumo, Long> {

    @NativeQuery(
        sqlResultSetMapping = "DetalhamentoConsumoMapping",
        value = """
            SELECT
                faixa_inicial,
                faixa_final, 
                valor_unitario,
                cobrado_por_metro_cubico,
                valor_unitario * cobrado_por_metro_cubico AS subtotal
            FROM (
                SELECT 
                    fc.de AS faixa_inicial, 
                    fc.ate AS faixa_final,
                    fc.valor_unitario, 
                    CASE 
                        WHEN LEAD(fc.id, 1) OVER (ORDER BY fc.de) IS NOT NULL THEN fc.ate
                        ELSE (?3 - fc.de) + 1
                    END AS cobrado_por_metro_cubico
                FROM
                    tabelas_tarifarias tt 
                INNER JOIN
                    faixas_consumo fc
                    ON fc.id_tabela_tarifaria = tt.id
                INNER JOIN
                    categorias_consumidores cc
                    ON cc.id = fc.id_categoria
                WHERE tt.data_vigencia_inicial <= ?2
                    AND tt.data_vigencia_final >= ?2
                    AND cc.nome = ?1
                    AND (fc.de <= ?3 OR fc.ate <= ?3)
            )
            """
        )
    List<DetalhamentoConsumoVO> findFaixaDeConsumoDaCategoriaNaTarifaVigente(
        String categoria,
        LocalDate now,
        Integer consumo
    );

}


ALTER TABLE faixas_consumo
    DROP CONSTRAINT fk_faixa_consumo_categoria;

ALTER TABLE faixas_consumo
    DROP CONSTRAINT fk_faixa_consumo_tarifa;

ALTER TABLE faixas_consumo
    ADD CONSTRAINT fk_faixa_consumo_categoria
        FOREIGN key (id_categoria)
        REFERENCES categorias_consumidores(id)
        ON DELETE CASCADE;

ALTER TABLE faixas_consumo
    ADD CONSTRAINT fk_faixa_consumo_tarifa
        FOREIGN KEY (id_tabela_tarifaria)
        REFERENCES tabelas_tarifarias(id)
        ON DELETE CASCADE;
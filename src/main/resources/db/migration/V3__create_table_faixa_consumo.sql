CREATE TABLE faixas_consumo
(
    id BIGSERIAL NOT NULL,
    id_categoria BIGINT NOT NULL,
    id_tabela_tarifaria BIGINT NOT NULL,
    de INTEGER,
    ate INTEGER,
    valor_unitario NUMERIC(10,2) NOT NULL
);

ALTER TABLE faixas_consumo
    ADD CONSTRAINT pk_faixa_consumo primary key (id);

ALTER TABLE faixas_consumo
    ADD CONSTRAINT fk_faixa_consumo_categoria foreign key (id_categoria) references categorias_consumidores(id);

ALTER TABLE faixas_consumo
    ADD CONSTRAINT fk_faixa_consumo_tarifa foreign key (id_tabela_tarifaria) references tabelas_tarifarias(id);

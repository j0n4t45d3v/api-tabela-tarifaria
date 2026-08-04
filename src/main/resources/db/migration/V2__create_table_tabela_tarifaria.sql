CREATE TABLE tabelas_tarifarias
(
    id BIGSERIAL NOT NULL,
    nome VARCHAR(40) NOT NULL,
    data_vigencia_inicial DATE NOT NULL,
    data_vigencia_final DATE NOT NULL
);

ALTER TABLE tabelas_tarifarias
    ADD CONSTRAINT pk_tabela_tarifaria primary key (id);

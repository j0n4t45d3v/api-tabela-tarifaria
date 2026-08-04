CREATE TABLE categorias_consumidores
(
    id BIGSERIAL NOT NULL,
    nome VARCHAR(40) NOT NULL
);

ALTER TABLE categorias_consumidores
    ADD CONSTRAINT pk_categoria_consumidor primary key (id);


CREATE TABLE lote (
    type character varying(31) NOT NULL,
    id integer NOT NULL,
    descricao character varying(255),
    lanceatual numeric(19,2),
    nome character varying(255),
    valorminimo numeric(19,2),
    area real,
    numerobanheiros integer,
    numeroquartos integer,
    tipo integer,
    comprador_id integer,
    vendedor_id integer NOT NULL
);

CREATE TABLE usuario (
    type character varying(31) NOT NULL,
    id integer NOT NULL,
    nome character varying(255)
);


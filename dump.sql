--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.2
-- Dumped by pg_dump version 9.5.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: lote; Type: TABLE; Schema: public; Owner: leilao
--

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


ALTER TABLE lote OWNER TO leilao;

--
-- Name: lote_id_seq; Type: SEQUENCE; Schema: public; Owner: leilao
--

CREATE SEQUENCE lote_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE lote_id_seq OWNER TO leilao;

--
-- Name: lote_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: leilao
--

ALTER SEQUENCE lote_id_seq OWNED BY lote.id;


--
-- Name: usuario; Type: TABLE; Schema: public; Owner: leilao
--

CREATE TABLE usuario (
    type character varying(31) NOT NULL,
    id integer NOT NULL,
    nome character varying(255)
);


ALTER TABLE usuario OWNER TO leilao;

--
-- Name: usuario_id_seq; Type: SEQUENCE; Schema: public; Owner: leilao
--

CREATE SEQUENCE usuario_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE usuario_id_seq OWNER TO leilao;

--
-- Name: usuario_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: leilao
--

ALTER SEQUENCE usuario_id_seq OWNED BY usuario.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: leilao
--

ALTER TABLE ONLY lote ALTER COLUMN id SET DEFAULT nextval('lote_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: leilao
--

ALTER TABLE ONLY usuario ALTER COLUMN id SET DEFAULT nextval('usuario_id_seq'::regclass);


--
-- Name: lote_pkey; Type: CONSTRAINT; Schema: public; Owner: leilao
--

ALTER TABLE ONLY lote
    ADD CONSTRAINT lote_pkey PRIMARY KEY (id);


--
-- Name: usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: leilao
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);


--
-- Name: fk_i73a66hy7a0a4cvr85i4q8vtc; Type: FK CONSTRAINT; Schema: public; Owner: leilao
--

ALTER TABLE ONLY lote
    ADD CONSTRAINT fk_i73a66hy7a0a4cvr85i4q8vtc FOREIGN KEY (comprador_id) REFERENCES usuario(id);


--
-- Name: fk_qeeap1sjfhq2dov6s55merxv0; Type: FK CONSTRAINT; Schema: public; Owner: leilao
--

ALTER TABLE ONLY lote
    ADD CONSTRAINT fk_qeeap1sjfhq2dov6s55merxv0 FOREIGN KEY (vendedor_id) REFERENCES usuario(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leilao.entidades;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author igor
 */
@Entity
@DiscriminatorColumn(name = "type")
@DiscriminatorValue("0")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user_seq")
    @SequenceGenerator(name = "user_seq")
    private Integer id;

    @Column
    private String nome;

    @OneToMany(mappedBy = "comprador")
    private List<Lote> compras;

    @OneToMany(mappedBy = "vendedor")
    private List<Lote> vendas;
    
    public Usuario(){
        this.nome="";
        this.compras= new ArrayList<>();
        this.vendas= new ArrayList<>();
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Lote> getCompras() {
        return compras;
    }

    public void setCompras(List<Lote> compras) {
        this.compras = compras;
    }

    public void addCompra(Lote compra) {
        this.compras.add(compra);
    }

    public List<Lote> getVendas() {
        return vendas;
    }

    public void setVendas(List<Lote> vendas) {
        this.vendas = vendas;
    }

    public void addVenda(Lote venda) {
        this.vendas.add(venda);
    }

}

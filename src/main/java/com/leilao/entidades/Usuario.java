/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leilao.entidades;

import java.math.BigDecimal;
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

    @Column(unique=true)
    private String nome;
    
    @Column
    private String apelido;

    @Column
    private String senha;
    
    @Column
    private BigDecimal saldo;

    @OneToMany(mappedBy = "comprador", fetch=FetchType.EAGER)
    private List<Lote> compras;

    @OneToMany(mappedBy = "vendedor", fetch=FetchType.EAGER)
    private List<Lote> vendas;
    
    public Usuario(){
        this.nome="";
        this.compras= new ArrayList<>();
        this.vendas= new ArrayList<>();
        this.saldo = BigDecimal.ZERO;
    }
    
    public Usuario(Usuario u){
        id = u.id;
        nome = u.nome;
        senha = u.senha;
        compras = u.compras;
        vendas = u.vendas;
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

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
    
    public void addSaldo(BigDecimal valor) {
        this.saldo = this.saldo.add(valor);
    }
    
    public void subtractSaldo(BigDecimal valor) throws Exception {
        if(this.saldo.subtract(valor).compareTo(BigDecimal.ZERO) >= 0) {
            this.saldo = this.saldo.subtract(valor);
        } else {
            throw new Exception("Saldo não pode ser negativo");
        }
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

    @Override
    public String toString() {
        return getNome();
    }

}

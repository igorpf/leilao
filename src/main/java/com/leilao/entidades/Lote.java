/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leilao.entidades;

import java.math.BigDecimal;
import java.util.Calendar;
import javax.persistence.*;

/**
 *
 * @author igor
 */
@Entity
@DiscriminatorColumn(name = "type")
@DiscriminatorValue("0")
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "lote_seq")
    @SequenceGenerator(name = "lote_seq")
    private Integer id;

    private Calendar dataCriacao;

    private Calendar dataFinal;

    private BigDecimal valorMinimo;

    private BigDecimal lanceAtual;

    private String nome;

    private String descricao;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Usuario vendedor;
    
    @ManyToOne
    private Usuario comprador;

    private Boolean aprovado;
    
    private Boolean finalizado;
    
    private Boolean vendido; 
    
    public Lote(){
        this.valorMinimo=BigDecimal.ZERO;
        this.lanceAtual=BigDecimal.ZERO;
        this.aprovado = false;
        this.finalizado = false;
        this.vendido = false;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Calendar getDataCriacao() {
        return dataCriacao;
    }

    public Calendar getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Calendar dataFinal) {
        this.dataFinal = dataFinal;
    }

    public BigDecimal getValorMinimo() {
        return valorMinimo;
    }

    public void setValorMinimo(BigDecimal valorMinimo) throws Exception {
        if (new BigDecimal("0").compareTo(valorMinimo) <= 0) {
            this.valorMinimo = valorMinimo;
        } else {
            throw new Exception("O valor não pode ser negativo");
        }
    }

    public BigDecimal getLanceAtual() {
        return lanceAtual;
    }

    public void setLanceAtual(BigDecimal lanceAtual) throws Exception {
        if (this.lanceAtual.compareTo(lanceAtual) >= 0) {
            throw new Exception("O novo lance deve ser maior do que o lance atual");
        } else if ((this.lanceAtual.add(this.valorMinimo)).compareTo(lanceAtual) > 0) {
            throw new Exception("O novo lance deve ser maior ou igual ao valor mínimo");
        }
        this.lanceAtual = lanceAtual;

        if(this.valorMinimo.compareTo(BigDecimal.ZERO) == 0) {
            this.valorMinimo = this.lanceAtual.multiply(new BigDecimal(0.05));
        }
    }
    
    public BigDecimal getLanceMinimo() {
        return lanceAtual.add(valorMinimo);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Usuario getVendedor() {
        return vendedor;
    }

    public void setVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
    }

    public Usuario getComprador() {
        return comprador;
    }

    public void setComprador(Usuario comprador) {
        this.comprador = comprador;
    }
    
    public Boolean isAprovado(){
        return aprovado;
    }
    public void setAprovado(Boolean aprovado){
        this.aprovado = aprovado;
    }

    public Boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(Boolean finalizado) {
        this.finalizado = finalizado;
    }

    public Boolean isVendido() {
        return vendido;
    }

    public void setVendido(Boolean vendido) {
        this.vendido = vendido;
    }

    @PrePersist
    protected void onCreate() {
        dataCriacao = Calendar.getInstance();
        dataFinal = Calendar.getInstance();
        dataFinal.add(Calendar.DATE, 7); // Leilão tem duração de uma semana
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Lote) {
            Lote toCompare = (Lote) o;
            return this.id.equals(toCompare.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

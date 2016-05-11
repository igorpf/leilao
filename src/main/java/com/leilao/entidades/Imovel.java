/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leilao.entidades;

import javax.persistence.*;

/**
 *
 * @author igor
 */
@Entity
@DiscriminatorValue("1")
public class Imovel extends Lote{
    
    private float area;
    
    private Tipo tipo;
    
    private int numeroQuartos;
    
    private int numeroBanheiros;
    
    public Imovel(){
        this.area=0.0f;
        this.tipo=Tipo.Residencial;
        this.numeroBanheiros=0;
        this.numeroQuartos=0;
    }
    
    public enum Tipo{
        Residencial, Comercial
    }

    public float getArea() {
        return area;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public int getNumeroQuartos() throws Exception{
        if(this.getTipo().equals(Tipo.Residencial))
                return numeroQuartos;
        else
            throw new UnsupportedOperationException("Disponível apenas em imóveis residenciais");
    }

    public void setNumeroQuartos(int numeroQuartos) {
        if(this.getTipo().equals(Tipo.Residencial))
                this.numeroQuartos = numeroQuartos;
        else
            throw new UnsupportedOperationException("Disponível apenas em imóveis residenciais");
        
    }

    public int getNumeroBanheiros() {
        return numeroBanheiros;
    }

    public void setNumeroBanheiros(int numeroBanheiros) {
        this.numeroBanheiros = numeroBanheiros;
    }
    
    
}

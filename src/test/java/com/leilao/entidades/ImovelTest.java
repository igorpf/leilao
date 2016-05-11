/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leilao.entidades;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author igor
 */
public class ImovelTest {
    
    private Imovel i1;
    
    private Imovel i2;
    
    @Before
    public void setUp() {
        i1 = new Imovel();
        i1.setTipo(Imovel.Tipo.Comercial);
        i2 = new Imovel();
    }
    
    @Test
    public void testSetNumeroQuartos() throws Exception {
        i2.setNumeroQuartos(5);
        assertEquals(i2.getNumeroQuartos(),5);
    }
    @Test(expected=Exception.class)
    public void testSetNumeroQuartosFail() throws Exception {
        i1.setNumeroQuartos(5);
    }
    
}

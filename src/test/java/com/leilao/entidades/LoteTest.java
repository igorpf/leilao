/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leilao.entidades;

import java.math.BigDecimal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author igor
 */
public class LoteTest {

    private Usuario c1;

    private Usuario v1;

    private Lote l1;

    @Before
    public void setUp() {
        c1 = new Usuario();
        v1 = new Usuario();
        l1 = new Lote();
    }
    @Test
    public void test() throws Exception{
        l1.setComprador(c1);
        l1.setVendedor(v1);
        c1.addCompra(l1);
        v1.addVenda(l1);
        l1.setLanceAtual(BigDecimal.ONE);
        assertEquals(l1.getVendedor(), v1);
        assertEquals(l1.getComprador(), c1);
        assertEquals(l1.getLanceAtual(), BigDecimal.ONE);
    }
    @Test(expected = Exception.class)
    public void testLanceMenorQueAtualFail() throws Exception{
        l1.setLanceAtual(BigDecimal.TEN);
        l1.setLanceAtual(BigDecimal.ONE);
    }
    @Test(expected = Exception.class)
    public void testLanceMenorQueMinimoFail() throws Exception{
        l1.setValorMinimo(BigDecimal.TEN);
        l1.setLanceAtual(BigDecimal.ONE);
    }
}

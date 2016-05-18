/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leilao.servicos;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.leilao.TestPersistenceConfig;
import com.leilao.entidades.Imovel;
import com.leilao.entidades.Usuario;
import java.util.List;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

/**
 *
 * @author igor
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {TestPersistenceConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class})
@DirtiesContext

public class ServicoImovelImplIT {

    @Autowired
    ServicoImovel servico;
    @Autowired 
    ServicoUsuario servicoUsuario;
    
    private Usuario u;
    @Before
    public void setUp() {
        u = new Usuario();
        u.setNome("Test");
        servicoUsuario.save(u);
    }


    @Test
    public void testGetUser() {
        assertThat(servicoUsuario.get(1).getNome(), equalTo("Test"));
    }
    
    @Test
    public void testSave(){
        Imovel i = new Imovel();
        i.setNome("Im");
        Usuario u1=servicoUsuario.get(1);
        i.setVendedor(u1);
        servico.save(i);
        assertThat(servico.get(1).getNome(),equalTo("Im"));
        assertThat(servico.get(1).getId(),equalTo(1));
    }
    /** Tem que falhar porque o lote não pode ficar sem vendedor!
     * 
     */
    @Test(expected=Exception.class)
    public void testSaveFail(){
        Imovel i = new Imovel();
        i.setNome("Im");
        servico.save(i);
    }
    /**
     * Test of delete method, of class ServicoImovelImpl.
     */
    @Test
    public void testDelete() {
        servico.delete(1);
        assertThat(servico.findAll().size(),equalTo(0));
    }

    

}

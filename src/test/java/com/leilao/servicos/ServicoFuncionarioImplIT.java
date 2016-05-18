/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leilao.servicos;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.leilao.TestPersistenceConfig;
import com.leilao.entidades.Funcionario;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
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
public class ServicoFuncionarioImplIT {

    @Autowired
    ServicoFuncionario servico;

    @Before
    public void setUp() {
        Funcionario f = new Funcionario();
        f.setNome("Bugginho");
        servico.save(f);
    }

    @Test
    public void testGet() {
        assertThat(servico.get(1).getNome(), equalTo("Bugginho"));
    }

    @Test
    public void testDelete() {
        servico.delete(1);
        assertThat(servico.get(1), equalTo(null));
    }


}

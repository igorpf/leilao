package com.leilao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {LeilaoApplication.class,PersistenceConfig.class})
public class LeilaoApplicationTests {

	@Test
	public void contextLoads() {
	}

}

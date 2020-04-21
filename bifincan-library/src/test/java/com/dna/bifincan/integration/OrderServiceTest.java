package com.dna.bifincan.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.dna.bifincan.integration.data.OrderDataManager;
import com.dna.bifincan.model.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext-test.xml" })
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@Transactional
public class OrderServiceTest extends OrderDataManager {

   
  	@Test
	public void dummy() {
  		User user = this.getUserUser("usman1");
  		
  		//this.createWelcomeOrder(user);
	}     
}

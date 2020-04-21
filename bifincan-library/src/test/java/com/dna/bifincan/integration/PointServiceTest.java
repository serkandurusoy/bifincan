package com.dna.bifincan.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.dna.bifincan.integration.data.PointDataManager;
import com.dna.bifincan.model.config.Configuration;
import com.dna.bifincan.model.type.PointTransactionType;
import com.dna.bifincan.model.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext-test.xml" })
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@Transactional
public class PointServiceTest extends PointDataManager {

    @Test  // a basic test ensuring the existence of the required data
    @Ignore
    public void testFetchingConfigEntriesByKey() {
     	log.info(">>>>>>> TEST: testFetchingConfigEntriesByKey() <<<<<<<<<");
     	
     	PointTransactionType[] types = PointTransactionType.values();
     	if(types != null) {
	     	for(PointTransactionType type : types) {
	     		if(type.getKey() != null) {
		     		Configuration c = this.findConfigParamByKey(type.getKey());
		     		assertTrue("It seems that a configuration parameter [" + type.getKey() + 
		     				"] has not been setup in the DB !!!", c != null);
		     		assertTrue("It seems that a configuration parameter [" + type.getKey() + 
		     				"] has not a value in the DB !!!", c.getValue() != null);
	     		}
	     	}
     	}

      	log.info(">>>>>>> END TEST: testFetchingConfigEntriesByKey() <<<<<<<<<");
    } 	
    	
    @Test 
    @Ignore
    public void testAddingTransactionPoints() {
     	log.info(">>>>>>> TEST: testAddingTransactionPoints() <<<<<<<<<");

     	/// add points
     	User user1_1 = this.getStandardUser();
     	int pointsBefore1 = user1_1.getPointsBalance();
     	
     	PointTransactionType type = null;
     	
     	type = PointTransactionType.INVITIATION_POINTS;
     	this.addPoints(user1_1, type, null);
     	
     	type = PointTransactionType.INVITIATION_SUCCESS_POINTS;
     	this.addPoints(user1_1, type, null);
     	
     	type = PointTransactionType.LOGIN_POINTS;
     	this.addPoints(user1_1, type, null);
     	
     	type = PointTransactionType.RATING_POINTS;
     	this.addPoints(user1_1, type, null);
     	
     	type = PointTransactionType.SIGNUP_POINTS;
     	this.addPoints(user1_1, type, null);
     	
     	int relSum = 0;
     	
     	type = PointTransactionType.GENERIC_SURVEY_POINTS;
     	this.addPoints(user1_1, type, null);  
     	relSum += 1;
     	
     	type = PointTransactionType.PRODUCT_ORDER;
     	this.addPoints(user1_1, type, 30);  
     	relSum -= 30;     	
     	
     	type = PointTransactionType.PRODUCT_SURVEY;
     	this.addPoints(user1_1, type, 11);  
     	relSum += 11;   
     	
     	type = PointTransactionType.QUIZ_POINTS;
     	this.addPoints(user1_1, type, 6);  
     	relSum += 6; 

     	//// check the results
     	User user1_2 = this.getStandardUser();
     	int pointsAfter1 = user1_2.getPointsBalance();
     	
     	int sum = 0;
     	Configuration config = null;
     	
     	config = this.findConfigParamByKey(PointTransactionType.INVITIATION_POINTS.getKey());
     	sum += Integer.valueOf(config.getValue());
     	
     	config = this.findConfigParamByKey(PointTransactionType.INVITIATION_SUCCESS_POINTS.getKey());
     	sum += Integer.valueOf(config.getValue());
     	
     	config = this.findConfigParamByKey(PointTransactionType.LOGIN_POINTS.getKey());
     	sum += Integer.valueOf(config.getValue());
     	
     	config = this.findConfigParamByKey(PointTransactionType.RATING_POINTS.getKey());
     	sum += Integer.valueOf(config.getValue());
     	
     	config = this.findConfigParamByKey(PointTransactionType.SIGNUP_POINTS.getKey());
     	sum += Integer.valueOf(config.getValue());    
     	
     	sum += relSum;
     	
     	assertEquals("It seems that the user balance is calculated in a wrong way", 
     			sum, pointsAfter1 - pointsBefore1);
     	
      	log.info(">>>>>>> END TEST: testAddingTransactionPoints() <<<<<<<<<");
    }
    
  	@Test
	public void dummy() {
  	//	User user1_1 = this.getStandardUser();
  		
  	//	this.addPoints(user1_1, PointTransactionType.INVITIATION_POINTS, 0);
	}     
}

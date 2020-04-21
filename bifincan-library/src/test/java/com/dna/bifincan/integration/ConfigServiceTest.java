package com.dna.bifincan.integration;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.dna.bifincan.integration.data.ConfigDataManager;
import com.dna.bifincan.model.config.Configuration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext-test.xml" })
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@Transactional
public class ConfigServiceTest extends ConfigDataManager {

    @Test  // a basic test ensuring the existence of the required data
    @Ignore
    public void testFetchingTestConfigEntries() {
     	log.info(">>>>>>> TEST: testFetchingTestConfigEntries() <<<<<<<<<");
     	
     	List<Configuration> dbEntries = this.getParameters();
     	
      	assertTrue("It seems that the stored test configuration data do not exist !!!", dbEntries != null);
      	assertTrue("It seems that the stored test configuration data are not the correct ones !!!", dbEntries.size() == 4);
      	
      	assertTrue("It seems that the memory cached test configuration data do not exist !!!", 
      			this.getStandardEntries() != null);
      	assertTrue("It seems that the memory cached test configuration data are not the correct ones !!!", 
      			this.getStandardEntries().size() == 4);

      	this.getStandardEntries().get(1).setValue("SDF_342D$$#23ss_SSdjk3;2");
      	
      	assertTrue("Wrong test configuration test data are provided through equals & hash functions !!!", 
				this.getStandardEntries().containsAll(dbEntries) == false);
      	
      	dbEntries.add(new Configuration("k1","v1","d1"));
      	List<Configuration> dbEntries2 = this.getParameters();
      	
      	assertTrue("It seems that the copy of entries is not updatable !!!", dbEntries.size() == 5);
      	assertTrue("It seems that the cached repository of entries is not immutable !!!", dbEntries2.size() == 4);
      	
      	log.info(">>>>>>> END TEST: testFetchingTestConfigEntries() <<<<<<<<<");
    } 	
    
    
    @Test  // a basic test ensuring the existence of the required data
    @Ignore
    public void testFetchingCreateEditUpdateOperations() {
     	log.info(">>>>>>> TEST: testFetchingEditConfigEntry() <<<<<<<<<");
     	
     	Configuration newConfig = addAnotherEntry();
     	
     	Configuration regConfig = this.find(newConfig.getKey());
     	
     	assertTrue("It seems that the new configuration object wasn't constructed !!!", newConfig != null);
      	assertTrue("It seems that the new configuration object wasn't saved !!!", regConfig != null);
      	assertTrue("It seems that the new configuration object wasn't persisted or fethed correctly  !!!", 
      			newConfig.equals(regConfig));
      	
      	final String newValue = "431331";
      	
      	newConfig.setValue(newValue);
      	assertTrue("It seems that the configuration object hasn't implemented correctly the equals & hash methods !!!", 
      			!newConfig.equals(regConfig));
      	
      	regConfig.setValue(newValue);
      	this.saveOrUpdate(regConfig);
      	
      	Configuration regConfig1 = this.find(newConfig.getKey());
      	assertTrue("It seems that the new conficuration object wasn't updated properly  !!!", 
      			newConfig.equals(regConfig1));
      	assertTrue("It seems that the value of the configuration parameter is not correct  !!!", 
      			newValue.equals(regConfig1.getValue()));
      	
      	log.info(">>>>>>> END TEST: testFetchingEditConfigEntry() <<<<<<<<<");
    }
    
  	@Test
	public void dummy() {

	}      
}

package com.dna.bifincan.integration.data;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dna.bifincan.model.config.Configuration;
import com.dna.bifincan.repository.configuration.ConfigurationRepository;
import com.dna.bifincan.service.ConfigurationService;

public class ConfigDataManager extends ConfigurationService {
	protected final static Logger log = LoggerFactory.getLogger(ConfigDataManager.class);

	private static String[][] data = new String[][] {
		{ "signupPoints", "Number of points won when a survey is completed" , "100" },
		{ "invitationPoints", "Number of points won when an invitation is sent to a friend" , "50" },
		{ "invitationSuccessPoints", "Number of points won when an invitation is activated by the person who received it" , "100" },
		{ "numInvitationsPerDay", "Number of maximum invitations that a user send daily" , "20" }
	};
	
	private List<Configuration> standardEntries = new ArrayList<Configuration>();
	
    @PersistenceContext
    protected EntityManager em;

	@SuppressWarnings("unused")
	private ConfigurationRepository configutationRepository;
	
	public ConfigDataManager() { }
	
	@Inject
    public ConfigDataManager(ConfigurationRepository configutationRepository) {
		super(configutationRepository);
		this.configutationRepository = configutationRepository;
	}
    
	@Before
	public void createTestData() {
    	log.info(">>>>>>> Initialing Config Test Data.... <<<<<<<<<");
    	
    	try {
    		deleteThemAll();
    	} catch(Exception ex) { 
    		ex.printStackTrace();
    	}
    	
    	addConfigEntries();
    	
    	log.info(">>>>>>> INITIALIZING Config Test Data : SUCCESS <<<<<<<<<");
    }

	@After
	public void destroyTestData() {
		deleteThemAll();
    }    
    
    @SuppressWarnings("unchecked")
	private void deleteThemAll() {
    	log.info(">>>>>>> Deleting Config Test Data.... <<<<<<<<<");

    	// delete test config entries
		Query query = em.createQuery("select c from Configuration c");			
		
		List<Configuration> entries = (List<Configuration>)query.getResultList(); 
		
		if(entries != null) {
			for(Configuration entry : entries) {
				em.remove(entry);
			}
		}
	
    	log.info(">>>>>>> Deleting Config Test Data : SUCCESS <<<<<<<<<");    	
    }
	
	private void addConfigEntries() {
		Configuration config = null;
		Configuration tc= null;
	
		if(data != null) {
			for(String[] entry : data) {
				config = new Configuration();
				
				config.setKey(entry[0]);
				config.setDescription(entry[1]);
				config.setValue(entry[2]);
				
				em.persist(config);
				
				tc = new Configuration();
				tc.setKey(config.getKey());
				tc.setDescription(config.getDescription());
				tc.setValue(config.getValue());
				
				standardEntries.add(tc);
			}
		//	this.reloadList();
		}
	}

	protected Configuration addAnotherEntry() {
		Configuration config = new Configuration("test_key", "test_descr", "test_val");
		
		this.saveOrUpdate(config);
		
		Configuration tc = new Configuration();
		
		tc.setKey(config.getKey());
		tc.setDescription(config.getDescription());
		tc.setValue(config.getValue());
		
		return tc;
	}
	
	protected List<Configuration> getStandardEntries() {
		return standardEntries;
	}

	protected void setStandardEntries(List<Configuration> standardEntries) {
		this.standardEntries = standardEntries;
	}	
	 
}

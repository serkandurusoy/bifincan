package com.dna.bifincan.integration.data;

import java.util.Calendar;
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
import com.dna.bifincan.model.order.PointTransactionAccounting;
import com.dna.bifincan.model.user.SubscriptionOption;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.model.user.verification.EmailVerification;
import com.dna.bifincan.model.user.verification.GsmVerification;
import com.dna.bifincan.repository.order.PointTransactionAccountingRepository;
import com.dna.bifincan.repository.user.UserRepository;
import com.dna.bifincan.service.ConfigurationService;
import com.dna.bifincan.service.PointTransactionAccountingService;

public class PointDataManager extends PointTransactionAccountingService {
	protected final static Logger log = LoggerFactory.getLogger(PointDataManager.class);
	
    @PersistenceContext
    protected EntityManager em;

    @Inject
	private PointTransactionAccountingRepository pointTransactionAccountingRepository;
    @Inject
	private ConfigurationService configurationService;
    @Inject
	private UserRepository userRepository;

	public PointDataManager() { }
	
	/*@Inject
	public PointDataManager(PointTransactionAccountingRepository pointTransactionAccountingRepository, 
			ConfigurationService configurationService,
			UserRepository userRepository) {
		super(pointTransactionAccountingRepository, configurationService, userRepository);
		this.pointTransactionAccountingRepository = pointTransactionAccountingRepository; 
		this.configurationService = configurationService;
		this.userRepository = userRepository; 
	}*/	
    
	@Before
	public void createTestData() {
    	log.info(">>>>>>> Initialing Point Test Data.... <<<<<<<<<");
    	
    	try {
    		deleteThemAll();
    	} catch(Exception ex) { 
    		ex.printStackTrace();
    	}
    	
    	addStandardUser();
    	
    	log.info(">>>>>>> INITIALIZING Point Test Data : SUCCESS <<<<<<<<<");
    }

	@After
	public void destroyTestData() {
		deleteThemAll();
    }    
    
    @SuppressWarnings("unchecked")
	private void deleteThemAll() {
    	log.info(">>>>>>> Deleting Point Test Data.... <<<<<<<<<");

    	// delete transaction history
		Query tQuery = em.createQuery("select t from PointTransactionAccounting t where t.user.password = 'qwaszx12'");			
		
		List<PointTransactionAccounting> transactions = (List<PointTransactionAccounting>)tQuery.getResultList(); 
		
		if(transactions != null) {
			for(PointTransactionAccounting t : transactions) {
				em.remove(t);
			}
		}
		
    	// delete test users
		Query uQuery = em.createQuery("select u from User u where u.password = 'qwaszx12'");			
		
		List<User> users = (List<User>)uQuery.getResultList(); 
		
		if(users != null) {
			for(User user : users) {
				em.remove(user);
			}
		}

    	log.info(">>>>>>> Deleting Point Test Data : SUCCESS <<<<<<<<<");    	
    }
	
	protected Configuration findConfigParamByKey(String key) {
		Configuration tc = this.configurationService.find(key);
		
		return tc;
	}
	
	private User addStandardUser() {
		User user = addUser(1965, "halloworld12");
		return user;
	}
	
	protected User addUser(int year, String username) {
		User user = new User();
		
		user.setActive(true);user.setVersion(1L);
		
		java.util.Date birthdate = new java.util.Date();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		birthdate = calendar.getTime();
		
		user.setBirthday(birthdate);user.setCreateDate(new java.util.Date());user.setEmail(year + "xyz@qwerty.com");
		
		EmailVerification ev = new EmailVerification();
		ev.setEmailVerificationSentTime(new java.util.Date());ev.setEmailVerified(true);
		
		user.setEmailVerification(ev);user.setFirstName("Firstname" + year);
		
		GsmVerification gv = new GsmVerification();
		gv.setGsmVerificationSentTime(new java.util.Date());gv.setGsmVerified(true);
		
		user.setGsmVerification(gv);user.setLastName("Lastname" + year);user.setPassword("qwaszx12");
	
		SubscriptionOption so = new SubscriptionOption();
		so.setAcceptSiteMail(true);so.setAcceptThirdpartyMail(true);so.setAcceptThirdpartySms(true);
		
		user.setSubscriptionOption(so);user.setUsername(username == null?"2w3e4r5t" + year:username);
		
		em.persist(user);
		
		return user;
	}
	
	protected User getStandardUser() {
		User user = null;
		try {
			Query query = em.createQuery("select u from User u where u.username = 'halloworld12'");
		
			user = (User)query.getSingleResult();
		} catch(Exception ex) { }
    	return user;
	}		
}

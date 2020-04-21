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
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.dna.bifincan.model.order.ShoppingCart;
import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.model.user.SubscriptionOption;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.model.user.verification.EmailVerification;
import com.dna.bifincan.model.user.verification.GsmVerification;
import com.dna.bifincan.repository.order.OrderRepository;
import com.dna.bifincan.repository.products.ProductRepository;
import com.dna.bifincan.service.OrderService;
import com.dna.bifincan.service.ProductService;

public class OrderDataManager extends AbstractTransactionalJUnit4SpringContextTests {
	protected final static Logger log = LoggerFactory.getLogger(OrderService.class);

    @PersistenceContext
    protected EntityManager em;

    @Inject
    protected OrderService orderService;
    @Inject
    protected OrderRepository orderRepository; 
    @Inject
    protected ProductService productService;    
    @Inject
    protected ProductRepository productRepository;
    
    private ShoppingCart shoppingCart = null;
	
	public OrderDataManager() { }
	
    
    @SuppressWarnings("unused")
	@Before
	public void createTestData() {
    	log.info(">>>>>>> Initialing Order Test Data.... <<<<<<<<<");
    	
    	try {
    		deleteThemAll();
    	} catch(Exception ex) { 
    		ex.printStackTrace();
    	}
    
    	addStandardUser();
    	createShoppingCart();
    	
    	log.info(">>>>>>> INITIALIZING Order Test Data : SUCCESS <<<<<<<<<");
    }

	@After
	public void destroyTestData() {
		deleteThemAll();
    }    
	
    @SuppressWarnings("unchecked")
	private void deleteThemAll() {
    	log.info(">>>>>>> Deleting Order Test Data.... <<<<<<<<<");
    	
    	// delete additional users
		Query uQuery = em.createQuery("select u from User u where u.password = 'qwaszx12'");			
		
		List<User> users = (List<User>)uQuery.getResultList(); 
		
		if(users != null) {
			for(User user : users) {
				em.remove(user);
			}
		}
		
    	log.info(">>>>>>> Deleting Order Test Data : SUCCESS <<<<<<<<<");    	
    }
	
    private void createShoppingCart() {
    	Product productStock1 = productRepository.findOne(1L);
    	Product productStock4 = productRepository.findOne(4L);
    	Product productStock7 = productRepository.findOne(7L);
    	
    	this.shoppingCart = new ShoppingCart();
    	
//    	this.shoppingCart.addProduct(productStock1);
//    	this.shoppingCart.addProduct(productStock4);
//    	this.shoppingCart.addProduct(productStock7);
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
    	return getUserUser("halloworld12");
	}	
	
	protected User getUserUser(String username) {
		User user = null;
		try {
			Query query = em.createQuery("select u from User u where u.username = :username");
			query.setParameter("username", username);
			
			user = (User)query.getSingleResult();
		} catch(Exception ex) { 
			ex.printStackTrace();
		}
    	return user;
	}

	public ShoppingCart getShoppingCart() {
		return shoppingCart;
	}

}

package com.dna.bifincan.integration.data;

import java.util.Arrays;
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

import com.dna.bifincan.integration.ProductServiceTest;
import com.dna.bifincan.model.products.Brand;
import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.model.products.ProductCategoryLevelThree;
import com.dna.bifincan.model.products.ProductOrderCriteria;
import com.dna.bifincan.model.user.SubscriptionOption;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.model.user.verification.EmailVerification;
import com.dna.bifincan.model.user.verification.GsmVerification;
import com.dna.bifincan.repository.products.ProductOrderCriteriaRepository;
import com.dna.bifincan.repository.products.ProductRepository;
import com.dna.bifincan.service.ProductService;

public class ProductDataManager extends ProductService {
	protected final static Logger log = LoggerFactory.getLogger(ProductServiceTest.class);

    @PersistenceContext
    protected EntityManager em;
    
	@SuppressWarnings("unused")
	private ProductRepository productRepository;
	@SuppressWarnings("unused")
	private ProductOrderCriteriaRepository productOrderCriteriaRepository;
	
	// This set of classes are based on some default records in the DB
	protected final String STANDARD_BRAND = "Samsung";
	protected final String STANDARD_CATEGORY = "Electronic Products";
	
	// Other standard names
	protected final String FIRST_PRODUCT = "Product 1";
	protected final String SECOND_PRODUCT = "Product 2";
	
	public ProductDataManager() { }
	
	@Inject
    public ProductDataManager(ProductRepository productRepository,
			ProductOrderCriteriaRepository productOrderCriteriaRepository) {
		super(productRepository, productOrderCriteriaRepository);
		this.productRepository = productRepository;
		this.productOrderCriteriaRepository = productOrderCriteriaRepository;
	}
    
    @SuppressWarnings("unused")
	@Before
	public void createTestData() {
    	log.info(">>>>>>> Initialing Product Test Data.... <<<<<<<<<");
    	
    	try {
    		deleteThemAll();
    	} catch(Exception ex) { 
    		ex.printStackTrace();
    	}
    	
    	addProducts();
    	addStandardUser();
    	
    	log.info(">>>>>>> INITIALIZING Product Test Data : SUCCESS <<<<<<<<<");
    }

	@After
	public void destroyTestData() {
		deleteThemAll();
    }    
	
    @SuppressWarnings("unchecked")
	private void deleteThemAll() {
    	log.info(">>>>>>> Deleting Product Test Data.... <<<<<<<<<");

    	// delete product criteria
		Query criQuery = em.createQuery("select p from ProductOrderCriteria p");			
		
		List<ProductOrderCriteria> criteria = (List<ProductOrderCriteria>)criQuery.getResultList(); 
		
		if(criteria != null) {
			for(ProductOrderCriteria criterio : criteria) {
				em.remove(criterio);
			}
		}
		
		// delete products
    	Query prodQuery = em.createQuery("select p from Product p where p.name in (:names)");			
    	prodQuery.setParameter("names", Arrays.asList(FIRST_PRODUCT, SECOND_PRODUCT));
    	
		List<Product> products = (List<Product>)prodQuery.getResultList(); 
		
		if(products != null) {
			for(Product product : products) {
				em.remove(product);
			}
		}
		
    	// delete additional users
		Query uQuery = em.createQuery("select u from User u where u.password = 'qwaszx12'");			
		
		List<User> users = (List<User>)uQuery.getResultList(); 
		
		if(users != null) {
			for(User user : users) {
				em.remove(user);
			}
		}
		
    	log.info(">>>>>>> Deleting Product Test Data : SUCCESS <<<<<<<<<");    	
    }
	
	protected Brand findStandardBrand() {
		Query query = em.createQuery("select b from Brand b where b.name = :name");
		query.setParameter("name", STANDARD_BRAND);

		Brand brand = (Brand)query.getSingleResult();

		return brand;
	}    
	
	protected ProductCategoryLevelThree findStandardProductCategory() {
		Query query = em.createQuery("select p from ProductCategoryLevelThree p where p.name = :name");
		query.setParameter("name", STANDARD_CATEGORY);

		ProductCategoryLevelThree category = (ProductCategoryLevelThree)query.getSingleResult();

		return category;
	}  	
	
	private void addProducts() {
    	Product p1 = createProduct(FIRST_PRODUCT);
    	Product p2 = createProduct(SECOND_PRODUCT);
    	
    	createProduct(p1,10);
    	em.persist(p1);
    	
    	createProduct(p2,100);	
    	em.persist(p2);
    	
    //	ProductOrderAgeCriteria poa1_1 = createProductOrderAgeCriteria(p1, 20, 30);
    //	em.persist(poa1_1);
    //	ProductOrderAgeCriteria poa1_2 = createProductOrderAgeCriteria(p2, 30, 40);
    //	em.persist(poa1_2);
	}	
	
	private Product createProduct(String name) {
		Product product = new Product();
		
		product.setName(name);
		product.setVersion(1L);
		product.setBarcode("XYZ" + name);
		product.setDescription("Descr for " + name);
		product.setShortDescription("Descr for " + name);
		
		return product;
	}
	
	private void createProduct(Product product, int points) {
		product.setVersion(1L);
		product.setActive(true);
		product.setPricePoints(points);
	}
	
	private ProductOrderCriteria createProductOrderAgeCriteria(Product product, 
					int minAge, int maxAge) {
	/*	ProductOrderAgeCriteria criterio = new ProductOrderAgeCriteria();
	
		criterio.setAgeMinimum(minAge);
		criterio.setAgeMaximum(maxAge);
		criterio.setProduct(product);
		criterio.setVersion(1L);
		
		return criterio;*/
		return null;
	}
	
	
	protected Product findProduct(String name) {
		Query query = em.createQuery("select p from Product p where p.name = :name");
		query.setParameter("name", name);

		Product product = (Product)query.getSingleResult();

		return product;
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

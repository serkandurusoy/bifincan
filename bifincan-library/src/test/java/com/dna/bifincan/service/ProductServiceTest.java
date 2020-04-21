package com.dna.bifincan.service;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.dna.bifincan.model.products.Brand;
import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.model.products.ProductOrderCriteria;
import com.dna.bifincan.model.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext-test.xml" })
public class ProductServiceTest extends AbstractTransactionalJUnit4SpringContextTests {
	private final static Logger log = LoggerFactory.getLogger(ProductServiceTest.class);

	@PersistenceContext
	EntityManager em;

	@BeforeClass
	public static void createEntityManagerFactory() {
		log.debug("beforeClass...");
	}

	@AfterClass
	public static void closeEntityManagerFactory() {
		log.debug("afterClass...");
	}

	@Before
	public void beginTransaction() {
	}

	@After
	public void rollbackTransaction() {

	}

	@Autowired
	ProductService service;
	private Brand brand;

	@Transactional
	void initializeData() {
		// em.getTransaction().begin();
		brand = new Brand("Cacocalo");
		em.persist(brand);

		Product cacocalo = new Product("Cacocalo Drink", "Cacocalo Drinks Short Desc", "barcode");
		cacocalo.setBrand(brand);
		em.persist(cacocalo);

		Product cacocalo2 = new Product("Cacocalo 2 Drink", "Cacocalo 2 Drinks Short Desc", "barcode2");
		cacocalo2.setBrand(brand);
		em.persist(cacocalo2);

	/*	ProductStock stock = new ProductStock();
		stock.setProduct(cacocalo);
		em.persist(stock);

		ProductStock stock2 = new ProductStock();
		stock2.setProduct(cacocalo);
		em.persist(stock2);*/

		ProductOrderCriteria ageCriteria = null;//new ProductOrderAgeCriteria(stock, 25, 60);
		em.persist(ageCriteria);
		// em.flush();
		// em.getTransaction().commit();
	}

	private void destroyData() {
		this.deleteFromTables(" product_order_criteria", "product_stock", "product", "brand");
	}

	@Inject
	UserService userService;

	@Test
	public void testFindProductsByUser() {
		User user = userService.findUserByUsername("hantsy");
		initializeData();
		List<Product> products = service.findProductsByCriteria(user);
		log.debug(" products size @" + products.size());

		destroyData();

	}

}

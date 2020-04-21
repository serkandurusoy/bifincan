package com.dna.bifincan.repository.products;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.products.Product;
import java.math.BigDecimal;

public interface ProductRepository extends JpaRepository<Product, Long> , ProductRepositoryCustom  {

	@Query("select distinct p from Product p where p.id in (:allResults)")	
	public List<Product> findAllByCriteria(@Param("allResults") Set<Long> allResults);
	
	@Query("select count(distinct p.brand) from Product p")	
	public Long countDistincBrands();
	
	@Query("select p.id from Product p where " +
	   	   " p.welcomeProduct=true and (p.stockQuantity - (p.orderQuantity + p.loss)) > 0 and p.active=true and p.id in (:allResults)")	   	   
	public List<Long> fetchWelcomeProducts(@Param("allResults") Set<Long> allResults);
	
	@Query("select p.id from Product p where (p.stockQuantity - (p.orderQuantity + p.loss)) <= 0 and p.active=true and (p.classifier = 0 or (p.classifier <> 0 and :userAge >= :targetAge)) order by p.entryDate desc")		   	   
	public List<Long> findOutOfStockProductIds(@Param("userAge") int userAge, @Param("targetAge") int targetAge);
	
	@Query("select p from Product p where (p.stockQuantity - (p.orderQuantity + p.loss)) <= 0 and p.active=true and (p.classifier = 0 or (p.classifier <> 0 and :userAge >= :targetAge)) order by p.entryDate desc")		   	   
	public List<Product> findOutOfStockProducts(@Param("userAge") int userAge, @Param("targetAge") int targetAge);	
	
	@Query("select p.id from Product p where  (p.stockQuantity - (p.orderQuantity + p.loss)) > 0 and p.active=true order by p.entryDate desc")	
	public List<Long> findInStockProductIds();	
	
	@Query("select p.id from Product p where p.active=true and p.classifier=0 order by p.entryDate desc")	
	public List<Long> findAllActiveProductIds();	
           
        @Query("select p from Product p where p.active=true and p.classifier=0 order by p.entryDate desc")	
	public List<Product> findAllActiveProducts();	
	
        @Query("select p from Product p where p.active=true order by p.entryDate desc")	
	public List<Product> findAllActiveProductsAllTypes();	
	
        @Query("select p from Product p where (p.stockQuantity - (p.orderQuantity + p.loss)) > 0 and p.active=true order by p.entryDate desc")	
	public List<Product> findAllActiveProductsAllTypesInStock();	
	
        @Query("select p from Product p where (p.stockQuantity - (p.orderQuantity + p.loss)) <= 0 and p.active=true order by p.entryDate desc")	
	public List<Product> findAllActiveProductsAllTypesOutOfStock();	
	
	@Query("select p.slug from Product p where p.active=true order by p.id desc")	
	public List<String> findAllActiveProductSlugs();		
	
	//@Query("select p.id from Product p where p.id in (:productIds) order by p.id desc")	
	//public List<Long> findProductIdsByCriteria(@Param("productIds") List<Long> productIds);	

	@Query("select p from Product p where p.id in (:productIds) order by p.id desc")	
	public List<Product> findProductsByCriteria(@Param("productIds") List<Long> productIds);	
	
	@Query("select p.id from Product p where p.slug = :slug")
	public Long findProductIdBySlug(@Param("slug") String slug);
	
	@Query("select p.id from Product p where p not in (select po.product from ProductOrderCriteria po) and p.active=true")
	public List<Long> findAllActiveUnrestricted();
	
	@Query("select p from Product p where p.id in (:checkedIds) and p.active=true and (p.stockQuantity - (p.orderQuantity + p.loss)) > 0  and (p.classifier = 0 or (p.classifier <> 0 and :userAge >= :targetAge)) and p.orderableProduct = 1 order by p.entryDate desc")
	public List<Product> findAvailableOrderableProductsByCriteria(@Param("checkedIds") Set<Long> checkedIds, @Param("userAge") int userAge, @Param("targetAge") int targetAge);	

	@Query("select p.id from Product p where p.id in (:checkedIds) and p.active=true and (p.stockQuantity - (p.orderQuantity + p.loss)) > 0  and (p.classifier = 0 or (p.classifier <> 0 and :userAge >= :targetAge)) and p.orderableProduct = 1 order by p.entryDate desc")
	public List<Long> findAvailableOrderableProductIdsByCriteria(@Param("checkedIds") Set<Long> checkedIds, @Param("userAge") int userAge, @Param("targetAge") int targetAge);	
	
	@Query("select p.id from Product p where p.id in (:checkedIds) and (p.classifier = 0 or (p.classifier <> 0 and :userAge >= :targetAge) ) order by p.entryDate desc")
	public List<Long> findLegalProductIdsByListAndUserAge(@Param("checkedIds") List<Long> checkedIds, @Param("userAge") int userAge, @Param("targetAge") int targetAge);
	
	@Query("select p from Product p where p.id in (:checkedIds) and (p.classifier = 0 or (p.classifier <> 0 and :userAge >= :targetAge) ) order by p.entryDate desc")
	public List<Product> findLegalProductsByListAndUserAge(@Param("checkedIds") List<Long> checkedIds, @Param("userAge") int userAge, @Param("targetAge") int targetAge);
	
	@Query("select p.id from Product p where p.id in (:checkedIds) and p.active=true and (p.stockQuantity - (p.orderQuantity + p.loss)) > 0 order by p.entryDate desc")
	public List<Long> findAvailableProductIdsByCriteria(@Param("checkedIds") Set<Long> checkedIds);
	
	@Query("select p.id from Product p where p.id not in (:checkedIds) and p.active=true and (p.stockQuantity - (p.orderQuantity + p.loss)) > 0  and (p.classifier = 0 or (p.classifier <> 0 and :userAge >= :targetAge)) order by p.entryDate desc")
	public List<Long> findOtherProductIdsByCriteria(@Param("checkedIds") Set<Long> checkedIds, @Param("userAge") int userAge, @Param("targetAge") int targetAge);
	
	@Query("select p.id from Product p where p.id not in (:checkedIds) and p.active=true and (p.stockQuantity - (p.orderQuantity + p.loss)) > 0  and (p.classifier = 0 or (p.classifier <> 0 and :userAge >= :targetAge)) and p.orderableProduct = 1 order by p.entryDate desc")
	public List<Long> findOtherOrderableProductIdsByCriteria(@Param("checkedIds") Set<Long> checkedIds, @Param("userAge") int userAge, @Param("targetAge") int targetAge);
	
	@Query("select p from Product p where p.id in (:productIds)")
	public List<Product> getProductsByIds(@Param("productIds") List<Long> productIds);

	@Query("select p from Product p where p.id in (:productIds) order by p.entryDate desc")
	public List<Product> getOrderedProductsByIds(@Param("productIds") List<Long> productIds);
	
	@Query("select p from Product p where p.slug in (:productSlugs)")
	public List<Product> findProductsBySlugs(@Param("productSlugs") Set<String> productSlugs);
	
	@Query("select p.id from Product p where p.id in (:productIds) and p.active=true and " +
			"(p.stockQuantity - (p.orderQuantity + p.loss)) > 0 and p.welcomeProduct=1")
	public List<Long> getActiveWelcomeProductIds(@Param("productIds") Set<Long> allResults);
	
	@Query("select p.id from Product p where p.id in (:productIds) and p.active=true and " +
			"(p.stockQuantity - (p.orderQuantity + p.loss)) > 0 and p.surpriseProduct=1") 
	public List<Long> getActiveSurpriseProductIds(@Param("productIds") Set<Long> allResults);
		
	@Query("select count(o) from Product o where o.category.id = :categoryId")
	public Long countByProductCategoryLevelThree(@Param("categoryId")Long categoryId);	
	
	@Query("select count(o) from Product o where o.brand.id = :brandId")
	public Long countByBrand(@Param("brandId")Long brandId);	
	
	@Query("select count(o) from Product o where (o.name = :name or o.barcode = :barcode or o.slug = :slug) and o.id <> :countryId")
	public Long countByUniqueFields(@Param("name")String name, @Param("barcode")String barcode, @Param("slug")String slug, @Param("countryId")Long countryId);	
	
	@Query("select count(o) from Product o where o.priceSource.id = :sourceId")
	public Long countByProductPriceSource(@Param("sourceId")Long sourceId);
	
	@Query("select o from Product o where o.name like :name")
	public List<Product> findByName(@Param("name")String name);	
	
	@Query("select count(o) from Product o where o.welcomeProduct = true and o.active = true and (o.stockQuantity - (o.orderQuantity + o.loss)) > 0")
	public Long countAvailableWelcomeProducts();

	@Query("select o.id from Product o where o.active = true and (o.stockQuantity - (o.orderQuantity + o.loss)) > 0 order by o.entryDate desc")
	public List<Long> findAllActiveWithStockProducts();	

	@Query("select p.id from Product p where p.id in (?1) and p.active = true and (p.stockQuantity - (p.orderQuantity + p.loss)) > 0 and p.pricePoints > ?2 and p.classifier = 0 and p.orderableProduct = 1")
	public List<Long> findPromoterProductIdsByCriteria(Set<Long> availableProductIds, Integer userBalance);

	@Query("select p.id from Product p where p.active = true and p.classifier = 0")
	public List<Long> findAllActiveNormalProductIds();
	
}

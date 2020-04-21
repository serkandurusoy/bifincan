package com.dna.bifincan.repository.products;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.products.ProductCategoryLevelThree;
import com.dna.bifincan.model.products.ProductCategoryLevelTwo;

public interface ProductCategoryLevelThreeRepository extends JpaRepository<ProductCategoryLevelThree, Long> {
	@Query("select count(o) from ProductCategoryLevelThree o where o.name = ?1 and o.parent.id = ?3 and o.parent.parent.id = ?4  and o.id <> ?2")
	public Long countByName(String name, Long id, Long parentCat2Id, Long parentCat1Id);
	
	@Query("select count(o) from ProductCategoryLevelThree o where o.parent.id = :categoryId")
	public Long countByProductCategoryLevelTwo(@Param("categoryId")Long categoryId);
	
	@Query("select o.id from ProductCategoryLevelThree o where o in (?1)")
	public List<Long> findIdsByEntities(List<ProductCategoryLevelThree> categories);
	
	@Query("select o.id from ProductCategoryLevelThree o where o in (?1) order by o.prioritized desc")
	public List<Long> findOrderedIdsByEntities(List<ProductCategoryLevelThree> categories);	
	
	@Query("select o.id from ProductCategoryLevelThree o where o in (?1) and o.prioritized = ?2")
	public List<Long> findIdsByEntitiesAndPriority(List<ProductCategoryLevelThree> categories, boolean prioritized);
	
    public List<ProductCategoryLevelThree> findAll(Sort sort);
    
	@Query("select o.categories from Brand o where o.id = ?1")
	public List<ProductCategoryLevelThree> findIdsByBrandId(Long brandId);	 
	
}

package com.dna.bifincan.repository.products;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.products.ProductCategoryLevelOne;
import com.dna.bifincan.model.products.ProductCategoryLevelTwo;

public interface ProductCategoryLevelTwoRepository extends JpaRepository<ProductCategoryLevelTwo, Long> {
	@Query("select count(o) from ProductCategoryLevelTwo o where o.parent.id = :categoryId")
	public Long countByProductCategoryLevelOne(@Param("categoryId")Long categoryId);
	
	@Query("select count(o) from ProductCategoryLevelTwo o where o.name = ?1 and o.parent.id = ?3 and o.id <> ?2")
	public Long countByName(String name, Long id, Long parentCat1);
	
    public List<ProductCategoryLevelTwo> findAll(Sort sort);
}

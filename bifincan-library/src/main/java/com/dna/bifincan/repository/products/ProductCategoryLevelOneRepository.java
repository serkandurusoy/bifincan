package com.dna.bifincan.repository.products;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.address.Country;
import com.dna.bifincan.model.products.ProductCategoryLevelOne;

public interface ProductCategoryLevelOneRepository extends JpaRepository<ProductCategoryLevelOne, Long> {
	@Query("select count(o) from ProductCategoryLevelOne o where o.name = :name and o.id <> :countryId")
	public Long countByName(@Param("name")String name, @Param("countryId")Long countryId);
	
    public List<ProductCategoryLevelOne> findAll(Sort sort);
}

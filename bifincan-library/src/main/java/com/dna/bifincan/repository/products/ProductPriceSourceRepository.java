package com.dna.bifincan.repository.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.products.ProductPriceSource;

public interface ProductPriceSourceRepository extends JpaRepository<ProductPriceSource, Long> {
	
	@Query("select count(o) from ProductPriceSource o where o.name = :name and o.id <> :sourceId")
	public Long countByName(@Param("name")String name, @Param("sourceId")Long sourceId);
	
}

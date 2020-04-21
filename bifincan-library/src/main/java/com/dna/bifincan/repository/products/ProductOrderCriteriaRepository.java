package com.dna.bifincan.repository.products;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.products.ProductOrderCriteria;
import com.dna.bifincan.model.user.User;

public interface ProductOrderCriteriaRepository extends JpaRepository<ProductOrderCriteria, Long> {
	@Query("select po from ProductOrderCriteria po order by po.product.id") 
	public List<ProductOrderCriteria> findAllOrderedByProduct();	
}

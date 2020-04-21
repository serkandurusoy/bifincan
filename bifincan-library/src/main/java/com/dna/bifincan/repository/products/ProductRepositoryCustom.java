package com.dna.bifincan.repository.products;

import java.util.List;

import com.dna.bifincan.model.products.Product;
import java.math.BigDecimal;

public interface ProductRepositoryCustom {

	public List<Product> findLatestActiveProducts(int maxResult);
        
        public BigDecimal sumMoneyValueOfAllProducts();

}

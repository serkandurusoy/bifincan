package com.dna.bifincan.admin.product;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.Page;

import com.dna.bifincan.model.address.City;
import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.service.AddressService;
import com.dna.bifincan.service.ProductService;

public class ProductDataModel extends LazyDataModel<Product> {
	private static final long serialVersionUID = 7444626998609941813L;
	
	private ProductService productService;

	public ProductDataModel(ProductService productService) {
		this.productService = productService;
	}

	@Override
	public List<Product> load(int first, int pageSize, String string, SortOrder so,
			Map<String, String> map) {
		Page<Product> pages= productService.findProducts(first / pageSize, pageSize);
		this.setRowCount((int)pages.getTotalElements());
		return pages.getContent();
	}

	@Override
	public Product getRowData(String rowKey) {
		return productService.findById(Long.valueOf(rowKey));
	}

	@Override
	public Object getRowKey(Product object) {
		return object.getId();
	}
	
}

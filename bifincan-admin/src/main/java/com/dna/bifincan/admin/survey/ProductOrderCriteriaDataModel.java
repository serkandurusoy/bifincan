package com.dna.bifincan.admin.survey;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.Page;

import com.dna.bifincan.model.products.ProductOrderCriteria;
import com.dna.bifincan.service.ProductService;

public class ProductOrderCriteriaDataModel extends LazyDataModel<ProductOrderCriteria> {
	private static final long serialVersionUID = -3461418299017221917L;
	
	private ProductService productService;

	public ProductOrderCriteriaDataModel(ProductService productService) {
		this.productService = productService;
	}

	@Override
	public List<ProductOrderCriteria> load(int first, int pageSize,
			String sortField, SortOrder sortOrder, Map<String, String> filters) {
		Page<ProductOrderCriteria> pages= productService.findProductOrderCriteria(first / pageSize, pageSize);
		this.setRowCount((int)pages.getTotalElements());
		return pages.getContent();
	}

	@Override
	public ProductOrderCriteria getRowData(String rowKey) {
		return productService.findProductOrderCriterionById(Long.valueOf(rowKey));
	}

	@Override
	public Object getRowKey(ProductOrderCriteria object) {
		return object.getId();
	}
	
}

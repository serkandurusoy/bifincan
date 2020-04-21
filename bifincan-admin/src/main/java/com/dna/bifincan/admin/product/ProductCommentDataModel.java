/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.admin.product;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.Page;

import com.dna.bifincan.model.address.Area;
import com.dna.bifincan.model.products.ProductComment;
import com.dna.bifincan.service.ProductService;

/**
 *
 * @author hantsy
 */
public class ProductCommentDataModel extends LazyDataModel<ProductComment> {

    private ProductService productService;

    public ProductCommentDataModel(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public List<ProductComment> load(int first, int pageSize, String string, SortOrder so,
            Map<String, String> map) {
        Page<ProductComment> pages = productService.findComments(first / pageSize, pageSize);
        this.setRowCount((int) pages.getTotalElements());
        return pages.getContent();
    }

    @Override
    public ProductComment getRowData(String rowKey) {
        return productService.findComment(Long.valueOf(rowKey));
    }

    @Override
    public Object getRowKey(ProductComment object) {
        return object.getId();
    }
}

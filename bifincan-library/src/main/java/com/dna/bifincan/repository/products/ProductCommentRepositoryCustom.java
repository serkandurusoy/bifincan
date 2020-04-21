/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.repository.products;

import com.dna.bifincan.model.products.ProductComment;
import java.util.List;

/**
 *
 * @author hantsy
 */
public interface ProductCommentRepositoryCustom {

    List<ProductComment> findRecentComments();
    
}

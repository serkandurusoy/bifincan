/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.admin.rating;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.Page;

import com.dna.bifincan.model.blog.BlogComment;
import com.dna.bifincan.model.rating.Rating;
import com.dna.bifincan.service.BlogService;
import com.dna.bifincan.service.RatingService;

/**
 *
 * @author hantsy
 */
public class RatingDataModel extends LazyDataModel<Rating> {
	private static final long serialVersionUID = 4834701786075664267L;
	
	private RatingService ratingService;

    public RatingDataModel(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Override
    public List<Rating> load(int first, int pageSize, String string, SortOrder so,
            Map<String, String> map) {
        Page<Rating> pages = ratingService.findRatings(first / pageSize, pageSize);
        this.setRowCount((int) pages.getTotalElements());
        return pages.getContent();
    }

    @Override
    public Rating getRowData(String rowKey) {
        return ratingService.findRating(Long.valueOf(rowKey));
    }

    @Override
    public Object getRowKey(Rating object) {
        return object.getId();
    }
}

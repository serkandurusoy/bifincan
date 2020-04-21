/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.admin.blog;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.Page;

import com.dna.bifincan.model.blog.BlogPost;
import com.dna.bifincan.service.BlogService;

/**
 * 
 * @author hantsy
 */
public class BlogPostDataModel extends LazyDataModel<BlogPost> {

	private BlogService blogService;

	public BlogPostDataModel(BlogService blogService) {
		this.blogService=blogService;
	}

	@Override
	public List<BlogPost> load(int first, int pageSize, String string, SortOrder so,
			Map<String, String> map) {
		Page<BlogPost> pages= blogService.findPosts(first / pageSize, pageSize);
		this.setRowCount((int)pages.getTotalElements());
		return pages.getContent();
	}

	@Override
	public BlogPost getRowData(String rowKey) {
		return blogService.findPost(Long.valueOf(rowKey));
	}

	public Object getRowKey(BlogPost object) {
		return object.getId();
	}
	
}

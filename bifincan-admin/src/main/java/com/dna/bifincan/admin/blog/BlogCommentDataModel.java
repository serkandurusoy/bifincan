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

import com.dna.bifincan.model.blog.BlogComment;
import com.dna.bifincan.service.BlogService;

/**
 *
 * @author hantsy
 */
public class BlogCommentDataModel extends LazyDataModel<BlogComment> {

    private BlogService blogService;

    public BlogCommentDataModel(BlogService blogService) {
        this.blogService = blogService;
    }

    @Override
    public List<BlogComment> load(int first, int pageSize, String string, SortOrder so,
            Map<String, String> map) {
        Page<BlogComment> pages = blogService.findComments(first / pageSize, pageSize);
        this.setRowCount((int) pages.getTotalElements());
        return pages.getContent();
    }

    @Override
    public BlogComment getRowData(String rowKey) {
        return blogService.findComment(Long.valueOf(rowKey));
    }

    @Override
    public Object getRowKey(BlogComment object) {
        return object.getId();
    }
}

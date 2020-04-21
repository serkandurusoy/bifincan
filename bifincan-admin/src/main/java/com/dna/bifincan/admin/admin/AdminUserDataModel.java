/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.admin.admin;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.Page;

import com.dna.bifincan.model.user.AdminUser;
import com.dna.bifincan.service.AdminUserService;

/**
 *
 * @author hantsy
 */
public class AdminUserDataModel extends LazyDataModel<AdminUser> {

    private AdminUserService userService;

    public AdminUserDataModel(AdminUserService userService) {
        this.userService = userService;
    }

    @Override
    public List<AdminUser> load(int first, int pageSize, String string, SortOrder so,
            Map<String, String> map) {
        Page<AdminUser> pages = userService.findUsers(first / pageSize, pageSize);
        this.setRowCount((int) pages.getTotalElements());
        return pages.getContent();
    }

    @Override
    public AdminUser getRowData(String rowKey) {
        return userService.findUser(Long.valueOf(rowKey));
    }

    @Override
    public Object getRowKey(AdminUser object) {
        return object.getId();
    }
}

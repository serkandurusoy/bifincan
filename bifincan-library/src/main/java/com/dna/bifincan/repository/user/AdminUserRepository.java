package com.dna.bifincan.repository.user;

import com.dna.bifincan.model.user.AdminUser;
import org.springframework.data.repository.query.Param;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface AdminUserRepository extends PagingAndSortingRepository<AdminUser, Long> {

    public AdminUser findByUsername(@Param("username")
    String username);

    public AdminUser findByEmail(@Param("email")
    String email);
    
}

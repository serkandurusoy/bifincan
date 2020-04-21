package com.dna.bifincan.service;

import com.dna.bifincan.model.user.AdminUser;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dna.bifincan.repository.user.AdminUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
@Named("adminUserService")
public class AdminUserService {

    @SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(AdminUserService.class);

    @Inject
    private AdminUserRepository userRepository;

    public AdminUser findUserByUsername(String _username) {
        return this.userRepository.findByUsername(_username);
    }

    public AdminUser findUserByEmail(String _email) {
        return this.userRepository.findByEmail(_email);
    }

    @Transactional
    public void saveUser(AdminUser user) {
        this.userRepository.save(user);
    }

    @Transactional
    public void deleteUser(AdminUser user) {
        this.userRepository.delete(user);
    }

    public Page<AdminUser> findUsers(int page, int pageSize) {
        Sort defaultSort = new Sort(new Sort.Order(Sort.Direction.DESC, "createDate"));
        Pageable cond = new PageRequest(page, pageSize, defaultSort);
        return userRepository.findAll(cond);
    }

    public AdminUser findUser(Long valueOf) {
        return userRepository.findOne(valueOf);
    }
}

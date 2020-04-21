package com.dna.bifincan.admin.security;

import com.dna.bifincan.model.user.AdminUser;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component(value = "saltSource")
public class SaltSourceImpl implements SaltSource {

    @Override
    public Object getSalt(UserDetails _user) {
	String salt =((AdminUser) _user).getSalt();
	return salt;
    }

}

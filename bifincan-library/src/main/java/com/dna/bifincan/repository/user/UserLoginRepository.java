package com.dna.bifincan.repository.user;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.user.User;
import com.dna.bifincan.model.user.UserLogin;
import java.util.Date;
import java.util.List;

public interface UserLoginRepository extends CrudRepository<UserLogin, Long> {

	@Query("select count(userLogin) from UserLogin userLogin where userLogin.user = :user and userLogin.loginTime >= :startCalendar and userLogin.loginTime <= :endCalendar")
	public Long countUserLoginsByDate(@Param("startCalendar") Date startCalendar, @Param("endCalendar") Date endCalendar,
			@Param("user") User user);
	
}

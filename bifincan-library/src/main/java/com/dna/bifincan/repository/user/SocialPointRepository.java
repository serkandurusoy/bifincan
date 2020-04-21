package com.dna.bifincan.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dna.bifincan.model.user.SocialPoint;
import com.dna.bifincan.model.user.SocialPointType;
import com.dna.bifincan.model.user.User;
import org.springframework.data.repository.query.Param;

public interface SocialPointRepository extends JpaRepository<SocialPoint, Long> {
	
	@Query("select count(s) from SocialPoint s where s.action = ?1 and s.url = ?2 and s.user = ?3")
	public Long countByParams(SocialPointType action, String url, User user); 	
        
        @Query("select count(s) from SocialPoint s where s.action = :pointType")
        public Long countByPointType(@Param("pointType") SocialPointType pointType);
}

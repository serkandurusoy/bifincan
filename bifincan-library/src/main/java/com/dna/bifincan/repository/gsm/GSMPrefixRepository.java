package com.dna.bifincan.repository.gsm;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.gsm.GsmPrefix;


public interface GSMPrefixRepository extends CrudRepository<GsmPrefix, Long> {
	public List<GsmPrefix> findAll(Sort sort);
	
	@Query("select count(o) from GsmPrefix o where o.code = :code and o.id <> :regionId")
	public Long countByCode(@Param("code")Integer code, @Param("regionId")Long regionId);		
}

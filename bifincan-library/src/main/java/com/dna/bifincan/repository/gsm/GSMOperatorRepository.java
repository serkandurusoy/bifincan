package com.dna.bifincan.repository.gsm;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.gsm.GsmOperator;


public interface GSMOperatorRepository extends CrudRepository<GsmOperator, Long> {

	public List<GsmOperator> findAll(Sort sort);
	
	@Query("select count(o) from GsmOperator o where o.name = :name and o.id <> :regionId")
	public Long countByName(@Param("name")String name, @Param("regionId")Long regionId);	
}

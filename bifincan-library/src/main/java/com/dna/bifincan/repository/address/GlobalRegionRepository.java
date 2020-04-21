package com.dna.bifincan.repository.address;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.address.GlobalRegion;

public interface GlobalRegionRepository extends CrudRepository<GlobalRegion, Long> {
	
	@Query("select count(o) from GlobalRegion o where o.name = :name and o.id <> :regionId")
	public Long countByName(@Param("name")String name, @Param("regionId")Long regionId);
	
	public List<GlobalRegion> findAll(Sort sort);
}

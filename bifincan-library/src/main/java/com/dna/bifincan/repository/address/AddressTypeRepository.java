package com.dna.bifincan.repository.address;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.address.AddressType;


public interface AddressTypeRepository extends CrudRepository<AddressType, Long> {
	public List<AddressType> findAll(Sort sort);
	
	@Query("select count(o) from AddressType o where o.name = :name and o.id <> :cityId")
	public Long countByName(@Param("name")String name, @Param("cityId")Long cityId);
}

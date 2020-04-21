package com.dna.bifincan.repository.address;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.address.LocalRegion;

public interface LocalRegionRepository extends PagingAndSortingRepository<LocalRegion, Long> {

	@Query("select count(o) from LocalRegion o where o.country.id = :regionId")
	public Long countByCountry(@Param("regionId")Long regionId);
	
	@Query("select count(o) from LocalRegion o where o.name = :name and o.id <> :regionId and o.country.id = :countryId and o.country.globalRegion.id = :globalRegionId")
	public Long countByName(@Param("name")String name, @Param("regionId")Long regionId, @Param("countryId")Long countryId, @Param("globalRegionId")Long globalRegionId);
	
    public List<LocalRegion> findAll(Sort sort);
}

package com.dna.bifincan.repository.address;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.address.Country;
import com.dna.bifincan.model.order.OrderDetails;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

public interface CountryRepository extends PagingAndSortingRepository<Country, Long> {

	@Query("select count(o) from Country o where o.globalRegion.id = :regionId")
	public Long countByGlobalRegion(@Param("regionId")Long regionId);
	
	@Query("select count(o) from Country o where o.name = :name and o.id <> :countryId and o.globalRegion.id =:globalRegionId")
	public Long countByName(@Param("name")String name, @Param("countryId")Long countryId, @Param("globalRegionId")Long globalRegionId);
	
    public List<Country> findAll(Sort sort);
}

package com.dna.bifincan.repository.address;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.dna.bifincan.model.address.County;
import com.dna.bifincan.model.address.District;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface DistrictRepository extends PagingAndSortingRepository<District, Long> {
	public List<District> findByCounty(County county, Sort sort);
	
	@Query("select count(o) from District o where o.name = :name and o.id <> :districtId and o.county.id = :countyId and o.county.city.id = :cityId and o.county.city.localRegion.id = :localRegionId and o.county.city.localRegion.country.id = :countryId and o.county.city.localRegion.country.globalRegion.id = :globalRegionId")
	public Long countByName(@Param("name")String name, @Param("districtId")Long districtId, @Param("countyId")Long countyId, @Param("cityId")Long cityId, 
			@Param("localRegionId")Long localRegionId, @Param("countryId")Long countryId, @Param("globalRegionId")Long globalRegionId);
	
	@Query("select count(o) from District o where o.county.id = :countyId")
	public Long countByCounty(@Param("countyId")Long countyId);	
}

package com.dna.bifincan.repository.address;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.dna.bifincan.model.address.City;
import com.dna.bifincan.model.address.County;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


public interface CountyRepository extends PagingAndSortingRepository<County, Long> {

	public List<County> findByCity(City city, Sort sort);

	@Query("select count(o) from County o where o.city.id = :cityId")
	public Long countByCity(@Param("cityId")Long cityId);	
	
	@Query("select count(o) from County o where o.name = :name and o.id <> :regionId and o.city.id = :cityId and o.city.localRegion.id = :localRegionId and o.city.localRegion.country.id = :countryId and o.city.localRegion.country.globalRegion.id = :globalRegionId")
	public Long countByName(@Param("name")String name, @Param("regionId")Long regionId, @Param("cityId")Long cityId, @Param("localRegionId")Long localRegionId, @Param("countryId")Long countryId, @Param("globalRegionId")Long globalRegionId);	
}

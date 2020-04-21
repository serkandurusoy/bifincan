package com.dna.bifincan.repository.address;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.dna.bifincan.model.address.City;
import com.dna.bifincan.model.survey.SurveyOption;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface CityRepository extends PagingAndSortingRepository<City, Long> {
	
	@Query("select count(o) from City o where o.name = :name and o.id <> :cityId and o.localRegion.id = :localRegionId and o.localRegion.country.id = :countryId and o.localRegion.country.globalRegion.id = :globalRegionId")
	public Long countByName(@Param("name")String name, @Param("cityId")Long cityId, @Param("localRegionId")Long localRegionId, @Param("countryId")Long countryId, @Param("globalRegionId")Long globalRegionId);
	
	@Query("select count(o) from City o where o.localRegion.id = :regionId")
	public Long countByLocalRegion(@Param("regionId")Long regionId);
	
	public List<City> findAll(Sort sort);
	
	@Query("select o from City o where o.name like :name")
	public List<City> findCitiesByName(@Param("name")String name);	
}

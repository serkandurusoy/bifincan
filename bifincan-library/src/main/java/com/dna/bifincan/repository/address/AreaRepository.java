package com.dna.bifincan.repository.address;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.address.Area;
import com.dna.bifincan.model.address.City;
import com.dna.bifincan.model.address.District;

public interface AreaRepository extends PagingAndSortingRepository<Area, Long>, AreaRepositoryCustom {

	public List<Area> findByDistrict(District district, Sort sort);

	@Query("select count(o) from Area o where o.name = :name and o.id <> :areaId and o.district.id = :districtId and o.district.county.id = :countyId and o.district.county.city.id = :cityId and o.district.county.city.localRegion.id = :localRegionId and o.district.county.city.localRegion.country.id = :countryId and o.district.county.city.localRegion.country.globalRegion.id = :globalRegionId")
	public Long countByName(@Param("name")String name, @Param("areaId")Long areaId, @Param("districtId")Long districtId, @Param("countyId")Long countyId, @Param("cityId")Long cityId, 
			@Param("localRegionId")Long localRegionId, @Param("countryId")Long countryId, @Param("globalRegionId")Long globalRegionId);
	
	@Query("select count(o) from Area o where o.district.id = :districtId")
	public Long countByDistrict(@Param("districtId")Long districtId);	
	
}

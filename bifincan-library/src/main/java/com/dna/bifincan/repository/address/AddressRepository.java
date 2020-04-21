package com.dna.bifincan.repository.address;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.address.Address;
import com.dna.bifincan.model.address.City;

public interface AddressRepository extends CrudRepository<Address, Long> {

	@Query("select count(o) from Address o where o.area.id = :areaId")
	public Long countByArea(@Param("areaId")Long areaId);
	
	@Query("select count(o) from Address o where o.addressType.id = :typeId")
	public Long countByAddressType(@Param("typeId")Long typeId);

	@Query("select count(o) from Address o where o.user.id = :userId and o.primary = true")
	public Long countPrimaryByUserId(@Param("userId")Long userId);	
	
	@Query("select o from Address o where o.user.id = :userId")
	public List<Address> findAddressByUserId(@Param("userId")Long userId);	
	
	@Query("select o from Address o where o.user.username = :username")
	public List<Address> findAddressByUsername(@Param("username")String username);	
        
        @Query("select count(a) from Address a where a.primary = true and a.user.active = true and a.area.district.county.city = :city")
        public Long countPrimaryByCityForActiveUsers(@Param("city") City city);
}

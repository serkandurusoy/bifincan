package com.dna.bifincan.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dna.bifincan.model.address.Address;
import com.dna.bifincan.model.address.AddressType;
import com.dna.bifincan.model.address.Area;
import com.dna.bifincan.model.address.City;
import com.dna.bifincan.model.address.Country;
import com.dna.bifincan.model.address.County;
import com.dna.bifincan.model.address.District;
import com.dna.bifincan.model.address.GlobalRegion;
import com.dna.bifincan.model.address.LocalRegion;
import com.dna.bifincan.repository.address.AddressRepository;
import com.dna.bifincan.repository.address.AddressTypeRepository;
import com.dna.bifincan.repository.address.AreaRepository;
import com.dna.bifincan.repository.address.CityRepository;
import com.dna.bifincan.repository.address.CountryRepository;
import com.dna.bifincan.repository.address.CountyRepository;
import com.dna.bifincan.repository.address.DistrictRepository;
import com.dna.bifincan.repository.address.GlobalRegionRepository;
import com.dna.bifincan.repository.address.LocalRegionRepository;

@Service
@Named("addressService")
@Transactional
public class AddressService  {
	
    @Inject
    private AddressRepository addressRepository;
    @Inject
    private AreaRepository areaRepository;
    @Inject
    private AddressTypeRepository addressTypeRepository;
    @Inject
    private CityRepository cityRepository;
    @Inject
    private CountyRepository countyRepository;
    @Inject
    private DistrictRepository districtRepository;
    @Inject
    private GlobalRegionRepository globalRegionRepository;
    @Inject
    private CountryRepository countryRepository;
    @Inject
    private LocalRegionRepository localRegionRepository;

    /**
     * Saves a country, but first checks for duplicate name.
     * @param region
     * @return  true for success or false for failure 
     */    
    public boolean saveCountry(Country country) {
    	Long total = countryRepository.countByName(country.getName(), 
    			country.getId() != null ? country.getId() : 0, country.getGlobalRegion().getId());
    	if(total == 0) {
    		this.countryRepository.save(country);
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public Page<Country> findCountries(int first, int MaxResult) {
        Pageable cond = new PageRequest(first, MaxResult);
        return countryRepository.findAll(cond);
    }

    public List<Country> findCountries() {
        List<Order> sortOrders = new ArrayList<Order>();

        Order order = new Order(Direction.ASC, "name");
        sortOrders.add(order);

        Sort sort = new Sort(sortOrders);
        return this.countryRepository.findAll(sort);
    }
    
    public Country findCountry(Long id) {
        return countryRepository.findOne(id);
    }

    public List<AddressType> getAddressTypes() {
        List<Order> sortOrders = new ArrayList<Order>();

        Order order = new Order(Direction.ASC, "name");
        sortOrders.add(order);

        Sort sort = new Sort(sortOrders);
        return this.addressTypeRepository.findAll(sort);
    }

    public List<City> getCities() {
        List<Order> sortOrders = new ArrayList<Order>();

        Order order = new Order(Direction.ASC, "name");
        sortOrders.add(order);

        Sort sort = new Sort(sortOrders);
        return this.cityRepository.findAll(sort);
    }

    public List<County> getCounties() {
        List<Order> sortOrders = new ArrayList<Order>();

        Order order = new Order(Direction.ASC, "name");
        sortOrders.add(order);

        Sort sort = new Sort(sortOrders);
        return (List<County>) this.countyRepository.findAll(sort);
    }
    
     public List<District> getDistricts() {
        List<Order> sortOrders = new ArrayList<Order>();

        Order order = new Order(Direction.ASC, "name");
        sortOrders.add(order);

        Sort sort = new Sort(sortOrders);
        return (List<District>) this.districtRepository.findAll(sort);
    }

    public List<County> findCountiesByCity(City city) {
        List<Order> sortOrders = new ArrayList<Order>();

        Order order = new Order(Direction.ASC, "name");
        sortOrders.add(order);

        Sort sort = new Sort(sortOrders);
        return this.countyRepository.findByCity(city, sort);
    }

    public List<District> findDistrictsByCounty(County county) {
        List<Order> sortOrders = new ArrayList<Order>();

        Order order = new Order(Direction.ASC, "name");
        sortOrders.add(order);

        Sort sort = new Sort(sortOrders);
        return this.districtRepository.findByCounty(county, sort);
    }

    public List<Area> findAreasByDistrict(District district) {
        List<Order> sortOrders = new ArrayList<Order>();

        Order order = new Order(Direction.ASC, "name");
        sortOrders.add(order);

        Sort sort = new Sort(sortOrders);
        return this.areaRepository.findByDistrict(district, sort);
    }

    public List<Area> findAreasByCityIdAndKeyword(Long cityId, String keyword) {
        return this.areaRepository.findAreasByCityIdAndKeyword(cityId, keyword);  
    }
    
    public List<GlobalRegion> getGlobalRegions() {
        List<Order> sortOrders = new ArrayList<Order>();

        Order order = new Order(Direction.ASC, "name");
        sortOrders.add(order);

        Sort sort = new Sort(sortOrders);
        return this.globalRegionRepository.findAll(sort);
    }

    /**
     * Saves a global region, but first checks for duplicate name.
     * @param region
     * @return  true for success or false for failure 
     */    
    public boolean saveGlobalRegion(GlobalRegion region) {
    	Long total = globalRegionRepository.countByName(region.getName(), 
    			region.getId() != null ? region.getId() : 0);
    	if(total == 0) {
    		this.globalRegionRepository.save(region);
    		return true;
    	} else {
    		return false;
    	}
    }

    public List<Country> getCountries() {
        List<Order> sortOrders = new ArrayList<Order>();

        Order order = new Order(Direction.ASC, "name");
        sortOrders.add(order);

        Sort sort = new Sort(sortOrders);
        return this.countryRepository.findAll(sort);
    }

    public List<LocalRegion> getLocalRegions() {
        List<Order> sortOrders = new ArrayList<Order>();

        Order order = new Order(Direction.ASC, "name");
        sortOrders.add(order);

        Sort sort = new Sort(sortOrders);
        return this.localRegionRepository.findAll(sort);
    }

    public Page<LocalRegion> findLocalRegions(int first, int pageSize) {
        Pageable cond = new PageRequest(first, pageSize);
        return localRegionRepository.findAll(cond);
    }

    
    /**
     * Saves a local region, but first checks for duplicate name.
     * @param region
     * @return  true for success or false for failure 
     */    
    public boolean saveLocalRegion(LocalRegion region) {
    	Long total = localRegionRepository.countByName(region.getName(), 
    			region.getId() != null ? region.getId() : 0, 
    					region.getCountry().getId(), region.getCountry().getGlobalRegion().getId());
    	if(total == 0) {
    		this.localRegionRepository.save(region);
    		return true;
    	} else {
    		return false;
    	}
    }
    
    /**
     * Saves a county, but first checks for duplicate name.
     * @param county
     * @return  true for success or false for failure 
     */    
    public boolean saveCounty(County county) {
    	Long total = countyRepository.countByName(county.getName(), 
    			county.getId() != null ? county.getId() : 0, county.getCity().getId(), 
    					county.getCity().getLocalRegion().getId(), 
    					county.getCity().getLocalRegion().getCountry().getId(), 
    					county.getCity().getLocalRegion().getCountry().getGlobalRegion().getId());
    	if(total == 0) {
    		this.countyRepository.save(county);
    		return true;
    	} else {
    		return false;
    	}
    }
    
    /**
     * Saves an area, but first checks for duplicate name.
     * @param county
     * @return  true for success or false for failure 
     */    
    public boolean saveArea(Area area) {
    	Long total = areaRepository.countByName(area.getName(), 
    			area.getId() != null ? area.getId() : 0,
    					area.getDistrict().getId(),
    					area.getDistrict().getCounty().getId(), 
    					area.getDistrict().getCounty().getCity().getId(), 
    					area.getDistrict().getCounty().getCity().getLocalRegion().getId(), 
    					area.getDistrict().getCounty().getCity().getLocalRegion().getCountry().getId(), 
    					area.getDistrict().getCounty().getCity().getLocalRegion().getCountry().getGlobalRegion().getId());
    	if(total == 0) {
    		this.areaRepository.save(area);
    		return true;
    	} else {
    		return false;
    	}
    }
    
    /**
     * Saves a district, but first checks for duplicate name.
     * @param district
     * @return  true for success or false for failure 
     */    
    public boolean saveDistrict(District district) {
    	Long total = districtRepository.countByName(district.getName(), 
    			district.getId() != null ? district.getId() : 0, 
    					district.getCounty().getId(), 
    					district.getCounty().getCity().getId(), 
    					district.getCounty().getCity().getLocalRegion().getId(), 
    					district.getCounty().getCity().getLocalRegion().getCountry().getId(), 
    					district.getCounty().getCity().getLocalRegion().getCountry().getGlobalRegion().getId());
    	if(total == 0) {
    		this.districtRepository.save(district);
    		return true;
    	} else {
    		return false;
    	}
    }
    
    /**
     * Saves an address type, but first checks for duplicate name.
     * @param type
     * @return  true for success or false for failure 
     */    
    public boolean saveAddressType(AddressType type) {
    	Long total = addressTypeRepository.countByName(type.getName(), 
    			type.getId() != null ? type.getId() : 0);
    	if(total == 0) {
    		this.addressTypeRepository.save(type);
    		return true;
    	} else {
    		return false;
    	}
    }
      
    public LocalRegion findLocalRegion(Long id) {
        return localRegionRepository.findOne(id);
    }

    public Page<City> findCities(int first, int pageSize) {
        Pageable cond = new PageRequest(first, pageSize);
        return cityRepository.findAll(cond);
    }

    /**
     * Saves a city, but first checks for duplicate name.
     * @param region
     * @return  true for success or false for failure 
     */    
    public boolean saveCity(City city) {
    	Long total = cityRepository.countByName(city.getName(), 
    			city.getId() != null ? city.getId() : 0, city.getLocalRegion().getId(), 
    					city.getLocalRegion().getCountry().getId(), 
    					city.getLocalRegion().getCountry().getGlobalRegion().getId());
    	if(total == 0) {
    		this.cityRepository.save(city);
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public City findCity(Long id) {
        return cityRepository.findOne(id);
    }

    public List<Address> findAddressesByUserId(Long userId) {
        return addressRepository.findAddressByUserId(userId);
    }
    
    public List<Address> findAddressesByUsername(String username) {
        return addressRepository.findAddressByUsername(username);
    }
    
    public Page<County> findCounties(int first, int pageSize) {
        Pageable cond = new PageRequest(first, pageSize);
        return countyRepository.findAll(cond);
    }

    public County findCounty(Long id) {
        return countyRepository.findOne(id);
    }

    public Page<District> findDistricts(int first, int pageSize) {
        Pageable cond = new PageRequest(first, pageSize);
        return districtRepository.findAll(cond);
    }

    public District findDistrict(Long id) {
        return districtRepository.findOne(id);
    }
    
     public Page<Area> findAreas(int first, int pageSize) {
        Pageable cond = new PageRequest(first, pageSize);
        return areaRepository.findAll(cond);
    }

    public Area findArea(Long id) {
        return areaRepository.findOne(id);
    }
    
    /**
     * Deletes a global region, but first checks for existing dependencies.
     * @param region
     * @return  true for success or false for failure (ONLY due to dependencies)
     */
    public boolean deleteGlobalRegion(GlobalRegion region) {
    	Long total = countryRepository.countByGlobalRegion(region.getId());
    	if(total == 0) {
    		// get a fresh copy of the entity
    		GlobalRegion tempRegion = this.globalRegionRepository.findOne(region.getId());    		
    		this.globalRegionRepository.delete(tempRegion);
    		return true;
    	} else {
    		return false;
    	}
    }
    
    
    /**
     * Deletes a country, but first checks for existing dependencies.
     * @param country
     * @return  true for success or false for failure (ONLY due to dependencies)
     */
    public boolean deleteCountry(Country country) {
    	Long total = localRegionRepository.countByCountry(country.getId());
    	if(total == 0) {
    		// get a fresh copy of the entity
    		Country tempCountry = this.countryRepository.findOne(country.getId());
    		this.countryRepository.delete(tempCountry);
    		return true;
    	} else {
    		return false;
    	}
    }
    
    /**
     * Deletes a local region, but first checks for existing dependencies.
     * @param local region
     * @return  true for success or false for failure (ONLY due to dependencies)
     */
    public boolean deleteLocalRegion(LocalRegion region) {
    	Long total = cityRepository.countByLocalRegion(region.getId());
    	if(total == 0) {
    		// get a fresh copy of the entity
    		LocalRegion tempRegion = this.localRegionRepository.findOne(region.getId());
    		this.localRegionRepository.delete(tempRegion);
    		return true;
    	} else {
    		return false;
    	}
    }
    
    /**
     * Deletes a city, but first checks for existing dependencies.
     * @param local city
     * @return  true for success or false for failure (ONLY due to dependencies)
     */
    public boolean deleteCity(City city) {
    	Long total = countyRepository.countByCity(city.getId());
    	if(total == 0) {
    		// get a fresh copy of the entity
    		City tempCity = this.cityRepository.findOne(city.getId());
    		this.cityRepository.delete(tempCity);
    		return true;
    	} else {
    		return false;
    	}
    }   
    
    /**
     * Deletes a county, but first checks for existing dependencies.
     * @param local county
     * @return  true for success or false for failure (ONLY due to dependencies)
     */
    public boolean deleteCounty(County county) {
    	Long total = districtRepository.countByCounty(county.getId());
    	if(total == 0) {
    		// get a fresh copy of the entity
    		County tempCounty = this.countyRepository.findOne(county.getId());
    		this.countyRepository.delete(tempCounty);
    		return true;
    	} else {
    		return false;
    	}
    }  
    
    /**
     * Deletes a district, but first checks for existing dependencies.
     * @param local district
     * @return  true for success or false for failure (ONLY due to dependencies)
     */
    public boolean deleteDistrict(District district) {
    	Long total = areaRepository.countByDistrict(district.getId());
    	if(total == 0) {
    		// get a fresh copy of the entity
    		District tempDistrict = this.districtRepository.findOne(district.getId());
    		this.districtRepository.delete(tempDistrict);
    		return true;
    	} else {
    		return false;
    	}
    }     
    
    /**
     * Deletes a area, but first checks for existing dependencies.
     * @param local area
     * @return  true for success or false for failure (ONLY due to dependencies)
     */
    public boolean deleteArea(Area area) {
    	Long total = addressRepository.countByArea(area.getId());
    	if(total == 0) {
    		// get a fresh copy of the entity
    		Area tempArea = this.areaRepository.findOne(area.getId());
    		this.areaRepository.delete(tempArea);
    		return true;
    	} else {
    		return false;
    	}
    }    
    
    /**
     * Deletes an address type, but first checks for existing dependencies.
     * @param local type
     * @return  true for success or false for failure (ONLY due to dependencies)
     */
    public boolean deleteAddressType(AddressType type) {
    	Long total = addressRepository.countByAddressType(type.getId());
    	if(total == 0) {
    		// get a fresh copy of the entity
    		AddressType tempType = this.addressTypeRepository.findOne(type.getId());
    		this.addressTypeRepository.delete(tempType);
    		return true;
    	} else {
    		return false;
    	}
    } 
    
	@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
    public List<City> findCitiesByName(String name) {
		return cityRepository.findCitiesByName("%" + name + "%");
    }
	
	@Transactional(propagation=Propagation.REQUIRED)
    public boolean makeAddressPrimary(Long addressId, String username) {		
		Address address = addressRepository.findOne(addressId);
		
		if(address != null) {
			// find all the addresses and reset their primary values to false
			List<Address> addresses = addressRepository.findAddressByUsername(username);
			if(addresses != null && !addresses.isEmpty()) {
				for(Address tAddress : addresses) {
					tAddress.setPrimary(false);
					addressRepository.save(tAddress);
				}
			}
			
			// set now the selected address as primary
	    	address.setPrimary(true);
	    	addressRepository.save(address);
    		return true;
		} 
		return false;
    }
    
	@Transactional(propagation=Propagation.REQUIRED)
    public boolean deleteUserAddress(Long addressId) {
    	Address address = addressRepository.findOne(addressId);
    	if(!address.getPrimary()) {
    		addressRepository.delete(address);
    		return true;
    	} 
    	
    	return false;
    }	
	
	@Transactional(propagation=Propagation.SUPPORTS)
    public Address findUserAddress(Long id) {
		return addressRepository.findOne(id);
    }	
	
	@Transactional(propagation=Propagation.REQUIRED)
    public void saveOrUpdateAddress(Address address) {
    	addressRepository.save(address);
    }		
	
}

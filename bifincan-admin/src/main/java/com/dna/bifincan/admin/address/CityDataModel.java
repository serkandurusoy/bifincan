/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.admin.address;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.Page;

import com.dna.bifincan.model.address.City;
import com.dna.bifincan.service.AddressService;

/**
 * 
 * @author hantsy
 */
public class CityDataModel extends LazyDataModel<City> {

	private AddressService addressService;

	public CityDataModel(AddressService addressService) {
		this.addressService=addressService;
	}

	@Override
	public List<City> load(int first, int pageSize, String string, SortOrder so,
			Map<String, String> map) {
		Page<City> pages= addressService.findCities(first / pageSize, pageSize);
		this.setRowCount((int)pages.getTotalElements());
		return pages.getContent();
	}



	@Override
	public City getRowData(String rowKey) {
		return addressService.findCity(Long.valueOf(rowKey));
	}

	@Override
	public Object getRowKey(City object) {
		return object.getId();
	}
	
	

}

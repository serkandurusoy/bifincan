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
import com.dna.bifincan.model.address.County;
import com.dna.bifincan.service.AddressService;

/**
 * 
 * @author hantsy
 */
public class CountyDataModel extends LazyDataModel<County> {

	private AddressService addressService;

	public CountyDataModel(AddressService addressService) {
		this.addressService=addressService;
	}

	@Override
	public List<County> load(int first, int pageSize, String string, SortOrder so,
			Map<String, String> map) {
		Page<County> pages= addressService.findCounties(first / pageSize, pageSize);
		this.setRowCount((int)pages.getTotalElements());
		return pages.getContent();
	}



	@Override
	public County getRowData(String rowKey) {
		return addressService.findCounty(Long.valueOf(rowKey));
	}

	@Override
	public Object getRowKey(County object) {
		return object.getId();
	}
	
	

}

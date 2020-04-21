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

import com.dna.bifincan.model.address.Area;
import com.dna.bifincan.service.AddressService;

/**
 * 
 * @author hantsy
 */
public class AreaDataModel extends LazyDataModel<Area> {

	private AddressService addressService;

	public AreaDataModel(AddressService addressService) {
		this.addressService=addressService;
	}

	@Override
	public List<Area> load(int first, int pageSize, String string, SortOrder so,
			Map<String, String> map) {
		Page<Area> pages= addressService.findAreas(first / pageSize, pageSize);
		this.setRowCount((int)pages.getTotalElements());
		return pages.getContent();
	}



	@Override
	public Area getRowData(String rowKey) {
		return addressService.findArea(Long.valueOf(rowKey));
	}

	@Override
	public Object getRowKey(Area object) {
		return object.getId();
	}
	
	

}

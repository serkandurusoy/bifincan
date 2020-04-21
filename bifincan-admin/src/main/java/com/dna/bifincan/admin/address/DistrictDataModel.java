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

import com.dna.bifincan.model.address.District;
import com.dna.bifincan.service.AddressService;

/**
 * 
 * @author hantsy
 */
public class DistrictDataModel extends LazyDataModel<District> {
	private static final long serialVersionUID = 27955430346997563L;
	
	private AddressService addressService;

	public DistrictDataModel(AddressService addressService) {
		this.addressService=addressService;
	}

	@Override
	public List<District> load(int first, int pageSize, String string, SortOrder so,
			Map<String, String> map) {
		Page<District> pages= addressService.findDistricts(first / pageSize, pageSize);
		this.setRowCount((int)pages.getTotalElements());
		return pages.getContent();
	}

	@Override
	public District getRowData(String rowKey) {
		return addressService.findDistrict(Long.valueOf(rowKey));
	}

	@Override
	public Object getRowKey(District object) {
		return object.getId();
	}
	
	

}

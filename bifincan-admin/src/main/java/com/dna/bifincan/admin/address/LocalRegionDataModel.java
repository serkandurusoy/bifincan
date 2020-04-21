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

import com.dna.bifincan.model.address.LocalRegion;
import com.dna.bifincan.service.AddressService;

/**
 * 
 * @author hantsy
 */
public class LocalRegionDataModel extends LazyDataModel<LocalRegion> {

	private AddressService addressService;

	public LocalRegionDataModel(AddressService addressService) {
		this.addressService=addressService;
	}

	@Override
	public List<LocalRegion> load(int first, int pageSize, String string, SortOrder so,
			Map<String, String> map) {
		Page<LocalRegion> pages= addressService.findLocalRegions(first / pageSize, pageSize);
		this.setRowCount((int)pages.getTotalElements());
		return pages.getContent();
	}

	@Override
	public LocalRegion getRowData(String rowKey) {
		return addressService.findLocalRegion(Long.valueOf(rowKey));
	}

	@Override
	public Object getRowKey(LocalRegion object) {
		return object.getId();
	}
	
	

}

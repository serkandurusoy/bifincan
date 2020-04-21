package com.dna.bifincan.repository.address;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dna.bifincan.model.address.Area;

public interface AreaRepositoryCustom {

	public List<Area> findAreasByCityIdAndKeyword(Long cityId, String keyword);	
}

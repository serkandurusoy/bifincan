package com.dna.bifincan.repository.address;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.dna.bifincan.model.address.Area;

public class AreaRepositoryImpl implements AreaRepositoryCustom {   

    @PersistenceContext
    EntityManager em;

	@SuppressWarnings("unchecked")
	public List<Area> findAreasByCityIdAndKeyword(Long cityId, String keyword) {
		List<Area> areas = null;
	
		StringBuilder bf = new StringBuilder("SELECT * FROM area a left outer join district d on a.district = d.id left outer join county c on d.county = c.id  ");
		String[] words = keyword.split(" ");
		if(words != null && words.length > 0) {
			bf.append(" where c.city = ").append(cityId); // where clause for city id
			
			// where clause for area names
			bf.append(" and ((");
			for(int i=0; i<words.length; i++) {
				bf.append(i==0?"":" or ").append(" a.name like '%").append(words[i]).append("%' COLLATE utf8_general_ci ");
			}
			bf.append(") ");
			
			// where clause for district names
			bf.append(" or (");
			for(int i=0; i<words.length; i++) {
				bf.append(i==0?"":" or ").append(" d.name like '%").append(words[i]).append("%' COLLATE utf8_general_ci ");
			}
			bf.append(") ");
			
			// where clause for county names
			bf.append(" or (");
			for(int i=0; i<words.length; i++) {
				bf.append(i==0?"":" or ").append(" c.name like '%").append(words[i]).append("%' COLLATE utf8_general_ci ");
			}
			bf.append(")) ");	
			
			// order clause
			bf.append(" order by c.name asc, d.name asc, a.name asc ");
			

		}
		
		//log.debug(">>> query is " + bf.toString());
		areas = (List<Area>)em.createNativeQuery(bf.toString(), Area.class).getResultList();  
		
		return areas; 
	}
}


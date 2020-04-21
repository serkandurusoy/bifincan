package com.dna.bifincan.repository.survey;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.survey.SurveyBrandProductCategory;


public interface SurveyBrandProductCategoryRepository extends JpaRepository<SurveyBrandProductCategory, Long> {

	@Query("select count(o) from SurveyBrandProductCategory o left join o.productCategories c where c.id = :regionId")
	public Long countByProductCategoryLevelThree(@Param("regionId")Long regionId);
	
	@Query("select s.id from SurveyBrandProductCategory s left join s.productCategories pc where s.active = true and pc.prioritized = 1")
	public List<Long> findActiveByProdCatPriority();	
	
	@Query("select s.id from SurveyBrandProductCategory s left join s.productCategories pc where s.active = true and pc.prioritized = 0 and s.id not in (?1)")
	public List<Long> findActiveNotInList(List<Long> ids);	
}

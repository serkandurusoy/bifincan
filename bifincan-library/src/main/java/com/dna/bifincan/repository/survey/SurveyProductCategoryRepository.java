package com.dna.bifincan.repository.survey;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dna.bifincan.model.products.ProductCategoryLevelThree;
import com.dna.bifincan.model.survey.SurveyProductCategory;
import com.dna.bifincan.model.survey.SurveyQuestion;
import com.dna.bifincan.model.user.User;

public interface SurveyProductCategoryRepository extends JpaRepository<SurveyProductCategory, Long> {

	@Query("select s.id from SurveyProductCategory s left join s.productCategories pc where s.active = true and pc.prioritized = ?1")
	public List<Long> findActiveByProdCatPriority(boolean priority);
	
	@Query("select s from SurveyProductCategory s left join fetch s.productCategories pc where s.id = ?1")
	public SurveyProductCategory findByIdAndFetchCategories(long id);
	
	@Query("select pc.id from SurveyProductCategory s left join s.productCategories pc where s.id = ?1 and pc.id not in (?2) order by pc.prioritized desc")
	public List<Long> fetchCategoriesNotInListByPriority(Long id, List<Long> ids);
	
}

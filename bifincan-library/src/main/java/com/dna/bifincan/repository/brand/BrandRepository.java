package com.dna.bifincan.repository.brand;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.address.City;
import com.dna.bifincan.model.products.Brand;
import com.dna.bifincan.model.products.ProductCategoryLevelThree;
import com.dna.bifincan.model.survey.Survey;
import com.dna.bifincan.model.user.User;

public interface BrandRepository extends JpaRepository<Brand, Long> {
	@Query("select b.id from Brand b where b.slug = :slug")
	public Long findBrandIdBySlug(@Param("slug") String slug);
	
	@Query("select b.id from Brand b where b.id not in (?1)")
	public List<Long> fetchBrandIdsNotInGivenList(List<Long> listOfIds);

	/*
	@Query("select b.id from Brand b where b.id not in (?1) and b.prioritized = ?2 order by b.createDate desc")
	public List<Long> fetchBrandIdsNotInGivenListByPriority(List<Long> listOfIds, boolean priority);
	*/
	
	@Query("select b.id from Brand b where b.id not in (?1) and b.prioritized = ?2")
	public List<Long> fetchBrandIdsNotInGivenListByPriority(List<Long> listOfIds, boolean priority);
	
	@Query("select b.id from Brand b where b.id not in (?1) and ?2 member of b.categories")
	public List<Long> fetchBrandIdsByCategoryNotInGivenList(List<Long> listOfIds, ProductCategoryLevelThree category);

	@Query("select b.id from Brand b order by b.prioritized desc")
	public List<Long> findAllBrandIds();

	/*
	@Query("select b.id from Brand b where b.id not in (select s.brand.id from SurveyAnswer s where s.surveyQuestion.survey.id = ?1) order by b.prioritized desc, b.createDate desc")
	public List<Long> fetchNewBrandIdsByPriority(Long surveyId);
	 */
	
	@Query("select b.id from Brand b where b.prioritized = ?2 and b.id not in (select s.brand.id from SurveyAnswer s where s.surveyQuestion.survey.id = ?1)")
	public List<Long> fetchNewBrandIdsByPriority(Long surveyId, boolean prioritized);
	
	
	@Query("select b.id from Brand b where ?1 member of b.categories")
	public List<Long> findAllBrandIdsByCategory(ProductCategoryLevelThree category);	
	
	@Query("select count(o) from Brand o where o.company.id = :companyId")
	public Long countByCompany(@Param("companyId")Long companyId);	
	
	@Query("select count(o) from Brand o where o.name = :name and o.id <> :regionId")
	public Long countByName(@Param("name")String name, @Param("regionId")Long regionId);
	
	@Query("select b from Brand b left join fetch b.categories c where c in (?1)")
	public List<Brand> fetchBrandIdsByCategories(List<ProductCategoryLevelThree> categories); 
	
	@Query("select b from Brand b left join fetch b.categories c where c in (?3) and b.id not in " +
			"(select s.brand.id from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2 and s.brand is not null and s.cycle.id = ?4 and s.brand.id = b.id)")			
	public List<Brand> countNonTakenBrandSurveysByUserAndProductCategoriesAndCycleId(User user, Survey survey, List<ProductCategoryLevelThree> categories, Long cycleId);
	
	@Query("select count(s.id) from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2 and s.brand.id = ?3 and s.productCategory.id = ?4 and s.cycle.id = ?5")			
	public Long countTakenBrandSurveysByUserAndBrandAndProductCategoryAndCycleId(User user, Survey survey, Long brandId, Long categoryId, Long cycleId);
	
	@Query("select b from Brand b left join fetch b.categories c where c in (?3) and b.id not in " +
			"(select s.brand.id from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2 and s.brand is not null)")			
	public List<Brand> countNonTakenBrandSurveysByUserAndProductCategories(User user, Survey survey, List<ProductCategoryLevelThree> categories);
	
	@Query("select b from Brand b left join fetch b.categories c where c in (?3) and c.id not in " +
			"(select s.productCategory.id from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2 and s.productCategory is not null and s.brand.id = b.id)")			
	public List<Brand> countNonTakenProdCatSurveysByUserAndProductCategories(User user, Survey survey, List<ProductCategoryLevelThree> categories);

	@Query("select b from Brand b left join fetch b.categories c where c in (?3) and c.id not in " +
			"(select s.productCategory.id from SurveyAnswer s where s.user = ?1 and s.surveyQuestion.survey = ?2 and s.productCategory is not null and s.brand.id = b.id and s.cycle.id = ?4)")			
	public List<Brand> countNonTakenProdCatSurveysByUserAndProductCategoriesAndCycleId(User user, Survey survey, List<ProductCategoryLevelThree> categories, Long cycleId);
	
	@Query("select b.id from Brand b where b in (?1) order by b.prioritized desc")
	public List<Long> fetchIdsByList(List<Brand> brands);
	
	@Query("select b from Brand b left join fetch b.categories where b.id = ?1")
	public Brand findById(Long brandId);	

	@Query("select count(b.id) from Brand b where b.id = ?1 and ?2 member of b.categories")
	public Long checkCategoryMemberShip(Long brandId, ProductCategoryLevelThree category);
	
	public List<Brand> findAll(Sort sort);
}

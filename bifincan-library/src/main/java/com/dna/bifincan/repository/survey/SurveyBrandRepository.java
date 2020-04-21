package com.dna.bifincan.repository.survey;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dna.bifincan.model.survey.SurveyBrand;

public interface SurveyBrandRepository extends JpaRepository<SurveyBrand, Long> {

}

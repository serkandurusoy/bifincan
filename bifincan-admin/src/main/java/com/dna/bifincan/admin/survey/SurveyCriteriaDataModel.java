package com.dna.bifincan.admin.survey;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.Page;

import com.dna.bifincan.model.survey.SurveyCriteria;
import com.dna.bifincan.service.SurveyService;

public class SurveyCriteriaDataModel extends LazyDataModel<SurveyCriteria> {
	private static final long serialVersionUID = -3461418299017221917L;
	
	private SurveyService surveyService;

	public SurveyCriteriaDataModel(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	@Override
	public List<SurveyCriteria> load(int first, int pageSize,
			String sortField, SortOrder sortOrder, Map<String, String> filters) {
		Page<SurveyCriteria> pages= surveyService.findSurveyCriteria(first / pageSize, pageSize);
		this.setRowCount((int)pages.getTotalElements());
		return pages.getContent();
	}

	@Override
	public SurveyCriteria getRowData(String rowKey) {
		return surveyService.findSurveyCriterionById(Long.valueOf(rowKey));
	}

	@Override
	public Object getRowKey(SurveyCriteria object) {
		return object.getId();
	}
	
}

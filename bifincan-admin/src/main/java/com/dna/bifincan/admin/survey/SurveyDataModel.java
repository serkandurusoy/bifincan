package com.dna.bifincan.admin.survey;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.Page;

import com.dna.bifincan.model.survey.Survey;
import com.dna.bifincan.model.survey.SurveyProfile;
import com.dna.bifincan.model.type.SurveyType;
import com.dna.bifincan.service.SurveyService;

public class SurveyDataModel extends LazyDataModel<Survey> {
	private static final long serialVersionUID = -4307294125462257651L;
	
	private SurveyService surveyService;
	private SurveyType surveyType;
	
 	public SurveyDataModel(SurveyService surveyService, SurveyType surveyType) {
		this.surveyService = surveyService;
		this.surveyType = surveyType;
	}

	@Override
	public List<Survey> load(int first, int pageSize,
			String sortField, SortOrder sortOrder, Map<String, String> filters) {
		Page<Survey> pages = surveyService.findSurveys(first / pageSize, pageSize, surveyType);
		this.setRowCount((int)pages.getTotalElements());
		return pages.getContent();
	}

	@SuppressWarnings("unchecked")
	@Override
	public SurveyProfile getRowData(String rowKey) {
		return (SurveyProfile)surveyService.findSurveyById(Long.valueOf(rowKey));
	}

	@Override
	public Object getRowKey(Survey object) {
		return object.getId();
	}
	
}

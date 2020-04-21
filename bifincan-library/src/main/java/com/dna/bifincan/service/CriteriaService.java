package com.dna.bifincan.service;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dna.bifincan.model.config.Criterion;
import com.dna.bifincan.model.type.OperatorType;
import com.dna.bifincan.model.type.OperatorValueType;
import com.dna.bifincan.model.type.PropertyType;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.repository.user.UserRepository;

@Service
@Named("criteriaService")
public class CriteriaService {
	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(CriteriaService.class);
    
    @Inject
    private UserRepository userRepository;
    @Inject
    private OrderService orderRepository;
    
    public CriteriaService() { }
    
    public boolean evaluateCriterion(Criterion criterion, User user) {
    	boolean result = true;
    	
		String operatorValue = criterion.getOperatorValue();

		Long targetValue = criterion.getOperatorValueType() != OperatorValueType.BOOLEAN ? 
				(operatorValue != null ? Long.parseLong(operatorValue) : null) : 0L; 
		OperatorType operatorType = criterion.getOperatorType();
		
		// get the property's type
		PropertyType propertyType = criterion.getPropertyType();
		if(propertyType == PropertyType.AGE) { // for user's age property
			if(operatorType == OperatorType.GREATER_OR_EQUAL) {
				if(!(targetValue.intValue() <= user.getAge())) { 
					result = false;
				}
			} else if(operatorType == OperatorType.LESS_OR_EQUAL) {
				if(!(targetValue.intValue() >= user.getAge())) { 
					result = false;
				}
			}
		} else if(propertyType == PropertyType.ADDRESS) { // for user's address property
			if(operatorType == OperatorType.EQUAL) {
				if(userRepository.findAddressMatches(user, targetValue) == 0) {
					result = false;
				}
			} else if(operatorType == OperatorType.NOT_EQUAL) { 
				if(userRepository.findAddressNotMatches(user, targetValue) == 0) {
					result = false;
				}
			}
		} else if(propertyType == PropertyType.EDUCATION) { // for user's education property
			if(operatorType == OperatorType.EQUAL) {
				if(user.getEducationType().ordinal() != targetValue.intValue()) {
					result = false;
				}
			} else if(operatorType == OperatorType.NOT_EQUAL) { 
				if(user.getEducationType().ordinal() == targetValue.intValue()) {
					result = false;
				}
			} 
		} else if(propertyType == PropertyType.EMPLOYMENT_STATUS) { // for user's employment status property
			if(operatorType == OperatorType.EQUAL) {
				if(user.getEmploymentStatusType().ordinal() != targetValue.intValue()) {
					result = false;
				}
			} else if(operatorType == OperatorType.NOT_EQUAL) { 
				if(user.getEmploymentStatusType().ordinal() == targetValue.intValue()) {
					result = false;
				}
			} 
		} else if(propertyType == PropertyType.GENDER) { // for user's gender property
			if(operatorType == OperatorType.EQUAL) {
				if(user.getGenderType().ordinal() != targetValue.intValue()) {
					result = false;
				}
			} else if(operatorType == OperatorType.NOT_EQUAL) { 
				if(user.getGenderType().ordinal() == targetValue.intValue()) {
					result = false;
				}
			} 
		} else if(propertyType == PropertyType.HAS_CHILDREN) { // for user's has children property
			if(operatorType == OperatorType.EQUAL) {
				if(user.getHasChildrenType().ordinal() != targetValue.intValue()) {
					result = false;
				}
			} else if(operatorType == OperatorType.NOT_EQUAL) { 
				if(user.getHasChildrenType().ordinal() == targetValue.intValue()) {
					result = false;
				}
			}
		} else if(propertyType == PropertyType.MARITAL_STATUS) { // for user's marital status property
			if(operatorType == OperatorType.EQUAL) {
				if(user.getMaritalStatusType().ordinal() != targetValue.intValue()) {
					result = false;
				}
			} else if(operatorType == OperatorType.NOT_EQUAL) { 
				if(user.getMaritalStatusType().ordinal() == targetValue.intValue()) {
					result = false;
				}
			} 
		} else if(propertyType == PropertyType.MONTHLY_INCOME) { // for user's monthly income property
			if(operatorType == OperatorType.EQUAL) {
				if(user.getMonthlyNetTLIncomeType().ordinal() != targetValue.intValue()) {
					result = false;
				}
			} else if(operatorType == OperatorType.NOT_EQUAL) { 
				if(user.getMonthlyNetTLIncomeType().ordinal() == targetValue.intValue()) {
					result = false;
				}
			}
		} else if(propertyType == PropertyType.PAST_PRODUCT_POINT_VALUE) { 
			long totalPoints = orderRepository.countPointsOfOrderedProductsByUser(user.getId());

			if(operatorType == OperatorType.GREATER_OR_EQUAL) {
				if(totalPoints < targetValue.longValue()) {
					result = false;
				}
			} else if(operatorType == OperatorType.LESS_OR_EQUAL) { 
				if(totalPoints > targetValue.longValue()) {
					result = false;
				}
			}
		} else if(propertyType == PropertyType.PAST_PRODUCT_QUANTITY) { 
			long totalProducts = orderRepository.countOrderedProductsByUser(user.getId());

			if(operatorType == OperatorType.GREATER_OR_EQUAL) {
				if(totalProducts < targetValue.longValue()) {
					result = false;
				}
			} else if(operatorType == OperatorType.LESS_OR_EQUAL) { 
				if(totalProducts > targetValue.longValue()) {
					result = false;
				}
			}
		} else if(propertyType == PropertyType.ID_VERIFICATION) { 
			result = user.isIdVerified(); 
		} else if(propertyType == PropertyType.ACTIVITY_LEVEL) { 
			if(operatorType == OperatorType.GREATER_OR_EQUAL) {
				if(user.getActivityLevel() < targetValue.intValue()) {
					result = false;
				}
			} else if(operatorType == OperatorType.LESS_OR_EQUAL) { 
				if(user.getActivityLevel() > targetValue.intValue()) {
					result = false;
				}
			}
		} 
		
		return result;
    }
}

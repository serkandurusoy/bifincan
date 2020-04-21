package com.dna.bifincan.model.config;

import java.io.Serializable;

import com.dna.bifincan.model.type.OperatorType;
import com.dna.bifincan.model.type.OperatorValueType;
import com.dna.bifincan.model.type.PropertyType;

public interface Criterion extends Serializable {
	public Long getId();
	public PropertyType getPropertyType();
	public OperatorType getOperatorType();
	public OperatorValueType getOperatorValueType();
	public String getOperatorValue();	
}

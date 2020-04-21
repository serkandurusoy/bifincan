package com.dna.bifincan.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dna.bifincan.model.BaseEntity;

@Named("entityConverter")
public class EntityConverter implements Converter, Serializable {
	static Logger log = LoggerFactory.getLogger(EntityConverter.class);
	private static final long serialVersionUID = -5137676309479323480L;

	@PersistenceContext
	protected EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public Object getAsObject(FacesContext facesContext, UIComponent component, String value) throws ConverterException {
		BaseEntity entity;
		//if (log.isDebugEnabled()) {
		//    log.debug("value is:  " + value);
		//}
		if (value == null || "".equals(value)) {
			entity = null;
		} else {
			Long id = new Long(value);
			entity = (BaseEntity) entityManager.find(getClazz(facesContext, component), id);
			if (entity == null) {
				//log.debug("There is no entity with id:  " + id);
				// throw new IllegalArgumentException("There is no entity with id:  " + id);
			}
		}
		if (log.isDebugEnabled()) {
			//log.debug("converted entity is:  " + entity);
		}
		return entity;
	}

	public String getAsString(FacesContext facesContext, UIComponent component, Object value) throws ConverterException {
		if (value == null) {
			return "";
		}
		if (value instanceof String) {
			return (String) value;
		}

		if (!(value instanceof BaseEntity)) {
			throw new IllegalArgumentException("This converter only handles instances of Entity");
		}
		BaseEntity entity = (BaseEntity) value;
		return entity.getId() == null ? "" : entity.getId().toString();
	}

	@SuppressWarnings("rawtypes")
	private Class getClazz(FacesContext facesContext, UIComponent component) {
		return component.getValueExpression("value").getType(facesContext.getELContext());
	}

}
package com.dna.bifincan.admin.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dna.bifincan.model.BaseEntity;
import com.dna.bifincan.model.products.ProductCategoryLevelThree;

@Named("dualListModelConverter")
public class DualListModelConverter implements Converter, Serializable {
    static Logger log = LoggerFactory.getLogger(DualListModelConverter.class);
    private static final long serialVersionUID = -5137676309479323480L;

    @PersistenceContext
    protected EntityManager entityManager;

    @SuppressWarnings("rawtypes")
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
        Object ret = null;

        if (arg1 instanceof PickList) {
            Object dualList = ((PickList) arg1).getValue();
            DualListModel dl = (DualListModel) dualList;
            for (Object o : dl.getSource()) {
                String id = String.valueOf(((BaseEntity) o).getId());
                if (arg2.equals(id)) {
                    ret = o;
                    break;
                }
            }
            if (ret == null)
                for (Object o : dl.getTarget()) {
                    String id = String.valueOf(((BaseEntity) o).getId());
                    if (arg2.equals(id)) {
                        ret = o;
                        break;
                    }
                }
        }

        return ret;
    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
        String str = "";
        if (arg2 instanceof ProductCategoryLevelThree) {
            str = String.valueOf(((ProductCategoryLevelThree) arg2).getId());
        }
        return str;
    }

    @SuppressWarnings({ "rawtypes", "unused" })
    private Class getClazz(FacesContext facesContext, UIComponent component) {
    	return component.getValueExpression("value").getType(facesContext.getELContext());
    }

}
package com.dna.bifincan.admin.order;

import com.dna.bifincan.admin.util.FacesUtils;
import com.dna.bifincan.admin.util.WebConstants;
import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

import com.dna.bifincan.admin.util.spring.ScopeType;
import com.dna.bifincan.model.order.Order;
import com.dna.bifincan.model.order.OrderDetails;
import com.dna.bifincan.service.SurveyService;
import java.util.Calendar;
import java.util.Date;

@Named("cargoListAction")
@Scope(ScopeType.VIEW)
public class CargoListAction extends OrderAction {

    private static final long serialVersionUID = -8420576013392289987L;
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(CargoListAction.class);

    @PostConstruct
    public void initialize() {
        this.dataModel = new CargoListDataModel(orderService);
    }

    // --- Helper methods --- //
    public String getOrderItemSummary(Order order) {
        if (order.getListOfOrderDetails() != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < order.getListOfOrderDetails().size(); i++) {
                OrderDetails item = order.getListOfOrderDetails().get(i);
                sb.append(i > 0 ? ", " : "").append(item.getProduct().getBrand().getName()).append(" ").append(item.getProduct().getName()).append(" (").append(FacesUtils.getBundleKey(WebConstants.MESSAGE_BUNDLE_KEY,item.getType().getLabel())).append(")");
            }
            return sb.toString();
        }
        return "";
    }

    public boolean isWelcomeOrder(Order order) {
        if (order.getPlacedTime().getTime() - order.getAddress().getUser().getCreateDate().getTime() < 100000) {
            return true;
        }
        return false;
    }
    
    public int daysSinceSignup(Order order) {
        Date createDate = order.getAddress().getUser().getCreateDate();
        Date today = new Date();
        long diffDays = (today.getTime()-createDate.getTime())/(24*60*60*1000);
        return (int) diffDays;            
    }
    
    public String getDateStamp() {
        String year = String.format("%04d", Calendar.getInstance().get(Calendar.YEAR));
        String month = String.format("%02d", Calendar.getInstance().get(Calendar.MONTH) + 1);
        String day = String.format("%02d", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        return year + "-" + month + "-" + day;
    }
    // --- Getters and Setters --- //
}

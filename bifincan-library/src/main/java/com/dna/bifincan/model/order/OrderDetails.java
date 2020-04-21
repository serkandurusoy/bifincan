package com.dna.bifincan.model.order;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.dna.bifincan.model.BaseEntity;
import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.model.type.OrderDetailsType;

@Entity
@Table(name = "order_details")
public class OrderDetails extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -3250884923499480354L;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "order_detail_type", nullable = false, length = 32)
    @Enumerated(EnumType.ORDINAL)
    private OrderDetailsType type;

    @Column(name = "survey_completed", nullable = false, columnDefinition = "TINYINT(1) unsigned")
    private boolean surveyCompleted;

    public OrderDetails() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderDetailsType getType() {
        return type;
    }

    public void setType(OrderDetailsType type) {
        this.type = type;
    }

    public boolean isSurveyCompleted() {
        return surveyCompleted;
    }

    public void setSurveyCompleted(boolean surveyCompleted) {
        this.surveyCompleted = surveyCompleted;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((order == null) ? 0 : order.hashCode());
        result = prime * result + ((product == null) ? 0 : product.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        OrderDetails other = (OrderDetails) obj;
        if (order == null) {
            if (other.order != null) {
                return false;
            }
        } else if (!order.equals(other.order)) {
            return false;
        }
        if (product == null) {
            if (other.product != null) {
                return false;
            }
        } else if (!product.equals(other.product)) {
            return false;
        }
        if (type != other.type) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OrderDetails [order=" + order.getId() + ", id=" + getId() + "]";
    }
}

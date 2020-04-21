package com.dna.bifincan.model.rating;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotEmpty;

import com.dna.bifincan.model.BaseEntity;
import com.dna.bifincan.model.order.OrderDetails;
import com.dna.bifincan.model.user.User;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;

/**
 * he persistent class for the rating database table.
 * 
 * 
 */
@Entity
@Table(name = "rating")
public class Rating extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -3027326113865043140L;

	@Column(name = "create_time", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;

	@Column(name = "star", nullable = false, columnDefinition = "TINYINT(1) unsigned")
	private Integer star;

	@Column(name = "opinion", nullable = false, length = 255)
	@NotEmpty
        @NotNull
        @NotBlank
	private String opinion;

	@Column(name = "comment", nullable = false, columnDefinition = "text")
        @Lob
	@NotEmpty
        @NotNull
        @NotBlank
	private String comment;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "order_detail_id", nullable = false)
	private OrderDetails orderDetail;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getStar() {
		return star;
	}

	public void setStar(Integer star) {
		this.star = star;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public OrderDetails getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(OrderDetails orderDetail) {
		this.orderDetail = orderDetail;
	}

}

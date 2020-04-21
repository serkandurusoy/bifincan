package com.dna.bifincan.model.quiz;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dna.bifincan.model.BaseEntity;
import com.dna.bifincan.model.order.OrderDetails;
import com.dna.bifincan.model.user.User;

@Entity
@Table(name = "quiz_answer")
public class QuizAnswer extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -6799999150622882843L;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "quiz_option_id", nullable = false)
	private QuizOption quizOption;
	
	@ManyToOne
	@JoinColumn(name = "order_detail_id", nullable = false)
	private OrderDetails orderDetail;
	
	@Column(name = "attendance_time", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date attendanceTime;
	
	public QuizAnswer() { }

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public QuizOption getQuizOption() {
		return quizOption;
	}

	public void setQuizOption(QuizOption quizOption) {
		this.quizOption = quizOption;
	}

	public Date getAttendanceTime() {
		return attendanceTime;
	}

	public void setAttendanceTime(Date attendanceTime) {
		this.attendanceTime = attendanceTime;
	}

	public OrderDetails getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(OrderDetails orderDetail) {
		this.orderDetail = orderDetail;
	}
	
}

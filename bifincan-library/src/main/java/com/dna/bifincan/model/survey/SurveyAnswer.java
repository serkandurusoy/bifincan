package com.dna.bifincan.model.survey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.dna.bifincan.model.BaseEntity;
import com.dna.bifincan.model.products.Brand;
import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.model.products.ProductCategoryLevelThree;
import com.dna.bifincan.model.user.User;

@Entity
@Table(name = "survey_answer", uniqueConstraints = 
		@UniqueConstraint(name = "UQ_ProdSurvey", columnNames ={"user_id","survey_question_id","product_id",
				"session_id","product_category_id","brand_id"}))			
public class SurveyAnswer extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "attendance_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date attendanceTime;

    @ManyToMany
    @JoinTable(
    		name = "survey_answer_option", 
    		joinColumns = @JoinColumn(name = "survey_answer_id", nullable = false), 
    		inverseJoinColumns = @JoinColumn(name = "survey_option_id", nullable = false))
    @JoinColumn(name = "survey_option_id") 
    private List<SurveyOption> surveyOptions = new ArrayList<SurveyOption>();

    @ManyToOne
    @JoinColumn(name = "survey_question_id", nullable = false)
    private SurveyQuestion surveyQuestion;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "product_category_id")
    private ProductCategoryLevelThree productCategory;

    @Column(name = "retries", nullable = false, columnDefinition = "INT unsigned")
    private int retries = 1;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="session_id")
    private UserSession session;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "instance_id")
    private SurveyInstance instance;   
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cycle_id")
    private SurveyCycle cycle;  
    
    @Column(name = "response_time", columnDefinition = "INT unsigned", nullable = false)
    private long responseTime;
    
    @Column(name = "expected_time", columnDefinition = "INT unsigned", nullable = false)
    private long expectedTime;
    
    public SurveyAnswer() { }

    public Date getAttendanceTime() {
		return attendanceTime;
	}

	public void setAttendanceTime(Date attendanceTime) {
		this.attendanceTime = attendanceTime;
	}

	public List<SurveyOption> getSurveyOptions() {
		return surveyOptions;
	}

	public void setSurveyOptions(List<SurveyOption> surveyOptions) {
		this.surveyOptions = surveyOptions;
	}

	public SurveyQuestion getSurveyQuestion() {
		return surveyQuestion;
	}

	public void setSurveyQuestion(SurveyQuestion surveyQuestion) {
		this.surveyQuestion = surveyQuestion;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public ProductCategoryLevelThree getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategoryLevelThree productCategory) {
		this.productCategory = productCategory;
	}

	public int getRetries() {
		return retries;
	}

	public void setRetries(int retries) {
		this.retries = retries;
	}

	public UserSession getSession() {
		return session;
	}

	public void setSession(UserSession session) {
		this.session = session;
	}
	
	public SurveyInstance getInstance() {
		return instance;
	}

	public void setInstance(SurveyInstance instance) {
		this.instance = instance;
	}
	
	public SurveyCycle getCycle() {
		return cycle;
	}

	public void setCycle(SurveyCycle cycle) {
		this.cycle = cycle;
	}
	
	public long getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(long responseTime) {
		this.responseTime = responseTime;
	}
	
	public long getExpectedTime() {
		return expectedTime;
	}

	public void setExpectedTime(long expectedTime) {
		this.expectedTime = expectedTime;
	}

	@Override
    public String toString() {
	return "SurveyAnswer [attendanceTime=" + attendanceTime + ", surveyOptions=" + surveyOptions
		+ ", surveyQuestion=" + surveyQuestion + ", user=" + user.getId() + "]";
    }

}
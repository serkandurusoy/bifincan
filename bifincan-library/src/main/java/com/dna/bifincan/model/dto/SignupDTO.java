package com.dna.bifincan.model.dto;

import java.io.Serializable;
import java.util.Date;



import com.dna.bifincan.model.address.Address;
import com.dna.bifincan.model.address.Area;
import com.dna.bifincan.model.address.City;
import com.dna.bifincan.model.address.County;
import com.dna.bifincan.model.address.District;
import com.dna.bifincan.model.gsm.GsmOperator;
import com.dna.bifincan.model.gsm.GsmPrefix;
import com.dna.bifincan.model.user.SubscriptionOption;
import com.dna.bifincan.model.user.UserInvitation;

public class SignupDTO implements Serializable {
	private static final long serialVersionUID = 6401043967246579244L;
	private String username;
	private String password;
	private String confirmedPassword;
	private String firstname;
	private String lastname;
	private String dateOfBirth;
	
	private String email;
	private String confirmationKey;
	private String invitationId;
	private String invitor;
	
	private GsmOperator gsmOperator = new GsmOperator();
	private GsmPrefix gsmPrefix = new GsmPrefix();
	private String gsmNumber;
	private boolean gsmVerified;
	
	private Address address = new Address();
	private City city;
	private County county;
	private District district;
	private Area area;
	
	private SubscriptionOption subscriptionOption = new SubscriptionOption();
	private UserInvitation invitation;
	private Date parsedBirthdate;
	
	private boolean initialized = true;
	private boolean sent = false;
	
	private String action = "signup";
	private boolean valid = true;
	
	private String gender;
	private String education;
	private String employmentStatus;
	private String montlyNetIncome;
	private String maritalStatus;
	private String hasChildren;
    
	private String gsmConfirmationCode;
	private boolean editable = true;
	private int messageId;
	private String messageKey;
	
	public SignupDTO() { }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmedPassword() {
		return confirmedPassword;
	}

	public void setConfirmedPassword(String confirmedPassword) {
		this.confirmedPassword = confirmedPassword;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public GsmOperator getGsmOperator() {
		return gsmOperator;
	}

	public void setGsmOperator(GsmOperator gsmOperator) {
		this.gsmOperator = gsmOperator;
	}

	public GsmPrefix getGsmPrefix() {
		return gsmPrefix;
	}

	public void setGsmPrefix(GsmPrefix gsmPrefix) {
		this.gsmPrefix = gsmPrefix;
	}

	public String getGsmNumber() {
		return gsmNumber;
	}

	public void setGsmNumber(String gsmNumber) {
		this.gsmNumber = gsmNumber;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public County getCounty() {
		return county;
	}

	public void setCounty(County county) {
		this.county = county;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public SubscriptionOption getSubscriptionOption() {
		return subscriptionOption;
	}

	public void setSubscriptionOption(SubscriptionOption subscriptionOption) {
		this.subscriptionOption = subscriptionOption;
	}

	public UserInvitation getInvitation() {
		return invitation;
	}

	public void setInvitation(UserInvitation invitation) {
		this.invitation = invitation;
	}

	public Date getParsedBirthdate() {
		return parsedBirthdate;
	}

	public void setParsedBirthdate(Date parsedBirthdate) {
		this.parsedBirthdate = parsedBirthdate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getConfirmationKey() {
		return confirmationKey;
	}

	public void setConfirmationKey(String confirmationKey) {
		this.confirmationKey = confirmationKey;
	}

	public String getInvitationId() {
		return invitationId;
	}

	public void setInvitationId(String invitationId) {
		this.invitationId = invitationId;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getEmploymentStatus() {
		return employmentStatus;
	}

	public void setEmploymentStatus(String employmentStatus) {
		this.employmentStatus = employmentStatus;
	}

	public String getMontlyNetIncome() {
		return montlyNetIncome;
	}

	public void setMontlyNetIncome(String montlyNetIncome) {
		this.montlyNetIncome = montlyNetIncome;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(String hasChildren) {
		this.hasChildren = hasChildren;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getGsmConfirmationCode() {
		return gsmConfirmationCode;
	}

	public void setGsmConfirmationCode(String gsmConfirmationCode) {
		this.gsmConfirmationCode = gsmConfirmationCode;
	}

	public boolean isGsmVerified() {
		return gsmVerified;
	}

	public void setGsmVerified(boolean gsmVerified) {
		this.gsmVerified = gsmVerified;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public String getInvitor() {
		return invitor;
	}

	public void setInvitor(String invitor) {
		this.invitor = invitor;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}
		
}

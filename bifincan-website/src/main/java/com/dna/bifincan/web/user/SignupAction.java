package com.dna.bifincan.web.user;

import com.dna.bifincan.exception.ConfigurationException;
import com.dna.bifincan.exception.NoWelcomeProductsException;
import com.dna.bifincan.exception.OrderProcessException;
import com.dna.bifincan.model.address.*;
import com.dna.bifincan.model.config.Configuration;
import com.dna.bifincan.model.dto.SignupDTO;
import com.dna.bifincan.model.gsm.GsmOperator;
import com.dna.bifincan.model.gsm.GsmPrefix;
import com.dna.bifincan.model.type.ConfigurationType;
import com.dna.bifincan.model.user.*;
import com.dna.bifincan.model.user.verification.EmailVerification;
import com.dna.bifincan.model.user.verification.GsmVerification;
import com.dna.bifincan.service.*;
import com.dna.bifincan.util.FacesUtils;
import com.dna.bifincan.util.WebConstants;
import com.dna.bifincan.util.spring.ScopeType;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.encoding.PasswordEncoder;

@Named("signupAction")
@Scope(ScopeType.VIEW)
public class SignupAction implements Serializable {
	private static final long serialVersionUID = -5740502428943593225L;
	private static final Logger log = LoggerFactory.getLogger(SignupAction.class);

	private SignupDTO signup = new SignupDTO();

	private List<County> counties;
	private List<District> districts;
	private List<Area> areas;
	
	private Future<String> gsmConfirmationCode;
	
	@Inject
	private UserService userService;
	@Inject
	private PasswordEncoder passwordEncoder;
	@Inject
	private MailService mailService;
	@Inject
	private AddressService addressService;
	@Inject
	private GSMService gsmService;
	@Inject
	private ProductService productService;
	@Inject
	private ConfigurationService configurationService;
    
	public SignupAction() { }

	@PostConstruct
	public void initialize() { }

	public void verify() {
		if(this.signup.isValid()) {
			Map<String, String> params =  FacesUtils.getExternalContext().getRequestParameterMap();
			
			if(this.signup.getEmail() == null) this.signup.setEmail(params.get("email"));
			if(this.signup.getConfirmationKey() == null) this.signup.setConfirmationKey(params.get("confirmationkey"));
			if(this.signup.getInvitationId() == null) this.signup.setInvitationId(params.get("invitedid"));
	
			try {
				if(this.signup.getAction() != null && (isValidParam(this.signup.getEmail()) && 
						isValidParam(this.signup.getConfirmationKey()) && isValidParam(this.signup.getInvitationId())) ) {
					
					UserInvitation invitation = userService.findInvitation(Long.parseLong(this.signup.getInvitationId()));
					if (invitation == null) {
						this.signup.setMessageId(WebConstants.SIGNUP_VERIFICATION_UNKNOWN);
						this.signup.setMessageKey("invitation_notfound");
						this.signup.setAction(null);
						return;
					} else {
						this.signup.setInvitor(invitation.getInviter().getFullName());
					}
		
					if (!invitation.getEmail().equals(this.signup.getEmail())) {
						this.signup.setMessageId(WebConstants.SIGNUP_VERIFICATION_INVITATION_EMAIL_NO_MATCH);
						this.signup.setMessageKey("invitation_email_notmatch");
						this.signup.setAction(null);
						return;
					} else {
						if(userService.countByUserEmail(invitation.getEmail()) > 0) {
							this.signup.setMessageId(WebConstants.SIGNUP_VERIFICATION_EMAIL_ALREADY_IN_USE);
							this.signup.setMessageKey("invitation_email_in_use");
							this.signup.setAction(null);
							return;
						}
					}
		
					
					String key = generateConfirmationKey(this.signup.getEmail(), invitation.getInviter().getUsername());
					
					if (!this.signup.getConfirmationKey().equals(key)) {
						this.signup.setMessageId(WebConstants.SIGNUP_VERIFICATION_CONFIRMATION_KEY_NO_MATCH);
						this.signup.setMessageKey("confirmationkey_notmatch");
						this.signup.setAction(null);
						return;
					}
                                        
                                        boolean inviterActive = invitation.getInviter().isActive();
	
					if (!inviterActive) {
						this.signup.setMessageId(WebConstants.SIGNUP_INVITER_INACTIVE);
						this.signup.setMessageKey("signup.invitation_inviter_inactive");
						this.signup.setAction(null);
						return;
					}
					
                                        Long totalAcceptions = userService.countByInvitation(Long.parseLong(this.signup.getInvitationId()));
					if (totalAcceptions > 0) {
						this.signup.setMessageId(WebConstants.SIGNUP_VERIFICATION_INVITATION_ALREADY_IN_USE);
						this.signup.setMessageKey("signup.invitationURL.usedInvitation.error");
						this.signup.setAction(null);
						return;
					}
	
                                        Long inactiveInvitees = userService.countInactiveInviteesByUser(invitation.getInviter());
                                        boolean isSpecialUser = invitation.getInviter().isAmbassador() || invitation.getInviter().isFounder();
                                        if (inactiveInvitees > 1 && !isSpecialUser) {
						this.signup.setMessageId(WebConstants.SIGNUP_INVITER_HAS_INACTIVE_INVITEES);
						this.signup.setMessageKey("signup.invitation_inviter_has_inactive_invitees");
						this.signup.setAction(null);
						return;
                                        }
                                        
					Long totalWelcomeProducts = productService.countAvailableWelcomeProducts();
					if (totalWelcomeProducts == 0) {
						this.signup.setMessageId(WebConstants.SIGNUP_VERIFICATION_NO_WELCOME_PRODUCTS);
						this.signup.setMessageKey("signup.invitationURL.noWelcomeProduct.error");
						this.signup.setAction(null);
						return;
					}
					
					this.signup.setInvitation(invitation);
					this.signup.setInitialized(false);
					
				} else if(this.signup.getAction() != null) {
					this.signup.setMessageId(WebConstants.SIGNUP_VERIFICATION_INVALID_INVITATION_URL);
					this.signup.setMessageKey("signup.invitationURL.error");
					this.signup.setAction(null);
				}
	
			} catch(Exception ex) {
				ex.printStackTrace();
				this.signup.setMessageId(WebConstants.SIGNUP_VERIFICATION_GENERAL_ERROR);
				this.signup.setMessageKey("error.generalErrorKey");
			}
		}
	}

	public void submit() {
		try {
			if(!validateSignup()) {
				this.signup.setCounty(this.signup.getAddress().getArea().getDistrict().getCounty());
				this.signup.setDistrict(this.signup.getAddress().getArea().getDistrict());
				this.signup.setEditable(false);
			//	this.signup.setGsmVerified(true); // optimistic set
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey("messages", 
				"error.generalErrorKey"));
			this.signup.setEditable(true);
			this.signup.setValid(false);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void signup() {
		try {
			if(!validateSignup()) {
				User user = new User();
	
				user.setGsmVerification(new GsmVerification());
				user.setSubscriptionOption(new SubscriptionOption());
				user.setEmailVerification(new EmailVerification());
				
				// User fields
				user.setUsername(this.signup.getUsername().toLowerCase());
				
				Calendar currentDateTime = Calendar.getInstance();
				user.setCreateDate(currentDateTime.getTime());
		
				user.setPassword(this.passwordEncoder.encodePassword(this.signup.getPassword(), user.getSalt()));
				
				user.setFirstName(this.signup.getFirstname());
				user.setLastName(this.signup.getLastname());
				user.setBirthday(this.signup.getParsedBirthdate());
				user.setEmail(this.signup.getEmail());
				
				Address address = this.signup.getAddress();
				address.setPrimary(true);
                                address.setShortName(this.signup.getAddress().getAddressType().getName());
				user.addAddress(address);
				
				// the asscociated invitation 
				user.setAcceptedInvitation(this.signup.getInvitation());
				
				user.setActive(true);

				// email verification fields
				EmailVerification emailVerification = new EmailVerification();
				emailVerification.setEmailVerified(true);
				emailVerification.setEmailVerificationSentTime(currentDateTime.getTime());
				user.setEmailVerification(emailVerification);

				// gsm verification fields 
				user.getGsmVerification().setGsmVerificationSentTime(currentDateTime.getTime());
				user.getGsmVerification().setGsmNumber(this.signup.getGsmNumber());
				user.getGsmVerification().setGsmOperator(this.signup.getGsmOperator());
				user.getGsmVerification().setGsmPrefix(this.signup.getGsmPrefix());
				user.getGsmVerification().setGsmVerified(true);

				// other fields
				user.setGenderType(GenderType.valueOf(this.signup.getGender()));
				user.setEducationType(EducationType.valueOf(this.signup.getEducation()));
				user.setEmploymentStatusType(EmploymentStatusType.valueOf(this.signup.getEmploymentStatus()));
				user.setMonthlyNetTLIncomeType(MonthlyNetTLIncomeType.valueOf(this.signup.getMontlyNetIncome()));
				user.setMaritalStatusType(MaritalStatusType.valueOf(this.signup.getMaritalStatus()));
				user.setHasChildrenType(HasChildrenType.valueOf(this.signup.getHasChildren()));
				
				user.setActivityLevel(1);
				
				// save the user
				this.userService.signupUser(user);

				// display success message
				//FacesUtils.addSuccessMessage(FacesUtils.getBundleKey("messages", "signup.successMessage"));
				this.signup.setMessageId(WebConstants.SIGNUP_SUCCESS);
				this.signup.setMessageKey("signup.successMessage");
				
				this.signup.setAction(null);
				this.signup.setValid(false);
				
				// send a welcome email
				Map model = new HashMap();
				model.put("baseurl", getBaseUrlWithPath());
				model.put("userfirstname", this.signup.getFirstname());
				model.put("username", this.signup.getUsername());
				model.put("password", this.signup.getConfirmedPassword());
				mailService.sendEmail(this.signup.getEmail(), "Bifincan'a hoşgeldin " + this.signup.getFirstname(), "signup-welcome", model);
			}
		} catch (Exception e) {
			if(e instanceof NoWelcomeProductsException || e instanceof OrderProcessException || 
					e instanceof ConfigurationException) {
				log.debug(e.getMessage());
				FacesUtils.addErrorMessage(FacesUtils.getBundleKey("messages", 
						"signup.invitationURL.noWelcomeProduct.error"));
			} else {
				e.printStackTrace();
				FacesUtils.addErrorMessage(FacesUtils.getBundleKey("messages", 
					"error.generalErrorKey"));
			}	
			this.signup.setValid(false);
		}
	}

	//--- listeners methods ---//
    public List<Area> completeArea(String query) { 
    	if(this.signup.getCity() != null && this.signup.getCity().getId() > 0) {
    		return addressService.findAreasByCityIdAndKeyword(this.signup.getCity().getId(), query);
    	}
    	return null;
    } 
    
	public void cityChangeEvent(AjaxBehaviorEvent e) {
		if(this.signup.getCity() != null) {
			this.counties = this.addressService.findCountiesByCity(this.signup.getCity());
		} else {
			if(this.counties != null) this.counties.clear();
		}	
		
		if(this.districts != null) this.districts.clear();
		if(this.areas != null) this.areas.clear();	
		this.signup.setCounty(null);
		this.signup.setDistrict(null);
		this.signup.setArea(null);
	}

	public void countyChangeEvent(AjaxBehaviorEvent e) {
		if(this.signup.getCounty() != null) {
			this.districts = this.addressService.findDistrictsByCounty(this.signup.getCounty());
		} else {
			if(this.districts != null) this.districts.clear();
		}

		if(this.areas != null) this.areas.clear();	
		this.signup.setDistrict(null);
		this.signup.setArea(null);
	}

	public void districtChangeEvent(AjaxBehaviorEvent e) {
		if(this.signup.getDistrict() != null) {
			this.areas = this.addressService.findAreasByDistrict(this.signup.getDistrict());
		} else {
			if(this.areas != null) this.areas.clear();
		}

		this.signup.setArea(null);
	}
	
	//--- helper methods ---//
	private boolean validateSignup() {
		boolean errors = false;

		// Username Verification
		User userVerification = this.userService.findUserByUsername(this.signup.getUsername().toLowerCase());
		if (userVerification != null) {
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey("messages", "signup.userName.error"));
			errors = true;
		}

		// Email Verification
		userVerification = this.userService.findUserByEmail(this.signup.getEmail());
		if (userVerification != null) {
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey("messages", "signup.email.error"));
			errors = true;
		}

		// Password and Confirm Password Check
		if (!this.signup.getPassword().equals(this.signup.getConfirmedPassword())) {
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey("messages", "signup.password.error"));
			errors = true;
		}

		// GSM number / prefix Check
		if (userService.countByGsmPrefixAndNumber(this.signup.getGsmPrefix().getCode(), this.signup.getGsmNumber()) > 0) {
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey("messages", "signup.gsm.notUnique"));
			errors = true;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			this.signup.setParsedBirthdate(sdf.parse(this.signup.getDateOfBirth()));
		} catch (ParseException e) {
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey("messages", "signup.dateFormat.error"));
			errors = true;
		}

		if(this.signup.getCity() == null) {
			FacesUtils.addErrorMessage("addressCity", FacesUtils.getBundleKey("messages", "field.required"));
			errors = true;
		}
	/*	if(this.signup.getCounty() == null) {
			FacesUtils.addErrorMessage("addressCounty", FacesUtils.getBundleKey("messages", "field.required"));
			errors = true;
		}
		if(this.signup.getDistrict() == null) {
			FacesUtils.addErrorMessage("addressDistrict", FacesUtils.getBundleKey("messages", "field.required"));
			errors = true;
		}*/
		if(this.signup.getAddress().getArea() == null) {
			FacesUtils.addErrorMessage("addressArea", FacesUtils.getBundleKey("messages", "field.required"));
			errors = true;
		}

		return errors;
	}
	
	public void sendGsmConfirmationCode(AjaxBehaviorEvent e) {
		User user = new User();
		
		GsmVerification gsmVerification = new GsmVerification();
		gsmVerification.setGsmNumber(this.signup.getGsmNumber());
		gsmVerification.setGsmOperator(this.signup.getGsmOperator());
		gsmVerification.setGsmPrefix(this.signup.getGsmPrefix());
		
		user.setGsmVerification(gsmVerification);
		
		this.gsmConfirmationCode = gsmService.saveUserGsmNumberAndSendConfirmationCode(user, FacesUtils.getBundleKey("messages", 
				WebConstants.GSM_VALIDATION_SUCCESS_MESSAGE));	
		
		
		this.signup.setGsmVerified(false);
	}
	
	public void resetEdit(AjaxBehaviorEvent e) {
		this.signup.setEditable(true);
		this.gsmConfirmationCode = null;
	}

	public void verifyGsmConfirmationCode(AjaxBehaviorEvent e) {
		try {
			String outcome = this.gsmConfirmationCode.get();

			if(outcome.equals(this.signup.getGsmConfirmationCode())) {
				setGsmConfirmationCode(null);
				this.signup.setGsmConfirmationCode(null);
				this.signup.setGsmVerified(true);

				return;	
			} 
		} catch(Exception ex) { 
			setGsmConfirmationCode(null);
			this.signup.setGsmConfirmationCode(null);
			
			log.error(ex.getMessage());
			
		}
		FacesUtils.addErrorMessage("gsmConfirmationCode", 
				"bu onay kodu doğru değil");			
	}
	
	
	private String generateConfirmationKey(String email2, String _currentUsername) {
		String confirmKey = passwordEncoder.encodePassword(email2, _currentUsername);
		return confirmKey;
	}
	

	public String getBaseUrlWithPath() {
		String applicationCanonicalURL = "https://www.bifincan.com";
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		String ctxPath = context.getRequestContextPath();
		Configuration canonicalUrlConfig = configurationService.find((ConfigurationType.
				APPLICATION_CANONICAL_URL.getKey()));
		if(canonicalUrlConfig != null) applicationCanonicalURL = canonicalUrlConfig.getValue();
		String baseUrlWithPath = applicationCanonicalURL + ctxPath;
		return baseUrlWithPath;
	}
	
    public List<GsmOperator> getGsmOperators() {
        return this.gsmService.getGsmOperators();
    }
    
    public List<GsmPrefix> getGsmPrefixs() {
        return this.gsmService.getGsmPrefixs();
    }

    public List<AddressType> getAddressTypes() {
        return this.addressService.getAddressTypes();   
    }

    public List<City> getCities() {
        return this.addressService.getCities();
    }	

	private boolean isValidParam(String paramValue) {
		return ( paramValue != null && !"".equals(paramValue.trim()) ) ? true : false; 
	}

	public List<County> getCounties() {
		return counties;
	}

	public void setCounties(List<County> counties) {
		this.counties = counties;
	}

	public List<District> getDistricts() {
		return districts;
	}

	public void setDistricts(List<District> districts) {
		this.districts = districts;
	}

	public List<Area> getAreas() {
		return areas;
	}

	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}

	public SignupDTO getSignup() {
		return signup;
	}

	public void setSignup(SignupDTO signup) {
		this.signup = signup;
	}


	public Future<String> getGsmConfirmationCode() {
		return gsmConfirmationCode;
	}


	public void setGsmConfirmationCode(Future<String> gsmConfirmationCode) {
		this.gsmConfirmationCode = gsmConfirmationCode;
	}	
	
}


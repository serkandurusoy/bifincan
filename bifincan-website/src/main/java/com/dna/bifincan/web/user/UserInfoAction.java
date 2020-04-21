package com.dna.bifincan.web.user;

import com.dna.bifincan.common.util.RandomStringGenerator;
import com.dna.bifincan.model.address.*;
import com.dna.bifincan.model.config.Configuration;
import com.dna.bifincan.model.dto.SignupDTO;
import com.dna.bifincan.model.gsm.GsmOperator;
import com.dna.bifincan.model.gsm.GsmPrefix;
import com.dna.bifincan.model.type.ConfigurationType;
import com.dna.bifincan.model.user.*;
import com.dna.bifincan.model.user.verification.EmailVerification;
import com.dna.bifincan.service.*;
import com.dna.bifincan.util.FacesUtils;
import com.dna.bifincan.util.WebConstants;
import com.dna.bifincan.util.spring.ScopeType;
import java.io.Serializable;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.encoding.PasswordEncoder;

@Named("userInfoAction")
@Scope(ScopeType.VIEW)  
public class UserInfoAction implements Serializable {
	private static final long serialVersionUID = 7570759913322326212L;
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(UserInfoAction.class);	

	private SignupDTO userInfo = new SignupDTO();

	private List<GsmOperator> gsmOperators;
	private List<GsmPrefix> gsmPrefixes;
	private List<Address> addresses;
	
    private Address address;
    
	private City city;
	private County county;
	private District district;
	private Area area;
	
	private List<County> counties;
	private List<District> districts;
	private List<Area> areas;
	private List<City> cities;
	private List<AddressType> addressTypes;
	private Future<String> gsmConfirmationCode;
	
	@Inject
	private RandomStringGenerator passwordGenerator;
	@Inject
	private PasswordEncoder passwordEncoder;
	@Inject
	private MailService mailService;
	@Inject
	private UserService userService;
	@Inject
	private ConfigurationService configurationService;
    @Inject
    PasswordEncoder encoder;
    @Inject
    private GSMService gsmService;
    @Inject
    private AddressService addressService;        
    
    
	public UserInfoAction() { }
	
	@PostConstruct
	public void initialize() { 
		loadUserInformation();
	}


	// --- action listeners --- //	
	public void changeEmail(AjaxBehaviorEvent e) {
		Principal principal = FacesUtils.getExternalContext().getUserPrincipal();
		String username = principal.getName();
		
		User user = userService.findUserByUsername(username);
		
		if(this.userInfo.getEmail().equals(user.getEmail())) {
		    FacesUtils.addErrorMessage("s-posta adresi değişmedi");
		    return;
		} else if(userService.countByUserEmail(this.userInfo.getEmail()) > 0) {
			FacesUtils.addErrorMessage("e-posta adresi başka bir üyeye zaten kayıtlı");
		    return;
		}

		Date sentTime = new Date();

		EmailVerification verification = user.getEmailVerification();
		if (verification == null) {
		    verification = new EmailVerification();
		    verification.setEmailVerificationSentTime(sentTime);
		    verification.setEmailVerified(false);
		    user.setEmailVerification(verification);
		} else {
			user.getEmailVerification().setEmailVerificationSentTime(sentTime);
			user.getEmailVerification().setEmailVerified(false);
		}

		String oldEmail = user.getEmail();
		//log.debug(">> oldEmail = " + oldEmail);
		user.setEmail(this.userInfo.getEmail());

		userService.changeEmail(user, oldEmail);
	
		updateUserTokenInSession(user); // update the user token in session in order to reflect the last changes
		
		String encodedVal = generateConfirmationKey(user);
	    String link = getBaseUrlWithPath() + "/eposta-dogrula/" + user.getUsername() + "/" + encodedVal;
	    
		try {
		    sendVerificationEmail(user, link);
		} catch (MailException ex) {
		    ex.printStackTrace();
		    FacesUtils.addErrorMessage("doğrulama bağlantısı e-posta adresine gönderilemedi");
		    return;
		} catch (MessagingException ex) {
		    ex.printStackTrace();
		    FacesUtils.addErrorMessage("doğrulama bağlantısı e-posta adresine gönderilemedi");
		    return;
		}

		FacesUtils.addSuccessMessage("değişikliği tamamlayabilmen için sana doğrulama bağlantısı içeren bir e-posta mesajı ilettim");
	}
	
	private void updateUserTokenInSession(User newUser) {
		HttpServletRequest request = (HttpServletRequest)FacesUtils.getExternalContext().getRequest();
		HttpSession session = request.getSession();
		
		User userToken = (User)session.getAttribute(WebConstants.CURRENT_USER_SESSION_KEY);

		newUser.setShoppingCart(userToken.getShoppingCart());
		newUser.setNoOfOrderableProducts(userToken.getNoOfOrderableProducts());
		
		session.setAttribute(WebConstants.CURRENT_USER_SESSION_KEY, newUser);
		userToken = null;	
	}
	
	public void reSendVerificationEmail(AjaxBehaviorEvent e) {
		Principal principal = FacesUtils.getExternalContext().getUserPrincipal();
		String username = principal.getName();
		
		User user = userService.findUserByUsername(username);
		String encodedVal = generateConfirmationKey(user);
	    String link = getBaseUrlWithPath() + "/eposta-dogrula/" + user.getUsername() + "/" + encodedVal;
	    
		try {
		    sendVerificationEmail(user, link);
		} catch (MailException ex) {
		    ex.printStackTrace();
		    FacesUtils.addErrorMessage("doğrulama bağlantısı e-posta adresine gönderilemedi");
		    return;
		} catch (MessagingException ex) {
		    ex.printStackTrace();
		    FacesUtils.addErrorMessage("doğrulama bağlantısı e-posta adresine gönderilemedi");
		    return;
		}

		FacesUtils.addSuccessMessage("değişikliği tamamlayabilmen için sana doğrulama bağlantısı içeren bir e-posta mesajı ilettim");
	}
	
	public void changePassword(AjaxBehaviorEvent e) {
		Principal principal = FacesUtils.getExternalContext().getUserPrincipal();
		String username = principal.getName();
		
		User user = userService.findUserByUsername(username);

		if( (this.userInfo.getPassword() != null && this.userInfo.getConfirmedPassword() != null) && 
				( !this.userInfo.getConfirmedPassword().equals(this.userInfo.getPassword()) ) ) {
		    FacesUtils.addErrorMessage("şifren ile doğrulaması uyuşmuyor");
		    return;
		}

		user.setPassword(passwordEncoder.encodePassword(this.userInfo.getPassword(), user.getSalt()));
		userService.saveUser(user);

		updateUserTokenInSession(user); // update the user token in session in order to reflect the last changes
		
		FacesUtils.addSuccessMessage("şifreni güncelledim");
	}
	
	public void changeGeneralInfo(AjaxBehaviorEvent e) {
		Principal principal = FacesUtils.getExternalContext().getUserPrincipal();
		String username = principal.getName();
		
		User user = userService.findUserByUsername(username);
		
		user.setFirstName(this.userInfo.getFirstname());
		user.setLastName(this.userInfo.getLastname());
		user.setGenderType(GenderType.valueOf(this.userInfo.getGender()));
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			user.setBirthday(sdf.parse(this.userInfo.getDateOfBirth()));
		} catch (ParseException ex) {
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey("messages", "signup.dateFormat.error"));
			return;
		}

		user.setEducationType(EducationType.valueOf(this.userInfo.getEducation()));
		user.setEmploymentStatusType(EmploymentStatusType.valueOf(this.userInfo.getEmploymentStatus()));
		user.setMonthlyNetTLIncomeType(MonthlyNetTLIncomeType.valueOf(this.userInfo.getMontlyNetIncome()));
		user.setMaritalStatusType(MaritalStatusType.valueOf(this.userInfo.getMaritalStatus()));
		user.setHasChildrenType(HasChildrenType.valueOf(this.userInfo.getHasChildren()));
		
		userService.saveUser(user);

		updateUserTokenInSession(user); // update the user token in session in order to reflect the last changes
		
		FacesUtils.addSuccessMessage(FacesUtils.getBundleKey("messages", "user.info.success"));
	}	

	public void changeGsmMailPreferences(AjaxBehaviorEvent e) {
		Principal principal = FacesUtils.getExternalContext().getUserPrincipal();
		String username = principal.getName();
		
		User user = userService.findUserByUsername(username);
		
		user.setSubscriptionOption(this.userInfo.getSubscriptionOption());
		
		userService.saveUser(user);

		updateUserTokenInSession(user); // update the user token in session in order to reflect the last changes
		
		FacesUtils.addSuccessMessage(FacesUtils.getBundleKey("messages", "user.preferences.success"));	
	}	
	
	public void changeGSMInfo(AjaxBehaviorEvent e) {
		Principal principal = FacesUtils.getExternalContext().getUserPrincipal();
		String username = principal.getName();
		
		User user = userService.findUserByUsername(username);
		
		if( (this.userInfo.getGsmNumber().equals(user.getGsmVerification().getGsmNumber()) &&  // no change at all
				this.userInfo.getGsmOperator().equals(user.getGsmVerification().getGsmOperator()) &&
					this.userInfo.getGsmPrefix().equals(user.getGsmVerification().getGsmPrefix()) )
				) {
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey("messages", "user.gsm.noChange"));
			return;
		} else if( this.userInfo.getGsmNumber().equals(user.getGsmVerification().getGsmNumber()) &&  // only the operator changed
				   !this.userInfo.getGsmOperator().equals(user.getGsmVerification().getGsmOperator()) &&
				   this.userInfo.getGsmPrefix().equals(user.getGsmVerification().getGsmPrefix()) ) {			
			
			user.getGsmVerification().setGsmOperator(this.userInfo.getGsmOperator());
			userService.saveUser(user);
			
			updateUserTokenInSession(user); // update the user token in session in order to reflect the last changes
			
			FacesUtils.addSuccessMessage(FacesUtils.getBundleKey("messages", "user.info.success"));	
			return;
		}

		if (userService.countByGsmPrefixAndNumberAndUserId(this.userInfo.getGsmPrefix().getCode(), 
				this.userInfo.getGsmNumber(), user.getId()) > 0) {
			FacesUtils.addErrorMessage(FacesUtils.getBundleKey("messages", "signup.gsm.notUnique"));
			return;
		}	
		
		user.getGsmVerification().setGsmVerificationSentTime(new Date());
		user.getGsmVerification().setGsmNumber(this.userInfo.getGsmNumber());
		user.getGsmVerification().setGsmOperator(this.userInfo.getGsmOperator());
		user.getGsmVerification().setGsmPrefix(this.userInfo.getGsmPrefix());
		user.getGsmVerification().setGsmVerified(false);		

		this.gsmConfirmationCode = gsmService.saveUserGsmNumberAndSendConfirmationCode(user, FacesUtils.getBundleKey("messages", 
				WebConstants.GSM_VALIDATION_SUCCESS_MESSAGE));
		this.userInfo.setGsmVerified(false);
		
		updateUserTokenInSession(user); // update the user token in session in order to reflect the last changes
	}	
	
	public void verifyGsmConfirmationCode(AjaxBehaviorEvent e) {
		
		try {
			String outcome = this.gsmConfirmationCode.get();

			if(outcome.equals(this.userInfo.getGsmConfirmationCode())) {
				Principal principal = FacesUtils.getExternalContext().getUserPrincipal();
				String username = principal.getName();
				
				User user = userService.changeGsmVerificationStatus(username, true);
				
				setGsmConfirmationCode(null);
				this.userInfo.setGsmConfirmationCode(null);
				this.userInfo.setGsmVerified(true);
				
				updateUserTokenInSession(user); // update the user token in session in order to reflect the last changes
				
				FacesUtils.addSuccessMessage(FacesUtils.getBundleKey("messages", "user.info.success"));	
				return;	
			} 
		} catch(Exception ex) { 
			setGsmConfirmationCode(null);
			this.userInfo.setGsmConfirmationCode(null);
			
			log.error(ex.getMessage());
		}
		
		FacesUtils.addErrorMessage("girdiğin kod doğru değil");		
	}
	
	
	public void resendGsmConfirmationCode(AjaxBehaviorEvent e) {
		Principal principal = FacesUtils.getExternalContext().getUserPrincipal();
		String username = principal.getName();
		
		User user = userService.findUserByUsername(username);
		
		this.gsmConfirmationCode = gsmService.saveUserGsmNumberAndSendConfirmationCode(user, FacesUtils.getBundleKey("messages", 
				WebConstants.GSM_VALIDATION_SUCCESS_MESSAGE));		
	}
	
	public String makeAdressPrimary() {
		Principal principal = FacesUtils.getExternalContext().getUserPrincipal();
		String username = principal.getName();
		
		this.address = null;
		
		Long addressId = (Long)FacesUtils.getFlash().get("addressId");
		if(addressService.makeAddressPrimary(addressId, username) == true) {  
			loadUserAddresses(username);
			
			this.updateUserTokenInSession(userService.findUserByUsername(username)); // update the user token in session
			
			FacesUtils.addSuccessMessage("adres bilgilerini güncelledim");
		} /*else {
			FacesUtils.addSuccessMessage("There is already a primary address.");
		}*/
		
		return null;
	}	
	
	public String deleteAddress() {
		Principal principal = FacesUtils.getExternalContext().getUserPrincipal();
		String username = principal.getName();
		
		this.address = null;
		
		Long addressId = (Long)FacesUtils.getFlash().get("addressId");
		
		if(addressService.deleteUserAddress(addressId)) { 
			FacesUtils.addSuccessMessage("adres bilgilerini güncelledim");
			
			loadUserAddresses(username);
			
			this.updateUserTokenInSession(userService.findUserByUsername(username)); // update the user token in session
		} else {
			FacesUtils.addSuccessMessage("ana adresini silemezsin");
		}

		return null;
	}
	
	public String findAddress() {
		Long addressId = (Long)FacesUtils.getFlash().get("addressId");
		this.address = addressService.findUserAddress(addressId);

		this.setArea(this.address.getArea());
		this.setDistrict(this.address.getArea().getDistrict());
		this.setCounty(this.address.getArea().getDistrict().getCounty());
		this.setCity(this.address.getArea().getDistrict().getCounty().getCity());

		this.districts = this.addressService.findDistrictsByCounty(this.getCounty());
		this.counties = this.addressService.findCountiesByCity(this.getCity());		
		this.areas = this.addressService.findAreasByDistrict(this.getDistrict());
		
		return null;
	}	
	
	public String addAddress() {
		this.address = new Address();

		this.city = null;
		this.county = null;
		this.district = null;
		this.area = null;
		
		this.counties = null;
		this.districts = null;
		this.areas = null;
		
		return null;
	}	
	
	public String saveOrUpdateAddress() {
		boolean hasErrors = false;
		
		if(this.getCity() == null) {
			FacesUtils.addErrorMessage("addressCity", FacesUtils.getBundleKey("messages", "field.required"));
			hasErrors = true;
		} 
		/*if(this.getCounty() == null) {
			FacesUtils.addErrorMessage("addressCounty", FacesUtils.getBundleKey("messages", "field.required"));
			hasErrors = true;
		}
		if(this.getDistrict() == null) {
			FacesUtils.addErrorMessage("addressDistrict", FacesUtils.getBundleKey("messages", "field.required"));
			hasErrors = true;
		}*/
		if(this.getAddress().getArea() == null) {
			FacesUtils.addErrorMessage("addressArea", FacesUtils.getBundleKey("messages", "field.required"));
			hasErrors = true;
		}	
		
		if(!hasErrors) {
			Principal principal = FacesUtils.getExternalContext().getUserPrincipal();
			String username = principal.getName();
			
			User user = userService.findUserByUsername(username);
			
			if(address.getId() == null) address.setUser(user);
			
			addressService.saveOrUpdateAddress(address);
			
			loadUserAddresses(username);
		
			this.updateUserTokenInSession(userService.findUserByUsername(username)); // refresh again the ... user and update the user token in session
			
			address = null;
			
			FacesUtils.addSuccessMessage("adres bilgilerini güncelledim");
		}
		
		return null;
	}
	
	public void cityChangeEvent(AjaxBehaviorEvent e) {
		if(this.getCity() != null) {
			this.counties = this.addressService.findCountiesByCity(this.getCity());
		} else {
			if(this.counties != null) this.counties.clear();
		}	
		
		if(this.districts != null) this.districts.clear();
		if(this.areas != null) this.areas.clear();	
		this.setCounty(null);
		this.setDistrict(null);
		this.setArea(null);
	}

	public void countyChangeEvent(AjaxBehaviorEvent e) {
		if(this.getCounty() != null) {
			this.districts = this.addressService.findDistrictsByCounty(this.getCounty());
		} else {
			if(this.districts != null) this.districts.clear();
		}

		if(this.areas != null) this.areas.clear();	
		this.setDistrict(null);
		this.setArea(null);
	}

	public void districtChangeEvent(AjaxBehaviorEvent e) {
		if(this.getDistrict() != null) {
			this.areas = this.addressService.findAreasByDistrict(this.getDistrict());
		} else {
			if(this.areas != null) this.areas.clear();
		}

		this.setArea(null);
	}
	
    public List<Area> completeArea(String query) { 
    	if(this.city != null && this.city.getId() > 0) {
    		return addressService.findAreasByCityIdAndKeyword(this.city.getId(), query);
    	}
    	return null;
    } 
    
	public void cancelEditAdrress(AjaxBehaviorEvent e) {
		this.address = null;

		this.city = null;
		this.county = null;
		this.district = null;
		this.area = null;
		
		this.counties = null;
		this.districts = null;
		this.areas = null;
	}
	
	// --- other helper methods --- //
	private void loadUserInformation() {
		Principal principal = FacesUtils.getExternalContext().getUserPrincipal();
		String username = principal.getName();
		
		User user = userService.findUserByUsername(username);
		if(user != null) {
			// load the user's main information
			this.userInfo.setUsername(user.getUsername());
			this.userInfo.setEmail(user.getEmail());
			
			this.userInfo.setFirstname(user.getFirstName());
			this.userInfo.setLastname(user.getLastName());
			this.userInfo.setGender(user.getGenderType().toString());
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			this.userInfo.setDateOfBirth(sdf.format(user.getBirthday()));
			
			this.userInfo.setEducation(user.getEducationType().toString());
			this.userInfo.setEmploymentStatus(user.getEmploymentStatusType().toString());
			this.userInfo.setMontlyNetIncome(user.getMonthlyNetTLIncomeType().toString());
			this.userInfo.setMaritalStatus(user.getMaritalStatusType().toString());
			this.userInfo.setHasChildren(user.getHasChildrenType().toString());
			
			this.userInfo.setGsmNumber(user.getGsmVerification().getGsmNumber());
			this.userInfo.setGsmOperator(user.getGsmVerification().getGsmOperator());
			this.userInfo.setGsmPrefix(user.getGsmVerification().getGsmPrefix());
			this.userInfo.setGsmVerified(user.getGsmVerification().isGsmVerified());
			
			this.userInfo.setSubscriptionOption(user.getSubscriptionOption());
			
			// load the addresses
			loadUserAddresses(username);
			
			this.addressTypes = this.addressService.getAddressTypes();  
			this.cities = this.addressService.getCities();
		}
		
		// load other info
		this.gsmOperators = this.gsmService.getGsmOperators();
		this.gsmPrefixes = this.gsmService.getGsmPrefixs();
	}
	
    @SuppressWarnings("unchecked")
	private void sendVerificationEmail(User _user, String link) throws MailException, MessagingException {
		@SuppressWarnings("rawtypes")
		Map model = new HashMap();
		model.put("baseurl", getBaseUrlWithPath());
		model.put("userfirstname", _user.getFirstName());
		model.put("link", link);
		mailService.sendEmail(this.userInfo.getEmail(), "E-posta adresini doğrulaman gerekiyor", "change-email", model);
    }
    
    public String getBaseUrlWithPath() {
        String applicationCanonicalURL = "https://www.bifincan.com";
        ExternalContext contextUrl = FacesContext.getCurrentInstance().getExternalContext();
        String ctxPath = contextUrl.getRequestContextPath();
        Configuration canonicalUrlConfig = configurationService.find((ConfigurationType.
                        APPLICATION_CANONICAL_URL.getKey()));
        if(canonicalUrlConfig != null) applicationCanonicalURL = canonicalUrlConfig.getValue();
        String baseUrlWithPath = applicationCanonicalURL + ctxPath;
        return baseUrlWithPath;
    }
    
    private String generateConfirmationKey(User user) {
		String encodedVal = encoder.encodePassword(user.getEmail(), saltSource(user));
		return encodedVal;
    }
    
    private String saltSource(User user) {
		return user.getUsername();
    }
 
	private void loadUserAddresses(String username) {
		this.addresses = addressService.findAddressesByUsername(username);	
	}    
	
	// -- setters & getters -- /
	public SignupDTO getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(SignupDTO userInfo) {
		this.userInfo = userInfo;
	}	
	
    public List<GsmOperator> getGsmOperators() {
        return this.gsmOperators;
    }
    
    public List<GsmPrefix> getGsmPrefixs() {
        return this.gsmPrefixes;
    }

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
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

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}   
	
    public List<AddressType> getAddressTypes() {
        return this.addressTypes;   
    }

	public List<City> getCities() {
		return cities;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}

	public Future<String> getGsmConfirmationCode() {
		return gsmConfirmationCode;
	}

	public void setGsmConfirmationCode(Future<String> gsmConfirmationCode) {
		this.gsmConfirmationCode = gsmConfirmationCode;
	}
	
}

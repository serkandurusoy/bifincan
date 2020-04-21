package com.dna.bifincan.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dna.bifincan.exception.ConfigurationException;
import com.dna.bifincan.exception.NoWelcomeProductsException;
import com.dna.bifincan.exception.OrderProcessException;
import com.dna.bifincan.model.address.Address;
import com.dna.bifincan.model.config.Configuration;
import com.dna.bifincan.model.type.ConfigurationType;
import com.dna.bifincan.model.type.PointTransactionType;
import com.dna.bifincan.model.user.EmailListEntry;
import com.dna.bifincan.model.user.SocialPoint;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.model.user.UserInvitation;
import com.dna.bifincan.model.user.UserLevelValue;
import com.dna.bifincan.model.user.UserLogin;
import com.dna.bifincan.model.user.verification.EmailVerification;
import com.dna.bifincan.model.user.verification.GsmVerification;
import com.dna.bifincan.repository.user.EmailListEntryRepository;
import com.dna.bifincan.repository.user.SocialPointRepository;
import com.dna.bifincan.repository.user.UserInvitationRepository;
import com.dna.bifincan.repository.user.UserLoginRepository;
import com.dna.bifincan.repository.user.UserRepository;

@Service
@Named("userService")
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Inject
    private UserRepository userRepository;
    @Inject
    private UserLoginRepository userLoginRepository;
    @Inject
    private UserInvitationRepository userInvitationRepository;
    @Inject
    private PointTransactionAccountingService transactionService;
    @Inject
    private ConfigurationService configurationService;
    @Inject
    private OrderService orderService;
    @Inject
    private SurveyService surveyService;
    @Inject
    private SocialPointRepository socialPointRepository;   
    @Inject
    private EmailListEntryRepository emailListEntryRepository;      
    
    
    public User findUserByUsername(String _username) {
        return this.userRepository.findByUsername(_username);
    }

    public User findUserByEmail(String _email) {
        return this.userRepository.findByEmail(_email);
    }

    @Transactional
    public void saveUser(User user) {
        this.userRepository.save(user);
    }

    @Transactional
    public void optOutInvitations(List<UserInvitation> invitations) {
        if (invitations != null && !invitations.isEmpty()) {
            for (UserInvitation invitation : invitations) {
                invitation.setOptedOut(true);
                this.userInvitationRepository.save(invitation);
            }
        }

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {NoWelcomeProductsException.class,
        OrderProcessException.class, ConfigurationException.class})
    public void signupUser(User user) throws OrderProcessException, NoWelcomeProductsException, ConfigurationException {
        // create the new user
        this.userRepository.save(user);

        // opt out all of the invitations
        List<UserInvitation> invitations = this.findNotOptedInvitationsByEmail(user.getEmail());
        optOutInvitations(invitations);

        // add invitation succession points to inviter's balance
        if(!user.getAcceptedInvitation().getInviter().isFounder()) {
        	this.transactionService.addConfigurationPoints(user.getAcceptedInvitation().getInviter(),
                PointTransactionType.INVITIATION_SUCCESS_POINTS);
        }
        
        // add sign-up points to new user's balance (ex friend)
        this.transactionService.addConfigurationPoints(user, PointTransactionType.SIGNUP_POINTS);

        // create the welcome order
        this.orderService.createWelcomeOrder(user);
    }

    @Transactional
    public User mergeUser(User _user) {
        return userRepository.save(_user);
    }

    @Transactional
    public int saveUserLoginStatus(UserLogin _log) {
        this.userLoginRepository.save(_log);

        // TODO User Points, a new table is needed which will store the fault
        // logins. Also have to tackle the runtime errors
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DATE), 0, 0, 0);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DATE), 23, 59, 59);

        Long loginCounts = this.userLoginRepository.countUserLoginsByDate(startCalendar.getTime(), endCalendar.getTime(), _log.getUser());

        if (loginCounts <= 1) {  // <= 1 because it has already save into UserLogin table !
            return transactionService.addConfigurationPoints(_log.getUser(), PointTransactionType.LOGIN_POINTS);
        }

        return _log.getUser().getPointsBalance();  // return the current balance
    }

    @Transactional
    public int saveInvitation(UserInvitation _invitation) {
        this.userInvitationRepository.save(_invitation);
        
        EmailListEntry listEntry = emailListEntryRepository.findOne(_invitation.getEmail());
        if(listEntry != null) {
        	emailListEntryRepository.delete(listEntry);
        }
        
        // Invitation Points for User
        if(!_invitation.getInviter().isFounder()) {
        	return transactionService.addConfigurationPoints(_invitation.getInviter(), PointTransactionType.INVITIATION_POINTS);
        } else {
        	return _invitation.getInviter().getPointsBalance();
        }
    }

    @Transactional
    public UserInvitation saveUserInvitation(UserInvitation _invitation) {
        return this.userInvitationRepository.save(_invitation);
    }

    public UserInvitation findInvitationByInviterAndEmail(User inviter, String email) {
        return this.userInvitationRepository.findByInviterAndEmail(inviter, email);
    }

    public UserInvitation findInvitation(Long invitedId) {
        return this.userInvitationRepository.findOne(invitedId);
    }

    public Long countByInvitation(Long invitedId) {
        return this.userRepository.countByInvitation(invitedId);
    }

    public Long countInactiveInviteesByUser(User inviter) {
        return this.userRepository.countInactiveInviteesByUser(inviter);
    }

    public Long countByUserEmail(String email) {
        return this.userRepository.countByUserEmail(email);
    }

    public Long countByUsername(String username) {
        return this.userRepository.countByUsername(username);
    }

    @Transactional
    public void updateInvitationOptedOut(Long invitedId) {
        UserInvitation _userInvitation = findInvitation(invitedId);
        if (_userInvitation != null) {
            _userInvitation.setOptedOut(true);
            this.userInvitationRepository.save(_userInvitation);

            // Invitation Success Points
            transactionService.addConfigurationPoints(_userInvitation.getInviter(), PointTransactionType.INVITIATION_SUCCESS_POINTS);
        }
    }

    public List<UserInvitation> findNotOptedInvitationsByEmail(String email) {
        return this.userInvitationRepository.findByEmailAndOptedOut(email);
    }

    public Long invitationsCountToday(User user) {
        Calendar _cal = Calendar.getInstance();
        _cal.set(_cal.get(Calendar.YEAR), _cal.get(Calendar.MONTH), _cal.get(Calendar.DATE), 0, 0, 0);

        Date _startDate = _cal.getTime();

        _cal.set(_cal.get(Calendar.YEAR), _cal.get(Calendar.MONTH), _cal.get(Calendar.DATE), 23, 59, 59);

        Date _endDate = _cal.getTime();

        if (log.isDebugEnabled()) {
            log.debug("_startDate-->endDate @" + _startDate + "@@" + _endDate);
        }

        return this.userInvitationRepository.countUserInvitationsByDate(_startDate, _endDate, user);
    }

    public boolean canUserOrder(User user) {
        boolean canOrder = false;

        if (user != null && user.isActive()) {
            // check the email & gsm verification
            EmailVerification emailVerification = user.getEmailVerification();
            GsmVerification gsmVerification = user.getGsmVerification();
            // find the user's primary address
            Address address = getUserPrimaryAddress(user);

            canOrder = (emailVerification.isEmailVerified() && gsmVerification.isGsmVerified())
                    && (address != null);
        }

        return canOrder;
    }

    @SuppressWarnings("rawtypes")
	public Address getUserPrimaryAddress(User user) {
        Address address = null;

        if (user != null) {
            Set<Address> addresses = user.getAddresses();
            if (addresses != null) {
                Iterator it = addresses.iterator();
                while (it.hasNext()) {
                    address = (Address) it.next();
                    if (!address.getPrimary()) {
                        address = null;
                    } else {
                        break;
                    }
                }
            }
        }

        return address;
    }

    public int getNoOfOrderableProducts(int activityLevel) {
        int numOfProducts = 0;

        Configuration config = configurationService.find((ConfigurationType.MAX_NO_OF_ORDERABLE_PRODUCTS.getKey()));
        if (config != null) {  // if the value is defined
            try {
                numOfProducts = Integer.parseInt(config.getValue());
            } catch (java.lang.NumberFormatException ex) {
            }
        }
        
        int numberOfBonusOrderableProducts = 0;
		Configuration bounusOrderableConfig = configurationService.find(ConfigurationType.NO_OF_BONUS_ORDERABLE_PRODUCTS.getKey());
		
        if (bounusOrderableConfig != null) {  // if the value is defined
            try {
            	numberOfBonusOrderableProducts = Integer.parseInt(bounusOrderableConfig.getValue());
            } catch (java.lang.NumberFormatException ex) {
            }
        }
        
        if(activityLevel == 1) numOfProducts = 0;
        else if(activityLevel == 3)  numOfProducts += numberOfBonusOrderableProducts;
        
        return numOfProducts;
    }

    public int checkForNewSurveyCycleByUser(User user) {
        return surveyService.checkForNewSurveyCycleByUser(user);
    }

    public List<User> findUsers() { // TODO: probably this will take additional args
        return userRepository.findUsers();
    }

    public List<User> findAllUsers() {
        return (List<User>) userRepository.findAll();
    }
    
    public List<User> findAndLockAllUsers() {
        return (List<User>) userRepository.findAndLockAll();
    }


    public List<UserInvitation> findInvitationsByEmail(String _email) {
        return this.userInvitationRepository.findByEmail(_email);
    }

    public List<UserInvitation> findInvitationsByInviter(User inviter) {
        return this.userInvitationRepository.findByInviter(inviter);
    }

    public long countInvitationByUser(User _user) {
        return this.userInvitationRepository.countByInviter(_user);
    }

    public long noOfUsers() {
        return this.userRepository.count();
    }
    
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer saveSocialPoint(SocialPoint socialPoint) {
    	if(socialPointRepository.countByParams(socialPoint.getAction(), socialPoint.getUrl(), 
    			socialPoint.getUser()) == 0) { 
    		socialPointRepository.save(socialPoint);
    		
    		return transactionService.addConfigurationPoints(socialPoint.getUser(), PointTransactionType.SOCIAL_POINTS);
    	}
    	return null;
    }
    
    @Transactional(propagation = Propagation.REQUIRED)
    public User changeGsmVerificationStatus(String username, boolean flag) {
    	User user = userRepository.findByUsername(username);
    	user.getGsmVerification().setGsmVerified(true);
    	userRepository.save(user);
    	
    	return user;
    }
    
    @Transactional(propagation = Propagation.REQUIRED)
    public void changeEmail(User user, String oldEmail) {
    	userRepository.save(user);
    	
    	List<UserInvitation> userInvitations = userInvitationRepository.findByEmail(oldEmail);
    	for(UserInvitation inv : userInvitations) {
    		inv.setEmail(user.getEmail());
    		userInvitationRepository.save(inv);
    	}
    }
    
    public long noOfSocialActions() {
        return this.socialPointRepository.count();
    }
    
    public Long countByGsmPrefixAndNumber(Integer gsmPrefix, String gsmNumber) {
        return this.userRepository.countByGsmPrefixAndNumber(gsmPrefix, gsmNumber); 
    }  

    public Long countByGsmPrefixAndNumberAndUserId(Integer gsmPrefix, String gsmNumber, Long userId) {
        return this.userRepository.countByGsmPrefixAndNumberAndUserId(gsmPrefix, gsmNumber, userId); 
    }  

    public List<UserInvitation> findUnsignedInvitationsInSevenDays() {
         Calendar _cal = Calendar.getInstance();
        _cal.add(Calendar.DAY_OF_MONTH, -7);
        Date _timestamp = _cal.getTime();
        return userInvitationRepository.findUnsigned(_timestamp);
    }
    
    public List<UserInvitation> findUnsignedInvitationsInThirtyDays() {
         Calendar _cal = Calendar.getInstance();
        _cal.add(Calendar.DAY_OF_MONTH, -30);
        Date _timestamp = _cal.getTime();
        return userInvitationRepository.findUnsigned(_timestamp);
    }
    
    public List<User> findByLongAbsence(int days) {
        Calendar from = Calendar.getInstance();
        from.add(Calendar.DATE, -days);
        from.set(Calendar.HOUR_OF_DAY, 15);
        from.set(Calendar.MINUTE, 0);
        from.set(Calendar.SECOND, 0);

        return userRepository.findByLongAbsence(from.getTime());
    }
    
    public List<User> findByNoInvitationActivity(int days) {
    	List<User> users = null;
    	
        Calendar from = Calendar.getInstance();
        from.add(Calendar.DATE, -days);
        from.set(Calendar.HOUR_OF_DAY, 15);
        from.set(Calendar.MINUTE, 0);
        from.set(Calendar.SECOND, 0);

        List<Long> recentlyLoggedInUserIds = userRepository.findIdsByRecentLogin(from.getTime());
        List<Long> recentInvActivityUserIds = null;
        
        if(recentlyLoggedInUserIds != null && !recentlyLoggedInUserIds.isEmpty()) {
        	recentInvActivityUserIds = userInvitationRepository.findIdsByRecentInvitationActivityAndTargetList(from.getTime(), 
        			recentlyLoggedInUserIds);
        	if(recentInvActivityUserIds != null && !recentInvActivityUserIds.isEmpty()) {
	        	/* the expected list will be the difference between the 2 lists ie. users recently logged in - 
	        	 		user with recent activity list */
	        	recentlyLoggedInUserIds.removeAll(recentInvActivityUserIds);
        	}	
        	
        	if(recentlyLoggedInUserIds.isEmpty()) recentlyLoggedInUserIds.add(0L); // to avoid an empty list
        	users = userRepository.findByTargetListOfIds(recentlyLoggedInUserIds);
        }
        
        return users;
    }    
    
    public List<User> findActiveSiteEmailAcceptedAndEmailVerifiedUsers() {
    	return userRepository.findActiveSiteEmailAcceptedAndEmailVerified();
    }
    
    public List<String> findNotOptedOutInvitationEmails() {
    	return userInvitationRepository.findActiveNotOptedOut();  
    }    

    public List<UserLevelValue> findAllActiveUserLevels() {
        return (List<UserLevelValue>) userRepository.findAllActiveUserLevels();
    }

    @Transactional
    public void updateUserActivityLevel(UserLevelValue _user) {
        userRepository.updateUserActivityLevel(_user.getUsername(), _user.getActivityLevel());
    }
    
}

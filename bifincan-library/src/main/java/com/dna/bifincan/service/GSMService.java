package com.dna.bifincan.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import javax.inject.Inject;
import javax.inject.Named;

import org.jfree.util.Log;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Mobilus.Sms.SmsMultiSender;
import com.dna.bifincan.model.config.Configuration;
import com.dna.bifincan.model.gsm.GsmOperator;
import com.dna.bifincan.model.gsm.GsmPrefix;
import com.dna.bifincan.model.type.ConfigurationType;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.repository.gsm.GSMOperatorRepository;
import com.dna.bifincan.repository.gsm.GSMPrefixRepository;
import com.dna.bifincan.repository.user.UserRepository;

@Service
@Named("gsmService")
public class GSMService {
	@Inject
	private GSMOperatorRepository gsmOperatorRepository;
	@Inject
	private GSMPrefixRepository gsmPrefixRepository;
	@Inject
	private UserRepository userRepository;
	@Inject
	private ConfigurationService configurationService;
	
	public List<GsmOperator> getGsmOperators() {
		List<Order> sortOrders = new ArrayList<Order>();

		Order order = new Order(Direction.ASC, "name");
		sortOrders.add(order);

		Sort sort = new Sort(sortOrders);

		return this.gsmOperatorRepository.findAll(sort);
	}

	public List<GsmPrefix> getGsmPrefixs() {
		List<Order> sortOrders = new ArrayList<Order>();

		Order order = new Order(Direction.ASC, "code");
		sortOrders.add(order);

		Sort sort = new Sort(sortOrders);
		return this.gsmPrefixRepository.findAll(sort);
	}
	
    /**
     * Saves a gsm operator, but first checks for duplicate name.
     * @param operator
     * @return  true for success or false for failure 
     */    
    public boolean saveGsmOperator(GsmOperator operator) {
    	Long total = gsmOperatorRepository.countByName(operator.getName(), 
    			operator.getId() != null ? operator.getId() : 0);
    	if(total == 0) {
    		this.gsmOperatorRepository.save(operator);
    		return true;
    	} else {
    		return false;
    	}
    }
    
    /**
     * Deletes a gsm operator, but first checks for existing dependencies.
     * @param gsmOperator
     * @return  true for success or false for failure (ONLY due to dependencies)
     */
    public boolean deleteGsmOperator(GsmOperator operator) {
    	Long total = userRepository.countByGsmOperator(operator.getId());
    	if(total == 0) {
    		// get a fresh copy of the entity
    		GsmOperator tempOperator = this.gsmOperatorRepository.findOne(operator.getId());
    		this.gsmOperatorRepository.delete(tempOperator);
    		return true;
    	} else {
    		return false;
    	}
    }   
    
    
    /**
     * Saves a gsm prefix, but first checks for duplicate name.
     * @param prefix
     * @return  true for success or false for failure 
     */    
    public boolean saveGsmPrefix(GsmPrefix prefix) {
    	Long total = gsmPrefixRepository.countByCode(prefix.getCode(), 
    			prefix.getId() != null ? prefix.getId() : 0);
    	if(total == 0) {
    		this.gsmPrefixRepository.save(prefix);
    		return true;
    	} else {
    		return false;
    	}
    }
    
    /**
     * Deletes a gsm prefix, but first checks for existing dependencies.
     * @param prefix
     * @return  true for success or false for failure (ONLY due to dependencies)
     */
    public boolean deleteGsmPrefix(GsmPrefix prefix) {
    	Long total = userRepository.countByGsmPrefix(prefix.getId());
    	if(total == 0) {
    		// get a fresh copy of the entity
    		GsmPrefix tempPrefix = this.gsmPrefixRepository.findOne(prefix.getId());
    		this.gsmPrefixRepository.delete(tempPrefix);
    		return true;
    	} else {
    		return false;
    	}
    }      
    
    @Async
    public Future<String> saveUserGsmNumberAndSendConfirmationCode(User user, String message) {
    	/* Updates the user's data in a separate transaction.
    	   This is necessary in order to avoid any long-running transaction waiting to commit just because 
    	   the SMS submission takes too long due to its synchronous nature
    	 */
    	if(user.getId() != null && user.getId() > 0) updateUserGSMInformation(user); 
    	 
    	try {
    		// get the credentials for GSM access from the configuration table
    		Configuration smsGatewayUsernameConfig = configurationService.find(ConfigurationType.SMS_GATEWAY_USERNAME.getKey());
    		Configuration smsGatewayPasswordConfig = configurationService.find(ConfigurationType.SMS_GATEWAY_PASSWORD.getKey());

            String username = smsGatewayUsernameConfig.getValue();
            String password = smsGatewayPasswordConfig.getValue();
            
            // construct and the send the message
    		SmsMultiSender cr = new SmsMultiSender(); 
	    	cr.SetUsername(username);
	    	cr.SetPassword(password);

	    	String gsmNumber = "0" + String.valueOf(user.getGsmVerification().getGsmPrefix().getCode()) + 
	    			String.valueOf(user.getGsmVerification().getGsmNumber());
	    	String confirmationCode = String.valueOf(gsmNumber.hashCode()).substring(1,7);
	    	
	    	//log.debug(">>> confirmationCode = " + confirmationCode);
	    	
	    	message = message.replace("{0}", confirmationCode);
	    	
	    	cr.AddMessage(gsmNumber, message); 
	    	Object[] result = cr.SendMessage();
	    	String returnCode = (String)result[0];  // the return code (should be the GSM number for success)

	    	//System.out.println(">>> confirmationCode = " + confirmationCode);
	    	//System.out.println(">>> returnCode = " + returnCode + ", confirmationCode = " + confirmationCode);
	    	// check the return value (2 characters from the gateway
	    	if(returnCode != null && returnCode.length() > 2) {  // that is.. the submission was successful return the confirmation code
	    		return new AsyncResult<String>(confirmationCode);
	    	} else { // else we have a standard error code; so log the error code and return null
	    		Log.error("While verifying the GSM number " + gsmNumber + " the SMS gateway return the code " + returnCode);
	    	}
    	} catch( Exception e) {
    		Log.error(e);
    	}

    	return new AsyncResult<String>(null);
    }

    
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    private void updateUserGSMInformation(User user) {
    	userRepository.save(user);
    }
}

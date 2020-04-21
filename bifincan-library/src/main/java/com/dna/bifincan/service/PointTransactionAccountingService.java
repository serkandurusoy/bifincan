package com.dna.bifincan.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dna.bifincan.common.util.DateUtils;
import com.dna.bifincan.common.util.Period;
import com.dna.bifincan.model.config.Configuration;
import com.dna.bifincan.model.order.PointTransactionAccounting;
import com.dna.bifincan.model.type.PointTransactionType;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.model.user.UserLevelValue;
import com.dna.bifincan.repository.order.PointTransactionAccountingRepository;
import com.dna.bifincan.repository.user.UserRepository;

@Service
@Named("pointTransactionAccountingService")
public class PointTransactionAccountingService {
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(ConfigurationService.class);

    @Inject
    private ConfigurationService configurationService;
    @Inject
    private UserRepository userRepository;
    @Inject
    private PointTransactionAccountingRepository pointTransactionAccountingRepository;

    public PointTransactionAccountingService() { }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int addConfigurationPoints(User user, PointTransactionType pointTransactionType) {
        Configuration configuration = null;

        String configKey = pointTransactionType.getKey();
        if (configKey != null) {
            configuration = this.configurationService.find(configKey);
        }

        if (configuration != null) {

            Integer points = null;
            try {
                points = Integer.parseInt(configuration.getValue());
              //  log.debug("points in configuration@" + points);
            } catch (NumberFormatException e) {
                throw new RuntimeException(configKey + " configuration, points configuration error.");
            }

            if (points != null && points.intValue() > 0) {
                return this.addPoints(user, pointTransactionType, points);
            } else {
                throw new RuntimeException(configKey + " configuration cannot be negative.");
            }

        } else {
            throw new RuntimeException(configKey + " configuration not found.");
        }

    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int addPoints(User user, PointTransactionType type, Integer points) {
        if (type == null) {
            throw new RuntimeException("The transaction type is not defined.");
        }

        PointTransactionAccounting transaction = null;

        // 1. Calculate the bounds for current date 
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());

        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Date from = today.getTime();

        today.set(Calendar.HOUR_OF_DAY, 23);
        today.set(Calendar.MINUTE, 59);
        today.set(Calendar.SECOND, 59);

        Date to = today.getTime();

        // 2. Find the current transaction accounting record (if there is any)
        // TODO: fix the query - it returns multiple records now..
      //  log.debug("user name @" + user.getUsername() + ", form @" + from + ", to @" + to);
        transaction = pointTransactionAccountingRepository.findTransactionByUsernameAndRecentDate(user.getUsername(), from, to);

        if (transaction == null) { // there is no record for the current day - create a new one
            transaction = new PointTransactionAccounting();

            transaction.setGenericSurveyPoints(0);
            transaction.setInvitationPoints(0);
            transaction.setInvitationSuccessPoints(0);
            transaction.setLoginPoints(0);
            transaction.setProductOrderPoints(0);
            transaction.setProductSurveyPoints(0);
            transaction.setQuizPoints(0);
            transaction.setRatingPoints(0);
            transaction.setSignupPoints(0);
            transaction.setBlogCommentPoints(0);
            transaction.setProductCommentPoints(0);
            transaction.setSocialPoints(0);
            transaction.setVotingPoints(0);
            
            transaction.setUser(user);
            transaction.setCreateDate(new Date());
        }

        // 3. Store the points into the right field(s)
        int pointsToBeAdded = 0;

        if (points != null) {
            pointsToBeAdded = points; // store it in another variable for possible post-calculations
        }

        switch (type) {
            // config case
            case INVITIATION_POINTS:
                transaction.setInvitationPoints(transaction.getInvitationPoints() + pointsToBeAdded);
                break;
            case INVITIATION_SUCCESS_POINTS:
                transaction.setInvitationSuccessPoints(transaction.getInvitationSuccessPoints() + pointsToBeAdded);
                break;
            case LOGIN_POINTS:
                transaction.setLoginPoints(pointsToBeAdded);
                if (transaction.getId() != null && transaction.getLoginPoints() > pointsToBeAdded) {
                    pointsToBeAdded = 0;  // for an existing transaction (for the current date) reset the value 
                }
                break;
            case SIGNUP_POINTS:
                transaction.setSignupPoints(pointsToBeAdded);
                break;
            case RATING_POINTS:
                transaction.setRatingPoints(transaction.getRatingPoints() + pointsToBeAdded);
                break;
            case BLOG_COMMENT_POINTS:
                transaction.setBlogCommentPoints(transaction.getBlogCommentPoints() + pointsToBeAdded);
                break;
            case PRODUCT_COMMENT_POINTS:
                transaction.setProductCommentPoints(transaction.getProductCommentPoints() + pointsToBeAdded);
                break;
            case SOCIAL_POINTS:
                transaction.setSocialPoints(transaction.getSocialPoints() + pointsToBeAdded);
                break;
                
            // custom cases	
            case PRODUCT_ORDER:
                transaction.setProductOrderPoints(transaction.getProductOrderPoints() + pointsToBeAdded);
                pointsToBeAdded = 0 - points;  // turn into negative value
                break;
            case PRODUCT_SURVEY:
                transaction.setProductSurveyPoints(transaction.getProductSurveyPoints() + pointsToBeAdded);
                break;
            case QUIZ_POINTS:
                transaction.setQuizPoints(transaction.getQuizPoints() + pointsToBeAdded);
                break;

            // non-config case
            case VOTING_POINTS:
                transaction.setVotingPoints(transaction.getVotingPoints() + pointsToBeAdded);
                break;                
            case GENERIC_SURVEY_POINTS:
                pointsToBeAdded = 1;  // standard value; not in the config
                transaction.setGenericSurveyPoints(transaction.getGenericSurveyPoints() + pointsToBeAdded);
                break;
        }

        //3. Create/update the record
        pointTransactionAccountingRepository.save(transaction);

        //4. Update the user's point balance in his/her account
        User curUser = userRepository.findByUsername(user.getUsername());

        int totalPoints = curUser.getPointsBalance() + pointsToBeAdded;
        curUser.setPointsBalance(totalPoints); // set the result

        userRepository.save(curUser); // update the user's record*/

        return totalPoints;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<PointTransactionAccounting> findTransactionsByDate(String username, Date selectionDate) {
        // 1. find the first (from) and last date of the selected month (to)
        Calendar fromCal = Calendar.getInstance();
        fromCal.setTime(selectionDate);

        fromCal.set(Calendar.DAY_OF_MONTH, 1);

        Date from = fromCal.getTime();

        fromCal.set(Calendar.HOUR_OF_DAY, 23);
        fromCal.set(Calendar.MINUTE, 59);
        fromCal.set(Calendar.SECOND, 59);
        fromCal.set(Calendar.DAY_OF_MONTH, fromCal.getActualMaximum(Calendar.DAY_OF_MONTH));

        Date to = fromCal.getTime();

        // 2. get the transaction records for the specified month
        List<PointTransactionAccounting> transactions = pointTransactionAccountingRepository.findTransactionsByUsernameAndDateSelection(username, from, to);

        return transactions;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Long calculateTotalPoints(String username) {
        return pointTransactionAccountingRepository.findTotalPointsByUsername(username);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Long calculateTotalGainedPoints(String username) {
        return pointTransactionAccountingRepository.findTotalGainedPointsByUsername(username);
    }
    
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Object> calculateGroupedTotalPoints(String username) {
        return pointTransactionAccountingRepository.findGroupedTotalPoints(username);
    }

    @SuppressWarnings("rawtypes")
	public List findLatestPointsStatistics() {
        return pointTransactionAccountingRepository.findLatestPointsStatistics();
    }

    public float calculateWeeklyAverage() {
     //   log.debug("call calculateWeeklyAverage@");
        Period lastWeek = DateUtils.lastWeekSpan();
        
        Long points=this.pointTransactionAccountingRepository.weeklyTotalPointsWithInvitationPoints(lastWeek.getFrom(), lastWeek.getTo());      
        Long users=this.userRepository.countActiveUsersUntilPreviousWeek(lastWeek.getFrom());
        
        if(users!=null && users.longValue()>0){
            return (float)(points.doubleValue()/users);
        }
        
        return 0.0f;
    }

    public float calculateWeeklyUserTotal(String username) {
      //  log.debug("call calculateWeeklyUserTotal@");
        Period lastWeek = DateUtils.lastWeekSpan();
        
        Long points=this.pointTransactionAccountingRepository.weeklyUserTotalPointsWithInvitationPoints( username, lastWeek.getFrom(), lastWeek.getTo());
        
        if(points!=null){
            return points.floatValue();
        }
        
        return 0.0f;
    }
}

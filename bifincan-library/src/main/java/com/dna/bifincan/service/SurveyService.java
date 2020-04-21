package com.dna.bifincan.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dna.bifincan.exception.DuplicateUserResponseException;
import com.dna.bifincan.exception.InvalidUserResponseException;
import com.dna.bifincan.exception.SurveyException;
import com.dna.bifincan.model.dto.SurveyDTO;
import com.dna.bifincan.model.dto.SurveyDataDTO;
import com.dna.bifincan.model.dto.SurveyQuestionDTO;
import com.dna.bifincan.model.order.OrderDetails;
import com.dna.bifincan.model.order.SurveyProductStatusForOrderDetails;
import com.dna.bifincan.model.products.Brand;
import com.dna.bifincan.model.products.Product;
import com.dna.bifincan.model.products.ProductCategoryLevelThree;
import com.dna.bifincan.model.quiz.QuizAnswer;
import com.dna.bifincan.model.quiz.QuizOption;
import com.dna.bifincan.model.quiz.QuizQuestion;
import com.dna.bifincan.model.rating.Rating;
import com.dna.bifincan.model.survey.Survey;
import com.dna.bifincan.model.survey.SurveyAnswer;
import com.dna.bifincan.model.survey.SurveyBrand;
import com.dna.bifincan.model.survey.SurveyBrandProductCategory;
import com.dna.bifincan.model.survey.SurveyCriteria;
import com.dna.bifincan.model.survey.SurveyCycle;
import com.dna.bifincan.model.survey.SurveyInstance;
import com.dna.bifincan.model.survey.SurveyOption;
import com.dna.bifincan.model.survey.SurveyProduct;
import com.dna.bifincan.model.survey.SurveyProductCategory;
import com.dna.bifincan.model.survey.SurveyQuestion;
import com.dna.bifincan.model.survey.SurveyQuestionCriteria;
import com.dna.bifincan.model.survey.UserSession;
import com.dna.bifincan.model.type.ConfigurationType;
import com.dna.bifincan.model.type.PointTransactionType;
import com.dna.bifincan.model.type.SurveyQuestionCriteriaType;
import com.dna.bifincan.model.type.SurveyType;
import com.dna.bifincan.model.user.User;
import com.dna.bifincan.repository.brand.BrandRepository;
import com.dna.bifincan.repository.order.OrderDetailsRepository;
import com.dna.bifincan.repository.order.SurveyProductStatusForOrderDetailsRepository;
import com.dna.bifincan.repository.products.ProductCategoryLevelThreeRepository;
import com.dna.bifincan.repository.products.ProductRepository;
import com.dna.bifincan.repository.survey.SurveyAnswerRepository;
import com.dna.bifincan.repository.survey.SurveyBrandProductCategoryRepository;
import com.dna.bifincan.repository.survey.SurveyBrandRepository;
import com.dna.bifincan.repository.survey.SurveyCriteriaRepository;
import com.dna.bifincan.repository.survey.SurveyCycleRepository;
import com.dna.bifincan.repository.survey.SurveyInstanceRepository;
import com.dna.bifincan.repository.survey.SurveyOptionRepository;
import com.dna.bifincan.repository.survey.SurveyProductCategoryRepository;
import com.dna.bifincan.repository.survey.SurveyProductRepository;
import com.dna.bifincan.repository.survey.SurveyProfileRepository;
import com.dna.bifincan.repository.survey.SurveyQuestionCriteriaRepository;
import com.dna.bifincan.repository.survey.SurveyQuestionRepository;
import com.dna.bifincan.repository.survey.SurveyRepository;
import com.dna.bifincan.repository.survey.UserSessionRepository;
import com.dna.bifincan.repository.user.UserRepository;

@Service
@Named("surveyService")
public class SurveyService {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(SurveyService.class);
    @Inject
    private SurveyRepository surveyRepository;
    @Inject
    private SurveyOptionRepository surveyOptionRepository;
    @Inject
    private SurveyQuestionRepository surveyQuestionRepository;
    @Inject
    private SurveyAnswerRepository surveyAnswerRepository;
    @Inject
    private CriteriaService criteriaService;
    @Inject
    private UserRepository userRepository;
    @Inject
    private SurveyCriteriaRepository surveyCriteriaRepository;
    @Inject
    private SurveyQuestionCriteriaRepository surveyQuestionCriteriaRepository;
    @Inject
    private PointTransactionAccountingService pointTransactionAccountingService;
    @Inject
    private OrderService orderService;
    @Inject
    private SurveyProductRepository surveyProductRepository;
    @Inject
    private RatingService ratingService;
    @Inject
    private BrandRepository brandRepository;
    @Inject
    private BrandService brandService;
    @Inject
    private SurveyProductStatusForOrderDetailsRepository surveyProductStatusForOrderDetailsRepository;
    @Inject
    private QuizService quizService;
    @Inject
    private OrderDetailsRepository orderDetailsRepository;
    @Inject
    private UserSessionRepository userSessionRepository;
    @Inject
    private SurveyProfileRepository surveyProfileRepository;
    @Inject
    private SurveyBrandRepository surveyBrandRepository;
    @Inject
    private SurveyBrandProductCategoryRepository surveyBrandProductCategoryRepository;
    @Inject
    private SurveyProductCategoryRepository surveyProductCategoryRepository;
    @Inject
    private ProductRepository productRepository;
    @Inject
    private SurveyInstanceRepository surveyInstanceRepository;
    @Inject
    private SurveyCycleRepository surveyCycleRepository;
    @Inject
    private ProductCategoryLevelThreeRepository productCategoryLevelThreeRepository;

    public SurveyService() {
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SurveyQuestionDTO getNextQuestionData(User user, String sessionId) throws SurveyException {
        SurveyQuestionDTO questionData = new SurveyQuestionDTO();

        // find user's last survey (if any)
        Survey survey = getUserLastSurvey(user);

        if (survey != null) { // get the next question of the last survey
            questionData.setSurveyQuestion(getNextSurveyQuestion(survey, user, false));
            if (survey instanceof SurveyProduct) {
                questionData.setType(SurveyQuestionDTO.Type.PRODUCT_SURVEY);
                // fetch the product information
                List<SurveyProductStatusForOrderDetails> orderDetailRefs = surveyProductStatusForOrderDetailsRepository.
                        findBySurveyAndUser(survey, user);
                if (orderDetailRefs != null && !orderDetailRefs.isEmpty()) {
                    questionData.setOrderDetails(orderDetailRefs.get(0).getOrderDetail());
                }
            } else {
                questionData.setType(SurveyQuestionDTO.Type.GENERIC_SURVEY);
            }
        }

        // if there is not any survey or no other survey's question 
        if (survey == null || questionData.getSurveyQuestion() == null) {
            SurveyDataDTO surveyData = null;
            if (orderService.isLastOrderSurveyable(user) && orderService.getRemainingDaysForNextAction(user,
                    ConfigurationType.TIME_REQUIRED_BEFORE_PRODUCT_SURVEYS) == 0) { // if there is an order with surveyable order items
                // 1. Here we know that there are items surveyable.
                // 2. Note: The flag of items is set to false only if ALL of the survey items have been passed.
                //	  So check below for an existence of a survey item regardless of the flag.
                // if(ratingService.findByOrderDetailAndUser(orderDetail, user)) {// check for rating
                // note: saving a quiz's answer (in another method) also SHOULD set the surveyable flag over order details to false

                // this will fetch a product product based survey for an order item
                surveyData = getProductSurveyData(user);

                if (surveyData.getSurvey() != null) { // # normal survey type
                    questionData.setType(SurveyQuestionDTO.Type.PRODUCT_SURVEY);
                    questionData.setSurveyQuestion(getNextSurveyQuestion(surveyData.getSurvey(),
                            user, true)); // fetch the first one question 
                    questionData.setSurveyRestarted(surveyData.isSurveyAlreadyTaken());
                } else if (surveyData.getRating() != null) {  // # rating type
                    // store a reference to the underlying orderDetails
                    questionData.setType(SurveyQuestionDTO.Type.RATING);
                    questionData.setRating(surveyData.getRating());
                } else if (surveyData.getQuizQuestion() != null) {  // # rating type
                    // store a reference to the underlying orderDetails
                    questionData.setType(SurveyQuestionDTO.Type.QUIZ);
                    questionData.setQuizQuestion(surveyData.getQuizQuestion());
                }

                // store a reference to the orderDetails
                questionData.setOrderDetails(surveyData.getOrderDetails());
                questionData.setSurveyStarted(surveyData.isSurveyStarted());
                // The outcome will be a SurveyData object with either a survey or quiz or rating info being set
                // and other flags as well which will indicate each case.
            } else { // no order or other surveyable order items or remaining days for product surveys > 0; so go ahead
                int count = 1;
                while (count <= 2) {
                    surveyData = getRandomSurveyData(user, sessionId, count > 1 ? true : false); // false: gets the last cycle; true: uses 0
                    if (surveyData.getSurvey() != null) {
                        Long lastCycleId = surveyData.getLastCycleId();
                        boolean needForCycle = (lastCycleId == null || lastCycleId == 0 || count > 1) ? true : false;
                        if (needForCycle == true) {
                            if (surveyAnswerRepository.countSessionIdOccurences(sessionId) > 0) {
                                //log.debug(">>> resetting survey.... = " + surveyData.getSurvey().getId());
                                return questionData;  // invalid cycle request (within the same session) ... reset it
                            }
                        }

                        questionData.setType(SurveyQuestionDTO.Type.GENERIC_SURVEY);
                        questionData.setSurveyQuestion(getNextSurveyQuestion(surveyData.getSurvey(),
                                user, true)); // fetch the first one question 
                        questionData.setSurveyRestarted(surveyData.isSurveyAlreadyTaken());
                        questionData.setSurveyStarted(surveyData.isSurveyStarted());
                        questionData.setLastCycleId(surveyData.getLastCycleId());

                        questionData.setNeedForNewCycle(needForCycle); // it will be used when saving the answer

                        break;
                    } else {
                        count++;
                    }
                }
            }

        }
        return questionData;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    private SurveyDataDTO getProductSurveyData(User user) throws SurveyException {
        SurveyDataDTO surveyData = null;//new SurveyData();

        // fetch the last order of user
        com.dna.bifincan.model.order.Order lastOrder = orderService.getLastOrder(user);

        if (lastOrder != null) {
            surveyData = new SurveyDataDTO();

            if (lastOrder.getListOfOrderDetails() != null && !lastOrder.getListOfOrderDetails().isEmpty()) {
                // iterate through the list of order details
                for (OrderDetails orderDetails : lastOrder.getListOfOrderDetails()) {
                    if (orderDetails.isSurveyCompleted() == false) { // there are pending surveys
                        SurveyProductStatusForOrderDetails productSurveyStatus = surveyProductStatusForOrderDetailsRepository.
                                findByUserAndOrderDetail(user, orderDetails);
                        // there is no product survey initiated for this order detail or it is pending 
                        if (productSurveyStatus == null || !productSurveyStatus.isSurveyCompleted()) {
                            Survey survey = null;
                            List<SurveyProduct> surveys = surveyProductRepository.findProductSurveysByProduct(orderDetails.getProduct());
                            if (surveys != null && !surveys.isEmpty()) {
                                survey = surveys.get(0);
                            } else {
                                throw new SurveyException(SurveyException.NO_PRODUCT_SURVEY,
                                        "Nor survey for ordered product " + orderDetails.getProduct().getId());
                            }
                            surveyData.setOrderDetails(orderDetails);
                            surveyData.setSurvey(survey);
                            if (productSurveyStatus == null) {
                                surveyData.setSurveyStarted(true);
                            }
                            break;
                        } else { // there is a completed product survey for this order details 
                            // ... so since the orderDetails.isSurveyCompleted() = false it should have pending ratings or quizes
                            Rating rating = ratingService.findByOrderDetailAndUser(orderDetails, user);
                            if (rating == null) { // no rating.. take it
                                rating = new Rating();
                                rating.setOrderDetail(orderDetails);
                                surveyData.setOrderDetails(orderDetails);
                                surveyData.setRating(rating);
                                surveyData.setSurveyStarted(true);
                                break;
                            } else {
                                // TODO:  consider also the active quiz and options
                                List<QuizQuestion> quizQuestions = quizService.findAllQuizQuestionByProduct(orderDetails);
                                if (quizQuestions != null && !quizQuestions.isEmpty()) {
                                    // at the moment we always have 1 question; however find the first active one
                                    for (QuizQuestion quizQuestion : quizQuestions) {
                                        if (quizQuestion.isActive()) {
                                            surveyData.setQuizQuestion(quizQuestions.get(0));
                                            surveyData.setOrderDetails(orderDetails);
                                            surveyData.setSurveyStarted(true);
                                            break;
                                        }
                                    }
                                }

                                if (surveyData.getQuizQuestion() == null) {
                                    throw new SurveyException(SurveyException.NO_QUIZ_SURVEY,
                                            "There is no quiz question for product " + +orderDetails.getProduct().getId());
                                }
                            }
                        }
                    }
                }
            }
        }

        return surveyData;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    private SurveyDataDTO getRandomSurveyData(User user, String sessionId, Boolean needForCycle) {
        // 1. Search for new surveys by random type which are either brand new ones or old ones but not taken yet (for brands & product categories) 
        SurveyDataDTO surveyData = fetchNewSurvey(user);

        // 2. If there is no new survey fetch an old survey ie. already taken in the past considering the current order of user's answers though
        if (surveyData.getSurvey() == null) {
            surveyData = fetchOldSurvey(user, needForCycle);
        }

        return surveyData;
    }

    public int checkForNewSurveyCycleByUser(User user) {
        if (surveyAnswerRepository.findLastCycleByUser(user.getId()) == null) {
            return 1;
        }

        SurveyDataDTO surveyData = fetchNewSurvey(user);

        if (surveyData.getSurvey() == null) {
            //log.debug(">>> NO new survey found...");
            surveyData = fetchOldSurvey(user, false);
            if (surveyData.getSurvey() == null) {
                //log.debug(">>> NO old survey found...");
                return 1; // no survey (new & old); need for new cycle
            } else {
                //log.debug(">>> OLD survey found !");
                return 0;  // there is an old survey
            }
        } /*else {
         log.debug(">>> NEW survey found !");
         }*/

        return -1;  // do nothing
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    private Map<String, Long> convertToHashes(List<Object[]> data) {
        Map<String, Long> result = new HashMap<String, Long>();

        if (data != null && !data.isEmpty()) {
            Object[] row = null;
            //for(int i=data.size()-1; i >= 0; i--) {
            for (int i = 0; i < data.size(); i++) {
                row = data.get(i);
                //log.debug(">> (Long)row[0] = " + (Long)row[0]);
                result.put(String.format("%d:%d:%d", row[0].hashCode(),
                        (row[1] != null ? row[1].hashCode() : 0),
                        (row[2] != null ? row[2].hashCode() : 0)),
                        (Long) row[0]);
            }
        }

        return result;
    }

    @SuppressWarnings("rawtypes")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SurveyDataDTO fetchOldSurvey(User user, Boolean needForCycle) {
        SurveyDataDTO surveyData = new SurveyDataDTO();
        //log.debug(">> R0: needForCycle = " + needForCycle + ", user.getId() = " + user.getId());
        Survey survey = null;

        // find the last cycle for this specific user
        Long lastCycleId = null;
        if (needForCycle != null && needForCycle == false) {
            //log.debug(">> R1: taking lastCycleId ...");
            lastCycleId = surveyAnswerRepository.findLastCycleByUser(user.getId());
            //log.debug(">> R2: fetched lastCycleId == " + lastCycleId);
        }
        if (lastCycleId == null) {
            lastCycleId = 0L;
        }
        //log.debug(">> R3: fetched lastCycleId == " + lastCycleId);

        // find the survey ids taken by the user within this last cycle
        List<Object[]> lastCycleSurveys = surveyAnswerRepository.findSurveyDataByCycle(lastCycleId);
        Map<String, Long> lastCycleSurveyIds = convertToHashes(lastCycleSurveys);
        //log.debug(">> R4: lastCycleSurveyIds = " + lastCycleSurveyIds);

        // find the survey ids initially taken by the user (preserve the order)
        List<Object[]> initialSurveys = surveyRepository.findInitialTakenSurveyDataByUser(user.getId());
        Map<String, Long> userInitialSurveyIds = convertToHashes(initialSurveys);
        //log.debug(">> R5: userInitialSurveyIds = " + userInitialSurveyIds);

        userInitialSurveyIds.entrySet().removeAll(lastCycleSurveyIds.entrySet());
        //log.debug(">> R6: userInitialSurveyIds = " + userInitialSurveyIds);

        if (userInitialSurveyIds != null && userInitialSurveyIds.size() > 0) {
            Set keys = userInitialSurveyIds.keySet();

            List keysList = Arrays.asList(keys.toArray());
            //System.out.println(">> R7a: keysList = " + keysList);
            Collections.shuffle(keysList, new Random());
            //System.out.println(">> R7b: keysList = " + keysList);

            Iterator it = keysList.iterator();
            // try to find a valid untaken survey 
            while (it.hasNext()) {
                String key = (String) it.next();
                survey = surveyRepository.findOne((Long) userInitialSurveyIds.get(key));

                //log.debug(">> R8: checking the survey id : " + survey.getId());

                boolean isInValid = false;

                if (survey.getType().equals(SurveyType.BRAND.name()) || survey.getType().equals(SurveyType.BRAND_PROD_CAT.name())) {
                    if (survey.getType().equals(SurveyType.BRAND.name())) {
                        // check for untaken brands for this survey and cycle
                        if (surveyAnswerRepository.countNonTakenBrandSurveysByUserAndCycleId(user, survey, lastCycleId) == 0) {
                            isInValid = true;  // no other brand is available for this survey.. reject it
                        }
                    } else if (survey.getType().equals(SurveyType.BRAND_PROD_CAT.name())) {
                        SurveyBrandProductCategory brandProdCatSurvey = (SurveyBrandProductCategory) survey;
                        String[] keyComps = key.split(":"); // split the identifier into its tokens (survey, brand, category)

                        // search if the survey has been taken for the specific brand & product category in the last cycle 
                        Long nonTakenNum = brandRepository.countTakenBrandSurveysByUserAndBrandAndProductCategoryAndCycleId(user, brandProdCatSurvey, Long.parseLong(keyComps[1]),
                                Long.parseLong(keyComps[2]), lastCycleId);
                        //log.debug(">> A1: nonTakenNum = " + nonTakenNum);

                        if (nonTakenNum > 0) {// the current pair of brand/product category has been taken; we need to pick up next survey 
                            isInValid = true; //.. so reject it
                            //log.debug(">> A2: inValid survey : " + survey.getId() + ", Long.parseLong(keyComps[1]) = " + Long.parseLong(keyComps[1])  + 
                            //		", Long.parseLong(keyComps[2]) = " + Long.parseLong(keyComps[2]) + ", lastCycleId = " + lastCycleId);
                        } else { // check for missing product categories (normally due to possible to removed associations)
                            ProductCategoryLevelThree category = productCategoryLevelThreeRepository.findOne(Long.parseLong(keyComps[2]));

                            // check if the survey CURRENTLY contains the product category  (because it could have it the past but not NOW)  
                            if (brandProdCatSurvey.getProductCategories().contains(category)) {
                                //log.debug(">>> A3: comp[2] = " + Long.parseLong(keyComps[2]) + ", comp[1] = " + Long.parseLong(keyComps[1]));

                                // check also if the brand CURRENTLY contains the product category  (because it could have it the past but not NOW)  
                                Long count = brandRepository.checkCategoryMemberShip(Long.parseLong(keyComps[1]), category);
                                //log.debug(">> A4: count = " + count);
                                if (count == 0) { // the product category is not a member of the brand's categories any more 
                                    isInValid = true;
                                    //log.debug(">> A5: inValid survey (due to missing product category) : " + survey.getId() + ", Long.parseLong(keyComps[1]) = " + Long.parseLong(keyComps[1])  + 
                                    //		", Long.parseLong(keyComps[2]) = " + Long.parseLong(keyComps[2]) + ", lastCycleId = " + lastCycleId);
                                }
                            } else { // the product category is not a member of the survey's categories any more 
                                //log.debug(">> A6: prod cat not contained in " + keyComps[2] + " survey " + brandProdCatSurvey.getId() + " ... so reject it");
                                isInValid = true; //.. so reject it
                            }
                        }
                    }

                } else if (survey.getType().equals(SurveyType.PROD_CAT.name())) {

                    //log.debug(">>> B2a. lastCycleId = " + lastCycleId);
                    SurveyProductCategory surveyProdCat = surveyProductCategoryRepository.findByIdAndFetchCategories(survey.getId());
                    //log.debug(">>> B2b. checking (prod cat) survey = " + surveyProdCat.getId());

                    // fetch the taken ids for the given survey and last cycle
                    List<Long> takenIds = surveyAnswerRepository.fetchTakenProdCatsByUserAndSurveyIdAndCycleId(user, survey.getId(), lastCycleId);
                    //log.debug(">>> B2c. takenIds = " + takenIds);

                    // fetch the CURRENT (because it could have more in the past) survey's product categories
                    List<Long> surveyCatIds = productCategoryLevelThreeRepository.findIdsByEntities(surveyProdCat.getProductCategories());
                    //log.debug(">>> B2d. surveyCatIds = " + surveyCatIds);

                    if (takenIds != null && surveyCatIds != null
                            && takenIds.containsAll(surveyCatIds)) { // if survey's product categories have ALL been taken in the past..
                        //log.debug(">>> B2e. rejecting (prod_cat) survey " + survey.getId()); 
                        isInValid = true; // .. reject it
                    }

                } else if (survey.getType().equals(SurveyType.PROFILE.name())) {
                    //log.debug(">>> B1a. lastCycleId = " + lastCycleId);

                    //log.debug(">>> B1a. checking (prof) survey " + survey.getId());
                    if (surveyAnswerRepository.countSurveyAnswersByUserAndSurveyAndCycleId(user, survey, lastCycleId) > 0) {
                        //log.debug(">>> B1a. rejecting (prof) survey " + survey.getId());
                        isInValid = true;
                    }

                }

                // when the survey is new (especially for BRAND & BRAND_PROD_CAT this also mean that 
                //		it might have been taken BUT NOT for all brands) AND it is valid upon user criteria 
                if (!isInValid && validateSurveyByCriteria(survey, user)) {
                    break;  // keep the survey, abandon the loop and return the survey
                } else { // not valid upon user criteria
                    //log.debug(">> R9: rejecting survey : " + survey.getId());
                    survey = null;  // reset the variablef
                }
            }
        }

        if (survey != null) {  // if a survey has been found after all
            //log.debug(">> R10: returning survey : " + survey.getId());
            surveyData.setSurvey(survey);
            surveyData.setSurveyAlreadyTaken(false);  // true: indicates that for BRAND & BRAND_PROD_CAT the survey is already taken - restarted 
            surveyData.setSurveyStarted(true);  // this indicates that the survey is new (for UI purposes)
            surveyData.setLastCycleId(lastCycleId);
        }

        return surveyData;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SurveyDataDTO fetchNewSurvey(User user) {
        //log.debug(">> D1: fetching new survey");
        SurveyDataDTO surveyData = new SurveyDataDTO();

        Survey survey = null;

        // create structure with randomly shuffled survey types
        List<Integer> typeValues = new ArrayList<Integer>();

        // add all the types to the list except Product & Profiles ones
        typeValues.add(SurveyType.BRAND.ordinal());
        typeValues.add(SurveyType.BRAND_PROD_CAT.ordinal());
        typeValues.add(SurveyType.PROD_CAT.ordinal());

        //System.out.println("@@@ 1.typeValues = " + typeValues);

        // shuffle the list
        Collections.shuffle(typeValues, new Random());

        // add the Profile survey type to the list as 1st one (always)
        typeValues.add(0, SurveyType.PROFILE.ordinal());

        //System.out.println("@@@ 2.typeValues = " + typeValues);

        //for(int i=0;i<SurveyType.values().length - 1;i++) typeValues.add(i); // CAREFUL: should exclude the 'prod' surveys (it is the last one therefore ..length-1)

        //Collections.shuffle(typeValues, new Random());

        int currentSurveyIdx = 0; // holds the current index in the survey types structure 

        Long lastCycleId = null;

        do {  // till the maximum number of survey types or a survey has been found
            // get the current survey type
            SurveyType randomSelSurveyType = SurveyType.values()[typeValues.get(currentSurveyIdx++)];

            //	System.out.println("@@@ 3.selecting type = " + randomSelSurveyType);
            List<Long> activeSurveyIds = null;

            switch (randomSelSurveyType) {
                case BRAND_PROD_CAT:
                    activeSurveyIds = surveyBrandProductCategoryRepository.findActiveByProdCatPriority();
                    //log.debug(">>> BRAND_PROD_CAT 1: activeSurveyIds : " + activeSurveyIds);
                    Collections.shuffle(activeSurveyIds, new java.util.Random());
                    //log.debug(">>> D2: BRAND_PROD_CAT 2: activeSurveyIds : " + activeSurveyIds);

                    List<Long> notPrioritized = surveyBrandProductCategoryRepository.findActiveNotInList(!activeSurveyIds.isEmpty() ? activeSurveyIds : Arrays.asList(0L));
                    Collections.shuffle(notPrioritized, new java.util.Random());

                    activeSurveyIds.addAll(notPrioritized);
                    //log.debug(">>> D3: BRAND_PROD_CAT 3: activeSurveyIds : " + activeSurveyIds);

                    break;
                case PROD_CAT:
                    activeSurveyIds = surveyProductCategoryRepository.findActiveByProdCatPriority(true);
                    //log.debug(">>> PROD_CAT : activeSurveyIds 1: " + activeSurveyIds);
                    Collections.shuffle(activeSurveyIds, new java.util.Random());
                    //log.debug(">>> PROD_CAT : activeSurveyIds 2: " + activeSurveyIds);

                    activeSurveyIds.addAll(surveyProductCategoryRepository.findActiveByProdCatPriority(false));
                    //log.debug(">>> PROD_CAT : activeSurveyIds 3: " + activeSurveyIds);
                    break;
                case PROFILE:
                    activeSurveyIds = new ArrayList<Long>();

                    List<Long> activePrioritizedSurveyIds = surveyProfileRepository.findActiveByPriority(true);
                    //log.debug(">>> 1. activePrioritizedSurveyIds = " + activePrioritizedSurveyIds);
                    Collections.shuffle(activePrioritizedSurveyIds, new Random()); // shuffle the ids
                    //log.debug(">>> 2. activePrioritizedSurveyIds = " + activePrioritizedSurveyIds);

                    List<Long> activeNonPrioritizedSurveyIds = surveyProfileRepository.findActiveByPriority(false);
                    //log.debug(">>> 3. activeNonPrioritizedSurveyIds = " + activeNonPrioritizedSurveyIds);
                    Collections.shuffle(activeNonPrioritizedSurveyIds, new Random()); // shuffle the ids
                    //log.debug(">>> 4. activeNonPrioritizedSurveyIds = " + activeNonPrioritizedSurveyIds);	

                    if (activePrioritizedSurveyIds != null) {
                        activeSurveyIds.addAll(activePrioritizedSurveyIds);
                    }
                    if (activeNonPrioritizedSurveyIds != null) {
                        activeSurveyIds.addAll(activeNonPrioritizedSurveyIds);
                    }
                    //log.debug(">>> " + randomSelSurveyType + " : activeSurveyIds : " + activeSurveyIds);
                    break;
                default: // actually ... the remaining types ie. profile & brands 
                    activeSurveyIds = surveyRepository.findActiveSurveyIdsByType(randomSelSurveyType.name());
                    ////log.debug(">>> 1. activeSurveyIds = " + activeSurveyIds);
                    Collections.shuffle(activeSurveyIds, new Random()); // shuffle the ids
                ////log.debug(">>> 2. activeSurveyIds = " + activeSurveyIds);
                //log.debug(">>> " + randomSelSurveyType + " : activeSurveyIds : " + activeSurveyIds);
            }


            if (activeSurveyIds.size() > 0) {  // if there are active survey ids found

                for (int i = 0; i < activeSurveyIds.size(); i++) {  // this continues till a survey is found or the loop is completed							
                    survey = surveyRepository.findOne(activeSurveyIds.get(i));  // fetch the candidate survey

                    // set a flag for invalid surveys (optimistic)
                    boolean isInValid = false;

                    if (randomSelSurveyType == SurveyType.BRAND || randomSelSurveyType == SurveyType.BRAND_PROD_CAT) {

                        if (randomSelSurveyType == SurveyType.BRAND) {
                            //log.debug(">> C1: checking brand : survey id = " + survey.getId());
                            // check for non-taken brands in the global scope for the given survey 
                            if (surveyAnswerRepository.countNonTakenBrandSurveysByUser(user, survey) == 0) { // there are no untaken brands.. 
                                //log.debug(">> C2: rejecting brand  : size = 0"); //.. so reject it
                                isInValid = true;
                            }
                        } else if (randomSelSurveyType == SurveyType.BRAND_PROD_CAT) {
                            SurveyBrandProductCategory brandProdCatSurvey = (SurveyBrandProductCategory) survey;
                            List<Brand> brands1 = brandRepository.countNonTakenBrandSurveysByUserAndProductCategories(user, survey,
                                    brandProdCatSurvey.getProductCategories());
                            List<Brand> brands2 = brandRepository.countNonTakenProdCatSurveysByUserAndProductCategories(user, survey,
                                    brandProdCatSurvey.getProductCategories());
                            //log.debug("D4: checking brand prod cat : survey id = " + survey.getId() + ", categories = " + brandProdCatSurvey.getProductCategories().size());

                            if ((brands1 == null || brands1.size() == 0)
                                    && (brands2 == null || brands2.size() == 0)) { // no other brand and/or prod cat is available for this survey; we need to pick up next survey
                                //log.debug("N: rejecting brand prod cat : brands1 = " + brands1.size() + ", brands2 = " + brands2.size());
                                isInValid = true;
                            }
                        }
                        if (isInValid == false) {
                            // find the last cycle for this specific user
                            lastCycleId = surveyAnswerRepository.findLastCycleByUser(user.getId());

                        }
                    } else if (randomSelSurveyType == SurveyType.PROFILE || randomSelSurveyType == SurveyType.PROD_CAT) {
                        if (randomSelSurveyType == SurveyType.PROFILE) {
                            //log.debug(">>> A1. checking (prof) survey " + survey.getId());

                            // check for given answers for this survey in global scope 
                            if (surveyAnswerRepository.countSurveyAnswersByUserAndSurvey(user, survey) > 0) { // if there are answers.. 
                                //log.debug(">>> A2. rejecting (prof) survey " + survey.getId());
                                isInValid = true;  // .. reject it
                            }
                        } else if (randomSelSurveyType == SurveyType.PROD_CAT) {
                            SurveyProductCategory surveyProdCat = surveyProductCategoryRepository.findByIdAndFetchCategories(survey.getId());
                            //log.debug(">>> B1. checking (prod_cat) survey " + survey.getId());

                            // fetch the taken product categories for this survey (ALL not only the current ones; from past as well) in global scope
                            List<Long> takenIds = surveyAnswerRepository.fetchTakenProdCatsByUserAndSurveyId(user, survey.getId());

                            // fetch the CURRENT (because there could be more in the past) product categories of this survey
                            List<Long> surveyCatIds = productCategoryLevelThreeRepository.findIdsByEntities(surveyProdCat.getProductCategories());

                            // check whether the survey's product categories have ALL been taken in the past
                            if (takenIds != null && surveyCatIds != null
                                    && takenIds.containsAll(surveyCatIds)) {  // ALL have been taken..
                                //log.debug(">>> B. rejecting (prod_cat) survey " + survey.getId());
                                isInValid = true; //.. so reject it
                            }
                        }
                        //log.debug(">>> A. lastCycleId = " + lastCycleId);
                        if (isInValid == false) { // there isn't new, so we need to fetch the last cycle id
                            // find the last cycle for this specific user
                            lastCycleId = surveyAnswerRepository.findLastCycleByUser(user.getId());
                            //log.debug(">>> A2. lastCycleId = " + lastCycleId);
                        }
                    }

                    /* when the survey is new (especially for BRAND & BRAND_PROD_CAT this also mean that 
                     it might have been taken BUT NOT for all brands) AND it is valid upon user criteria */
                    if (!isInValid && validateSurveyByCriteria(survey, user)) {
                        break;  // keep the survey, abandon the loop and return the survey
                    } else { // already taken in ALL cases and/or is not valid upon user criteria
                        survey = null;  // reset the variable
                    }

                }  // end: internal for loop (iterating through a list of survey ids for a given type)

                if (survey != null) {  // if a survey has been found after all
                    surveyData.setSurvey(survey);
                    surveyData.setSurveyAlreadyTaken(false);  // false: indicates that for BRAND & BRAND_PROD_CAT surveys we still have brands not associated with them
                    surveyData.setSurveyStarted(true);  // this indicates that the survey is new (for UI purposes)
                    if (lastCycleId != null) {
                        surveyData.setLastCycleId(lastCycleId); // used for finding non-used brands for brand & product category surveys 
                    }
                }

            }  // end:  totalActiveSurveys > 0

        } while (survey == null && (currentSurveyIdx < (SurveyType.values().length - 1)));
        // we may still not have a survey after all... 2nd step would solve this (by fetching old ones)				

        return surveyData;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Brand fetchSurveyRandomUntakenBrand(User user, Survey survey, ProductCategoryLevelThree category,
            boolean isSurveyRestarted, String sessionId, Long lastCycleId) {
        Brand brand = null;

        List<Long> listOfTakenIds = null;
        List<Long> nonTakenBrandIds = null;

        //log.debug(">> lastCycleId = " + lastCycleId + ", isSurveyRestarted = " + isSurveyRestarted);
        if (category == null) {  // searching in ALL brands (either non-taken or not)
            //listOfTakenIds = (isSurveyRestarted == false) ? surveyAnswerRepository.fetchTakenSurveyBrandIdsByUserAndSessionId(user, survey, sessionId) : null;
            listOfTakenIds = (isSurveyRestarted == false) ? surveyAnswerRepository.fetchTakenSurveyBrandIdsByUserAndCycleId(user, survey, lastCycleId) : null;
            //log.debug(">> 1. listOfTakenIds = " + listOfTakenIds);
            if (listOfTakenIds != null && !listOfTakenIds.isEmpty()) {
                nonTakenBrandIds = brandRepository.fetchBrandIdsNotInGivenList(listOfTakenIds);
            } else {
                nonTakenBrandIds = brandRepository.findAllBrandIds();
            }
        } else {  // searching in ALL brands of specific category (either non-taken or not)
            //listOfTakenIds = (isSurveyRestarted == false) ?  surveyAnswerRepository.fetchTakenSurveyBrandIdsByUserAndProductCategoryAndSessionId(user, survey, category, sessionId) : null;
            listOfTakenIds = (isSurveyRestarted == false) ? surveyAnswerRepository.fetchTakenSurveyBrandIdsByUserAndProductCategoryAndCycleId(user, survey, category, lastCycleId) : null;
            //log.debug(">> 2. listOfTakenIds = " + listOfTakenIds);
            if (listOfTakenIds != null && !listOfTakenIds.isEmpty()) {
                nonTakenBrandIds = brandRepository.fetchBrandIdsByCategoryNotInGivenList(listOfTakenIds, category);
            } else {
                nonTakenBrandIds = brandRepository.findAllBrandIdsByCategory(category);
            }
        }
        if (nonTakenBrandIds != null && !nonTakenBrandIds.isEmpty()) {
            int randomIndex = RandomUtils.nextInt(nonTakenBrandIds.size());
            Long brandId = nonTakenBrandIds.get(randomIndex);
            brand = brandRepository.findOne(brandId);
        }
        return brand;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProductCategoryLevelThree fetchSurveyRandomUntakenProductCategory(User user, Survey survey, Long lastCycleId) {
        ProductCategoryLevelThree productCategoryLevelThree = null;

        if (lastCycleId == null) {
            lastCycleId = 0L;
        }
        //log.debug(">> D1a : lastCycleId  = " + lastCycleId);

        List<Long> listOfTakenIds = surveyAnswerRepository.fetchTakenProdCatIdsByUserAndCycleId(user, survey, lastCycleId);
        //log.debug(">> D1b : listOfTakenIds  = " + listOfTakenIds);

        SurveyProductCategory prodCatSurvey = (SurveyProductCategory) survey;

        List<Long> surveyProdCatIds = new ArrayList<Long>();

        // fetch the prioritized (ie. prioritized = 1) CURRENT (it might have more in the past..) survey's product categories 
        List<Long> prioritizedSurveyProdCatIds = productCategoryLevelThreeRepository.findIdsByEntitiesAndPriority(
                prodCatSurvey.getProductCategories(), true);
        Collections.shuffle(prioritizedSurveyProdCatIds, new Random()); // shuffle the ids
        //log.debug(">> D1c. prioritizedSurveyProdCatIds = " + prioritizedSurveyProdCatIds);

        // fetch the prioritized (ie. prioritized = 1) CURRENT (it might have more in the past..) survey's product categories 
        List<Long> nonPrioritizedSurveyProdCatIds = productCategoryLevelThreeRepository.findIdsByEntitiesAndPriority(
                prodCatSurvey.getProductCategories(), false);
        Collections.shuffle(nonPrioritizedSurveyProdCatIds, new Random()); // shuffle the ids
        //log.debug(">> D1d. nonPrioritizedSurveyProdCatIds = " + nonPrioritizedSurveyProdCatIds);

        // merge all the survey's product categories lists into one BUT preserving the order (ie.prioritized first, then the others)
        if (prioritizedSurveyProdCatIds != null) {
            surveyProdCatIds.addAll(prioritizedSurveyProdCatIds);
        }
        if (nonPrioritizedSurveyProdCatIds != null) {
            surveyProdCatIds.addAll(nonPrioritizedSurveyProdCatIds);
        }
        //log.debug(">> D1e. surveyProdCatIds = " + surveyProdCatIds);

        // if one or more of the categories have already been used..
        if (listOfTakenIds != null && !listOfTakenIds.isEmpty()) {
            for (Long prodCatId : surveyProdCatIds) { // ..so iterate through the survey's product categories till to find an untaken one
                if (!listOfTakenIds.contains(prodCatId)) {
                    //log.debug(">> D1f. picking up the category " + prodCatId);
                    productCategoryLevelThree = productCategoryLevelThreeRepository.findOne(prodCatId);
                    break;
                }
            }
        }

        // none of the categories has been used so far.. 
        if (productCategoryLevelThree == null) { // ..so get the first available one
            //log.debug(">> D1g. picking up the category " + surveyProdCatIds.get(0));
            productCategoryLevelThree = productCategoryLevelThreeRepository.findOne(surveyProdCatIds.get(0));
        }

        return productCategoryLevelThree;
    }

    @Transactional
    public Object[] fetchSurveyRandomUntakenBrandAndProductCategory(User user, Survey survey, boolean isSurveyRestarted,
            String sessionId, Long lastCycleId) {
        Object[] results = new Object[2];

        ////log.debug(">> lastCycleId = " + lastCycleId + ", isSurveyRestarted = " + isSurveyRestarted + ", survey.id = " + survey.getId());
        if (survey instanceof SurveyBrand) {  // searching for brands
            // fetch the already taken brand ids related to the last cycle (if exists); otherwise (ie. no cycle) create a null list
            List<Long> listOfTakenIds = (lastCycleId != null) ? surveyAnswerRepository.fetchTakenSurveyBrandIdsByUserAndCycleId(user,
                    survey, lastCycleId) : null;
            ////log.debug(">> D1a. listOfTakenIds = " + listOfTakenIds);

            // add the taken brand ids to the excluded list (if it is not null)
            List<Long> excludedIds = new ArrayList<Long>();
            excludedIds.add(0L); // add a dummy element to prevent empty list related exceptions
            if (listOfTakenIds != null) {
                excludedIds.addAll(listOfTakenIds);
            }

            // searches for new prioritized brand ids (no taken at all with priority  = true) 
            List<Long> newPrioritizedBrandIds = brandRepository.fetchNewBrandIdsByPriority(survey.getId(), true);
            ////log.debug(">> D1ba. newPrioritizedBrandIds = " + newPrioritizedBrandIds);	
            Collections.shuffle(newPrioritizedBrandIds, new Random()); // shuffle the list
            ////log.debug(">> D1bb. newPrioritizedBrandIds = " + newPrioritizedBrandIds);	
            excludedIds.addAll(newPrioritizedBrandIds);  // add the new ones to the list of excluded ids
            ////log.debug(">> D1bc. excludedIds = " + excludedIds);	


            // searches for new NON-prioritized brand ids (no taken at all with priority  = false) 
            List<Long> newNonPrioritizedBrandIds = brandRepository.fetchNewBrandIdsByPriority(survey.getId(), false);
            ////log.debug(">> D1ca. newNonPrioritizedBrandIds = " + newNonPrioritizedBrandIds);	
            Collections.shuffle(newNonPrioritizedBrandIds, new Random()); // shuffle the list
            ////log.debug(">> D1cb. newNonPrioritizedBrandIds = " + newNonPrioritizedBrandIds);	
            excludedIds.addAll(newNonPrioritizedBrandIds);  // add the new ones to the list of excluded ids
            ////log.debug(">> D1cc. excludedIds = " + excludedIds);	


            // fetches the old prioritized non-taken brand ids (ie. the old non-taken ones with priority = true)
            List<Long> oldNonTakenPrioritizedBrandIds = brandRepository.fetchBrandIdsNotInGivenListByPriority(excludedIds, true);
            ////log.debug(">> D1da. oldNonTakenPrioritizedBrandIds = " + oldNonTakenPrioritizedBrandIds);
            Collections.shuffle(oldNonTakenPrioritizedBrandIds, new Random()); // shuffle the old ids
            ////log.debug(">> D1db. oldNonTakenPrioritizedBrandIds = " + oldNonTakenPrioritizedBrandIds);

            // fetches the old NON-prioritized non-taken brand ids (ie. the old non-taken ones with priority = false)
            List<Long> oldNonTakenNonPrioritizedBrandIds = brandRepository.fetchBrandIdsNotInGivenListByPriority(excludedIds, false);
            ////log.debug(">> D1ea. oldNonTakenNonPrioritizedBrandIds = " + oldNonTakenNonPrioritizedBrandIds);
            Collections.shuffle(oldNonTakenNonPrioritizedBrandIds, new Random()); // shuffle the old ids
            ////log.debug(">> D1eb. oldNonTakenNonPrioritizedBrandIds = " + oldNonTakenNonPrioritizedBrandIds);			


            // merge all of the valid lists (ie. new prioritized + new non-prioritized + old prioritized non-taken + old non-prioritized non-taken IDs)
            List<Long> listOfBrandIds = new ArrayList<Long>();
            if (newPrioritizedBrandIds != null) {
                listOfBrandIds.addAll(newPrioritizedBrandIds);
            }
            if (newNonPrioritizedBrandIds != null) {
                listOfBrandIds.addAll(newNonPrioritizedBrandIds);
            }
            if (oldNonTakenPrioritizedBrandIds != null) {
                listOfBrandIds.addAll(oldNonTakenPrioritizedBrandIds);
            }
            if (oldNonTakenNonPrioritizedBrandIds != null) {
                listOfBrandIds.addAll(oldNonTakenNonPrioritizedBrandIds);
            }
            ////log.debug(">> D1f. listOfBrandIds = " + listOfBrandIds);


            if (listOfBrandIds != null && !listOfBrandIds.isEmpty()) {
                Long brandId = listOfBrandIds.get(0); // get the 1st occurence 
                ////log.debug(">> D1g. brand picked-up  = " + brandId);
                results[0] = brandRepository.findOne(brandId);  // store the random fetched brand
                results[1] = null;  // store null (ie. no product category since it is a BRAND survey)
            }
        } else if (survey instanceof SurveyBrandProductCategory) {  // searching for brands
            SurveyBrandProductCategory brandProdCatSurvey = (SurveyBrandProductCategory) survey;

            // find all the brands associated with one or more of the survey's categories
            List<Brand> allBrands = brandRepository.fetchBrandIdsByCategories(brandProdCatSurvey.getProductCategories()); // due to JPA restriction fetch first the brand entities
            List<Long> allBrandIds = brandRepository.fetchIdsByList(allBrands); // get now their ids (possible refactoring here)
            //log.debug(">> F0: allBrandIds = " + allBrandIds);
            List<Long> surveyProdCatIds = productCategoryLevelThreeRepository.findIdsByEntities(brandProdCatSurvey.getProductCategories());
            //log.debug(">> F1: surveyProdCatIds = " + surveyProdCatIds);

            List<Long> takenBrandIds = null;
            if (lastCycleId != null) {
                takenBrandIds = surveyAnswerRepository.fetchTakenBrandIdsByUserAndProductCategoriesAndCycleId(user, survey,
                        brandProdCatSurvey.getProductCategories(), lastCycleId);
                //log.debug(">> F2: takenBrandIds = " + takenBrandIds);
            } else {
                takenBrandIds = surveyAnswerRepository.fetchTakenBrandIdsByUserAndProductCategories(user, survey,
                        brandProdCatSurvey.getProductCategories());
                //log.debug(">> F3a: takenBrandIds = " + takenBrandIds);
            }

            // find the non-taken brands (the difference) which have not been surveyed at all for the given survey, cycle id, user
            List<Long> nonTakenBrandIds = null;
            if ((allBrandIds != null && takenBrandIds != null)
                    && allBrandIds.size() > takenBrandIds.size()) {
                nonTakenBrandIds = new ArrayList<Long>();
                nonTakenBrandIds.addAll(allBrandIds);
                nonTakenBrandIds.removeAll(takenBrandIds); // leave 
            }
            //log.debug(">> F3b: nonTakenBrandIds = " + nonTakenBrandIds);
            // fetch the 1st brand and one of its categories
            if (nonTakenBrandIds != null && !nonTakenBrandIds.isEmpty()) {  // [all > taken]
                Collections.shuffle(nonTakenBrandIds, new java.util.Random());
                Long brandId = nonTakenBrandIds.get(0);
                //log.debug(">> F3c: selectedBrandId = " + brandId);
                Brand brand = brandRepository.findById(brandId);
                results[0] = brand;  // store the random fetched brand
                List<ProductCategoryLevelThree> brandProdCats = productCategoryLevelThreeRepository.findIdsByBrandId(brandId);
                // NOTE: possible refactoring here; it may have categories not supported by the survey
                if (brandProdCats != null && !brandProdCats.isEmpty()) {
                    List<Long> brandProdCatIds = productCategoryLevelThreeRepository.findOrderedIdsByEntities(brandProdCats);
                    //log.debug(">> F5: brandProdCatIds = " + brandProdCatIds);
                    //log.debug(">> F6: surveyProdCatIds = " + surveyProdCatIds);
                    for (Long brandProdCatId : brandProdCatIds) {
                        if (surveyProdCatIds.contains(brandProdCatId)) {
                            ////log.debug(">> F6. picking up the category " + brandProdCatId);
                            results[1] = productCategoryLevelThreeRepository.findOne(brandProdCatId);
                            //log.debug(">> F7: selected brandProdCatId = " + brandProdCatId);
                            break;
                        }
                    }
                }
            } else { // [all <= taken]
                Brand brand = null;
                ProductCategoryLevelThree category = null;

                Collections.shuffle(allBrandIds, new java.util.Random());
                //log.debug(">> A1: shuffled allBrandIds = " + allBrandIds);
                for (Long brandId : allBrandIds) {
                    List<Long> takenProdCatIds = null;
                    if (lastCycleId != null) {
                        takenProdCatIds = surveyAnswerRepository.fetchTakenSurveyProdCatIdsByUserAndBrandIdAndCycleId(user,
                                survey, brandId, lastCycleId);
                        //log.debug(">> A2a: takenProdCatIds = " + takenProdCatIds);
                    } else {
                        takenProdCatIds = surveyAnswerRepository.fetchTakenSurveyProdCatIdsByUserAndBrandId(user,
                                survey, brandId);
                        //log.debug(">> A2b: takenProdCatIds = " + takenProdCatIds);
                    }

                    brand = brandRepository.findById(brandId);
                    if (takenProdCatIds != null && !takenProdCatIds.isEmpty()) { // if one or more of the categories have been used
                        List<ProductCategoryLevelThree> brandProdCats = productCategoryLevelThreeRepository.findIdsByBrandId(brandId);
                        List<Long> brandProdCatIds = productCategoryLevelThreeRepository.findOrderedIdsByEntities(brandProdCats);
                        //log.debug(">> A3a: brandProdCatIds = " + brandProdCatIds);
                        for (Long brandProdCatId : brandProdCatIds) {
                            if (!takenProdCatIds.contains(brandProdCatId) && surveyProdCatIds.contains(brandProdCatId)) {
                                //log.debug(">> A3a1: selected brandProdCatId = " + brandProdCatId);
                                category = productCategoryLevelThreeRepository.findOne(brandProdCatId);
                                break;
                            }
                        }
                    } else { // none of the categories has been used
                        List<ProductCategoryLevelThree> brandProdCats = productCategoryLevelThreeRepository.findIdsByBrandId(brandId);
                        List<Long> brandProdCatIds = productCategoryLevelThreeRepository.findOrderedIdsByEntities(brandProdCats);
                        //log.debug(">> A3b: brandProdCatIds = " + brandProdCatIds);
                        for (Long brandProdCatId : brandProdCatIds) {
                            if (surveyProdCatIds.contains(brandProdCatId)) {
                                //log.debug(">> A3b1: selected brandProdCatId = " + brandProdCatId);
                                category = productCategoryLevelThreeRepository.findOne(brandProdCatId);
                                break;
                            }
                        }
                    }
                    if (category != null) {
                        break;
                    }
                }
                if (category != null) {
                    results[0] = brand;		// store the brand
                }
                if (category != null) {
                    results[1] = category;  // store the random brand's category
                }
                //log.debug(">> A4a: final brandId = " + ((brand != null ) ? brand.getId() : "null"));
                //log.debug(">> A4b: final category = " + ((category != null) ? category.getId() : "null"));
            }

        }

        return results;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    private Survey getUserLastSurvey(User user) {
        Survey survey = null;

        // construct the paging infoo
        List<Order> sortOrders = new ArrayList<Order>();
        Order order = new Order(Direction.DESC, "id");
        sortOrders.add(order);

        // find the last user's survey answer (if any)
        Sort sort = new Sort(sortOrders);
        List<SurveyAnswer> surveyAnswers = surveyAnswerRepository.findByUser(user, new PageRequest(0, 1, sort));

        if (surveyAnswers != null && !surveyAnswers.isEmpty()) { // if there are answers 
            SurveyAnswer surveyAnswer = surveyAnswers.get(0); // get the answer 
            if (surveyAnswer != null) {  // get the corresponding survey 
                survey = surveyAnswer.getSurveyQuestion().getSurvey();
            }
        }

        return survey;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Survey> findAll() {
        return surveyRepository.findAll();
    }

    @SuppressWarnings("unused")
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    protected SurveyQuestion getNextSurveyQuestion(Survey survey, User user, boolean restart) {
        SurveyQuestion surveyQuestion = null;

        List<Order> sortOrders = new ArrayList<Order>();

        Order order = new Order(Direction.DESC, "id");
        sortOrders.add(order);

        Sort sort = new Sort(sortOrders);

        // finds the answers based on the last survey instance plus the current position of the last taken question
        SurveyInstance lastSurveyInstance = surveyInstanceRepository.findLatestByUserAndSurvey(user, survey);
        List<SurveyAnswer> lastSurveyAnswers = surveyAnswerRepository.findByUserAndMaxSurveyInstance(user, survey,
                lastSurveyInstance);

        int lastQuestionPosition = 0;
        SurveyAnswer lastSurveyAnswer = null;

        if (restart == false) {  // extract the last answer & its question's current position (if there are answers)
            if (lastSurveyAnswers != null && !lastSurveyAnswers.isEmpty()) {
                lastSurveyAnswer = lastSurveyAnswers.get(0);
                if (lastSurveyAnswer != null) {
                    lastQuestionPosition = lastSurveyAnswer.getSurveyQuestion().getPosition();
                }
            }
        }

        // keep on trying till a next question is found with a position >= than the current one
        while (surveyQuestion == null) {
            /* finds all the next candidate questions ie. those ones which have a position greater than the current one 
             (just retrieved previously) */
            List<SurveyQuestion> surveyQuestions = (List<SurveyQuestion>) surveyQuestionRepository.
                    findNextQuestionByPosition(lastQuestionPosition, survey.getId());

            // check if there are any results
            // if there is another one next question
            if (surveyQuestions != null && surveyQuestions.size() > 0) {

                surveyQuestion = surveyQuestions.get(0);
                // here validate the question upon the question criteria
                // if validation fails
                if (validateQuestionByCriteria(lastSurveyAnswers, surveyQuestion, user) == false) {
                    /*
                     * validation failed - continue fetching another "next"
                     * question (actually that one with the next smallest
                     * position in relation to the position which was just
                     * checked)
                     */
                    surveyQuestion = null; // reset the reference
                    lastQuestionPosition++; // increase the index
                    continue; // try again...
                } else {
                    break;
                }

            } else { // no more questions in the survey
                break; // stop looping - return null
            }
        }

        return surveyQuestion;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    private boolean validateSurveyByCriteria(Survey survey, User user) {
        List<SurveyCriteria> criteria = surveyCriteriaRepository.findBySurveyId(survey.getId());

        if (criteria != null && !criteria.isEmpty()) {
            for (SurveyCriteria criterion : criteria) {
                if (!criteriaService.evaluateCriterion(criterion, user)) {
                    return false;
                }

                if (criterion.getSurveyOption() != null) { // for survey's option definition 
                    // points to the candidate survey
                    Long countOptsMatches = userRepository.findOptionMatches(user.getId(),
                            criterion.getSurveyOption().getId());

                    if (countOptsMatches == 0) {
                        //	log.debug(">>> REJECTING SURVEY (opt eq) : " + survey.getId());
                        return false;
                    }
                }
            }
        }

        // either no criteria found at all or the survey was validated upon them
        // successfully
        return true;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    private boolean validateQuestionByCriteria(List<SurveyAnswer> lastSurveyAnswers, SurveyQuestion surveyQuestion, User user) {
        boolean valid = true;

        if (lastSurveyAnswers != null && !lastSurveyAnswers.isEmpty()) {
            List<SurveyOption> surveyOptions = new ArrayList<SurveyOption>();
            for (SurveyAnswer surveyAnswer : lastSurveyAnswers) {
                surveyOptions.addAll(surveyAnswer.getSurveyOptions());
            }

            /*
             * 1. When an OPTION_RESTRICTS_QUESTION criterion is set, that means that AT LEAST 1 (OR combination) such criterion is sufficient
             *    to make the next question invalid. 
             */
            if (surveyQuestionCriteriaRepository.findByQuestionIdAndAnswerOptionsAndRule(
                    surveyQuestion.getId(), surveyOptions, SurveyQuestionCriteriaType.OPTION_RESTRICTS_QUESTION) > 0) {
                valid = false;
                /*
                 * 1. When an OPTION_REQUIRED_BY_QUESTION criterion is set, that means that ALL (AND combination) the criteria should be satisfied
                 *    in order to make the next question valid. 
                 */
            } else {
                Long countSatisfiedReqCriteria = surveyQuestionCriteriaRepository.findByQuestionIdAndAnswerOptionsAndRule(
                        surveyQuestion.getId(), surveyOptions, SurveyQuestionCriteriaType.OPTION_REQUIRED_BY_QUESTION);
                Long countReqCriteria = surveyQuestionCriteriaRepository.findByQuestionIdAndRule(
                        surveyQuestion.getId(), SurveyQuestionCriteriaType.OPTION_REQUIRED_BY_QUESTION);
                if (countSatisfiedReqCriteria.longValue() < countReqCriteria.longValue()) {
                    valid = false;
                }
            }
        }

        return valid;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int saveSurveyAnswer(User user, String sessionId, SurveyQuestionDTO data) throws
            DuplicateUserResponseException, InvalidUserResponseException, Exception {
        SurveyAnswer surveyAnswer = new SurveyAnswer();

        surveyAnswer.setAttendanceTime(Calendar.getInstance().getTime());
        surveyAnswer.setSurveyQuestion(data.getSurveyQuestion());
        surveyAnswer.setUser(user);
        surveyAnswer.setVersion(0L);
        surveyAnswer.setExpectedTime(data.getExpectedResponseTime());
        surveyAnswer.setResponseTime(data.getActualResponseTime());

        UserSession userSession = userSessionRepository.findBySessionId(sessionId);
        if (userSession == null) {
            userSession = new UserSession(sessionId);
        }

        surveyAnswer.setSession(userSession);

        if (data.isSurveyStarted()) {
            //log.debug(">>>> setting a new survey instance....");

            surveyAnswer.setInstance(new SurveyInstance());

            // set the cycle id ....
        } else {
            //log.debug(">>>> setting an existing instance....");

            SurveyInstance instance = surveyInstanceRepository.findLatestByUserAndSurvey(user,
                    surveyAnswer.getSurveyQuestion().getSurvey());
            surveyAnswer.setInstance(instance);

            // find and set the correct cycle id ....
        }

        if (data.isNeedForNewCycle() == true) {
            //log.debug(">>>> setting a new survey cycle....");

            surveyAnswer.setCycle(new SurveyCycle());

            // set the cycle id ....
        } else {
            //log.debug(">>>> setting an existing cycle....");

            SurveyCycle cycle = surveyCycleRepository.findLatestByUser(user);
            surveyAnswer.setCycle(cycle);

            // find and set the correct cycle id ....
        }

        SurveyProductStatusForOrderDetails surveyOrderDetailStatus = null;
        if (data.getType() == SurveyQuestionDTO.Type.PRODUCT_SURVEY) { // if it is a product survey
            if (surveyAnswerRepository.countSurveyAnswersByUserAndQuestion(user, sessionId, data.getSurveyQuestion()) > 0) {
                throw new DuplicateUserResponseException("There is already an answer for the specified question");
            } else if (surveyAnswerRepository.countSurveyAnswersByUserAndQuestionPosition(user, data.getSurveyQuestion().getSurvey(), sessionId,
                    data.getSurveyQuestion().getPosition()) > 0) { // check whether there are questions in a higher position 
                throw new InvalidUserResponseException("It's not possible to answer a question with in a lower position");
            }

            surveyOrderDetailStatus = surveyProductStatusForOrderDetailsRepository.findBySurveyAndUserAndOrderDetail(
                    data.getSurveyQuestion().getSurvey(), user, data.getOrderDetails());

            surveyAnswer.setProduct(data.getOrderDetails().getProduct());  // set the product reference

        } else {  // if it is a generic survey
            if (data.getBrand() != null && data.getProductCategory() == null) {  // check for invalid brand only
                if (surveyAnswerRepository.coundBySurveyQuestionAndUserAndSessionIdAndBrand(user, sessionId, data.getSurveyQuestion(),
                        data.getBrand()) > 0) {
                    throw new DuplicateUserResponseException(DuplicateUserResponseException.DUPL_BRAND_ERROR,
                            "There is already an answer for the specified question and brand");
                }
            } else if (data.getBrand() != null && data.getProductCategory() != null) { // check for invalid prod. category & brand
                if (surveyAnswerRepository.coundBySurveyQuestionAndUserAndSessionIdAndProductCategoryBrand(user, sessionId, data.getSurveyQuestion(),
                        data.getProductCategory(), data.getBrand()) > 0) {
                    throw new DuplicateUserResponseException(DuplicateUserResponseException.DUPL_BRAND_AND_PROD_CATEGORY_ERROR,
                            "There is already an answer for the specified question, brand and product category");
                }
            } else if (data.getBrand() == null && data.getProductCategory() != null) { // check for invalid prod. category only
                if (surveyAnswerRepository.coundBySurveyQuestionAndUserAndSessionIdAndProductCategory(user, sessionId, data.getSurveyQuestion(),
                        data.getProductCategory()) > 0) {
                    throw new DuplicateUserResponseException(DuplicateUserResponseException.DUPL_PROD_CATEGORY_ERROR,
                            "There is already an answer for the specified question and product category");
                }
            } else { // profile surveys
                if (surveyAnswerRepository.coundBySurveyQuestionAndUserAndSessionId(user, sessionId, data.getSurveyQuestion()) > 0) {
                    throw new DuplicateUserResponseException(DuplicateUserResponseException.DUPL_PROFILE_ERROR,
                            "There is already an answer for the specified question");
                }
            }

            if (data.getBrand() != null) {
                surveyAnswer.setBrand(data.getBrand());
            }
            if (data.getProductCategory() != null) {
                surveyAnswer.setProductCategory(data.getProductCategory());
            }
        }

        List<SurveyOption> surveyOptions = new ArrayList<SurveyOption>();
        if (!data.getSurveyQuestion().isSelectMultiple()) {
            surveyOptions.add(data.getSurveyOption());
        } else {
            for (String answer : data.getMultiAnswers()) {
                for (SurveyOption surveyOption : data.getSurveyQuestion().getOptions()) {
                    if (answer.equals(surveyOption.getId().toString())) {
                        surveyOptions.add(surveyOption);
                        break;
                    }
                }

            }
        }

        surveyAnswer.setSurveyOptions(surveyOptions);

        this.surveyAnswerRepository.save(surveyAnswer);

        if (data.getActualResponseTime() < data.getExpectedResponseTime()) {
            //increment user's counter
            user.setFastResponseCounter(user.getFastResponseCounter() + 1);
            userRepository.save(user);
        }

        if (data.getType() == SurveyQuestionDTO.Type.PRODUCT_SURVEY) {
            if (surveyOrderDetailStatus == null) {  // create record in survey status table 		
                SurveyProductStatusForOrderDetails surveyProductStatus = new SurveyProductStatusForOrderDetails();
                surveyProductStatus.setSurvey(data.getSurveyQuestion().getSurvey());
                surveyProductStatus.setOrderDetail(data.getOrderDetails());
                surveyProductStatus.setUser(user);

                if (getNextSurveyQuestion(data.getSurveyQuestion().getSurvey(), user, false) == null) {
                    surveyProductStatus.setSurveyCompleted(true);
                }
                surveyProductStatusForOrderDetailsRepository.save(surveyProductStatus);
            } else {
                // set the surveyable flag properly if there is no next question for the survey
                if (getNextSurveyQuestion(data.getSurveyQuestion().getSurvey(), user, false) == null) {
                    surveyOrderDetailStatus.setSurveyCompleted(true);
                }
            }
        }

        if (data.getType() == SurveyQuestionDTO.Type.PRODUCT_SURVEY) {
            return this.pointTransactionAccountingService.addPoints(surveyAnswer.getUser(),
                    PointTransactionType.PRODUCT_SURVEY, data.getOrderDetails().getProduct().getPointsGainedPerQuestion());
        } else if (data.getType() == SurveyQuestionDTO.Type.RATING) {
            return this.pointTransactionAccountingService.addConfigurationPoints(surveyAnswer.getUser(),
                    PointTransactionType.RATING_POINTS);
        } else {
            return this.pointTransactionAccountingService.addPoints(surveyAnswer.getUser(),
                    PointTransactionType.GENERIC_SURVEY_POINTS, 1);  // always 1
        }

    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int saveRating(User user, SurveyQuestionDTO data) throws DuplicateUserResponseException, InvalidUserResponseException {
        SurveyProductStatusForOrderDetails orderDetailsStatus = surveyProductStatusForOrderDetailsRepository.findByUserAndOrderDetail(user,
                data.getRating().getOrderDetail());
        if (orderDetailsStatus == null || orderDetailsStatus.isSurveyCompleted() == false) { // check for non completed product survey
            throw new InvalidUserResponseException("The product survey is either not completed or it doesn't exist ");
        } else if (ratingService.findByOrderDetailAndUser(data.getRating().getOrderDetail(), user) != null) { // check for duplicate rating
            throw new DuplicateUserResponseException("There is already a rating for this product");
        }

        Rating rating = data.getRating();

        rating.setUser(user);
        rating.setCreateTime(new Date());
        rating.setVersion(0L);

        this.ratingService.saveRating(rating);

        return this.pointTransactionAccountingService.addConfigurationPoints(user,
                PointTransactionType.RATING_POINTS);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Brand fetchLastQuestionBrand(User user, Survey survey) {
        Brand brand = null;

        SurveyAnswer surveyAnswer = surveyAnswerRepository.fetchLastSurveyAnswerByUserAndSurvey(user, survey);
        if (surveyAnswer != null) {
            brand = surveyAnswer.getBrand();
        }
        return brand;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SurveyAnswer fetchLastAnswer(User user, Survey survey) {
        return surveyAnswerRepository.fetchLastSurveyAnswerByUserAndSurvey(user, survey);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Object[] saveQuizAnswer(User user, SurveyQuestionDTO data) throws DuplicateUserResponseException, InvalidUserResponseException {
        Object[] results = new Object[2];

        SurveyProductStatusForOrderDetails orderDetailsStatus = surveyProductStatusForOrderDetailsRepository.findByUserAndOrderDetail(user,
                data.getOrderDetails());

        if ((orderDetailsStatus == null || orderDetailsStatus.isSurveyCompleted() == false)
                || (ratingService.findByOrderDetailAndUser(data.getOrderDetails(), user) == null)) { // check for non completed product survey and/or rating
            throw new InvalidUserResponseException("The product survey and/or rating is either not completed or it doesn't exist ");
        } else if (quizService.findAnswerByOrderDetailAndUser(data.getOrderDetails(), user) != null) { // check for duplicate quiz
            throw new DuplicateUserResponseException("There is already a quiz for this product");
        }
        OrderDetails orderDetails = data.getOrderDetails();

        QuizAnswer quizAnswer = new QuizAnswer();

        quizAnswer.setAttendanceTime(new Date());
        quizAnswer.setOrderDetail(orderDetails);
        quizAnswer.setQuizOption(data.getQuizOption());
        quizAnswer.setUser(user);
        quizAnswer.setVersion(0L);

        this.quizService.saveQuizAnswer(quizAnswer);

        orderDetails.setSurveyCompleted(true);
        orderDetailsRepository.save(orderDetails); // close eventually the survey process for this order item

        for (QuizOption option : data.getQuizQuestion().getQuizOptions()) {
            if (option.getId().longValue() != data.getQuizOption().getId().longValue()) {
                if (option.isCorrectOption()) {
                    results[0] = option;
                    break;
                }
            }
        }

        // add points only if there is a correct answer
        if (results[0] == null) { // if the quiz answer is the correct one
            results[1] = this.pointTransactionAccountingService.addPoints(user,
                    PointTransactionType.QUIZ_POINTS, data.getQuizQuestion().getPoints());
        } else { // if it is wrong no points !
            results[1] = new Integer(0);
        }


        return results;
    }

    /**
     * It will fetch the survey product stock, which are not yet started by the
     * user for a particular order_detail_id
     *
     * @param user
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<SurveyProduct> getSurveyProduct(User user, Long orderDetailID) {
        return this.surveyProductRepository.findActiveProductSurveyByUserAndOrderDetailIDNotUsed(user, orderDetailID);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Long countClosedSurveysByOrderDetailsAndUserId(List<Long> orderItemIds, Long userId) {
        return surveyProductRepository.countClosedSurveysByOrderDetailsAndUserId(orderItemIds, userId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SurveyQuestion getLastStartedSurveyQuestionIfSurveyNotCompleted(User _user) {
        Survey survey = getUserLastSurvey(_user);

        if (survey != null) {
            SurveyQuestion surveyQuestion = getNextSurveyQuestion(survey, _user, false);
            if (surveyQuestion != null) {
                return surveyQuestion;
            }
        }

        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<SurveyOption> findProfileOptionsByTitle(String title) {
        return surveyOptionRepository.findProfileOptionsByTitle("%" + title + "%");
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Page<SurveyCriteria> findSurveyCriteria(int first, int pageSize) {
        Pageable cond = new PageRequest(first, pageSize);
        return surveyCriteriaRepository.findAll(cond);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SurveyCriteria findSurveyCriterionById(Long criterioId) {
        return surveyCriteriaRepository.findOne(criterioId);
    }

    /**
     * Saves a survey criterion.
     *
     * @param criterion
     * @return true for success or false for failure
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean saveSurveyCriteria(SurveyCriteria criterion) {
        this.surveyCriteriaRepository.save(criterion);

        return true;
    }

    /**
     * Deletes a survey criterion.
     *
     * @param criterion
     * @return true for success or false for failure (ONLY due to dependencies)
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean deleteSurveyCriteria(SurveyCriteria criterion) {
        SurveyCriteria tempCriterion = this.surveyCriteriaRepository.findOne(criterion.getId());
        this.surveyCriteriaRepository.delete(tempCriterion);

        return true;
    }

    @SuppressWarnings("rawtypes")
    public Page findSurveys(int first, int pageSize, SurveyType type) {
        Pageable cond = new PageRequest(first, pageSize);

        switch (type) {
            case PROFILE:
                return surveyProfileRepository.findAll(cond);
            case BRAND:
                return surveyBrandRepository.findAll(cond);
            case PROD_CAT:
                return surveyProductCategoryRepository.findAll(cond);
            case BRAND_PROD_CAT:
                return surveyBrandProductCategoryRepository.findAll(cond);
            case PROD:
                return surveyProductRepository.findAll(cond);
            default:
                return surveyRepository.findAll(cond);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Survey findSurveyById(Long surveyId) {
        return surveyRepository.findOne(surveyId);
    }

    /**
     * Saves a survey.
     *
     * @param survey
     * @return true for success or false for failure
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean saveSurvey(Survey survey) {
        survey.setCreateDate(new Date());
        this.surveyRepository.save(survey); // no checks at the moment

        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean updateSurvey(SurveyDTO survey) {
        Survey curSurvey = this.surveyRepository.findOne(survey.getId());
        curSurvey.setTitle(survey.getTitle());
        curSurvey.setActive(survey.isActive());
        curSurvey.setPrioritized(survey.isPrioritized());
        this.surveyRepository.save(curSurvey);

        return true;
    }

    /**
     * Deletes a survey, but first checks for existing dependencies.
     *
     * @param survey
     * @return true for success or false for failure (ONLY due to dependencies)
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean deleteSurvey(Survey survey) {
        Long total = surveyQuestionRepository.countBySurvey(survey.getId());
        if (total == 0) {
            this.surveyRepository.delete(survey);
            return true;
        } else {
            return false;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<SurveyQuestion> getSurveyQuestionsBySurveyId(Long surveyId) {
        Survey survey = surveyRepository.findOne(surveyId);
        if (survey != null) {
            return survey.getSurveyQuestions();
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean addSurveyQuestionOption(SurveyOption surveyOption, Long questionId) {

        if (surveyOption.getId() == null || surveyOption.getId() == 0) {
            SurveyQuestion surveyQuestion = surveyQuestionRepository.findOne(questionId);
            if (surveyQuestion == null) {
                return false;
            }

            surveyOption.setQuestion(surveyQuestion);
            surveyOptionRepository.save(surveyOption);

            surveyQuestion.getOptions().add(surveyOption);
        } else {
            SurveyOption tempSurveyOption = surveyOptionRepository.findOne(surveyOption.getId());

            tempSurveyOption.setTitle(surveyOption.getTitle());
            tempSurveyOption.setActive(surveyOption.isActive());

            surveyOptionRepository.save(tempSurveyOption); // save the modifications			
        }

        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean saveOrUpdateSurveyQuestion(SurveyQuestion surveyQuestion, Long surveyId) {

        if (surveyQuestion.getId() == null || surveyQuestion.getId() == 0) {
            Survey survey = surveyRepository.findOne(surveyId);
            if (survey == null) {
                return false;
            }

            surveyQuestion.setSurvey(survey);

            Integer maxPos = surveyQuestionRepository.findMaxPositionBySurveyId(surveyId);
            surveyQuestion.setPosition((maxPos != null) ? maxPos + 1 : 1);

            surveyQuestionRepository.save(surveyQuestion);

            survey.getSurveyQuestions().add(surveyQuestion);

        } else {
            SurveyQuestion tempSurveyQuestion = surveyQuestionRepository.findOne(surveyQuestion.getId());

            tempSurveyQuestion.setTitle(surveyQuestion.getTitle());
            tempSurveyQuestion.setActive(surveyQuestion.isActive());
            // continue with other properties based on type
            //...
            tempSurveyQuestion.setSelectMultiple(surveyQuestion.isSelectMultiple());
            tempSurveyQuestion.setOptionRandom(surveyQuestion.isOptionRandom());

            surveyQuestionRepository.save(tempSurveyQuestion); // save the modifications

        }

        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean updateSurveyQuestionOption(SurveyOption surveyOption) {
        if (surveyOption != null) {
            surveyOptionRepository.save(surveyOption);

            return true;
        } else {
            return false;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean deleteSurveyQuestionOption(Long optionId, Long parentQuestionId) {
        SurveyOption tempSurveyOption = this.surveyOptionRepository.findOne(optionId);

        Long total1 = surveyAnswerRepository.countBySurveyOption(tempSurveyOption);
        if (total1 == 0) {
            SurveyQuestion tempSurveyQuestion = this.surveyQuestionRepository.findOne(parentQuestionId);
            this.surveyOptionRepository.delete(tempSurveyOption);
            tempSurveyQuestion.getOptions().remove(tempSurveyOption);
            return true;
        } else {
            return false;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean deleteSurveyQuestion(Long questionId, Long surveyId) {
        SurveyQuestion tempSurveyQuestion = this.surveyQuestionRepository.findOne(questionId);

        Long total1 = surveyAnswerRepository.countBySurveyQuestionId(tempSurveyQuestion.getId());
        if (total1 == 0) {
            Survey tempSurvey = this.surveyRepository.findOne(surveyId);
            this.surveyQuestionRepository.delete(tempSurveyQuestion);
            tempSurvey.getSurveyQuestions().remove(tempSurveyQuestion);
            return true;
        } else {
            return false;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SurveyQuestion findSurveyQuestion(Long surveyId) {
        return surveyQuestionRepository.findOne(surveyId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<SurveyQuestionCriteria> findSurveyQuestionCriteriaByQuestionId(Long questionId) {
        return surveyQuestionCriteriaRepository.findByQuestionId(questionId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<SurveyQuestion> findQuestionsInLowerPosition(Integer position, Long surveyId) {
        return surveyQuestionRepository.findQuestionsInLowerPosition(position, surveyId);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean saveSurveyQuestionCriterion(SurveyQuestionCriteria questionCriterion, Long surveyQuestionId) {
        if (questionCriterion.getId() == null || questionCriterion.getId() > 0) {
            SurveyQuestion surveyQuestion = surveyQuestionRepository.findOne(surveyQuestionId);
            questionCriterion.setQuestion(surveyQuestion);
        }

        this.surveyQuestionCriteriaRepository.save(questionCriterion); // no checks at the moment

        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean deleteSurveyQuestionCriterion(SurveyQuestionCriteria questionCriterion) {
        this.surveyQuestionCriteriaRepository.delete(questionCriterion); // no checks at the moment

        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean addProductInProductSurvey(Product product, Long surveyId) {
        SurveyProduct surveyProduct = surveyProductRepository.findOne(surveyId);
        if (surveyProduct != null) {
            surveyProduct.getProducts().add(product);
            surveyProductRepository.save(surveyProduct);

            return true;
        }
        return false;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean deleteProductFromProductSurvey(Long productId, Long surveyId) {
        SurveyProduct surveyProduct = surveyProductRepository.findOne(surveyId);
        Product product = productRepository.findOne(productId);
        if (surveyProduct != null && product != null) {
            surveyProduct.getProducts().remove(product);
            surveyProductRepository.save(surveyProduct);

            return true;
        }
        return false;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean addProductProductLevelThreeInProductCategorySurvey(ProductCategoryLevelThree category, Long surveyId) {
        Survey survey = surveyRepository.findOne(surveyId);
        if (survey != null) {
            if (survey instanceof SurveyBrandProductCategory) {
                SurveyBrandProductCategory surveyBrandProductCategory = (SurveyBrandProductCategory) survey;
                surveyBrandProductCategory.getProductCategories().add(category);
            } else if (survey instanceof SurveyProductCategory) {
                SurveyProductCategory surveyProductCategory = (SurveyProductCategory) survey;
                surveyProductCategory.getProductCategories().add(category);
            }
            surveyRepository.save(survey);

            return true;
        }
        return false;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean deleteCategoryFromProductSurvey(ProductCategoryLevelThree category, Long surveyId) {
        Survey survey = surveyRepository.findOne(surveyId);

        if (survey != null) {
            if (survey instanceof SurveyBrandProductCategory) {
                SurveyBrandProductCategory surveyBrandProductCategory = (SurveyBrandProductCategory) survey;
                surveyBrandProductCategory.getProductCategories().remove(category);

            } else if (survey instanceof SurveyProductCategory) {
                SurveyProductCategory surveyProductCategory = (SurveyProductCategory) survey;
                surveyProductCategory.getProductCategories().remove(category);
            }
            surveyRepository.save(survey);

            return true;
        }
        return false;


    }

    public long noOfSurveyAnswerOptions() {
        return this.surveyAnswerRepository.countAnswerOptions();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Long findLastCycleByUser(Long userId) {
        return surveyAnswerRepository.findLastCycleByUser(userId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Long countSurveyAnswersByUserSinceDate(User user, Long days) {
        Date sinceDate;
        sinceDate = new Date(System.currentTimeMillis()-days*24*60*60*1000);
        return surveyAnswerRepository.countSurveyAnswersByUserSinceDate(user, sinceDate);
    }

}

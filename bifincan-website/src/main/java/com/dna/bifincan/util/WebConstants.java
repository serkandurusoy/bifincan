package com.dna.bifincan.util;

public abstract class WebConstants {
	// order's constants
	public static final String SEL_DATE_KEY = "selectionDate";
	public static final String BALANCE = "balance";	
	public static final String TOTAL_POINTS = "totalPoints";
	public static final String ORDER_ID_PARAM = "orderId";
	public static final String CONFIRMATION_CODE_PARAM = "confirmationCode";
    public static final String PREPARED_STATUS_KEY = "order.status.prepared";
    public static final String DELIVERY_STATUS_KEY = "order.status.delivery";
    public static final String DELIVERED_STATUS_KEY = "order.status.delivered";
    
    // survey's constants
	public static final String SURVEY_PROFILE_KEY = "survey.profileSurvey";
    public static final String SURVEY_BRAND_KEY = "survey.brandSurvey";
    public static final String QUESTION_CREATED_TIME = "question.created.time";
    
    // product's constants
    public static final String PRODUCT_SLUG = "productSlug";
    // general keys
    public static final String ATTEMPT_COUNTER="ATTEMPT_COUNTER";
    public static final String CURRENT_USER_SESSION_KEY="currentUser";
    public static final String MESSAGE_BUNDLE_KEY = "messages";
    public static final String CURRENT_SURVEY_SESSION_KEY="currentSurvey";
    public static final String NEED_FOR_NEW_SURVEY_CYCLE = "needForCycle";
    public static final String GSM_VALIDATION_SUCCESS_MESSAGE = "gsm.validation.success";
    
    // servlet's keys
    public static final String NOT_AVAILABLE_BRAND_LOGO_NAME = "nobrandlogo.png";
    public static final String NOT_AVAILABLE_PRODUCT_SMALL_IMG_NAME = "noproductsmallimage.jpg";
    public static final String NOT_AVAILABLE_PRODUCT_LARGE_IMG_NAME = "noproductlargeimage.jpg";
    public static final String NOT_AVAILABLE_PRODUCT_DETAIL_IMG_NAME = "noproductdetailimage.jpg";
    public static final String NOT_AVAILABLE_BLOG_SMALL_IMG_NAME = "nopostsmallimage.jpg";
    public static final String NOT_AVAILABLE_BLOG_LARGE_IMG_NAME = "nopostlargeimage.jpg";
    public static final String JPEG_MIME = "image/jpeg";
    public static final String PNG_MIME = "image/png";
    public static final String DETAIL_IMG = "detay";  // means detail
    public static final String LARGE_IMG = "buyuk";  // means large
    public static final String SMALL_IMG = "kucuk";  // means small
    
    // user's keys
    public static int LOW_USER_ACTIVITY = 1;
    public static int NORMAL_USER_ACTIVITY = 2;
    public static int HIGH_USER_ACTIVITY = 3;
    
    // signup keys
    public static int SIGNUP_VERIFICATION_UNKNOWN = 0;
    public static int SIGNUP_VERIFICATION_INVITATION_NOT_FOUND = 1;
    public static int SIGNUP_VERIFICATION_INVITATION_EMAIL_NO_MATCH = 2;
    public static int SIGNUP_VERIFICATION_CONFIRMATION_KEY_NO_MATCH = 3;
    public static int SIGNUP_VERIFICATION_INVITATION_ALREADY_IN_USE = 4;
    public static int SIGNUP_VERIFICATION_NO_WELCOME_PRODUCTS = 5;
    public static int SIGNUP_VERIFICATION_INVALID_INVITATION_URL = 6;
    public static int SIGNUP_VERIFICATION_GENERAL_ERROR = 7;
    public static int SIGNUP_VERIFICATION_EMAIL_ALREADY_IN_USE = 8;    
    public static int SIGNUP_SUCCESS = 9; 
    public static int SIGNUP_INVITER_INACTIVE = 10; 
    public static int SIGNUP_INVITER_HAS_INACTIVE_INVITEES = 11; 
    
    // warn message keys
    
    // error message keys
    public static final String ERROR_INVALID_CONFIRMATION_CODE_KEY = "error.invalidConfirmationCode";
    public static final String ERROR_WHILE_PROCESSING_ORDER_KEY = "error.whileProcessingOrder";
    public static final String ERROR_WHILE_PLACING_ORDER_KEY = "error.whilePlacingOrder";
    public static final String ERROR_NO_PRODUCTS_IN_CART_KEY = "error.noProductsInCart";
    public static final String ERROR_NO_RIGHTS_TO_ORDER_KEY = "error.noRightsToOrder";
    public static final String ERROR_NO_AVAILABLE_POINTS_KEY = "error.noAvailablePoints";    
    public static final String ERROR_PRODUCT_NOT_AVAILABLE_KEY = "error.noAvailableProduct";    
    public static final String ERROR_INVALID_NUMBER_OF_PRODUCTS_IN_CART_KEY = "error.invalidNumOfProductsInCart";   
    public static final String ERROR_DUPLICATE_ANSWER_KEY = "error.duplicateAnswerKey";  
    public static final String ERROR_INVALID_ANSWER_KEY = "error.invalidAnswerKey"; 
    public static final String ERROR_DUPLICATE_RATING_KEY = "error.duplicateRatingKey";  
    public static final String ERROR_INVALID_RATING_KEY = "error.invalidRatingKey";  
    public static final String ERROR_DUPLICATE_QUIZ_KEY = "error.duplicateQuizKey";  
    public static final String ERROR_INVALID_QUIZ_KEY = "error.invalidQuizKey";      
    public static final String ERROR_GENERAL_ERROR_KEY = "error.generalErrorKey";
    public static final String ERROR_INVALID_AGE_KEY = "error.invalidUserAge";
    public static final String ERROR_NO_PRODUCT_SURVEY = "error.noProductSurvey";    
    public static final String ERROR_NO_QUIZ_SURVEY = "error.noQuizSurvey";   
    public static final String ERROR_NO_PRODUCT_PROMOTER_KEY = "error.noProductPromoter";    
    public static final String ERROR_LOW_USER_ACTIVITY_LEVEL = "error.tooLowUserActivity";            
    
    public static final String MSG_CORRECT_CONFIRMATION_CODE_KEY = "msg.correctConfirmationCode";  
    
}

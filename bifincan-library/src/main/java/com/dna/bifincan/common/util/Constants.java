package com.dna.bifincan.common.util;

public abstract class Constants {
	// order's constants
	public static final String SEL_DATE_KEY = "selectionDate";
	public static final String BALANCE = "balance";	
	public static final String TOTAL_POINTS = "totalPoints";
	public static final String ORDER_ID_PARAM = "orderId";
	public static final String CONFIRMATION_CODE_PARAM = "confirmationCode";
    public static final String PREPARED_STATUS_KEY = "order.status.prepared";
    public static final String DELIVERY_STATUS_KEY = "order.status.delivery";
    public static final String DELIVERED_STATUS_KEY = "order.status.delivered";
    
    // product's constants
    public static final String PRODUCT_SLUG = "productSlug";
    public static final int MINIMUM_AGE_FOR_CLASSIFIED_PRODUCTS = 18; 
    
    // general keys
    public static final String CURRENT_USER_SESSION_KEY="currentUser";
    public static final String MESSAGE_BUNDLE_KEY = "messages";
    
    public static final String QUESTION_NODE_KEY = "QUESTION";
    public static final String OPTION_NODE_KEY = "OPTION";
    
    // servlet's keys
    public static String NOT_AVAILABLE_BRAND_LOGO_NAME = "nobrandlogo.png";
    public static String NOT_AVAILABLE_PRODUCT_SMALL_IMG_NAME = "noproductsmallimage.jpg";
    public static String NOT_AVAILABLE_PRODUCT_LARGE_IMG_NAME = "noproductlargeimage.jpg";
    public static String NOT_AVAILABLE_PRODUCT_DETAIL_IMG_NAME = "noproductdetailimage.jpg";
    public static String NOT_AVAILABLE_BLOG_SMALL_IMG_NAME = "nopostsmallimage.jpg";
    public static String NOT_AVAILABLE_BLOG_LARGE_IMG_NAME = "nopostlargeimage.jpg";
    public static String JPEG_MIME = "image/jpeg";
    public static String PNG_MIME = "image/png";
    public static String DETAIL_IMG = "detay";  // means detail
    public static String LARGE_IMG = "buyuk";  // means large
    public static String SMALL_IMG = "kucuk";  // means small
    
    // error message keys
    public static String ERROR_INVALID_CONFIRMATION_CODE_KEY = "error.invalidConfirmationCode";
    public static String ERROR_WHILE_PROCESSING_ORDER_KEY = "error.whileProcessingOrder";
    public static String ERROR_WHILE_PLACING_ORDER_KEY = "error.whilePlacingOrder";
    public static String ERROR_NO_PRODUCTS_IN_CART_KEY = "error.noProductsInCart";
    public static String ERROR_NO_RIGHTS_TO_ORDER_KEY = "error.noRightsToOrder";
    public static String ERROR_NO_AVAILABLE_POINTS_KEY = "error.noAvailablePoints";    
    public static String ERROR_PRODUCT_NOT_AVAILABLE_KEY = "error.noAvailableProduct";    
    public static String ERROR_INVALID_NUMBER_OF_PRODUCTS_IN_CART_KEY = "error.invalidNumOfProductsInCart";       
    
    public static String MSG_CORRECT_CONFIRMATION_CODE_KEY = "msg.correctConfirmationCode";  
    public static String RELOAD_CACHE_KEY = "_@3e_45!t5e";
}

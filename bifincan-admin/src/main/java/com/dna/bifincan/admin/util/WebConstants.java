package com.dna.bifincan.admin.util;

public abstract class WebConstants {
	// order's constants
    
    // product's constants
    public static final String PRODUCT_SLUG = "productSlug";

    // general keys
    public static final String CURRENT_USER_SESSION_KEY="currentUser";
    public static final String MESSAGE_BUNDLE_KEY = "messages";
    
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
    
    // other constants
    public static String SUCCESS_DELETION_KEY = "success.delete"; 
    public static String ERROR_DELETION_KEY = "error.delete"; 
    public static String SUCCESS_INSERT_KEY = "success.insert"; 
    public static String ERROR_INSERT_KEY = "error.insert";
    public static String SUCCESS_UPDATE_KEY = "success.update"; 
    public static String ERROR_UPDATE_KEY = "error.update";     
    public static String ERROR_GEN_ERROR_KEY = "error.generalError";       
}

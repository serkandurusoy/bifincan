package com.dna.bifincan.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public abstract class DateUtils {
	private final static String YEAR_MONTH = "yyyy MMMM";
	private final static String YYYYMMDD = "yyyy-MM-dd";	
	private final static String DDMMYYYY = "dd/MM/yyyy";	
	
    public static Date formatYYYYMMDDTokenToDate(String source) {
    	Date result = null;
    	if(source != null) {
	    	try {
	    		SimpleDateFormat formatter = new SimpleDateFormat(YYYYMMDD);
	    		result = formatter.parse(source);
	    	} catch(java.text.ParseException ex) { 
	    		throw new IllegalArgumentException(ex);
	    	}
    	}	
    	return result;
    }
    
    public static String formatDateToMonthAndYear(String source) {
    	FacesUtils.getFacesContext().getViewRoot().getLocale();
    	Date tDate = formatYYYYMMDDTokenToDate(source);
    	SimpleDateFormat formatter = new SimpleDateFormat(YEAR_MONTH, FacesUtils.getCurrentLocale());
    	return formatter.format(tDate);
    }
    
    public static String formatDateToYYYYMMDD(Date source) {
    	SimpleDateFormat formatter = new SimpleDateFormat(YYYYMMDD);
    	return formatter.format(source);
    }  
    
    public static String formatDateToDDMMYYYY(Date source) {
    	SimpleDateFormat formatter = new SimpleDateFormat(DDMMYYYY);
    	return formatter.format(source);
    }  
    
    public static int extractDateOfMonth(Date source) {
    	int day = 0;
    	if(source != null) {
	  		Calendar cal = Calendar.getInstance();
	  		cal.setTime(source);
	  		day = cal.get(Calendar.DAY_OF_MONTH);
    	}
    	return day;
    }
    
}

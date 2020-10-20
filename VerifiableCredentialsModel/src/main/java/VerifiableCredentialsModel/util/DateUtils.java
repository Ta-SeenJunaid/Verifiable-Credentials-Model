package VerifiableCredentialsModel.util;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DateUtils {
	
    private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

	
	private static String TIME_ZONE = "Asia/Dhaka";
	
	private static String STRING_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
    private static String UTC_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static String getCurrentTimeStampString() {
    	return String.valueOf(System.currentTimeMillis());
    }
    
    public static Long getCurrentTimeStamp() {
    	return System.currentTimeMillis();
    }
    
    public static String getNoMillisecondTimeStampString() {
        return String.valueOf(Instant.now().getEpochSecond());
    }
    
    public static Long getNoMillisecondTimeStamp() {
        return Instant.now().getEpochSecond();
    }

}

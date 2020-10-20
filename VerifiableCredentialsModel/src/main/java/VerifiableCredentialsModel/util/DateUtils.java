package VerifiableCredentialsModel.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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
    
    public static boolean isAfterCurrentTime(Long date) {
        
    	if (String.valueOf(date) != null 
    			&& String.valueOf(date).length() 
    			== getCurrentTimeStampString().length()) {
    		return date >  getCurrentTimeStamp();
    	} else if (String.valueOf(date) != null
                && String.valueOf(date).length() == getNoMillisecondTimeStampString().length()) {
                return date > getNoMillisecondTimeStamp();
            } else {
            	return false;
            }
    }
    
    public static Long convertToNoMillisecondTimeStamp(Long date) {
        if (String.valueOf(date) == null) {
            logger.error("the timestamp is null.");
            return null;
        }
        if (String.valueOf(date) != null
            && String.valueOf(date).length() != getCurrentTimeStampString().length()) {
            if (String.valueOf(date).length() == getNoMillisecondTimeStampString().length()) {
                return date;
            }
            logger.error("the timestamp is illegal.");
            return null;
        }
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern(STRING_DATE_FORMAT);
        String time = ftf.format(
            LocalDateTime.ofInstant(Instant.ofEpochMilli(date),
                ZoneId.of(TIME_ZONE)));
        LocalDateTime parse = LocalDateTime.parse(time, ftf);
        return LocalDateTime.from(parse).atZone(ZoneId.of(TIME_ZONE)).toInstant().getEpochSecond();
    }

}

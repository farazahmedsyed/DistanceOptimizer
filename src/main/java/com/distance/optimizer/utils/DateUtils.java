package com.distance.optimizer.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author FarazAhmed
 */
public class DateUtils {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static Date formatDate (String date) throws ParseException {
        if (date == null || date.isEmpty()) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT);
        return df.parse(date);
    }

    public static String format (Date date, String format)  {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public static Date addDay(Date src, int value){
    	Calendar cal = GregorianCalendar.getInstance();
    	cal.setTime(src);
    	cal.add(Calendar.DAY_OF_MONTH, value);
    	return cal.getTime();
    }

    public static String getDayOfWeek (Date date) {
        if (EntityHelper.isNull(date))
            return null;

        switch (date.getDay()) {
            case 1:
                return "M";
            case 2:
                return "T";
            case 3:
                return "W";
            case 4:
                return "TH";
            case 5:
                return "F";
            case 6:
                return "S";
            default:
                return "SU";
        }
    }

}

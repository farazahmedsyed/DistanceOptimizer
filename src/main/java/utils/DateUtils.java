package utils;

import org.apache.log4j.Logger;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by Venturedive on 10/14/2016.
 */
public class DateUtils {

    private static final Logger LOGGER = Logger.getLogger(DateUtils.class);
    public static final int HOURS_IN_A_DAY = 24;
    public static final int MINUTES_IN_AN_HOUR = 60;
    public static final int SECONDS_IN_A_MINUTE = 60;
    public static final long SECONDS_IN_A_DAY = 86400l;
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATE_FORMAT_SHORT = "d/M/yyyy";


    public static String formate(Date date, String format) throws Exception {
    	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
    	return df.format(date);
    }

    public static LocalDateTime getCurrentTimeUTC() {
        return new LocalDateTime(DateTimeZone.UTC);
    }

    public static LocalDateTime convertToLocal(LocalDateTime timeUTC, Integer offsetMillis) {
        return timeUTC.plusMillis(offsetMillis);
    }

    public static java.time.LocalTime convertToLocal(java.time.LocalTime timeUTC, Integer offsetMillis) {
        return timeUTC.plusSeconds(offsetMillis / 1000);
    }

    public static java.time.LocalDateTime convertToLocal(java.time.LocalDateTime dateTimUTC, Integer offsetMillis) {
        return dateTimUTC.plusSeconds(offsetMillis / 1000);
    }

    public static Date convertToLocal (Date dateUTC, Integer offsetMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateUTC);
        calendar.add(Calendar.MILLISECOND, offsetMillis);
        return calendar.getTime();
    }

    public static Date convertToUTC (Date dateUTC, Integer offsetMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateUTC);
        calendar.add(Calendar.MILLISECOND, (-1)*offsetMillis);
        return calendar.getTime();
    }

    public static LocalDateTime convertToUTC(LocalDateTime timeLocal, Integer offsetMillis) {
        offsetMillis = (offsetMillis)*(-1);
        return timeLocal.plusMillis(offsetMillis);
    }

    public static java.time.LocalDateTime convertToUTC(java.time.LocalDateTime localDateTime, Integer offsetMillis) {
        Integer offsetSeconds = offsetMillis/1000;
        offsetSeconds = (offsetSeconds)*(-1);
        return localDateTime.plusSeconds(offsetSeconds);
    }

    public static LocalDateTime getConstantDateTime(LocalDate date, String time) {
        LocalTime localTime = new LocalTime(time);
        return date.toLocalDateTime(localTime);
    }

    public static java.time.LocalDateTime getConstantDateTime(java.time.LocalDate date, String time) {
        java.time.LocalTime localTime = java.time.LocalTime.parse(time);
        return java.time.LocalDateTime.of(date, localTime);
    }

    public static String formatLocalDateTime (LocalDateTime localDateTime) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT);
        return df.format(localDateTime.toDate());
    }

    public static LocalDateTime parseLocalDateTime (String localDateTime) {
        org.joda.time.format.DateTimeFormatter dtf = DateTimeFormat.forPattern(DATE_TIME_FORMAT);
        return dtf.parseLocalDateTime(localDateTime);
    }

    public static String formatLocalDateTime (java.time.LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return formatter.format(localDateTime);
    }

    public static String formatDate (Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT);
        return df.format(date);
    }

    public static Date formatDate (String date) throws ParseException {
        if (date == null || date.isEmpty()) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT);
        return df.parse(date);
    }

    public static Date format (String date, String format) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.parse(date);
    }

    public static String format (Date date, String format)  {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public static java.time.LocalDate formatLocalDate (String date) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return java.time.LocalDate.parse(date, formatter);
    }

    public static String formatLocalTime (java.time.LocalTime localTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
        return formatter.format(localTime);
    }

    public static java.time.LocalTime parseLocalTime (String localTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
        return java.time.LocalTime.parse(localTime, formatter);
    }

    public static Boolean timeBefore (LocalDateTime dateTime1, LocalDateTime dateTime2) {
        LocalTime localTime1 = new LocalTime(dateTime1);
        LocalTime localTime2 = new LocalTime(dateTime2);
        if (localTime1.getHourOfDay() < localTime2.getHourOfDay()) {
            return true;
        }
        else if (localTime1.getHourOfDay() == localTime2.getHourOfDay()) {
          return localTime1.getMinuteOfHour() < localTime2.getMinuteOfHour();
        }
        else {
            return false;
        }
    }

    public static Boolean timeAfter (LocalDateTime dateTime1, LocalDateTime dateTime2) {
        LocalTime localTime1 = new LocalTime(dateTime1);
        LocalTime localTime2 = new LocalTime(dateTime2);
        if (localTime1.getHourOfDay() > localTime2.getHourOfDay()) {
            return true;
        }
        else if (localTime1.getHourOfDay() == localTime2.getHourOfDay()) {
          return localTime1.getMinuteOfHour() > localTime2.getMinuteOfHour();
        }
        else {
            return false;
        }
    }

    public static Boolean timeBefore (java.time.LocalTime localTime1, java.time.LocalTime localTime2) {
        if (localTime1.getHour() < localTime2.getHour()) {
            return true;
        }
        else if (localTime1.getHour() == localTime2.getHour()) {
            return localTime1.getMinute() < localTime2.getMinute();
        }
        else {
            return false;
        }
    }

    public static Boolean timeAfter (java.time.LocalTime localTime1, java.time.LocalTime localTime2) {
        if (localTime1.getHour() > localTime2.getHour()) {
            return true;
        }
        else if (localTime1.getHour() == localTime2.getHour()) {
            return localTime1.getMinute() > localTime2.getMinute();
        }
        else {
            return false;
        }
    }

    public static Boolean timeBetween (java.time.LocalTime current, java.time.LocalTime start, java.time.LocalTime end) {
        if (timeAfter(current, start) && timeBefore(current, end))
            return true;
        return false;
    }

    public static Boolean timeEquals (java.time.LocalTime localTime1, java.time.LocalTime localTime2) {
        if (localTime1.getHour() == localTime2.getHour() && localTime1.getMinute() == localTime2.getMinute())
            return true;
        return false;
    }

    public static Date addYear(Date src, int value){
    	Calendar cal = GregorianCalendar.getInstance();
    	cal.setTime(src);
    	cal.add(Calendar.YEAR,value);
    	return cal.getTime();
    }
    public static Date addMonth(Date src, int value){
    	Calendar cal = GregorianCalendar.getInstance();
    	cal.setTime(src);
    	cal.add(Calendar.MONTH,value);
    	return cal.getTime();
    }
    public static Date addDay(Date src, int value){
    	Calendar cal = GregorianCalendar.getInstance();
    	cal.setTime(src);
    	cal.add(Calendar.DAY_OF_MONTH,value);
    	return cal.getTime();
    }
    public static Date addHours(Date src, int value){
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(src);
        cal.add(Calendar.HOUR, value);
        return cal.getTime();
    }
    public static Date addMinutes(Date src, int value){
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(src);
        cal.add(Calendar.MINUTE, value);
        return cal.getTime();
    }
    
    /**
     * returns a list of days (Date objects without time) that occur between the
     * provided range. The list is inclusive, which means if provided from date
     * 2 Feb 2016 05:25:13 to date 4 Feb 19:45:11, the result would be a list
     * which contains 
     * [2 Feb 2016 00:00:00, 3 Feb 2016 00:00:00, 4 Feb 2016 00:00:00]
     * 
     * @param from start of the range
     * @param to end of the range
     * @return list of date objects with no time set to 00:00:00
     */
    
    public static List<Date> getDaysBetweenRange(Date from, Date to) {
      List<Date> days = new ArrayList<>();
      Date internalFrom = new Date(from.getTime());
      while (internalFrom.getTime() <= to.getTime()) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(internalFrom);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        days.add(calendar.getTime());
        internalFrom.setTime(internalFrom.getTime() + (SECONDS_IN_A_DAY * 1000));
      }
      return days;
    }
    
    /**
     * return the first instant of the day in the date provided
     * @param date
     * @return Date
     */
    public static Date getDayStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
    /**
     * return the final instant of the day in the date provided
     * @param date
     * @return Date
     */
    public static Date getDayEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static LocalDate plusBusinessDays (LocalDate originalDate, Integer daysToAdd) {
        LocalDate newDate = originalDate;
        Integer i = 0;
        while(i<daysToAdd)
        {
            newDate = newDate.plusDays(1);
            if(newDate.getDayOfWeek()<=5)
            {
                i++;
            }
        }
        return newDate;
    }

    public static java.time.LocalDate plusBusinessDays (java.time.LocalDate originalDate, Integer daysToAdd) {
        java.time.LocalDate newDate = originalDate;
        Integer i = 0;
        while(i<daysToAdd)
        {
            newDate = newDate.plusDays(1);
            if(newDate.getDayOfWeek().getValue()<=5)
            {
                i++;
            }
        }
        return newDate;
    }

    public static Date asDate(java.time.LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(java.time.LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static java.time.LocalDate asLocalDate(Date date) {
        return java.time.Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static java.time.LocalDateTime asLocalDateTime(Date date) {
        return java.time.Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static void setTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public static void setTimeToEndofDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }

    public static Date getBeginningOfCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        DateUtils.setTimeToBeginningOfDay(cal);
        return cal.getTime();
    }

    public static Date getEndOfCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        DateUtils.setTimeToEndofDay(cal);
        return cal.getTime();
    }
}

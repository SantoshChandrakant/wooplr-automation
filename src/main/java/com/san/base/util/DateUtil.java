package com.san.base.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {


	/** default date fromat */
    private static final String DEF_DATE_FORMAT = "dd/MM/yyyy";
	/** default date time format */
    private static final String DEF_DATE_TIME_FORMAT = "dd/MM/yyyy hh:mm:ss a";
	/** default date time format in 24 hrs */
    private static final String DEF_DATE_FORMAT_24_HRS = "dd/MM/yyyy HH:mm";
    
	/**
	 * To get a date by some days after the given date.
	 * 
	 * @param date the given date
	 * @param noOfDays days
	 * @return the target date
	 * 
	 */
	public static Date addDays(Date date, int noOfDays) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, noOfDays);
		return cal.getTime();
	}
     
	/**
	 * To judge if the first date is after the second one.
	 * 
	 * @param date1 the first date
	 * @param date2 the second date
	 * @return if is after
	 * 
	 */
	public static  boolean after(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		cal1.set(Calendar.HOUR_OF_DAY,0);
		cal1.set(Calendar.MINUTE,0);
		cal1.set(Calendar.SECOND,0);
		cal1.set(Calendar.MILLISECOND,0);
		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		cal2.set(Calendar.HOUR_OF_DAY,0);
		cal2.set(Calendar.MINUTE,0);
		cal2.set(Calendar.SECOND,0);
		cal2.set(Calendar.MILLISECOND,0);
		
		return cal1.after(cal2);
	}

	/**
	 * To judge if the first date is before the second one.
	 * 
	 * @param date1 the first date
	 * @param date2 the second date
	 * @return if is before
	 * 
	 */
	public static boolean before(Date date1,Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		cal1.set(Calendar.HOUR_OF_DAY,0);
		cal1.set(Calendar.MINUTE,0);
		cal1.set(Calendar.SECOND,0);
		cal1.set(Calendar.MILLISECOND,0);
		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		cal2.set(Calendar.HOUR_OF_DAY,0);
		cal2.set(Calendar.MINUTE,0);
		cal2.set(Calendar.SECOND,0);
		cal2.set(Calendar.MILLISECOND,0);
		
		return cal1.before(cal2);
	}

	/**
	 * To format a date using the appointed format string.
	 * 
	 * @param date a date
	 * @param format format string
	 * @return formatted date in string
	 */
	public static String formatDate(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}

	/**
	 * To get the date of specific days after today.
	 * 
	 * @param days days
	 * @return the date
	 */
	public static Date getDateAfter(int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, days);
		return calendar.getTime();
	}

	/**
	 * To get the date of specific days before today.
	 * 
	 * @param days days
	 * @return the date
	 */
	public static Date getDateBefore(int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, -1*days);
		return calendar.getTime();
	}


	/**
	 * get date time
	 * @param date given date
	 * @param hour given hour
	 * @param minute given minute
	 * @return date object
	 * @throws ParseException thrown while parsing the date
	 * 
	 */
	public static Date getDateTime(Date date, int hour, int minute) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, hour);
		calendar.set(Calendar.MINUTE, minute);
		return calendar.getTime();
	}

	/**
	 * get date time
	 * @param dateStr date as string
	 * @param hour given hour
	 * @param minute given minute
	 * @return date object
	 * @throws ParseException thrown while parsing the date
	 * 
	 */
	public static Date getDateTime(String dateStr, int hour, int minute) throws ParseException {
		Date dt = DateUtil.toDate(dateStr, null);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dt);
		calendar.set(Calendar.HOUR, hour);
		calendar.set(Calendar.MINUTE, minute);
		return calendar.getTime();
	}

	/**
	 * get date time in 24 hrs format
	 * @param dt given date
	 * @return datetime
	 * 
	 */
	public static String getDateTimeIn24HRs(Date dt){
		return toDateTimeString(dt,DEF_DATE_FORMAT_24_HRS);
	}

	/**
	 * get hour from the given date
	 * @param date given date
	 * @return hour in 24-hrs format
	 * 
	 */
	public static int getHour(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * get minute from the given date
	 * @param date given date
	 * @return minute
	 * 
	 */
	public static int getMinute(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * Determines whether the given date is <i>n</i> days
	 * old. The value of <i>n</i> is provided as the second
	 * parameter to this method.
	 * <p>
	 * The method performs a date difference between the
	 * current date and the input date. If the difference
	 * is greater than the number days specified by the
	 * second parameter then true is returned, false otherwise.
	 *
	 * @param input the date to be tested.
	 *
	 * @param days an integer containing the number of days
	 *
	 * @return true if the specified date is <i>n</i> days
	 *          behind the current date, false otherwise.
	 * 
	 */
	public static boolean isDateNDaysOld(Date input,int days) {
		if(input == null)
			throw new NullPointerException("Input date is null");
		Calendar today=Calendar.getInstance();
		Calendar inputDate=Calendar.getInstance();
		inputDate.setTime(input);
		//add days to the input date.
		inputDate.add(Calendar.DATE,days);
		//Still today after the input date
		//then the given date is very much
		//behind the number of days given.
		return today.after(inputDate);
	}

    /**
	 * To get an instance of java.util.Date from a string
	 * 
	 * @param dateString the string represents a date
	 * @param format format string
	 * @return an instance of java.util.Date
	 * @throws ParseException
	 */
	public static Date parseDate(String dateString, String format) throws ParseException {
		return new SimpleDateFormat(format).parse(dateString);
	}

	/**
	 * get formatted date
	 * @param strDate date as string
	 * @param format date format
	 * @return Date object
	 * @throws ParseException thrown while parsing the date
	 * 
	 */
	public static Date toDate(String strDate, String format) throws ParseException {
		if (StringUtil.isEmpty(format))
			format = DEF_DATE_FORMAT;
		DateFormat formatter = new SimpleDateFormat(format);
		return formatter.parse(strDate);
	}

	/**
	 * get date as string
	 * @param date Date object
	 * @param format date format
	 * @return formatted date
	 * 
	 */
	public static String toDateString(Date date, String format) {
		if (StringUtil.isEmpty(format))
			format = DEF_DATE_FORMAT;
		DateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}

	/**
	 * get formatted date
	 * @param date date object
	 * @return formatted Date
	 * @throws ParseException thrown while parsing the date
	 * 
	 */
	public static Date toDateTime(Date date) throws ParseException {
		DateFormat formatter = new SimpleDateFormat(DEF_DATE_TIME_FORMAT);
		return toDate(formatter.format(date), DEF_DATE_TIME_FORMAT);
	}

	/**
	 * get formatted date
	 * @param date date object
	 * @param format date format
	 * @return formatted Date
	 * @throws ParseException thrown while parsing the date
	 * 
	 */
	public static Date toDateTime(Date date, String format) throws ParseException {
		if (StringUtil.isEmpty(format))
			format = DEF_DATE_FORMAT;
		DateFormat formatter = new SimpleDateFormat(format);
		return toDate(formatter.format(date), format);
	}

	/**
	 * get date as string
	 * 
	 * @param date
	 *            Date object
	 * @param format
	 *            date format
	 * @return formatted date time
	 * 
	 */
	public static String toDateTimeString(Date date, String format) {
		if (StringUtil.isEmpty(format))
			format = DEF_DATE_TIME_FORMAT;
		DateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}

	
}

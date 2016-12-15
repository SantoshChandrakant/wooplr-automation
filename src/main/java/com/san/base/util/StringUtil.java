package com.san.base.util;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import com.wooplr.base.controller.Logging;



public final class StringUtil {

	private static final String NUM_PATTERN = "((-|\\+)?[0-9]+(\\.[0-9]+)?)+";

	public static String arrayToString(Long[] arr) {
		String str = null;
		if (arr != null && arr.length > 0) {
			str = "";
			for (int i = 0; arr != null && i < arr.length; i++) {
				str = str + arr[i].toString() + "|";
			}

			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

	/**
	 * get comma seperated string
	 * 
	 * @param obj
	 *            an array not as wrapper
	 * @return comma separated string
	 */
	public static String getCSV(Object[] obj) {

		if (obj == null || obj.length == 0)
			return "";

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < obj.length; i++) {
			sb.append(obj[i] + ",");
		}
		return sb == null ? null : sb.toString().substring(0,
				sb.toString().length() - 1);
	}

	/**
	 * Deprecated - use the Integer form of this method instead. 
	 * 
	 * Given a string that represents a month (i.e. "1" or "2"), this method will return the 3-letter 
	 * abbreviation for that month, so "1" would return "JAN", "2" would return "FEB", etc.
	 * 
	 * @param month
	 * @return
	 */
	@Deprecated
	public static String getMonthInWords( String month ) {
        // Parse out the desired month. If it can't be parsed into an int, it should throw an exception
		// Subtract 1 from the int, because the DateUtils are 0-indexed at January
		int monthIndex;
		try {
			monthIndex = Integer.parseInt( month );
		} catch ( NumberFormatException ex ) {
			//TODO Keeping this as a null return so the functionality stays the same as before--but this should really throw an exception
			//Meanwhile we'll at least give a helpful error message
			Logging.log( "The String passed in cannot be parsed as an Integer: " + month );
			return null;
		}
        
		//Now that we've scrubbed the integer out of the string passed in, do the real work
        return getMonthInWords( monthIndex );
	}

	/**
	 * Given an integer, this method will return the 3-letter abbreviation for that month, so 1 
	 * would return "JAN", 2 would return "FEB", etc.
	 * 
	 * @param month
	 * @return
	 */
	protected static String getMonthInWords( int monthIndex ) {
		//Set up a new date object to have the month passed in
		Date monthDate;
		int offsetMonthIndex = monthIndex - 1;
		try {
			
			monthDate = DateUtils.setMonths( new Date(), offsetMonthIndex );
		} catch ( IllegalArgumentException ex ) {
			//TODO This still returns null, but these try/catch blocks should go away and these exceptions should bubble up
			//Meanwhile here's a helpful error message
			Logging.log( "The number passed in is out of bounds for a month. It should be only 1-12, but you passed in [" + offsetMonthIndex + "]" );
			return null;
		}
        
        //Format the string to return just the month, and only return the first 3 characters. We could also use SimpleDateFormat instead
		//TODO - this doesn't deal with locales, so we should consider setting it to Locale.US somewhere earlier
        String monthStr = String.format( "%tB", monthDate ).substring( 0, 3 );
        
        //Return it in upper case
        return monthStr.toUpperCase();
	}

	protected static int offsetMonthIndex(int monthIndex) {
		return monthIndex - 1;
	}

	public static boolean isBoolean(String str) {
		return ("false".equalsIgnoreCase(str) || "true".equalsIgnoreCase(str));
	}

	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	public static boolean isNumeric(String input) {

		return input.matches(NUM_PATTERN);

	}

	public static boolean isPositive(String str) {
		try {
			if (str == null || str.trim().length() == 0)
				return false;
			return Double.parseDouble(str) > 0;
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * 
	 * @param str
	 *            inputr string
	 * @return trimmed string
	 */
	public static String safeTrim(String str) {
		return str == null ? null : str.toString().trim();
	}

	/**
	 * Converts a double to string in two decimal format (no rounding--the number is just lopped off)
	 * .9811539845 returns ".98" 
	 * 2165461.236 returns "2165461.23"
	 * 
	 * @param doubleValue a floating point number
	 * @return <code>String</code>, a String containing a number, set to two decimal places
	 */
	public static String toDoubleDigit( double doubleValue ) {
		//String.format forces us to round the number up or down, and there don't seem to be any options to truncate.
		// This method cuts it down to 00.000 format, then substrings out the last decimal
		String formattedStr = String.format( "%-1.3f", doubleValue );
		return formattedStr.substring( 0, formattedStr.length() - 1 );
	}

	private StringUtil() {
	}

	/**
	 * convert first letter of the string to upper case
	 * 
	 * @param origin
	 * @return
	 */
	public static String toUpperCaseFirstLetter(String origin) {
		String ret = origin;
		if (StringUtils.isNotEmpty(origin)) {
			ret = origin.substring(0, 1).toUpperCase();
			ret = ret + origin.substring(1);
		}
		return ret;
	}

	/**
	 * convert first letter of the string to lower case
	 * 
	 * @param origin
	 * @return
	 */
	public static String toLowerCaseFirstLetter(String originStr) {
		String ret = null;
		if (StringUtils.isEmpty(originStr)) {
			ret = originStr;
		} else {
			ret = String.valueOf(originStr.charAt(0)).toLowerCase();
			ret = ret.concat(originStr.substring(1));
		}
		return ret;
	}

	/**
	 * return get method name
	 * 
	 * @param origin
	 * @return
	 */
	public static String getGetterName(String origin) {
		String ret = toUpperCaseFirstLetter(origin);
		if (StringUtils.isNotEmpty(ret)) {
			return "get" + ret;
		}
		return ret;
	}

	/**
	 * return set method name
	 * 
	 * @param origin
	 * @return
	 */
	public static String getSetterName(String origin) {
		String ret = toUpperCaseFirstLetter(origin);
		if (StringUtils.isNotEmpty(ret)) {
			return "set" + ret;
		}
		return ret;
	}

	/**
	 * get the regex in string
	 * 
	 * @param originalStr
	 * @param regex
	 * @return
	 */
	public static String getMatchedRegex(String originalStr, String regex) {
		String ret = null;
		Pattern p1 = Pattern.compile(regex);
		Matcher m1 = p1.matcher(originalStr);
		if (m1.find()) {
			ret = m1.group(0);
		}
		return ret;
	}

	/**
	 * replace with regex
	 * 
	 * @param originalStr
	 * @param regex
	 * @return
	 */
	public static String replaceWithRegex(String originalStr, String regex) {
		Pattern p1 = Pattern.compile(regex);
		Matcher m1 = p1.matcher(originalStr);
		return m1.replaceAll("");
	}
	
	public static boolean isEquals(String expected, String original){
		if(expected == null) return true;
		
		if(!expected.equals(original)){
			Logging.log(null,
					"ASSERTION FAILED: expected is " + expected + ", " +
					"but the displayed vaule is " + original, true, true);
			return false;
		}
		return true;
	}
}
package com.wooplr.base.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Appender;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.google.common.collect.Lists;
import com.google.gdata.util.common.html.HtmlToText;
import com.wooplr.base.driver.web.IScreenshotListener;
import com.wooplr.base.driver.web.ScreenShot;
import com.wooplr.base.helper.StringHelper;
import com.wooplr.base.reporter.PluginsUtil;

/**
 * Provides log methods for Web and API testing
 * 
 */
public class Logging {

	private static Map<String, Map<String, Map<String, List<String>>>> pageListenerLogMap = Collections
			.synchronizedMap(new HashMap<String, Map<String, Map<String, List<String>>>>());

	private static final Set<IScreenshotListener> screenshotListeners = new LinkedHashSet<IScreenshotListener>();

	/**
	 * Ability to register IScreenshotListeners.
	 */
	public static void registerScreenshotListener(
			IScreenshotListener screenshotListener) {
		screenshotListeners.add(screenshotListener);
	}

	public static void logScreenshot(final ScreenShot screenshot) {
		for (final IScreenshotListener screenshotListener : screenshotListeners) {
			new Thread() {
				public void run() {
					try {
						screenshotListener.onScreenshotCaptured(screenshot); 
					} catch (Exception e) {
						// catching all listener impl exceptions to keep
						// continue with tests execution.
						// who ever implementing listener they must handle the
						// excetpions properly.
						System.err
								.println("Error in ScreenshotListener implementation "
										+ screenshotListener.getClass()
												.getName()
										+ ". "
										+ e.getMessage());
						// stacktrace will help to pin point issue.
						e.printStackTrace();
					}
				}
			}.start();
		}
	}

	/**
	 * Log method
	 * 
	 * @param message
	 */
	public static void error(String message) {
		message = "<li><b><font color='#FF0000'>" + message
				+ "</font></b></li>";
		log(null, message, false, false);
	}

	public static Logger getLogger(Class<?> clz) {
		boolean rootIsConfigured = Logger.getRootLogger().getAllAppenders()
				.hasMoreElements();
		if (!rootIsConfigured) {
			BasicConfigurator.configure();
			Logger.getRootLogger().setLevel(Level.INFO);
			Appender appender = (Appender) Logger.getRootLogger()
					.getAllAppenders().nextElement();
			appender.setLayout(new PatternLayout(" %-5p %d [%t] %C{1}: %m%n"));
		}
		return Logger.getLogger(clz);
	}

	public static Map<String, Map<String, List<String>>> getPageListenerLog(
			String pageListenerClassName) {
		return pageListenerLogMap.get(pageListenerClassName);
	}

	public static List<String> getPageListenerLogByMethodInstance(
			ITestResult testResult) {

		for (Entry<String, Map<String, Map<String, List<String>>>> listenerEntry : pageListenerLogMap
				.entrySet()) {
			if (!PluginsUtil.getInstance().isTestResultEffected(
					listenerEntry.getKey()))
				continue;

			Map<String, Map<String, List<String>>> pageMap = listenerEntry
					.getValue();
			for (Entry<String, Map<String, List<String>>> pageEntry : pageMap
					.entrySet()) {
				Map<String, List<String>> errorMap = pageEntry.getValue();
				String methodInstance = StringHelper.constructMethodSignature(
						testResult.getMethod().getConstructorOrMethod()
								.getMethod(), testResult.getParameters());
				return errorMap.get(methodInstance);
			}
		}

		return null;
	}

	/**
	 * Log method
	 * 
	 * @param message
	 */
	public static void info(String message) {
		message = "<li><font color='#008000'>" + message + "</font></li>";
		log(null, message, false, false);
	}

	/**
	 * Log method
	 * 
	 * @param message
	 */
	public static void log(String message) {
		log(null, message, false, false);
	}

	/**
	 * Log
	 * 
	 * @param message
	 * @param logToStandardOutput
	 */
	public static void log(String message, boolean logToStandardOutput) {
		log(null, message, false, logToStandardOutput);
	}

	public static void log(String url, String message, boolean failed,
			boolean logToStandardOutput) {

		if (message == null)
			message = "";

		message = message.replaceAll("\\n", "<br/>");

		if (failed) {
			message = "<span style=\"font-weight:bold;color:#FF0066;\">"
					+ message + "</span>";
		}

		Reporter.log(message, logToStandardOutput); 
		//Reporter.log(escape(message), logToStandardOutput); // fix for testng6.7
	}

	public static String escape(String message) {
		return message.replaceAll("\\n", "<br/>").replaceAll("<", "@@lt@@")
				.replaceAll(">", "^^gt^^");
	}

	public static String unEscape(String message) {
		message = message.replaceAll("<br/>", "\\n").replaceAll("@@lt@@", "<")
				.replaceAll("\\^\\^gt\\^\\^", ">");

		message = HtmlToText.htmlToPlainText(message);
		return message;
	}

	public static String getDate() {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss,SSS ");
		return df.format(new Date());
	}

	public static void logAPICall(String url, String message, boolean failed) {
		log(url, "<li>"
				+ (failed ? getDate() + "<b>FailedAPICall</b>: " : getDate()
						+ "APICall: ") + message + "</li>", failed,
				false);
	}

	public static void logDBCall(String message, boolean failed) {
		log(null, "<li>"
				+ (failed ? getDate() + "<b>FailedDBCall</b>: " : getDate()
						+ "DBCall: ") + message + "</li>", failed, false);
	}

	public static void logEmailStep(String message, boolean failed) {
		log(null, "<li>"
				+ (failed ? getDate() + "<b>FailedEmailStep</b>: " : getDate()
						+ "EmailStep: ") + message + "</li>", failed, false);
	}

	public static void logRESTAPICall(String url, String message, boolean failed) {
		log(url, "<li>"
				+ (failed ? getDate() + "<b>FailedRESTAPICall</b>: " : getDate()
						+ "RESTAPICall: ") + message + "</li>", failed,
				false);
	}
	
	public static void logSSHCall(String message, boolean failed) {
		log(null, "<li>"
				+ (failed ? getDate() + "<b>FailedSSHStep</b>: " : getDate()
						+ "SSHStep: ") + message + "</li>", failed, false);
	}

	public static void logWebOutput(String url, String message, boolean failed) {
		log(url, getDate() + "Output: " + message + "<br/>", failed, false);
	}

	public static void logWebStep(String url, String message, boolean failed) {
		log(url, "<li>"
				+ (failed ? getDate() + "<b>FailedStep</b>: " : getDate()
						+ "Step: ") + message + "</li>", failed, false);
	}

	public static String buildScreenshotLog(ScreenShot screenShot) {
		StringBuffer sbMessage = new StringBuffer("");
		if (screenShot.getLocation() != null)
			sbMessage.append("<a href='" + screenShot.getLocation()
					+ "' target=url>location</a>");
		if (screenShot.getHtmlSourcePath() != null)
			sbMessage.append(" | <a href='" + screenShot.getHtmlSourcePath()
					+ "' target=html>html</a>");
		if (screenShot.getImagePath() != null)
			sbMessage.append(" | <a href='" + screenShot.getImagePath()
					+ "' class='lightbox'>screenshot</a>");
		if (screenShot.getCalLog() != null)
			sbMessage.append(" | <a href='" + screenShot.getCalLog()
					+ "' target=cal>RlogId</a>");
		if (screenShot.getPageId() != null)
			sbMessage.append(" | PageId=" + screenShot.getPageId());
		return sbMessage.toString();
	}

	/**
	 * Log method
	 * 
	 * @param message
	 */
	public static void warning(String message) {
		message = "<li><font color='#FFFF00'>" + message + "</font></li>";
		log(null, message, false, false);
	}

	public static ArrayList<String> getRawLog(ITestResult result) {
		ArrayList<String> messages = Lists.newArrayList();
		for (String line : Reporter.getOutput(result)) {
			line = unEscape(line);
			messages.add(line);
		}
		return messages;
	}

	

	private static String getAPIRequestName(Properties reqHeaders,
			String xmlRequest) {
		String xmlRequestName = "";
		try {
			if (reqHeaders != null && !reqHeaders.isEmpty()) {
				if (!(reqHeaders.getProperty("X-spire-SOA-OPERATION-NAME") == null))
					xmlRequestName = reqHeaders
							.getProperty("X-spire-SOA-OPERATION-NAME")
							+ "Request";
				else if (!(reqHeaders.getProperty("X-spire-API-CALL-NAME") == null))
					xmlRequestName = reqHeaders
							.getProperty("X-spire-API-CALL-NAME") + "Request";
			} else {
				xmlRequestName = getXMLTitle(xmlRequest);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return xmlRequestName;

	}

	protected static String getXMLTitle(String xmlString) {
		String xmlTitle = "";

		String startPattern = "<S:Body>";
		String endPattern = "xmlns=";
		xmlTitle = "";
		if (xmlString.indexOf(startPattern) > 0) {
			int startIndex = xmlString.indexOf(startPattern) + 13;
			int endIndex = xmlString.indexOf(endPattern, startIndex);

			xmlTitle = xmlString.substring(startIndex, endIndex);
			xmlTitle = xmlTitle.replace('<', ' ');
			xmlTitle = xmlTitle.replace('>', ' ');
			xmlTitle = xmlTitle.trim();
		} else {
			Pattern requestType = Pattern.compile("<(\\w+)RequestType>");
			Matcher matcher = requestType.matcher(xmlString);
			if (matcher.find()) {
				xmlTitle = matcher.group().replace("<", "")
						.replace("Type>", "");
			}
		}
		System.out.println("XML title : " + xmlTitle);
		return xmlTitle;

	}

	
}
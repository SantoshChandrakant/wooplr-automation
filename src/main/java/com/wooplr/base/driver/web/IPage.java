package com.wooplr.base.driver.web;

/**
 * This is the page interface. It will be useful for testing Headers,
 *  Java Script and Accessibility.
 * 
 * 
 */
public interface IPage {
	String getBodyText();

	String getHtmlSavedToPath();

	String getHtmlSource();

	String getJSErrors();

	String getLocation();

	String getRlogId();

	String getTitle();
}

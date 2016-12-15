package com.san.base.util;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class ExpectedConditionsUtil {
	 /**
	   * An Expectation for checking that an element is either invisible or not
	   * present on the DOM.
	   *
	   * @param locator used to find the element
	   */
	  public static ExpectedCondition<Boolean> activeOfElementLocated(
	      final By locator) {
	    return new ExpectedCondition<Boolean>() {
	      public Boolean apply(WebDriver driver) {
	        try {
	          return driver.findElement(locator).getAttribute("class").equalsIgnoreCase("active");
	        } catch (NoSuchElementException e) {
	          // Returns true because the element is not present in DOM. The
	          // try block checks if the element is present but is invisible.
	          return true;
	        } catch (StaleElementReferenceException e) {
	          // Returns true because stale element reference implies that element
	          // is no longer visible.
	          return true;
	        }
	      }
	    };
	  }
	  
	  /**
	   * An Expectation for checking that an element is either invisible or not
	   * present on the DOM.
	   *
	   * @param locator used to find the element
	   */
	  public static ExpectedCondition<Boolean> inactiveOfElementLocated(
	      final By locator) {
	    return new ExpectedCondition<Boolean>() {
	      public Boolean apply(WebDriver driver) {
	        try {
	          return driver.findElement(locator).getAttribute("class").equalsIgnoreCase("inactive");
	        } catch (NoSuchElementException e) {
	          // Returns true because the element is not present in DOM. The
	          // try block checks if the element is present but is invisible.
	          return true;
	        } catch (StaleElementReferenceException e) {
	          // Returns true because stale element reference implies that element
	          // is no longer visible.
	          return true;
	        }
	      }
	    };
	  }

}

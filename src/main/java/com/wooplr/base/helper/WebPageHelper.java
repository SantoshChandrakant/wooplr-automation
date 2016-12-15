package com.wooplr.base.helper;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.w3c.dom.html.HTMLElement;

import com.google.common.base.Function;
import com.wooplr.base.controller.Logging;

/**
 * It would be better to have this usage in the pageObjects class than in test
 * if we can.
 * 
 */
public class WebPageHelper {

	static long TIMEOUT_S = 60;
	static int INT_TIMEOUT_S = Integer.parseInt(Long.toString(TIMEOUT_S));
	static long WAIT_TIMEOUT = 30L;
	protected static final Logger logger = Logging.getLogger(HTMLElement.class);

	protected static Function<WebDriver, WebElement> presenceOfElementLocated(final By locator) {
		return new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(locator);
			}
		};
	}

	/**
	 * Waits for the element to be visible until a timeout of 30 secs.
	 * 
	 * @param driver
	 * @param locator
	 */
	public static void waitForElementToBeVisible(final WebDriver driver, final By locator) throws RuntimeException {
		Wait<WebDriver> wait = new WebDriverWait(driver, WAIT_TIMEOUT);
		try {
			wait.until(new ExpectedCondition<WebElement>() {
				public WebElement apply(WebDriver driver) {
					// driver.switchTo().defaultContent();
					WebElement element = driver.findElement(locator);
					if (element.isDisplayed()) {
						return element;
					}
					return null;
				}
			});
		} catch (Exception e) {
			throw new RuntimeException(
					"Exception while waiting for " + locator + ". Exception:" + e + " on " + driver.getCurrentUrl());
		}
	}

	public static WebElement waitForElementAndReturnElement(final WebDriver driver, final By locator)
			throws RuntimeException {
		waitForElementToBeVisible(driver, locator);
		return driver.findElement(locator);
	}

	/**
	 * Waits for the element to be visible until the specified timeout.
	 * 
	 * @param driver
	 *            {@link WebDriver}
	 * @param locator
	 *            {@link By}
	 * @param timeOut
	 *            long
	 */
	public static void waitForElementToBeVisible(final WebDriver driver, final By locator, long timeOut)
			throws RuntimeException {
		Wait<WebDriver> wait = new WebDriverWait(driver, timeOut);
		try {
			wait.until(new ExpectedCondition<WebElement>() {
				public WebElement apply(WebDriver driver) {
					// driver.switchTo().defaultContent();
					WebElement element = driver.findElement(locator);
					if (element.isDisplayed()) {
						return element;
					}
					return null;
				}
			});
		} catch (Exception e) {
			throw new RuntimeException("Exception while waiting for " + locator + ". Exception:" + e);
		}
	}

	/**
	 * Waits for the given text until timing out at 30 secs.
	 * 
	 * @param driver
	 * @param locator
	 * @param text
	 */
	public static void waitForText(final WebDriver driver, final By locator, final String text) {
		Wait<WebDriver> wait = new WebDriverWait(driver, WAIT_TIMEOUT);
		try {
			wait.until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver webDriver) {
					String currentText = "";
					try {
						currentText = driver.findElement(locator).getText();
					} catch (Exception e) {
						// ignore if element is not present.
					}
					logger.info("Waiting for:" + text + " Found:" + currentText);
					return currentText.contains(text);
				}
			});

		} catch (Exception e) {
			throw new RuntimeException(
					"Exception while waiting for text " + text + " in " + locator + ". Exception:" + e);
		}
	}

	/**
	 * Waits until the given element is either hidden or deleted.
	 * 
	 * @param locator
	 * @param timeout
	 */
	public static void waitUntilElementDisappears(final WebDriver driver, final By locator) {
		Wait<WebDriver> wait = new WebDriverWait(driver, WAIT_TIMEOUT);
		try {
			wait.until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					try {
						WebElement element = driver.findElement(locator);
						logger.info(locator + " spotted..");
						return Boolean.valueOf(!element.isDisplayed());
					} catch (NotFoundException e) {
						logger.info(locator + " disappeared..");
						return Boolean.TRUE;
					} catch (StaleElementReferenceException se) {
						logger.info(locator + " disappeared..");
						return Boolean.TRUE;
					}
				}
			});
		} catch (Exception e) {
			throw new RuntimeException(locator + " did not disappear.");
		}
	}

	public static void waitForTitleStartingWithString(final WebDriver driver, final String title) {
		try {
			(new WebDriverWait(driver, WAIT_TIMEOUT)).until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver d) {
					return d.getTitle().startsWith(title);
				}
			});
		} catch (Exception e) {
			throw new RuntimeException(title + " did not show up after polling for " + WAIT_TIMEOUT + " secs.");
		}
	}

	public static void waitForTitleContainingString(final WebDriver driver, final String title) {
		try {
			(new WebDriverWait(driver, WAIT_TIMEOUT)).until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver d) {
					return d.getTitle().contains(title);
				}
			});
		} catch (Exception e) {
			throw new RuntimeException(title + " did not show up after polling for " + WAIT_TIMEOUT + " secs.");
		}
	}

	public static void waitForCurrentUrlToContainString(final WebDriver driver, final String url) {
		try {
			(new WebDriverWait(driver, WAIT_TIMEOUT)).until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver d) {
					return d.getCurrentUrl().contains(url);
				}
			});
		} catch (Exception e) {
			throw new RuntimeException(url + " was not present in current url: " + driver.getCurrentUrl()
					+ " after polling for " + WAIT_TIMEOUT + " secs.");
		}
	}

	public static void waitForCurrentUrlToMatchString(final WebDriver driver, final String url) {
		try {
			(new WebDriverWait(driver, WAIT_TIMEOUT)).until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver d) {
					return d.getCurrentUrl().trim().equals(url.trim());
				}
			});
		} catch (Exception e) {
			throw new RuntimeException(url + " did not match current url: " + driver.getCurrentUrl()
					+ " after polling for " + WAIT_TIMEOUT + " secs.");
		}
	}

	/**
	 * Explicitly waits for the specified milliseconds.Use this method only if
	 * absolutely neccessary.
	 * 
	 * @param milliSeconds
	 */
	public static void holdUntil(long milliSeconds) {
		long waitUntilTime = System.currentTimeMillis() + (milliSeconds);
		while (System.currentTimeMillis() < waitUntilTime) {
			// do nothing just wait for seconds.
		}
	}

	public static void waitForSeconds(WebDriver driver, int waitTime) {
		driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);
	}

	/*
	 * Below method will switch to alert and accept
	 */
	public static void acceptAlerts(WebDriver driver) {

		try {
			WebDriverWait wait = new WebDriverWait(driver, 180);
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			Logging.log("Accepting the Alert");
			alert.accept();
		} catch (Exception e) {
			// exception handling
		}

	}

	/*
	 * Below method will check is alert present or not
	 */
	public static boolean isAlertPresent(WebDriver driver) {
		try {
			driver.switchTo().alert();
			Logging.log("Alert is Present");
			return true;
		} catch (NoAlertPresentException Ex) {
			return false;
		}
	}

	/*
	 * Below methd will check element Return true , if exist otherwise return
	 * false
	 */

	public static boolean isElementVisiable(WebDriver driver, By by) {
		
		try {
			waitForElementToBeVisible(driver, by);
			if (driver.findElements(by).size() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

	}

	/*
	 * below method will check is the given text present in a web page.
	 * 
	 * Returns true if exist, otherwise returns false
	 */

	public static boolean isTextPresent(WebDriver driver, String text) {

		try {

			return driver.getPageSource().contains(text);

		} catch (Exception e) {
			return false;
		}

	}

	/*
	 * Below method retunrs the current title of the page
	 */
	public static String getTitle(WebDriver driver) {

		Logging.log("Getting Title of Current Page");

		return driver.getTitle();
	}

	/*
	 * Below method retunr the title of the child window page
	 */
	public static String getChildWindowTitle(WebDriver driver) throws Exception {

		String currentHandle = driver.getWindowHandle();

		Set<String> handles = driver.getWindowHandles();

		for (String string : handles) {

			if (!currentHandle.equalsIgnoreCase(string)) {
				Logging.log("Switching to Child Window");
				driver.switchTo().window(string);
				Thread.sleep(8000);
				Logging.log("Getting Child Window Title");
				return driver.getTitle();
			}

		}

		return null;
	}

	/*
	 * Below method will check the check box
	 */

	public static void checkCheckBox(WebDriver driver, By by) {
		waitForElementToBeVisible(driver, by);
		if (!driver.findElement(by).isSelected()) {
			driver.findElement(by).click();
		}

	}

	/*
	 * Below method will uncheck the check box
	 */

	public static void unCheckCheckBox(WebDriver driver, By by) {
		waitForElementToBeVisible(driver, by);
		if (driver.findElement(by).isSelected()) {
			driver.findElement(by).click();
		}

	}

	/*
	 * Below method will check the checkbox Return true if selected, otherwise
	 * return false
	 */
	public static boolean isCheckBoxSelected(WebDriver driver, By by) {
		waitForElementToBeVisible(driver, by);
		if (driver.findElement(by).isSelected()) {
			Logging.log("CheckBox is Selected");
			return true;
		} else {
			Logging.log("CheckBox is not Selected");
			return false;
		}
	}

	/*
	 * Below method will check is alert present or not
	 */
	public static String getAlertText(WebDriver driver) {
		try {

			Alert alert = driver.switchTo().alert();
			Logging.log("Getting Text from the Alert Message");
			return alert.getText();

		} catch (NoAlertPresentException Ex) {
			return null;
		}
	}

	/*
	 * Below method return the title of the child window page and close the
	 * child window
	 */
	public static String getChildWindowTitleAndClose(WebDriver driver) throws Exception {

		String currentHandle = driver.getWindowHandle();

		Set<String> handles = driver.getWindowHandles();

		String title = null;

		for (String string : handles) {

			if (!currentHandle.equalsIgnoreCase(string)) {
				Logging.log("Switching to Child Window");
				driver.switchTo().window(string);
				Thread.sleep(8000);
				Logging.log("Getting Child Window Title");
				title = driver.getTitle();
				Logging.log("Closing Child Window");
				driver.close();
			}

		}

		driver.switchTo().window(currentHandle);

		return title;
	}

	/**
	 * Waits for the element to be visible and click.
	 * 
	 * @param driver
	 * @param locator
	 */

	public static void clicElement(final WebDriver driver, final By locator) {

		try {

			Logging.log("Clicking the locator" + locator.toString());
			waitForElementToBeVisible(driver, locator);

			driver.findElement(locator).click();
		} catch (Exception e) {
			throw new RuntimeException("Exception while waiting for " + locator + ". Exception:" + e + " on ");
		}

	}

	public static String getElementText(final WebDriver driver, final By locator) {

		Logging.log("get the text of the locator" + locator.toString());
		waitForElementToBeVisible(driver, locator);

		return driver.findElement(locator).getText();

	}

	public static void enterText(final WebDriver driver, final By locator, String text) {
		try {
			Logging.log("enter text into the locator" + locator.toString());
			waitForElementToBeVisible(driver, locator);
			driver.findElement(locator).clear();
			driver.findElement(locator).sendKeys(text);
		} catch (Exception e) {
			throw new RuntimeException("Exception:" + e + " on ");
		}

	}

	public static void TypeAndEnterKey(final WebDriver driver, final By locator, String text) {
		try {
			Logging.log("enter text into the locator" + locator.toString());
			waitForElementToBeClickable(driver, locator);
			driver.findElement(locator).clear();
			driver.findElement(locator).sendKeys(text);
			driver.findElement(locator).sendKeys(Keys.ENTER);
		} catch (Exception e) {
			throw new RuntimeException("Exception:" + e + " on ");
		}

	}

	public static void selectdropdownBytext(final WebDriver driver, final By locator, String text) {

		Logging.log("Select drop down by visible txt: " + text);
		waitForElementToBeVisible(driver, locator);

		new Select(driver.findElement(locator)).selectByVisibleText(text);

	}

	public static void selectdropdownByindex(final WebDriver driver, final By locator, int index) {

		Logging.log("Select drop down by visible txt: " + index);
		waitForElementToBeVisible(driver, locator);

		new Select(driver.findElement(locator)).selectByIndex(index);

	}

	public static void scrollToBottom(final WebDriver driver) {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}

	/**
	 * for wait till loading page
	 * 
	 * @param driver
	 * @param locator
	 */
	public static void waitForElementToBeDisappeared(final WebDriver driver, final By locator) {
		WebDriverWait wait = new WebDriverWait(driver, TIMEOUT_S);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	public static List<WebElement> getElements(final WebDriver driver, final By locator) {
		Logging.log("getElements" + locator);
		waitForElementToBeVisible(driver, locator);
		List<WebElement> listElements = driver.findElements(locator);
		return listElements;
	}

	/**
	 * wait till the text is disappeared on UI.
	 * @throws InterruptedException 
	 */
	public static boolean waitForTextToBeDisappeared(final WebDriver driver, String text){
		Logging.log("waitForTextToBeDisappeared" + text);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		try {
			while (true == driver.getPageSource().contains(text)) {
				Logging.log("waitForTextToBeDisappeared---->" + text);
				logger.info("waitForTextToBeDisappeared---->" + text);
				Thread.sleep(500);
			}
			Thread.sleep(1000);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public static void waitForVisibilityOfElementLocated(final WebDriver driver, By locator) {
		WebDriverWait wait = new WebDriverWait(driver, TIMEOUT_S);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));

	}

	public static void waitForElementToBeClickable(final WebDriver driver, By locator) {
		WebDriverWait wait = new WebDriverWait(driver, TIMEOUT_S);
		wait.until(ExpectedConditions.elementToBeClickable(locator));

	}
	public static void waitForVisibilityOfElement(final WebDriver driver, WebElement element) {
		WebDriverWait wait = new WebDriverWait(driver, TIMEOUT_S);
		wait.until(ExpectedConditions.visibilityOf(element));

	}

	public void dragAndDrop(final WebDriver driver, By bySource, By byDestination) {
		WebElement sourceElement = null;
		WebElement destinationElement = null;
		try {
			sourceElement = driver.findElement(bySource);
			destinationElement = driver.findElement(byDestination);
			if (sourceElement.isDisplayed() && destinationElement.isDisplayed()) {
				Actions action = new Actions(driver);
				action.dragAndDropBy(sourceElement, 173, 70);
				// action.dragAndDrop(sourceElement,
				// destinationElement).build().perform();
			} else {
				System.out.println("Element was not displayed to drag");
			}
		} catch (StaleElementReferenceException e) {
			System.out.println("Element with " + sourceElement + "or" + destinationElement
					+ "is not attached to the page document " + e.getStackTrace());
		} catch (NoSuchElementException e) {
			System.out.println("Element " + sourceElement + "or" + destinationElement + " was not found in DOM "
					+ e.getStackTrace());
		} catch (Exception e) {
			System.out.println("Error occurred while performing drag and drop operation " + e.getStackTrace());
		}
	}

	public void dragAndDropBy(final WebDriver driver, By bySource, int x, int y) {
		WebElement sourceElement = null;
		try {
			sourceElement = driver.findElement(bySource);
			if (sourceElement.isDisplayed()) {
				Actions action = new Actions(driver);
				action.dragAndDropBy(sourceElement, x, y);
			} else {
				System.out.println("Element was not displayed to drag");
			}
		} catch (StaleElementReferenceException e) {
		} catch (NoSuchElementException e) {
		} catch (Exception e) {
			System.out.println("Error occurred while performing drag and drop operation " + e.getStackTrace());
		}
	}

	public void dragHoldAndDrop(final WebDriver driver, By bySource, By byDestination) {
		WebElement sourceElement = null;
		WebElement destinationElement = null;
		try {
			sourceElement = driver.findElement(bySource);
			destinationElement = driver.findElement(byDestination);
			if (sourceElement.isDisplayed() && destinationElement.isDisplayed()) {
				Actions action = new Actions(driver);
				action.clickAndHold(sourceElement).moveToElement(destinationElement).release(destinationElement).build();
				Thread.sleep(500);
				action.perform();
			} else {
				System.out.println("Element was not displayed to drag");
			}
		} catch (StaleElementReferenceException e) {
			System.out.println("Element with " + sourceElement + "or" + destinationElement
					+ "is not attached to the page document " + e.getStackTrace());
		} catch (NoSuchElementException e) {
			System.out.println("Element " + sourceElement + "or" + destinationElement + " was not found in DOM "
					+ e.getStackTrace());
		} catch (Exception e) {
			System.out.println("Error occurred while performing drag and drop operation " + e.getStackTrace());
		}
	}
	
	public boolean verifyTextAndWaitTillItDisappers(final WebDriver driver,String text){
//		Assert.assertTrue(isTextPresent(driver, text));
		logger.info("isTextPreset is passed -->"+text);
		try {
			Thread.sleep(1000);
			while (true == driver.getPageSource().contains(text)) {
				Logging.log("waiting for text to be disappeared---->" + text);
				logger.info("waiting for text to be disappeared---->" + text);
				Thread.sleep(500);
			}
			Thread.sleep(1000);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 
	 * @param driver
	 * @param by
	 */
	public void actionMoveToElement(final WebDriver driver, By by){
		waitForElementToBeClickable(driver, by);
		WebElement element = driver.findElement(by);
		Actions actions = new Actions(driver);
		actions.moveToElement(element).click().perform();
	}
	
	public static void sleep(int milliseconds){
		
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}

/**
 * 
 */
package com.san.base.util;

import static org.testng.internal.EclipseInterface.ASSERT_LEFT;
import static org.testng.internal.EclipseInterface.ASSERT_MIDDLE;
import static org.testng.internal.EclipseInterface.ASSERT_RIGHT;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.Reporter;

import com.wooplr.base.controller.ContextManager;
import com.wooplr.base.controller.Logging;

/**
 * @author Raghavender
 *
 * A enhanced Assertion supports soft assert. It uses soft assert if context
 * softAssertEnabled is true.
 * 
 * soft assert - test case continues when validation fails.
 */
public class AssertionUtil {



    public static final String COLON = " : ";
    private static final String IS_NULL = " IS NULL";
    private static final String IS_NOT_NULL = " IS NOT NULL";
    private static final String ASSERTION_PASS = "ASSERTION PASS: ";
    private static final String ASSERTION_FAILED = "ASSERTION FAILED: ";

    private static void _addVerificationFailure(Throwable e) {
	ContextManager.getThreadContext().addVerificationFailures(Reporter.getCurrentTestResult(), e);
    }

    private static String failFormat(Object actual, Object expected, String message) {
	String formatted = ASSERTION_FAILED;
	if (null != message) {
	    formatted = message + COLON;
	}
	return formatted + ASSERT_LEFT + expected + ASSERT_MIDDLE + actual + ASSERT_RIGHT;
    }

    private static String passFormat(Object actual, String message) {
	return ASSERTION_PASS + message + " as expected " + COLON + actual;
    }

    // /////////////// Assertion Methods ////////////////////////////
    public static void assertEquals(Object actual, Object expected, String message) {
	if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
	    softAssertEquals(actual, expected, message);
	} else {
	    try {
		Assert.assertEquals(actual, expected, message);
	    } catch (AssertionError e) {
		Logging.error(failFormat(actual, expected, message));
		throw e;
	    }
	}
	Logging.info(passFormat(actual, message));
    }

    public static void assertNotEquals(Object actual, Object expected, String message) {
	if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
	    softAssertNotEquals(actual, expected, message);
	} else {
	    try {
		Assert.assertNotEquals(actual, expected, message);
	    } catch (AssertionError e) {
		Logging.error(failFormat(actual, expected, message));
		throw e;
	    }
	}
	Logging.info(passFormat(actual, message));
    }

    public static void assertContains(String actual, String contained, String message) {
	if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
	    softAssertContains(actual, contained, message);
	} else {
	    if (StringUtils.isEmpty(actual)) {
		throw new AssertionError(message + " is null.");
	    }
	    try {
		Assert.assertTrue(actual.contains(contained), message);
	    } catch (AssertionError e) {
		Logging.error(message + COLON + " does not contain " + ASSERT_LEFT + contained + ASSERT_MIDDLE + actual + ASSERT_RIGHT);
		throw e;
	    }
	}
	Logging.info(passFormat(actual, message));
    }

    public static void assertSame(Object actual, Object expected, String message) {
	if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
	    softAssertSame(actual, expected, message);
	} else {
	    try {
		Assert.assertSame(actual, expected, message);
	    } catch (AssertionError e) {
		Logging.error(failFormat(actual, expected, message));
		throw e;
	    }
	}
	Logging.info(passFormat(actual, message));
    }

    public static void assertNotSame(Object actual, Object expected, String message) {
	if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
	    softAssertNotSame(actual, expected, message);
	} else {
	    try {
		Assert.assertNotSame(actual, expected, message);
	    } catch (AssertionError e) {
		Logging.error(failFormat(actual, expected, message));
		throw e;
	    }
	}
	Logging.info(passFormat(actual, message));
    }

    public static void assertTrue(boolean condition, String message) {
	if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
	    softAssertTrue(condition, message);
	} else {
	    try {
		Assert.assertTrue(condition, message);
	    } catch (AssertionError e) {
		Logging.error(failFormat(false, true, message));
		throw e;
	    }
	}
	Logging.info(ASSERTION_PASS + "Assertion result is expected = TRUE - [" + message + "]");
    }

    public static void assertFalse(boolean condition, String message) {
	if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
	    softAssertFalse(condition, message);
	} else {
	    try {
		Assert.assertFalse(condition, message);
	    } catch (AssertionError e) {
		Logging.error(failFormat(true, false, message));
		throw e;
	    }
	}
	Logging.info(ASSERTION_PASS + "Assertion result is expected = FALSE - [" + message + "]");
    }

    public static void assertNull(Object object, String message) {
	if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
	    softAssertNull(object, message);
	} else {
	    try {
		Assert.assertNull(object, message);
	    } catch (AssertionError e) {
		Logging.error(message + IS_NOT_NULL);
		throw e;
	    }
	}
	Logging.info(passFormat(IS_NULL, message));
    }

    public static void assertNotNull(Object object, String message) {
	if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
	    softAssertNotNull(object, message);
	} else {
	    try {
		Assert.assertNotNull(object, message);
	    } catch (AssertionError e) {
		Logging.error(message + IS_NULL);
		throw e;
	    }
	}
	Logging.info(passFormat(IS_NOT_NULL, message));
    }

    public static void assertUrlNoResponse(String url, String resourceName, boolean useProxy) {/*
	if (StringUtils.isEmpty(url)) {
	    Logging.error(resourceName + ": URL is empty.");
	    Assert.fail("URL is empty");
	}
	boolean ret = LinksChecker.validateResponseCode(url, resourceName, useProxy);
	String msg = resourceName + " : No response of URL.";
	if (!ret) {
	    Logging.error(msg + " - " + url);
	    if (ContextManager.getThreadContext().isSoftAssertEnabled()) {
		_addVerificationFailure(new AssertionError(msg));
	    } else {
		throw new AssertionError(msg);
	    }
	}
	Logging.info(ASSERTION_PASS + resourceName + " URL is OK - " + url);
    */}

    public static void fail(String message) {
	Assert.fail(message);
    }

    public static List<Throwable> getVerificationFailures() {
	return ContextManager.getThreadContext().getVerificationFailures(Reporter.getCurrentTestResult());
    }

    // /////////////// Soft Assertion Methods ////////////////////////////

    public static void softAssertEquals(Object actual, Object expected, String message) {

	try {
	    Assert.assertEquals(actual, expected, message);
	    Logging.info(passFormat(actual, message));
	} catch (Throwable e) {
	    Logging.error(failFormat(actual, expected, message));
	    _addVerificationFailure(e);
	}
    }

    public static void softAssertNotEquals(Object actual, Object expected, String message) {

	try {
	    Assert.assertNotEquals(actual, expected, message);
	    Logging.info(passFormat(actual, message));
	} catch (Throwable e) {
	    Logging.error(failFormat(actual, expected, message));
	    _addVerificationFailure(e);
	}
    }

    public static boolean softAssertEqualsWithReturn(Object actual, Object expected, String message) {

	try {
	    Assert.assertEquals(actual, expected, message);
	    Logging.info(passFormat(actual, message));
	} catch (Throwable e) {
	    Logging.error(failFormat(actual, expected, message));
	    _addVerificationFailure(e);
	    return false;
	}
	return true;
    }

    public static void softAssertFalse(boolean condition, String message) {
	try {
	    Assert.assertFalse(condition, message);
	    Logging.info(ASSERTION_PASS + "Assertion result is expected = FALSE - [" + message + "]");
	} catch (Throwable e) {
	    Logging.error(failFormat(true, false, message));
	    _addVerificationFailure(e);
	}
    }

    public static void softAssertNotNull(Object object, String message) {
	try {
	    Assert.assertNotNull(object, message);
	    Logging.info(passFormat(IS_NOT_NULL, message));
	} catch (Throwable e) {
	    Logging.error(message + IS_NOT_NULL);
	    _addVerificationFailure(e);
	}
    }

    public static void softAssertNotSame(Object actual, Object expected, String message) {
	try {
	    Assert.assertNotSame(actual, expected, message);
	    Logging.info(passFormat(actual, message));
	} catch (Throwable e) {
	    Logging.error(failFormat(actual, expected, message));
	    _addVerificationFailure(e);
	}
    }

    public static void softAssertNull(Object object, String message) {
	try {
	    Assert.assertNull(object, message);
	    Logging.info(passFormat(IS_NULL, message));
	} catch (Throwable e) {
	    Logging.error(message + IS_NULL);
	    _addVerificationFailure(e);
	}
    }

    public static void softAssertSame(Object actual, Object expected, String message) {
	try {
	    Assert.assertSame(actual, expected, message);
	    Logging.info(passFormat(actual, message));
	} catch (Throwable e) {
	    Logging.error(failFormat(actual, expected, message));
	    _addVerificationFailure(e);
	}
    }

    public static void softAssertTrue(boolean condition, String message) {
	try {
	    Assert.assertTrue(condition, message);
	    Logging.info(ASSERTION_PASS + "Assertion result is expected = TRUE - [" + message + "]");
	} catch (Throwable e) {
	    Logging.error(failFormat(false, true, message));
	    _addVerificationFailure(e);
	}
    }

    public static void softAssertContains(String actual, String contained, String message) {
	try {
	    if (StringUtils.isEmpty(actual)) {
		throw new AssertionError(message + IS_NULL);
	    }
	    Assert.assertTrue(actual.contains(contained), message);
	    Logging.info(passFormat(actual, message));
	} catch (Throwable e) {
	    Logging.error(message + COLON + " does not contain " + ASSERT_LEFT + contained + ASSERT_MIDDLE + actual + ASSERT_RIGHT);
	    _addVerificationFailure(e);
	}
    }


}

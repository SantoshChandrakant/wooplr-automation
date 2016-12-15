package com.wooplr.base.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;

/**
 * A enhanced Assertion supports soft assert can be used in test case directly.
 * 
 * soft assert - test case continues when validation fails.
 * 
 */
public class SoftAssertion {

	private Map<ITestResult, List<Throwable>> verificationFailuresMap = new HashMap<ITestResult, List<Throwable>>();
	
	public SoftAssertion() {
	}
	
	public SoftAssertion (Map<ITestResult, List<Throwable>> verificationFailuresMap){
		this.verificationFailuresMap = verificationFailuresMap;
	}

	public Map<ITestResult, List<Throwable>> getVerificationFailuresMap() {
		return verificationFailuresMap;
	}

	protected void _addVerificationFailure(Throwable e) {
		List<Throwable> verificationFailures = getVerificationFailures();
		verificationFailuresMap.put(Reporter.getCurrentTestResult(), verificationFailures);
		verificationFailures.add(e);
		Logging.log(null, "ASSERTION FAILED: " + e.getMessage(), true, true);
	}

	// /////////////// Assertion Methods ////////////////////////////

	public void assertEquals(boolean actual, boolean expected, String message) {
		try {
			Assert.assertEquals(actual, expected, message);
		} catch (Throwable e) {
			_addVerificationFailure(e);
		}
	}

	public void assertEquals(byte actual, byte expected, String message) {
		try {
			Assert.assertEquals(actual, expected, message);
		} catch (Throwable e) {
			_addVerificationFailure(e);
		}
	}

	public void assertEquals(byte[] actual, byte[] expected, String message) {
		try {
			Assert.assertEquals(actual, expected, message);
		} catch (Throwable e) {
			_addVerificationFailure(e);
		}
	}

	public void assertEquals(char actual, char expected, String message) {
		try {
			Assert.assertEquals(actual, expected, message);
		} catch (Throwable e) {
			_addVerificationFailure(e);
		}
	}

	@SuppressWarnings("rawtypes")
	public void assertEquals(Collection actual, Collection expected, String message) {
		try {
			Assert.assertEquals(actual, expected, message);
		} catch (Throwable e) {
			_addVerificationFailure(e);
		}
	}

	public void assertEquals(double actual, double expected, String message) {
		try {
			Assert.assertEquals(actual, expected, message);
		} catch (Throwable e) {
			_addVerificationFailure(e);
		}
	}

	public void assertEquals(float actual, float expected, String message) {
		try {
			Assert.assertEquals(actual, expected, message);
		} catch (Throwable e) {
			_addVerificationFailure(e);
		}
	}

	public void assertEquals(int actual, int expected, String message) {
		try {
			Assert.assertEquals(actual, expected, message);
		} catch (Throwable e) {
			_addVerificationFailure(e);
		}
	}

	public void assertEquals(long actual, long expected, String message) {
		try {
			Assert.assertEquals(actual, expected, message);
		} catch (Throwable e) {
			_addVerificationFailure(e);
		}
	}

	public void assertEquals(Object actual, Object expected, String message) {
		try {
			Assert.assertEquals(actual, expected, message);
		} catch (Throwable e) {
			_addVerificationFailure(e);
		}
	}

	public void assertEquals(Object[] actual, Object[] expected, String message) {
		try {
			Assert.assertEquals(actual, expected, message);
		} catch (Throwable e) {
			_addVerificationFailure(e);
		}
	}

	public void assertEquals(short actual, short expected, String message) {
		try {
			Assert.assertEquals(actual, expected, message);
		} catch (Throwable e) {
			_addVerificationFailure(e);
		}
	}

	public void assertEquals(String actual, String expected, String message) {
		try {
			Assert.assertEquals(actual, expected, message);
		} catch (Throwable e) {
			_addVerificationFailure(e);
		}
	}

	public boolean assertEquals2(String actual, String expected, String message) {
		try {
			Assert.assertEquals(actual, expected, message);
		} catch (Throwable e) {
			_addVerificationFailure(e);
			return false;
		}
		return true;
	}

	public void assertFalse(boolean condition, String message) {
		try {
			Assert.assertFalse(condition, message);
		} catch (Throwable e) {
			_addVerificationFailure(e);
		}
	}

	public void assertNotNull(Object object, String message) {
		try {
			Assert.assertNotNull(object, message);
		} catch (Throwable e) {
			_addVerificationFailure(e);
		}
	}

	public void assertNotSame(Object actual, Object expected, String message) {
		try {
			Assert.assertNotSame(actual, expected, message);
		} catch (Throwable e) {
			_addVerificationFailure(e);
		}
	}

	public void assertNull(Object object, String message) {
		try {
			Assert.assertNull(object, message);
		} catch (Throwable e) {
			_addVerificationFailure(e);
		}
	}

	public void assertSame(Object actual, Object expected, String message) {
		try {
			Assert.assertSame(actual, expected, message);
		} catch (Throwable e) {
			_addVerificationFailure(e);
		}
	}

	public void assertTrue(boolean condition, String message) {
		try {
			Assert.assertTrue(condition, message);
		} catch (Throwable e) {
			_addVerificationFailure(e);
		}
	}

	public void fail(String message) {
		Assert.fail(message);
	}

	public void logAll(String message) {
		if (verificationFailuresMap != null && !verificationFailuresMap.isEmpty()) {
			Assert.fail(message);
		}
	}

	public List<Throwable> getVerificationFailures() {
		List<Throwable> verificationFailures = verificationFailuresMap.get(Reporter.getCurrentTestResult());
		return verificationFailures == null ? new ArrayList<Throwable>() : verificationFailures;
	}

}

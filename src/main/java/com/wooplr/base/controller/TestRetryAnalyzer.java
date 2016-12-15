package com.wooplr.base.controller;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Implement to be able to have a chance to retry a failed test
 * 
 *
 */
public class TestRetryAnalyzer implements IRetryAnalyzer {
	private static final String TEST_RETRY_COUNT = "testRetryCount";
	private static final String TEST_RETRY_INTERVAL = "testRetryInterval";
	private int count = 1;
	private int maxCount = 1;

	public TestRetryAnalyzer() {
		String retryMaxCount = System.getProperty(TEST_RETRY_COUNT);
		if (retryMaxCount != null) {
			maxCount = Integer.parseInt(retryMaxCount);
		}
	}

	public void setMaxCount(int count) {
		this.maxCount = count;
	}

	public int getCount() {
		return this.count;
	}

	public int getMaxCount() {
		return this.maxCount;
	}

	public synchronized boolean retry(ITestResult result) {
		String testClassName = String.format("%s.%s", result.getMethod()
				.getRealClass().toString(), result.getMethod().getMethodName());

		if (count <= maxCount) {
			result.setAttribute("RETRY", new Integer(count));

			Logging.log("[RETRYING] " + testClassName + " FAILED, "
					+ "Retrying " + count + " time", true);
			count += 1;

			String retryInterval = System.getProperty(TEST_RETRY_INTERVAL);
			if (retryInterval != null) {
				try {
					int intervalSeconds = Integer.parseInt(retryInterval);
					Logging.log("Retry after " + intervalSeconds + " seconds",
							true);
					Thread.sleep(intervalSeconds * 1000);
				} catch (Exception ex) {
				}
			}
			return true;
		}
		return false;
	}
}


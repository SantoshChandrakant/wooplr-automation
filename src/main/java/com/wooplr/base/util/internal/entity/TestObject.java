package com.wooplr.base.util.internal.entity;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Santosh C
 *
 */
public class TestObject {

	public static final String TEST_CASE_ID = "WooplrTestObject.TestCaseId";
	public static final String TEST_TITLE = "WooplrTestObject.TestTitle";
	public static final String TEST_SITE = "WooplrTestObject.TestSite";
	public static final String TEST_IS_ACTIVE = "WooplrTestObject.IsActive";

	private String testCaseId = "";
	private String testMethod = "";
	private String testTitle = "";
	private String testSite = "";
	private String testTags = "";
	private String testSteps = "";
	private String testData = "";
	private String expected = "";
	private String exception = "";

	public String getTestMethod() {
		return testMethod;
	}

	public void setTestMethod(String testMethod) {
		this.testMethod = testMethod;
	}

	public String getExpected() {
		return expected;
	}

	public void setExpected(String expected) {
		this.expected = expected;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	private boolean isActive = true;
	private String description = "";

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(String testCaseId) {
		this.testCaseId = testCaseId;
	}

	public String getTestSite() {
		return testSite;
	}

	public void setTestSite(String testSite) {
		this.testSite = testSite;
	}

	public String getTestTags() {
		return testTags;
	}

	public void setTestTags(String testTags) {
		this.testTags = testTags;
	}

	public String getTestTitle() {
		return testTitle;
	}

	public void setTestTitle(String testTitle) {
		this.testTitle = testTitle;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("[TestCaseId=" + testCaseId);
		if (StringUtils.isNotEmpty(testMethod)) {
			stringBuffer.append("|TestMethod=" + testMethod);
		}
		if (StringUtils.isNotEmpty(testTitle)) {
			stringBuffer.append("|TestTitle=" + testTitle);
		}
		if (StringUtils.isNotEmpty(testSite)) {
			stringBuffer.append("|Site=" + testSite);
		}
		if (StringUtils.isNotEmpty(testTags)) {
			stringBuffer.append("|DPTag=" + testTags);
		}
		if (!isActive) {
			stringBuffer.append("|IsActive=" + isActive);
		}
		stringBuffer.append("]");
		return stringBuffer.toString();
	}

	public String getTestSteps() {
		return testSteps;
	}

	public void setTestSteps(String testSteps) {
		this.testSteps = testSteps;
	}

	public String getTestData() {
		return testData;
	}

	public void setTestData(String testData) {
		this.testData = testData;
	}

}

package com.wooplr.base.controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.xml.XmlTest;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.wooplr.base.util.internal.entity.TestObject;

/**
 * TestPlan takes charge of setup and teardown including initialize context,
 * clean up drivers and deal with customized TearDownService. Basically all the
 * Spire test plan should extend TestPlan class.
 * 
 */
public abstract class TestPlan {

	public WebDriver driver = null;

	private static final Logger logger = Logging.getLogger(TestPlan.class);
	private Date start;
	String localPath = System.getProperty("user.dir") + File.separator + "test-output" + File.separator + "screenshots"
			+ File.separator;

	public ITestContext testContexInfo;

	static {
	}

	/*
	 * Below method will initialize the driver once test method started
	 * execution
	 */
	public void initializeDriver(WebDriver driver, TestObject testObject) {

		Logging.log("Initializing the driver");

		this.driver = driver;

		if (testObject != null) {
			Thread.currentThread().setName(testObject.getTestTitle());
		}

	}

	/*
	 * Below method will kill driver
	 */
	public void tearDown() {

		if (this.driver != null) {
			this.driver.quit();
		}

	}

	@AfterMethod(alwaysRun = true)
	public void afterTestMethod(Method method) {

		// Clean ups for test level services
		tearDown();

		logger.info(Thread.currentThread() + " Finish method " + method.getName());

	}

	@AfterMethod(alwaysRun = true)
	public void _takeScreenShotOnFailure(ITestResult testResult) throws IOException {
		if (testResult.getStatus() == ITestResult.FAILURE || testResult.getStatus() == ITestResult.SKIP
				|| testResult.getStatus() == ITestResult.SUCCESS_PERCENTAGE_FAILURE) {
			Logging.log("takeScreenShotOnFailure :: Capturing Screenshots !!!");
			if (driver == null)
				return;
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			String destPath = localPath + testResult.getMethod().getMethodName() + ".png";
			File destPathFile = new File(destPath);
			FileUtils.copyFile(scrFile, destPathFile);
		}

	}

	@AfterSuite(alwaysRun = true)
	public void afterTestSuite() {

		tearDown();
		long milliSeconds = new Date().getTime() - start.getTime();
		ContextManager.getThreadContext().setTime(milliSeconds / 1000 + "");
		logger.info("Test Suite Execution Time: " + (new Date().getTime() - start.getTime()) / 1000 / 60 + " minutes.");

	}

	/**
	 * Configure Test Params setting
	 * 
	 * @param xmlTest
	 */
	@BeforeTest(alwaysRun = true)
	public void beforeTest(ITestContext testContex, XmlTest xmlTest) {
		ContextManager.initTestLevelContext(testContex, xmlTest);
	}

	@BeforeMethod(alwaysRun = true)
	public void beforeTestMethod(Object[] parameters, Method method, ITestContext testContex, XmlTest xmlTest) {
		logger.info(Thread.currentThread() + " Start method " + method.getName());
		ContextManager.initThreadContext(testContex, xmlTest);

		if (method != null) {
			ContextManager.getThreadContext().setAttribute(Context.TEST_METHOD_SIGNATURE,
					constructMethodSignature(method, parameters));
		}

	}

	@BeforeSuite(alwaysRun = true)
	public void beforeTestSuite(ITestContext testContex) {

		start = new Date();

		System.setProperty("AUTOMATION_FRAMEWORK", "SPIRE_AUTOMATION");

		ContextManager.initGlobalContext(testContex);

		// to support to call function in @beforeSuite
		ContextManager.initThreadContext(testContex, null);

		readTestParameterAnnotations();

		if (ContextManager.getGlobalContext().getHealthCheck().equalsIgnoreCase("true")) {
			Assertion.assertTrue(checkStatusCodeForApplication(), "Applocation is down, so skipping all tests");
		}

		testContexInfo = testContex;

	}

	/*
	 * @BeforeSuite(alwaysRun = true) public void setUp() { File filePath = new
	 * File(localPath); if (!filePath.exists()) { if (filePath.mkdir()) {
	 * System.out.println("Directory created !!!"); } else { Logging.log(
	 * "Not created Directory!!"); } } else { if (filePath.listFiles().length >
	 * 0) { for (File file : filePath.listFiles()) { if (file.delete()) {
	 * System.err.println("deleted files"); } } } } }
	 */

	private boolean checkStatusCodeForApplication() {

		try {

			WebClient webClient = new WebClient(BrowserVersion.CHROME);

			webClient.getOptions().setUseInsecureSSL(true);
			webClient.getOptions().setPrintContentOnFailingStatusCode(false);
			webClient.getOptions().setThrowExceptionOnScriptError(false);

			System.setProperty("jsse.enableSNIExtension", "false");
			System.setProperty("com.sun.net.ssl.enableECC", "false");

			int code = webClient.getPage(ContextManager.getGlobalContext().getHostAddress()).getWebResponse()
					.getStatusCode();
			webClient.close();

			return code == 200 ? true : false;

		} catch (Exception e) {

			Logging.log("Application is down, so skiiping all tests");

			return false;
		}
	}

	private String constructMethodSignature(Method method, Object[] parameters) {
		return method.getDeclaringClass().getCanonicalName() + "." + method.getName() + "("
				+ constructParameterString(parameters) + ")";
	}

	/**
	 * Remove name space from parameters
	 * 
	 * @param parameters
	 * @return
	 */
	private String constructParameterString(Object[] parameters) {
		StringBuffer sbParam = new StringBuffer();

		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				if (parameters[i] == null) {
					sbParam.append("null, ");
				} else if (parameters[i] instanceof java.lang.String) {
					sbParam.append("\"").append(parameters[i]).append("\", ");
				} else {
					sbParam.append(parameters[i]).append(", ");
				}
			}
		}

		if (sbParam.length() > 0)
			sbParam.delete(sbParam.length() - 2, sbParam.length() - 1);

		return sbParam.toString();

	}

	/**
	 * Get the value of property with TestParameter annotation from system
	 * property, system env, or TestNG parameter
	 * 
	 */
	protected void readTestParameterAnnotations() {
		try {
			Field[] fields = this.getClass().getDeclaredFields();
			AccessibleObject.setAccessible(fields, true);

			for (Field field : fields) {
				if (field.getAnnotation(TestParameter.class) != null) {
					String name = field.getAnnotation(TestParameter.class).name();

					if (System.getProperty(name) != null) {
						field.set(this, System.getProperty(name));
					} else if (System.getenv(name) != null) {
						field.set(this, System.getenv(name));
					} else if (ContextManager.getThreadContext().getTestNGContext().getCurrentXmlTest()
							.getParameter(name) != null) {
						field.set(this, ContextManager.getThreadContext().getTestNGContext().getCurrentXmlTest()
								.getParameter(name));
					}
				}
			}
		} catch (Exception ex) {
		}
	}

	// after method takes the screen shots..
	@AfterMethod(alwaysRun = true)
	public void afterMethod(ITestResult result) throws Exception {
		// if (!result.isSuccess())
		// SpireScreenShot.takeScreenShoot(driver, result.getMethod());
	}

}

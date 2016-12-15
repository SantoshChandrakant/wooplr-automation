//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.wooplr.base.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;

import com.wooplr.base.driver.web.ScreenShot;

public class Context {
	public static final String BROWSER = "browser";
	public static final String HOSTADDRESS = "host";
	public static final String SOURCE_BRANCH_NAME = "source_branch_name";
	public static final String SELENIUM_GRID = "selenium_grid";
	public static final String NEW_BRANCH = "new_branch";
	public static final String REPOSITORIES = "repositories";
	public static final String TEST_EXECUTION_TIME = "time";
	public static final String TEST_CONFIG = "testConfig";
	public static final String ENVIRONMENT = "environment_type";
	public static final String POOL = "pool";
	public static final String POOL_TYPE = "poolType";
	public static final String IE_DRIVER_PATH = "ieDriverPath";
	public static final String CHROME_DRIVER_PATH = "chromeDriverPath";
	public static final String BROWSER_DOWNLOAD_DIR = "browserDownloadDir";
	public static final String IMPLICIT_WAIT_TIME_OUT = "implicitWaitTimeOut";
	public static final String EXPLICIT_WAIT_TIME_OUT = "explicitWaitTimeOut";
	public static final String PAGE_LOAD_TIME_OUT = "pageLoadTimeout";
	public static final String WEB_DRIVER_GRID = "webDriverGrid";
	public static final String WEB_RUN_MODE = "webRunMode";
	public static final String WEB_RUN_BROWSER = "browser";
	public static final String WEB_RUN_BROWSER_VERSION = "browserVersion";
	public static final String WEB_SESSION_TIME_OUT = "webSessionTimeOut";
	public static final String USE_FIREFOX_DEFAULT_PROFILE = "firefoxProfle";
	public static final String CHROME_PROFILE_PATH = "chromeProfilePath";
	public static final String HEALTH_CHECK = "healthCheck";
	public static final String EMAIL_TO_LIST = "emailToList";
	public static final String PRODUCT_NAME = "productName";
	public static final String PRIORITY = "priority";
	public static final String WEB_PROXY_ENABLED = "webProxyEnabled";
	public static final String WEB_PROXY_TYPE = "webProxyType";
	public static final String WEB_PROXY_ADDRESS = "webProxyAddress";
	public static final String API_PROXY_HOST = "apiProxyHost";
	public static final String API_PROXY_PORT = "apiProxyPort";
	public static final String API_CONTEXT = "apiContext";
	public static final String TEST_OBJECT = "testObject";
	public static final String OPEN_REPORT_IN_BROWSER = "openReportInBrowser";
	public static final String CAPTURE_SNAPSHOT = "captureSnapshot";
	public static final String DB_LOG_ENABLED = "dbLogEnabled";
	public static final String SOFT_ASSERT_ENABLED = "softAssertEnabled";
	public static final String OUTPUT_DIRECTORY = "outputDirectory";
	public static final String EMAIL_RETENTION_POLICY = "emailRetentionPolicy";
	public static final String URL_CONVERT_CLASS = "urlConvertClass";
	public static final String WEB_DRIVER_LISTENER = "webDriverListener";
	public static final String TEST_METHOD_SIGNATURE = "testMethodSignature";
	public static final String TEST_DATA_FILE = "testDataFile";
	private static Logger logger = Logging.getLogger(Context.class);
	private LinkedList<TearDownService> teardowns = new LinkedList();
	private Map<ITestResult, List<Throwable>> verificationFailuresMap = new HashMap();
	private Map<String, Object> contextDataMap = Collections.synchronizedMap(new HashMap());
	private ITestContext testNGContext = null;
	private LinkedList<ScreenShot> screenshots = new LinkedList();

	public Context(ITestContext context) {
		this.testNGContext = context;
		this.setContextAttribute(context, "browser", System.getProperty("browser"), "*Firefox");
		this.setContextAttribute(context, "host", System.getProperty("host"), "http://woopler100.appspot.com");
		this.setContextAttribute(context, "priority", System.getProperty("priority"), "P1-Critical,P2-High,P3-Medium,P4-Low");
		this.setContextAttribute(context, "source_branch_name", System.getProperty("source_branch_name"), "source_branch_name");
		this.setContextAttribute(context, "selenium_grid", System.getProperty("selenium_grid"), "false");
		this.setContextAttribute(context, "new_branch", System.getProperty("new_branch"), "new_branch");
		this.setContextAttribute(context, "repositories", System.getProperty("repositories"), "repositories");
		this.setContextAttribute(context, "time", System.getProperty("time"), "0");
		this.setContextAttribute(context, "environment_type", System.getProperty("environment_type"), "TEST");
		this.setContextAttribute(context, "healthCheck", System.getProperty("healthCheck"), "true");
		this.setContextAttribute(context, "emailToList", System.getProperty("emailToList"), "santosh@wooplr.com");
		this.setContextAttribute(context, "productName", System.getProperty("productName"), "Wooplr");
		this.setContextAttribute(context, "testDataFile", System.getProperty("testDataFile"), "testCase");
		this.setContextAttribute(context, "pool", System.getProperty("pool"), "staging");
		this.setContextAttribute(context, "poolType", System.getProperty("poolType"), "qa");
		this.setContextAttribute(context, "firefoxProfle", System.getProperty("firefoxProfle"), "spireAutoProfile");
		this.setContextAttribute(context, "chromeProfilePath", System.getProperty("chromeProfilePath"), "%LOCALAPPDATA%\\Google\\Chrome\\User Data\\SpireChromeProfile");
		this.setContextAttribute(context, "webSessionTimeOut", System.getProperty("webSessionTimeOut"), "90000");
		this.setContextAttribute(context, "implicitWaitTimeOut", System.getProperty("implicitWaitTimeOut"), "5");
		this.setContextAttribute(context, "explicitWaitTimeOut", System.getProperty("explicitWaitTimeOut"), "15");
		this.setContextAttribute((ITestContext) context, "browserVersion", System.getProperty("browserVersion"), (String) null);
		this.setContextAttribute((ITestContext) context, "chromeDriverPath", System.getProperty("chromeDriverPath"), (String) null);
		this.setContextAttribute((ITestContext) context, "ieDriverPath", System.getProperty("ieDriverPath"), (String) null);
		this.setContextAttribute((ITestContext) context, "browserDownloadDir", System.getProperty("browserDownloadDir"), (String) null);
		this.setContextAttribute(context, "webProxyEnabled", System.getProperty("webProxyEnabled"), "false");
		this.setContextAttribute((ITestContext) context, "webProxyType", System.getProperty("webProxyType"), (String) null);
		this.setContextAttribute((ITestContext) context, "webProxyAddress", System.getProperty("webProxyAddress"), (String) null);
		this.setContextAttribute((ITestContext) context, "apiProxyHost", System.getProperty("apiProxyHost"), (String) null);
		this.setContextAttribute((ITestContext) context, "apiProxyPort", System.getProperty("apiProxyPort"), (String) null);
		this.setContextAttribute((ITestContext) context, "openReportInBrowser", System.getProperty("openReportInBrowser"), (String) null);
		this.setContextAttribute(context, "captureSnapshot", System.getProperty("captureSnapshot"), "true");
		this.setContextAttribute(context, "dbLogEnabled", System.getProperty("dbLogEnabled"), "true");
		this.setContextAttribute(context, "softAssertEnabled", System.getProperty("softAssertEnabled"), "false");
		this.setContextAttribute(context, "emailRetentionPolicy", System.getProperty("emailRetentionPolicy"), "1");

		this.setContextAttribute((ITestContext) context, "webDriverListener", System.getProperty("webDriverListener"), (String) null);
		if (context != null) {
			this.setContextAttribute((String) "outputDirectory", (String) null, context.getOutputDirectory(), (String) null);
			this.setContextAttribute(context);
			(new File(context.getOutputDirectory() + "/screenshots")).mkdirs();
			(new File(context.getOutputDirectory() + "/htmls")).mkdirs();
			(new File(context.getOutputDirectory() + "/xmls")).mkdirs();
			(new File(context.getOutputDirectory() + "/textfiles/")).mkdirs();
		}
	}

	public LinkedList<ScreenShot> getScreenshots() {
		return this.screenshots;
	}

	public void addScreenShot(ScreenShot screenShot) {
		this.deleteExceptionSnapshots();
		this.screenshots.addLast(screenShot);
	}

	private void deleteExceptionSnapshots() {
		try {
			int size = this.screenshots.size();
			if (size == 0) {
				return;
			}

			ScreenShot screenShot = (ScreenShot) this.screenshots.get(size - 1);
			if (screenShot.isException() && screenShot.getFullImagePath() != null) {
				(new File(screenShot.getFullImagePath())).delete();
				this.screenshots.remove(size - 1);
			}
		} catch (Exception var3) {
			;
		}

	}

	public ScreenShot getExceptionScreenShot() {
		return this.screenshots.size() > 0 && ((ScreenShot) this.screenshots.getLast()).isException() ? (ScreenShot) this.screenshots.getLast() : null;
	}

	public void addTearDownService(TearDownService tearDown) {
		this.teardowns.add(tearDown);
	}

	public void addVerificationFailures(ITestResult result, List<Throwable> failures) {
		this.verificationFailuresMap.put(result, failures);
	}

	public void addVerificationFailures(ITestResult result, Throwable failure) {
		if (this.verificationFailuresMap.get(result) != null) {
			((List) this.verificationFailuresMap.get(result)).add(failure);
		} else {
			ArrayList failures = new ArrayList();
			failures.add(failure);
			this.addVerificationFailures(result, (List) failures);
		}

	}

	public Object getAttribute(String name) {
		Object obj = this.contextDataMap.get(name);
		return obj == null ? "Firefox" : obj;
	}

	public String getBrowserDownloadDir() {
		return this.getAttribute("browserDownloadDir") != null ? (String) this.getAttribute("browserDownloadDir") : this.getOutputDirectory() + "\\downloads\\";
	}

	public String getChromeDriverPath() {
		return (String) this.getAttribute("chromeDriverPath");
	}

	public double getEmailRetentionPolicy() {
		try {
			return Double.parseDouble((String) this.getAttribute("emailRetentionPolicy"));
		} catch (Exception var2) {
			return 1.0D;
		}
	}

	public int getExplicitWaitTimeout() {
		Integer timeout;
		try {
			timeout = Integer.valueOf(Integer.parseInt((String) this.getAttribute("explicitWaitTimeOut")));
		} catch (Exception var3) {
			timeout = Integer.valueOf(15);
		}

		return (double) timeout.intValue() < this.getImplicitWaitTimeout() ? (int) this.getImplicitWaitTimeout() : timeout.intValue();
	}

	public void setExplicitWaitTimeout(double timeout) {
		this.setAttribute("explicitWaitTimeOut", Double.valueOf(timeout));
	}

	public String getIEDriverPath() {
		return (String) this.getAttribute("ieDriverPath");
	}

	public double getImplicitWaitTimeout() {
		try {
			return Double.parseDouble((String) this.getAttribute("implicitWaitTimeOut"));
		} catch (Exception var2) {
			return 5.0D;
		}
	}

	public void setImplicitWaitTimeout(double timeout) {
		this.setAttribute("implicitWaitTimeOut", Double.valueOf(timeout));
	}

	public void setTime(String time) {
		this.setAttribute("time", time);
	}

	public String getOpenReportInBrowser() {
		return (String) this.getAttribute("openReportInBrowser");
	}

	public String getCandidateId() {
		return (String) this.getAttribute("candidateId");
	}

	public void setOpenReportInBrowser(String browser) {
		this.setAttribute("openReportInBrowser", browser);
	}

	public String getOutputDirectory() {
		return (String) this.getAttribute("outputDirectory");
	}

	public String getPool() {
		return (String) this.getAttribute("pool");
	}

	public void setPool(String pool) {
		this.setAttribute("pool", pool);
	}

	public String getInstance() {
		return (String) this.getAttribute("instance");
	}

	public void setInstance(String instance) {
		this.setAttribute("instance", instance);
	}

	public String getFirefoxProfile() {
		return (String) this.getAttribute("firefoxProfle");
	}

	public void setFirefoxProfile(String firefoxDefaultProfile) {
		this.setAttribute("firefoxProfle", firefoxDefaultProfile);
	}

	public String getChromeProfilePath() {
		return (String) this.getAttribute("chromeProfilePath");
	}

	public String getBrowser() {
		return (String) this.getAttribute("browser");
	}

	public void setBrowser(String browser) {
		this.setAttribute("browser", browser);
	}

	public String getHostAddress() {
		return (String) this.getAttribute("host");
	}

	public String getUIHostAddress() {
		return (String) this.getAttribute("ui_host");
	}

	public String getRealm() {
		return (String) this.getAttribute("realm");
	}

	public String getSourceBranchName() {
		return (String) this.getAttribute("source_branch_name");
	}

	public String getSeleniumGrid() {
		return (String) this.getAttribute("selenium_grid");
	}

	public String getNewBranch() {
		return (String) this.getAttribute("new_branch");
	}

	public String getRepositories() {
		return (String) this.getAttribute("repositories");
	}

	public String getTime() {
		return (String) this.getAttribute("time");
	}

	public String getSchemaName() {
		return (String) this.getAttribute("schemaName");
	}

	public String getSearchSchemaName() {
		return (String) this.getAttribute("searchSchemaName");
	}

	public String getCommonName() {
		return (String) this.getAttribute("commonName");
	}

	public String getPriority() {
		return (String) this.getAttribute("priority");
	}

	public String getUserid() {
		return (String) this.getAttribute("userId");
	}

	public String getPassword() {
		return (String) this.getAttribute("password");
	}

	public String getBIZHostAddress() {
		return (String) this.getAttribute("biz_host");
	}

	public String getDISHostAddress() {
		return (String) this.getAttribute("dis_host");
	}

	public String getSEARCHHOSTADDRESS() {
		return (String) this.getAttribute("search_host");
	}

	public String getDBHostAddress() {
		return (String) this.getAttribute("db_host");
	}

	public String getDBPASSWORD() {
		return (String) this.getAttribute("db_password");
	}

	public String getSearchHostName() {
		return (String) this.getAttribute("searchHostName");
	}

	public String getEntityHostAddress() {
		return (String) this.getAttribute("entity_host");
	}

	public String getEnvironment() {
		return (String) this.getAttribute("environment_type");
	}

	public String getMauticHostAddress() {
		return (String) this.getAttribute("mautic_host");
	}

	public String getMauticDbHostAddress() {
		return (String) this.getAttribute("mautic_dbhost");
	}

	public String getPoolType() {
		return (String) this.getAttribute("poolType");
	}

	public String getSuiteParameter(String key) {
		return System.getProperty(key) != null ? System.getProperty(key) : this.getTestNGContext().getSuite().getParameter(key);
	}

	public LinkedList<TearDownService> getTearDownServices() {
		return this.teardowns;
	}

	public String getTestDataFile() {
		return (String) this.getAttribute("testDataFile");
	}

	public void setTestDataFile(String testDataFile) {
		this.setAttribute("testDataFile", testDataFile);
	}

	public String getTestMethodSignature() {
		return (String) this.getAttribute("testMethodSignature");
	}

	public ITestContext getTestNGContext() {
		return this.testNGContext;
	}

	public String getWebDriverListener() {
		return (String) this.getAttribute("webDriverListener");
	}

	public List<Throwable> getVerificationFailures(ITestResult result) {
		List verificationFailures = (List) this.verificationFailuresMap.get(result);
		return (List) (verificationFailures == null ? new ArrayList() : verificationFailures);
	}

	public String getWebBrowserVersion() {
		return (String) this.getAttribute("browserVersion");
	}

	public String getWebDriverGrid() {
		return (String) this.getAttribute("webDriverGrid");
	}

	public String getWebProxyAddress() {
		return (String) this.getAttribute("webProxyAddress");
	}

	public String getWebProxyType() {
		return (String) this.getAttribute("webProxyType");
	}

	public String getWebRunBrowser() {
		return (String) this.getAttribute("browser");
	}

	public String getWebRunMode() {
		return (String) this.getAttribute("webRunMode");
	}

	public int getWebSessionTimeout() {
		try {
			return Integer.parseInt((String) this.getAttribute("webSessionTimeOut"));
		} catch (Exception var2) {
			return 90000;
		}
	}

	public boolean isCaptureSnapshot() {
		try {
			return Boolean.parseBoolean((String) this.getAttribute("captureSnapshot"));
		} catch (Exception var2) {
			return true;
		}
	}

	public String getUrlConvertClass() {
		return (String) this.getAttribute("urlConvertClass");
	}

	public boolean isDBLogEnabled() {
		try {
			return Boolean.parseBoolean((String) this.getAttribute("dbLogEnabled"));
		} catch (Exception var2) {
			return true;
		}
	}

	public boolean isSoftAssertEnabled() {
		try {
			return Boolean.parseBoolean((String) this.getAttribute("softAssertEnabled"));
		} catch (Exception var2) {
			return false;
		}
	}

	public void setAttribute(String name, Object value) {
		this.contextDataMap.put(name, value);
	}

	private void setContextAttribute(ITestContext context) {
		if (context != null) {
			Map testParameters = context.getSuite().getXmlSuite().getParameters();
			Iterator var3 = testParameters.entrySet().iterator();

			while (var3.hasNext()) {
				Entry entry = (Entry) var3.next();
				String attributeName = (String) entry.getKey();
				if (this.contextDataMap.get(entry.getKey()) == null) {
					String sysPropertyValue = System.getProperty((String) entry.getKey());
					String suiteValue = (String) entry.getValue();
					this.setContextAttribute((String) attributeName, sysPropertyValue, suiteValue, (String) null);
				}
			}
		}

	}

	private void setContextAttribute(ITestContext context, String attributeName, String sysPropertyValue, String defaultValue) {
		String suiteValue = null;
		if (context != null) {
			suiteValue = context.getSuite().getParameter(attributeName);
		}

		this.contextDataMap.put(attributeName, sysPropertyValue != null ? sysPropertyValue : (suiteValue != null ? suiteValue : defaultValue));
	}

	private void setContextAttribute(String attributeName, String sysPropertyValue, String suiteValue, String defaultValue) {
		this.contextDataMap.put(attributeName, sysPropertyValue != null ? sysPropertyValue : (suiteValue != null ? suiteValue : defaultValue));
	}

	public void setHOSTADDRESS(String HOSTADDRESS) {
		this.setAttribute(HOSTADDRESS, HOSTADDRESS);
	}

	public void setUIHOSTADDRESS(String UIHOSTADDRESS) {
		this.setAttribute(UIHOSTADDRESS, UIHOSTADDRESS);
	}

	public void setBIZHOSTADDRESS(String BIZHOSTADDRESS) {
		this.setAttribute(BIZHOSTADDRESS, BIZHOSTADDRESS);
	}

	public void setDISHOSTADDRESS(String DISHOSTADDRESS) {
		this.setAttribute(DISHOSTADDRESS, DISHOSTADDRESS);
	}

	public void setSEARCHHOSTADDRESS(String SEARCHHOSTADDRESS) {
		this.setAttribute(SEARCHHOSTADDRESS, SEARCHHOSTADDRESS);
	}

	public void setSEARCHHOSTNAME(String SEARCHHOSTNAME) {
		this.setAttribute(SEARCHHOSTNAME, SEARCHHOSTNAME);
	}

	public void setDBHOSTADDRESS(String DBHOSTADDRESS) {
		this.setAttribute(DBHOSTADDRESS, DBHOSTADDRESS);
	}

	public void setDBPASSWORD(String DBPASSWORD) {
		this.setAttribute(DBPASSWORD, DBPASSWORD);
	}

	public void setUSERID(String USERID) {
		this.setAttribute(USERID, USERID);
	}

	public void setPASSWORD(String PASSWORD) {
		this.setAttribute(PASSWORD, PASSWORD);
	}

	public void setENTITYHOSTADDRESS(String ENTITYHOSTADDRESS) {
		this.setAttribute(ENTITYHOSTADDRESS, ENTITYHOSTADDRESS);
	}

	public void setEnvironment(String ENVIRONMENT) {
		this.setAttribute(ENVIRONMENT, ENVIRONMENT);
	}

	public void setPriority(String PRIORITY) {
		this.setAttribute(PRIORITY, PRIORITY);
	}

	public String getHealthCheck() {
		return (String) this.getAttribute("healthCheck");
	}

	public String getEmailToList() {
		return (String) this.getAttribute("emailToList");
	}

	public void setEmailToList(String emailToList) {
		this.setAttribute("emailToList", emailToList);
	}

	public String getProductName() {
		return (String) this.getAttribute("productName");
	}

	public void setProductName(String productName) {
		this.setAttribute("productName", productName);
	}

	public String getMONGODBHOSTADDRESS() {
		return (String) this.getAttribute("mongo_Db_Host");
	}

	public void setMONGODBHOSTADDRESS(String MONGODBHOSTADDRESS) {
		this.setAttribute(MONGODBHOSTADDRESS, MONGODBHOSTADDRESS);
	}

	public String getMONGODBPORT() {
		return (String) this.getAttribute("mongo_Db_Port");
	}

	public void setMONGODBPORT(String MONGODBPORT) {
		this.setAttribute(MONGODBPORT, MONGODBPORT);
	}

	public String getMONGODATABASENAME() {
		return (String) this.getAttribute("mongo_Db_Name");
	}

	public void setMONGODATABASENAME(String MONGODATABASENAME) {
		this.setAttribute(MONGODATABASENAME, MONGODATABASENAME);
	}
}

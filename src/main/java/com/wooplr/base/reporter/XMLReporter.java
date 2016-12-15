package com.wooplr.base.reporter;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestNGMethod;
import org.testng.Reporter;
import org.testng.internal.Utils;
import org.testng.reporters.XMLReporterConfig;
import org.testng.reporters.XMLStringBuffer;
import org.testng.reporters.XMLSuiteResultWriter;
import org.testng.xml.XmlSuite;

import com.wooplr.base.controller.ContextManager;
import com.wooplr.base.controller.Logging;


/**
 * The main entry for the XML generation operation
 */
public class XMLReporter implements IReporter {
	private static Logger logger = Logging.getLogger(XMLReporter.class);

	private XMLReporterConfig config = new XMLReporterConfig();
	private XMLStringBuffer rootBuffer;

	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		if (Utils.isStringEmpty(config.getOutputDirectory())) {
			config.setOutputDirectory(outputDirectory);
		}

		rootBuffer = new XMLStringBuffer("");
		rootBuffer.push(XMLReporterConfig.TAG_TESTNG_RESULTS);

		writeReporterOutput(rootBuffer);
		for (int i = 0; i < suites.size(); i++) {
			writeSuite(xmlSuites.get(i), suites.get(i));
		}
		rootBuffer.pop();
		// Utils.writeUtf8File(config.getOutputDirectory(),
		// "testng-results-spire.xml", rootBuffer.toXML());
		try {
			Utils.writeUtf8File(ContextManager.getGlobalContext().getOutputDirectory(), "testng-results-spire.xml", rootBuffer.toXML());
		} catch (Throwable e) {
			logger.warn("Ex", e);
		}
	}

	// TODO: This is not the smartest way to implement the config
	public int getFileFragmentationLevel() {
		return config.getFileFragmentationLevel();
	}

	public String getOutputDirectory() {
		return config.getOutputDirectory();
	}

	public int getStackTraceOutputMethod() {
		return config.getStackTraceOutputMethod();
	}

	private Properties getSuiteAttributes(ISuite suite) {
		Properties props = new Properties();
		props.setProperty(XMLReporterConfig.ATTR_NAME, suite.getName());
		return props;
	}

	public String getTimestampFormat() {
		return XMLReporterConfig.getTimestampFormat();
	}

	private Set<ITestNGMethod> getUniqueMethodSet(Collection<ITestNGMethod> methods) {
		Set<ITestNGMethod> result = new LinkedHashSet<ITestNGMethod>();
		for (ITestNGMethod method : methods) {
			result.add(method);
		}
		return result;
	}

	public boolean isGenerateDependsOnGroups() {
		return config.isGenerateDependsOnGroups();
	}

	public boolean isGenerateDependsOnMethods() {
		return config.isGenerateDependsOnMethods();
	}

	public boolean isGenerateGroupsAttribute() {
		return config.isGenerateGroupsAttribute();
	}

	public boolean isSplitClassAndPackageNames() {
		return config.isSplitClassAndPackageNames();
	}

	private File referenceSuite(XMLStringBuffer xmlBuffer, ISuite suite) {
		String relativePath = suite.getName() + File.separatorChar + "testng-results.xml";
		File suiteFile = new File(config.getOutputDirectory(), relativePath);
		Properties attrs = new Properties();
		attrs.setProperty(XMLReporterConfig.ATTR_URL, relativePath);
		xmlBuffer.addEmptyElement(XMLReporterConfig.TAG_SUITE, attrs);
		return suiteFile;
	}

	public void setFileFragmentationLevel(int fileFragmentationLevel) {
		config.setFileFragmentationLevel(fileFragmentationLevel);
	}

	public void setGenerateDependsOnGroups(boolean generateDependsOnGroups) {
		config.setGenerateDependsOnGroups(generateDependsOnGroups);
	}

	public void setGenerateDependsOnMethods(boolean generateDependsOnMethods) {
		config.setGenerateDependsOnMethods(generateDependsOnMethods);
	}

	public void setGenerateGroupsAttribute(boolean generateGroupsAttribute) {
		config.setGenerateGroupsAttribute(generateGroupsAttribute);
	}

	public void setOutputDirectory(String outputDirectory) {
		config.setOutputDirectory(outputDirectory);
	}

	public void setSplitClassAndPackageNames(boolean splitClassAndPackageNames) {
		config.setSplitClassAndPackageNames(splitClassAndPackageNames);
	}

	public void setStackTraceOutputMethod(int stackTraceOutputMethod) {
		config.setStackTraceOutputMethod(stackTraceOutputMethod);
	}

	public void setTimestampFormat(String timestampFormat) {
		config.setTimestampFormat(timestampFormat);
	}

	private void writePoolBuildInformation(ISuite suite, XMLStringBuffer xmlBuffer) {
		Properties prop = new Properties();
		prop.put("user", System.getProperty("user.name"));
		try {
			prop.put("host", InetAddress.getLocalHost().getCanonicalHostName());
		} catch (UnknownHostException e) {
		}

		// List<BugfoeTestMethodIgnore> ignoreList =
		// AbstractStep.getIgnoreList();
		//
		// Set<ITestResult> failedSet = ctx.getFailedTests().getAllResults();
		// for (ITestResult testResult : failedSet) {
		// Method method = testResult.getMethod().getMethod();
		// for (BugfoeTestMethodIgnore ignoredMethod : ignoreList) {
		// if(ignoredMethod.getMethodSignature().equals(method.getDeclaringClass().getCanonicalName()
		// + "." + method.getName())){
		//
		// }
		// }
		// }

		// prop.put("title", (String)
		// ContextManager.getGlobalContext().getAttribute(Context.REPORT_TITLE));
		// prop.put("hub",
		// ContextManager.getGlobalContext().getAttribute(Context.SELENIUM_HUB));
		prop.put("pool", ContextManager.getGlobalContext().getPool());
		// prop.put("runid", (String)
		// ContextManager.getGlobalContext().getAttribute(Context.RUN_ID));
		prop.put("runstatus", "");
		// prop.put("buildid", (String)
		// ContextManager.getGlobalContext().getAttribute(Context.BUILD_ID));
		// prop.put("buildtag", (String)
		// ContextManager.getGlobalContext().getAttribute(Context.BUILD_TAG));
		xmlBuffer.push("bugfoe-run-info", prop);
		xmlBuffer.push("config-spec");
		// xmlBuffer.addCDATA((String)
		// ContextManager.getGlobalContext().getAttribute(Context.BUILD_CONFIG_SPEC));
		xmlBuffer.pop();

		xmlBuffer.pop();
	}


	private void writeReporterOutput(XMLStringBuffer xmlBuffer) {
		// TODO: Cosmin - maybe a <line> element isn't indicated for each line.
		// Also escaping might be considered
		xmlBuffer.push(XMLReporterConfig.TAG_REPORTER_OUTPUT);
		List<String> output = Reporter.getOutput();
		for (String line : output) {
			xmlBuffer.push(XMLReporterConfig.TAG_LINE);
			xmlBuffer.addCDATA(line);
			xmlBuffer.pop();
		}
		xmlBuffer.pop();
	}

	private void writeSuite(XmlSuite xmlSuite, ISuite suite) {

		switch (config.getFileFragmentationLevel()) {
		case XMLReporterConfig.FF_LEVEL_NONE:
			writeSuiteToBuffer(rootBuffer, suite);
			break;
		case XMLReporterConfig.FF_LEVEL_SUITE:
		case XMLReporterConfig.FF_LEVEL_SUITE_RESULT:
			File suiteFile = referenceSuite(rootBuffer, suite);
			writeSuiteToFile(suiteFile, suite);
		}
	}

	private void writeSuiteGroups(XMLStringBuffer xmlBuffer, ISuite suite) {
		xmlBuffer.push(XMLReporterConfig.TAG_GROUPS);
		Map<String, Collection<ITestNGMethod>> methodsByGroups = suite.getMethodsByGroups();
		for (String groupName : methodsByGroups.keySet()) {
			Properties groupAttrs = new Properties();
			groupAttrs.setProperty(XMLReporterConfig.ATTR_NAME, groupName);
			xmlBuffer.push(XMLReporterConfig.TAG_GROUP, groupAttrs);
			Set<ITestNGMethod> groupMethods = getUniqueMethodSet(methodsByGroups.get(groupName));
			for (ITestNGMethod groupMethod : groupMethods) {
				Properties methodAttrs = new Properties();
				methodAttrs.setProperty(XMLReporterConfig.ATTR_NAME, groupMethod.getMethodName());
				methodAttrs.setProperty(XMLReporterConfig.ATTR_METHOD_SIG, groupMethod.toString());
				methodAttrs.setProperty(XMLReporterConfig.ATTR_CLASS, groupMethod.getRealClass().getName());
				xmlBuffer.addEmptyElement(XMLReporterConfig.TAG_METHOD, methodAttrs);
			}
			xmlBuffer.pop();
		}
		xmlBuffer.pop();
	}

	private void writeSuiteToBuffer(XMLStringBuffer xmlBuffer, ISuite suite) {
		xmlBuffer.push(XMLReporterConfig.TAG_SUITE, getSuiteAttributes(suite));

		writePoolBuildInformation(suite, rootBuffer);

		writeSuiteGroups(xmlBuffer, suite);

		Map<String, ISuiteResult> results = suite.getResults();
		XMLSuiteResultWriter suiteResultWriter = new XMLSuiteResultWriter(config);
		for (Map.Entry<String, ISuiteResult> result : results.entrySet()) {
			suiteResultWriter.writeSuiteResult(xmlBuffer, result.getValue());
		}

		xmlBuffer.pop();
	}

	private void writeSuiteToFile(File suiteFile, ISuite suite) {
		XMLStringBuffer xmlBuffer = new XMLStringBuffer("");
		writeSuiteToBuffer(xmlBuffer, suite);
		File parentDir = suiteFile.getParentFile();
		if (parentDir.exists() || suiteFile.getParentFile().mkdirs()) {
			Utils.writeFile(parentDir.getAbsolutePath(), "testng-results.xml", xmlBuffer.toXML());
		}
	}
}

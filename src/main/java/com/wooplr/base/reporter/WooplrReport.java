package com.wooplr.base.reporter;

import org.testng.*;
import org.testng.collections.Lists;
import org.testng.internal.Utils;
import org.testng.log4testng.Logger;
import org.testng.xml.XmlSuite;

import com.wooplr.base.controller.Context;
import com.wooplr.base.controller.ContextManager;
import com.wooplr.base.controller.Logging;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Reporter that generates a single-page HTML report of the suite test results.
 * <p>
 * Based on TestNG built-in implementation: org.testng.reporters.EmailableReporter2
 * </p>
 */
public class WooplrReport implements IReporter {

    private static final Logger LOG = Logger.getLogger(WooplrReport.class);
    private static String timeZone = "GMT-7";
    private static SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
    private static SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss a");
    private static String outFilename = "custom-report.html";
    private static NumberFormat integerFormat = NumberFormat.getIntegerInstance();
    private static NumberFormat decimalFormat = NumberFormat.getNumberInstance();
    protected PrintWriter writer;
    protected List<SuiteResult> suiteResults = Lists.newArrayList();
    private String outputDirectory;
    private StringBuilder buffer = new StringBuilder();

    public static File getReportLocation() {
        return new File("\\" + outFilename);
    }

    public static void main(String args[]) {
        double value = 1;
        double value2 = 2;
        double result = (value / value2);

        System.out.print(result + " " + result * 100);

        // executeCmd("Chrome", "http://www.google.com");
    }

    public static void executeCmd(String browserPath, String theUrl) {
        String cmdLine = null;
        String osName = System.getProperty("os.name");

        if (osName.startsWith("Windows")) {
            cmdLine = "start " + theUrl;
            // on NT, you need to start cmd.exe because start is not
            // an external command but internal, you need to start the
            // command interpreter
            // cmdLine = "cmd.exe /c " + cmdLine;
            cmdLine = "rundll32 SHELL32.DLL,ShellExec_RunDLL " + browserPath + " " + theUrl;
        } else if (osName.startsWith("Mac")) {
            cmdLine = "open " + theUrl;
        } else {
            //  Linux
            cmdLine = "open " + browserPath + " " + theUrl;
        }
        try {
            Runtime.getRuntime().exec(cmdLine);
        } catch (Exception e) {
            Logging.info("" + e);
        }
    }

    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
                               String outputDirectory) {
        try {
            File f = new File(ContextManager.getGlobalContext().getOutputDirectory());
            setOutputDirectory(f.getParentFile().getAbsolutePath());
            writer = createWriter(getOutputDirectory());
        } catch (IOException e) {
            LOG.error("Unable to create output file", e);
            return;
        }
        for (ISuite suite : suites) {
            suiteResults.add(new SuiteResult(suite));
        }

        writeDocumentStart();
        writeHead();
        writeBody();
        writeDocumentEnd();
        writer.close();
        String browserPath = (String) ContextManager.getGlobalContext().getAttribute(Context.OPEN_REPORT_IN_BROWSER);
        if (browserPath != null && browserPath.trim().length() > 0) {
            executeCmd(browserPath, getOutputDirectory() + getReportLocation());
        }
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    protected PrintWriter createWriter(String outdir) throws IOException {
        new File(outdir).mkdirs();
        return new PrintWriter(new BufferedWriter(new FileWriter(new File(outdir, outFilename))));
    }

    protected void writeDocumentStart() {
        writer.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
        writer.print("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
    }

    protected void writeHead() {
        writer.print("<head>");
        writer.print("<title>Wooplr Automation Report</title>");
        // writer.print("<h1><img src=\"https://www.recruiter.com/i/wp-content/uploads/2013/12/Spire-logo.png\"></h1>");
        writeStylesheet();   
        writer.print("</head>");
    }

    protected void writeStylesheet() {
        writer.print("<style type=\"text/css\">");
        writer.print("table {margin-bottom:10px;border-collapse:collapse;empty-cells:show}");
        writer.print("th,td {border:1px solid #009;padding:.25em .5em}");
        writer.print("th {vertical-align:bottom}");
        writer.print("td {vertical-align:top}");
        writer.print("table a {font-weight:bold}");
        writer.print(".stripe td {background-color: #E6EBF9}");
        writer.print(".num {text-align:right}");
        writer.print(".passedodd td {background-color: #3F3}");
        writer.print(".passedeven td {background-color: #0A0}");
        writer.print(".skippedodd td {background-color: #DDD}");
        writer.print(".skippedeven td {background-color: #CCC}");
        writer.print(".failedodd td,.attn {background-color: #F33}");
        writer.print(".failedeven td,.stripe .attn {background-color: #D00}");
        writer.print(".stacktrace {white-space:pre;font-family:monospace}");
        writer.print(".totop {font-size:85%;text-align:center;border-bottom:2px solid #000}");
        writer.print("</style>");
    }

    protected void writeBody() {
        writer.print("<body bgcolor=\"#E6E6FA\">");
        writer.print("<div class=\"center\">");
        writeReportTitle("<font color=\"Grey\">Wooplr Automation Report");
        writeSuiteSummary();
        writeScenarioSummary();
        writeScenarioDetails();
        writer.print("</div>");
        writer.print("</body>");   
    }


    protected void writeReportTitle(String title) {
        writer.print("<center><img src=\"http://res.wooplr.com/image/upload/h_120/assets/website/icon/wooplr.png\"></center> <center><h1>" + title + " - " + getDateAsString() + "</h1></center>");
    }

    protected void writeDocumentEnd() {
        writer.print("</html>");
    }

    protected void writeSuiteSummary() {

        int totalTests = 0;
        double passPercent = 0;
        int totalPassedTests = 0;
        int totalSkippedTests = 0;
        int totalFailedTests = 0;
        long totalDuration = 0;
        writer.print("<center>");
        writer.print("<table>");
        writer.print("<tr>");
        writer.print("<th>Test</th>");
        writer.print("<th>Total Tests </th>");
       
        writer.print("<th>Passed</th>");
        
        writer.print("<th>Failed</th>");
        writer.print("<th>Skipped</th>");
        writer.print("<th>Time Taken (Seconds)</th>");
        writer.print("<th>Pass % </th>");
        writer.print("<th>Included Groups</th>");
        writer.print("<th>Excluded Groups</th>");
        
        writer.print("</tr>");
        //   writer.print("</center>");

        int testIndex = 0;
        for (SuiteResult suiteResult : suiteResults) {
            writer.print("<tr><th colspan=\"7\">");
            writer.print(Utils.escapeHtml(suiteResult.getSuiteName()));
            writer.print("</th></tr>");

            for (TestResult testResult : suiteResult.getTestResults()) {
                
                passPercent = testResult.getPassPercent();
                int passedTests = testResult.getPassedTestCount();
                int skippedTests = testResult.getSkippedTestCount();
                int failedTests = testResult.getFailedTestCount();
                long duration = testResult.getDuration();
                int totalTempTest=testResult.getTotalTestCount();

                writer.print("<tr");
                if ((testIndex % 2) == 1) {
                    writer.print(" class=\"stripe\"");
                }
                writer.print(">");

                buffer.setLength(0);
                writeTableData(buffer.append("<a href=\"#t").append(testIndex).append("\">")
                        .append(Utils.escapeHtml(testResult.getTestName())).append("</a>").toString());
                writeTableData(integerFormat.format(totalTempTest), "num");
                
                writeTableData(integerFormat.format(passedTests), "num");
                
                writeTableData(integerFormat.format(failedTests), (failedTests > 0 ? "num attn" : "num"));
                writeTableData(integerFormat.format(skippedTests), (skippedTests > 0 ? "num attn" : "num"));
                writeTableData(decimalFormat.format(millisecondsToSeconds(duration)), "num");
                writeTableData(decimalFormat.format(passPercent)+"%", "num");
                writeTableData(testResult.getIncludedGroups());
                writeTableData(testResult.getExcludedGroups());
                

                writer.print("</tr>");

                totalPassedTests += passedTests;
                totalTests +=totalTempTest;
                totalSkippedTests += skippedTests;
                totalFailedTests += failedTests;
                totalDuration += duration;

                testIndex++;
            }
        }

        // Print totals if there was more than one test
        if (testIndex >= 1) {
            writer.print("<tr>");
            writer.print("<th>Total</th>");
            writeTableData(integerFormat.format(totalTests), "num");
            
            writeTableHeader(integerFormat.format(totalPassedTests), "num");
            writeTableHeader(integerFormat.format(totalFailedTests), (totalFailedTests > 0 ? "num attn" : "num"));
            writeTableHeader(integerFormat.format(totalSkippedTests), (totalSkippedTests > 0 ? "num attn" : "num"));
            
            /*writeTableHeader(decimalFormat.format(millisecondsToSeconds(totalDuration)), "num");*/
            writeTableHeader(ContextManager.getThreadContext().getTime(), "num");
            writeTableData(decimalFormat.format((((double)totalPassedTests/(double)totalTests))*100)+"%", "num");
            writer.print("<th colspan=\"2\"></th>");
            
            writer.print("</tr>");
        }

        writer.print("</table>");
        writer.print("</center>");
    }

    /**
     * Writes a summary of all the test scenarios.
     */
    protected void writeScenarioSummary() {
        writer.print("<center>");
        writer.print("<table>");
        writer.print("<thead>");
        writer.print("<tr>");
        writer.print("<th>Class</th>");
        writer.print("<th>Method</th>");
        writer.print("<th>Name</th>");
        writer.print("<th>Start</th>");
        writer.print("<th>Seconds</th>");
        writer.print("</tr>");
        writer.print("</thead>");

        int testIndex = 0;
        int scenarioIndex = 0;
        for (SuiteResult suiteResult : suiteResults) {
            writer.print("<tbody><tr><th colspan=\"5\">");
            writer.print(Utils.escapeHtml(suiteResult.getSuiteName()));   
            writer.print("</th></tr></tbody>");

            for (TestResult testResult : suiteResult.getTestResults()) {
                writer.print("<tbody id=\"t");
                writer.print(testIndex);
                writer.print("\">");

                String testName = Utils.escapeHtml(testResult.getTestName());

                scenarioIndex += writeScenarioSummary(testName
                                + " &#8212; failed (configuration methods)",
                        testResult.getFailedConfigurationResults(), "failed",
                        scenarioIndex
                );
                scenarioIndex += writeScenarioSummary(testName
                                + " &#8212; failed", testResult.getFailedTestResults(),
                        "failed", scenarioIndex
                );
                scenarioIndex += writeScenarioSummary(testName
                                + " &#8212; skipped (configuration methods)",
                        testResult.getSkippedConfigurationResults(), "skipped",
                        scenarioIndex
                );
                scenarioIndex += writeScenarioSummary(testName
                                + " &#8212; skipped",
                        testResult.getSkippedTestResults(), "skipped",
                        scenarioIndex
                );
                scenarioIndex += writeScenarioSummary(testName
                                + " &#8212; passed", testResult.getPassedTestResults(),
                        "passed", scenarioIndex
                );

                writer.print("</tbody>");

                testIndex++;
            }
        }

        writer.print("</table>");
        writer.print("<center>");
    }

    /**
     * Writes the scenario summary for the results of a given state for a single
     * test.
     */
    private int writeScenarioSummary(String description, List<ClassResult> classResults,
                                     String cssClassPrefix, int startingScenarioIndex) {
        int scenarioCount = 0;
        if (!classResults.isEmpty()) {
            writer.print("<tr><th colspan=\"5\">");
            writer.print(description);
            writer.print("</th></tr>");

            int scenarioIndex = startingScenarioIndex;
            int classIndex = 0;
            for (ClassResult classResult : classResults) {
                String cssClass = cssClassPrefix + ((classIndex % 2) == 0 ? "even" : "odd");

                buffer.setLength(0);

                int scenariosPerClass = 0;
                int methodIndex = 0;
                for (MethodResult methodResult : classResult.getMethodResults()) {
                    List<ITestResult> results = methodResult.getResults();
                    int resultsCount = results.size();
                    assert resultsCount > 0;

                    ITestResult aResult = results.iterator().next();
                    String methodName = Utils.escapeHtml(aResult.getMethod().getMethodName());
                    long start = aResult.getStartMillis();
                    long duration = aResult.getEndMillis() - start;

                    // The first method per class shares a row with the class
                    // header
                    if (methodIndex > 0) {
                        buffer.append("<tr class=\"").append(cssClass)
                                .append("\">");

                    }

                    // Write the timing information with the first scenario per method
                    buffer.append("<td><a href=\"#m").append(scenarioIndex)
                            .append("\">").append(methodName + "</a></td>")
                            .append("<td rowspan=\"1\">" + aResult.getName() + "</td>")
                            .append("<td rowspan=\"")
                            .append(resultsCount).append("\">").append(parseUnixTimeToTimeOfDay(start))
                            .append("</td>").append("<td rowspan=\"")
                            .append(resultsCount).append("\">")
                            .append(decimalFormat.format(millisecondsToSeconds(duration))).append("</td></tr>");
                    scenarioIndex++;

                    // Write the remaining scenarios for the method
                    for (int i = 1; i < resultsCount; i++) {
                        buffer.append("<tr class=\"").append(cssClass)
                                .append("\">").append("<td><a href=\"#m")
                                .append(scenarioIndex).append("\">")
                                .append(methodName + "</a></td>")
                                .append("<td rowspan=\"1\">" + aResult.getName() + "</td></tr>");
                        scenarioIndex++;
                    }

                    scenariosPerClass += resultsCount;
                    methodIndex++;
                }

                // Write the test results for the class
                writer.print("<tr class=\"");
                writer.print(cssClass);
                writer.print("\">");
                writer.print("<td rowspan=\"");
                writer.print(scenariosPerClass);
                writer.print("\">");
                writer.print(Utils.escapeHtml(classResult.getClassName()));
                writer.print("</td>");
                writer.print(buffer);

                classIndex++;
            }
            scenarioCount = scenarioIndex - startingScenarioIndex;
        }
        return scenarioCount;
    }

    /**
     * Writes the details for all test scenarios.
     */
    protected void writeScenarioDetails() {
        int scenarioIndex = 0;
        for (SuiteResult suiteResult : suiteResults) {
            for (TestResult testResult : suiteResult.getTestResults()) {
                writer.print("<h2>");
                writer.print(Utils.escapeHtml(testResult.getTestName()));
                writer.print("</h2>");

                scenarioIndex += writeScenarioDetails(
                        testResult.getFailedConfigurationResults(), scenarioIndex);
                scenarioIndex += writeScenarioDetails(
                        testResult.getFailedTestResults(), scenarioIndex);
                scenarioIndex += writeScenarioDetails(
                        testResult.getSkippedConfigurationResults(), scenarioIndex);
                scenarioIndex += writeScenarioDetails(
                        testResult.getSkippedTestResults(), scenarioIndex);
                scenarioIndex += writeScenarioDetails(
                        testResult.getPassedTestResults(), scenarioIndex);
            }
        }
    }

    /**
     * Writes the scenario details for the results of a given state for a single
     * test.
     */
    private int writeScenarioDetails(List<ClassResult> classResults, int startingScenarioIndex) {
        int scenarioIndex = startingScenarioIndex;
        for (ClassResult classResult : classResults) {
            String className = classResult.getClassName();
            
            List<MethodResult> methods = classResult.getMethodResults();
            methods=removeFailedTest(methods);
            for (MethodResult methodResult : methods) {
                List<ITestResult> results1 = methodResult.getResults();
                Set<ITestResult> results=new HashSet<ITestResult>(results1);
                assert !results.isEmpty();

                ITestResult mResult = results.iterator().next();
                String label = Utils.escapeHtml(className + "#"
                        + mResult.getMethod().getMethodName() + " ( " + mResult.getName() + " )");
                for (ITestResult result : results) {
                    writeScenario(scenarioIndex, label, result);
                    scenarioIndex++;
                }
            }
        }

        return scenarioIndex - startingScenarioIndex;
    }
    
    public List<MethodResult> removeFailedTest(List<MethodResult> methods){
    	
    	List<MethodResult> newMethods = new ArrayList<WooplrReport.MethodResult>();
    	int i=0;
    	
    	for (MethodResult methodResult : methods) {
    		
    		List<ITestResult> newList = new ArrayList<ITestResult>();
    		
    		List<ITestResult> results=methodResult.getResults();
    		
    		MethodResult newMethod = new MethodResult(newList);
    		
    		for (ITestResult iTestResult : results) {
    			
    			boolean isDuplicate=false;
    			
    			  			
    			if (newList.size()==0) {
    				newList.add(iTestResult);
    			}
        		
    			for (ITestResult list : newList) {
    				
    				if (list!=null && list.getParameters()!=null && list.getParameters().length>0) {
    					
    					if ((list.getParameters()[0].toString()).equalsIgnoreCase(iTestResult.getParameters()[0].toString())) {
        					isDuplicate = true;
    					}
    					
    				}
    				
    				
    				
    			}
    			
        		if (!isDuplicate){
        			newList.add(iTestResult);
        		}
				
			}
    		
    		System.out.println(results.size());
    		System.out.println(newList.size());
    		
    		newMethods.add(newMethod);
		}
    	
    	return newMethods;
    	
    }

    /**
     * Writes the details for an individual test scenario.
     */
    private void writeScenario(int scenarioIndex, String label,
                               ITestResult result) {
        writer.print("<h3 id=\"m");
        writer.print(scenarioIndex);
        writer.print("\">");
        writer.print(label);
        writer.print("</h3>");

        writer.print("<table class=\"result\">");

        // Write test parameters (if any)
        Object[] parameters = result.getParameters();
        int parameterCount = (parameters == null ? 0 : parameters.length);
        if (parameterCount > 0) {
            writer.print("<tr class=\"param\">");
            for (int i = 1; i <= parameterCount; i++) {
                writer.print("<th>Parameter #");
                writer.print(i);
                writer.print("</th>");
            }
            writer.print("</tr><tr class=\"param stripe\">");
            for (Object parameter : parameters) {
                writer.print("<td>");
                writer.print(Utils.escapeHtml(Utils.toString(parameter, Object.class)));
                writer.print("</td>");
            }
            writer.print("</tr>");
        }

        // Write reporter messages (if any)
        List<String> reporterMessages = Reporter.getOutput(result);
        if (!reporterMessages.isEmpty()) {
            writer.print("<tr><th");
            if (parameterCount > 1) {
                writer.print(" colspan=\"");
                writer.print(parameterCount);
                writer.print("\"");
            }
            writer.print(">Messages</th></tr>");

            writer.print("<tr><td");
            if (parameterCount > 1) {
                writer.print(" colspan=\"");
                writer.print(parameterCount);
                writer.print("\"");
            }
            writer.print(">");
            writeReporterMessages(reporterMessages);
            writer.print("</td></tr>");
        }

        // Write exception (if any)
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            writer.print("<tr><th");
            if (parameterCount > 1) {
                writer.print(" colspan=\"");
                writer.print(parameterCount);
                writer.print("\"");
            }
            writer.print(">");
            writer.print((result.getStatus() == ITestResult.SUCCESS ? "Expected Exception"
                    : "Exception"));
            writer.print("</th></tr>");

            writer.print("<tr><td");
            if (parameterCount > 1) {
                writer.print(" colspan=\"");
                writer.print(parameterCount);
                writer.print("\"");
            }
            writer.print(">");
            writeStackTrace(throwable);
            writer.print("</td></tr>");
        }

        writer.print("</table>");
        writer.print("<p class=\"totop\"><a href=\"#summary\">back to summary</a></p>");
    }

    protected void writeReporterMessages(List<String> reporterMessages) {
        writer.print("<div class=\"messages\">");
        Iterator<String> iterator = reporterMessages.iterator();
        assert iterator.hasNext();
        writer.print(Utils.escapeHtml(iterator.next()));
        while (iterator.hasNext()) {
            writer.print("<br/>");
            writer.print(Utils.escapeHtml(iterator.next()));
        }
        writer.print("</div>");
    }

    protected void writeStackTrace(Throwable throwable) {
        writer.print("<div class=\"stacktrace\">");
        writer.print(Utils.stackTrace(throwable, true)[0]);
        writer.print("</div>");
    }

    /**
     * Writes a TH element with the specified contents and CSS class names.
     *
     * @param html       the HTML contents
     * @param cssClasses the space-delimited CSS classes or null if there are no
     *                   classes to apply
     */
    protected void writeTableHeader(String html, String cssClasses) {
        writeTag("th", html, cssClasses);
    }

    /**
     * Writes a TD element with the specified contents.
     *
     * @param html the HTML contents
     */
    protected void writeTableData(String html) {
        writeTableData(html, null);
    }

    /**
     * Writes a TD element with the specified contents and CSS class names.
     *
     * @param html       the HTML contents
     * @param cssClasses the space-delimited CSS classes or null if there are no
     *                   classes to apply
     */
    protected void writeTableData(String html, String cssClasses) {
        writeTag("td", html, cssClasses);
    }

    /**
     * Writes an arbitrary HTML element with the specified contents and CSS
     * class names.
     *
     * @param tag        the tag name
     * @param html       the HTML contents
     * @param cssClasses the space-delimited CSS classes or null if there are no
     *                   classes to apply
     */
    protected void writeTag(String tag, String html, String cssClasses) {
        writer.print("<");
        writer.print(tag);
        if (cssClasses != null) {
            writer.print(" class=\"");
            writer.print(cssClasses);
            writer.print("\"");
        }
        writer.print(">");
        writer.print(html);
        writer.print("</");
        writer.print(tag);
        writer.print(">");
    }

    /*
    Methods to improve time display on report
     */
    protected String getDateAsString() {
        Date date = new Date();
        sdfdate.setTimeZone(TimeZone.getTimeZone(timeZone));
        return sdfdate.format(date);
    }

    protected String parseUnixTimeToTimeOfDay(long unixSeconds) {
        Date date = new Date(unixSeconds);
        sdftime.setTimeZone(TimeZone.getTimeZone(timeZone));
        return sdftime.format(date);
    }

    protected double millisecondsToSeconds(long ms) {
        return new BigDecimal(ms / 1000.00).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * Groups {@link TestResult}s by suite.
     */
    protected static class SuiteResult {
        private final String suiteName;
        private final List<TestResult> testResults = Lists.newArrayList();

        public SuiteResult(ISuite suite) {
            suiteName = suite.getName();
            for (ISuiteResult suiteResult : suite.getResults().values()) {
                testResults.add(new TestResult(suiteResult.getTestContext()));
            }
        }

        public String getSuiteName() {
            return suiteName;
        }

        /**
         * @return the test results (possibly empty)
         */
        public List<TestResult> getTestResults() {
            return testResults;
        }
    }
    
    

    /**
     * Groups {@link ClassResult}s by test, type (configuration or test), and
     * status.
     */
    protected static class TestResult {
        /**
         * Orders test results by class name and then by method name (in
         * lexicographic order).
         */
        protected static final Comparator<ITestResult> RESULT_COMPARATOR = new Comparator<ITestResult>() {
            public int compare(ITestResult o1, ITestResult o2) {
                int result = o1.getTestClass().getName()
                        .compareTo(o2.getTestClass().getName());
                if (result == 0) {
                    result = o1.getMethod().getMethodName()
                            .compareTo(o2.getMethod().getMethodName());
                }
                return result;
            }
        };

        private final String testName;
        private final List<ClassResult> failedConfigurationResults;
        private final List<ClassResult> failedTestResults;
        private final List<ClassResult> skippedConfigurationResults;
        private final List<ClassResult> skippedTestResults;
        private final List<ClassResult> passedTestResults;
        private final int totalTestCount;
        private final double passPercent;
        private final int failedTestCount;
        private final int skippedTestCount;
        private final int passedTestCount;
        private final long duration;
        private final String includedGroups;
        private final String excludedGroups;

        public TestResult(ITestContext context) {
            testName = context.getName();

            Set<ITestResult> passedTests = deleteDuplicates(context.getPassedTests()
                    .getAllResults());
            
            Set<ITestResult> failedConfigurations = ignorePassedTests(passedTests, deleteDuplicates(context
                    .getFailedConfigurations().getAllResults()));
            
            Set<ITestResult> failedTests = ignorePassedTests(passedTests, deleteDuplicates(context.getFailedTests()
                    .getAllResults()));
            
            //failedTests=deleteDuplicates(failedTests);
            
            Set<ITestResult> skippedConfigurations = ignorePassedTests(passedTests, deleteDuplicates(context
                    .getSkippedConfigurations().getAllResults()));
            Set<ITestResult> skippedTests = ignorePassedTests(passedTests, deleteDuplicates(context.getSkippedTests()
                    .getAllResults()));
            
            
            
            failedConfigurationResults = groupResults(failedConfigurations);
            failedTestResults = groupResults(failedTests);
            skippedConfigurationResults = groupResults(skippedConfigurations);
            skippedTestResults = groupResults(skippedTests);
            passedTestResults = groupResults(passedTests);

            failedTestCount = failedTests.size();
            skippedTestCount = skippedTests.size();
            passedTestCount = passedTests.size();
            totalTestCount = failedTestCount + skippedTestCount + passedTestCount;
            double value = ((double) passedTestCount / (double) totalTestCount);
            passPercent = value * 100;
            duration = context.getEndDate().getTime() - context.getStartDate().getTime();
            includedGroups = formatGroups(context.getIncludedGroups());
            excludedGroups = formatGroups(context.getExcludedGroups());
        }
        
        public static Set<ITestResult> deleteDuplicates(Set<ITestResult> testResults){
        	
        	Set<ITestResult> newSet = new HashSet<ITestResult>();
        	
        	boolean isDuplicate = false;
        	
        	for (ITestResult iTestResult : testResults) {
        		
        		isDuplicate = false;
    			
        		if (newSet.size()==0) {
    				newSet.add(iTestResult);
    			}
        		
        		for (ITestResult result : newSet) {
        			
        			if (result!=null && result.getParameters()!=null && result.getParameters().length>0) {
        				
        				if (iTestResult.getParameters()!=null && iTestResult.getParameters().length>0 ) {
						
        					if ((result.getParameters()[0].toString()).equalsIgnoreCase(iTestResult.getParameters()[0].toString())) {
                				isDuplicate=true;
            				}
        					
						}
        				
        				
        			}
        			
        			
        		}
        		
        		if (!isDuplicate) {
        			newSet.add(iTestResult);
    			}
        		
    		}
        	
        	return newSet;
        	
        }
        
        public static Set<ITestResult> ignorePassedTests(Set<ITestResult> passedTests, Set<ITestResult> testResults){
        		
        	boolean isTestPassed=false;
        	
        	for (ITestResult pTest : passedTests) {
        		
        		isTestPassed=false;
				
        		for (ITestResult result : testResults) {
        			
        			if (result!=null && result.getParameters()!=null && result.getParameters().length>0) {
        				
        				if (pTest.getParameters()!=null && pTest.getParameters().length>0 ) {
        					
        					if ((result.getParameters()[0].toString()).equalsIgnoreCase(pTest.getParameters()[0].toString())) {
                				testResults.remove(result);
                				break;
        					}
        					
        				}
        				
        				
        			}
					
        			
        			
				}
        		
			}
        	
        	return testResults;
	 
        }

        /**
         * Groups test results by method and then by class.
         */
        protected List<ClassResult> groupResults(Set<ITestResult> results) {
            List<ClassResult> classResults = Lists.newArrayList();
            if (!results.isEmpty()) {
                List<MethodResult> resultsPerClass = Lists.newArrayList();
                List<ITestResult> resultsPerMethod = Lists.newArrayList();

                List<ITestResult> resultsList = Lists.newArrayList(results);
                Collections.sort(resultsList, RESULT_COMPARATOR);
                Iterator<ITestResult> resultsIterator = resultsList.iterator();
                assert resultsIterator.hasNext();

                ITestResult result = resultsIterator.next();
                resultsPerMethod.add(result);

                String previousClassName = result.getTestClass().getName();
                String previousMethodName = result.getMethod().getMethodName();
                while (resultsIterator.hasNext()) {
                    result = resultsIterator.next();

                    String className = result.getTestClass().getName();
                    if (!previousClassName.equals(className)) {
                        // Different class implies different method
                        assert !resultsPerMethod.isEmpty();
                        resultsPerClass.add(new MethodResult(resultsPerMethod));
                        resultsPerMethod = Lists.newArrayList();

                        assert !resultsPerClass.isEmpty();
                        classResults.add(new ClassResult(previousClassName,
                                resultsPerClass));
                        resultsPerClass = Lists.newArrayList();

                        previousClassName = className;
                        previousMethodName = result.getMethod().getMethodName();
                    } else {
                        String methodName = result.getMethod().getMethodName();
                        if (!previousMethodName.equals(methodName)) {
                            assert !resultsPerMethod.isEmpty();
                            resultsPerClass.add(new MethodResult(resultsPerMethod));
                            resultsPerMethod = Lists.newArrayList();

                            previousMethodName = methodName;
                        }
                    }
                    resultsPerMethod.add(result);
                }
                assert !resultsPerMethod.isEmpty();
                resultsPerClass.add(new MethodResult(resultsPerMethod));
                assert !resultsPerClass.isEmpty();
                classResults.add(new ClassResult(previousClassName,
                        resultsPerClass));
            }
            return classResults;
        }

        public String getTestName() {
            return testName;
        }

        /**
         * @return the results for failed configurations (possibly empty)
         */
        public List<ClassResult> getFailedConfigurationResults() {
            return failedConfigurationResults;
        }

        /**
         * @return the results for failed tests (possibly empty)
         */
        public List<ClassResult> getFailedTestResults() {
            return failedTestResults;
        }

        /**
         * @return the results for skipped configurations (possibly empty)
         */
        public List<ClassResult> getSkippedConfigurationResults() {
            return skippedConfigurationResults;
        }

        /**
         * @return the results for skipped tests (possibly empty)
         */
        public List<ClassResult> getSkippedTestResults() {
            return skippedTestResults;
        }

        /**
         * @return the results for passed tests (possibly empty)
         */
        public List<ClassResult> getPassedTestResults() {
            return passedTestResults;
        }

        public int getFailedTestCount() {
            return failedTestCount;
        }

        public int getSkippedTestCount() {
            return skippedTestCount;
        }

        public int getTotalTestCount() {
            return totalTestCount;
        }

        public double getPassPercent() {
            return passPercent;
        }

        public int getPassedTestCount() {
            return passedTestCount;
        }

        public long getDuration() {
            return duration;
        }

        public String getIncludedGroups() {
            return includedGroups;
        }

        public String getExcludedGroups() {
            return excludedGroups;
        }

        /**
         * Formats an array of groups for display.
         */
        protected String formatGroups(String[] groups) {
            if (groups.length == 0) {
                return "";
            }

            StringBuilder builder = new StringBuilder();
            builder.append(groups[0]);
            for (int i = 1; i < groups.length; i++) {
                builder.append(", ").append(groups[i]);
            }
            return builder.toString();
        }
    }

    /**
     * Groups {@link MethodResult}s by class.
     */
    protected static class ClassResult {
        private final String className;
        private final List<MethodResult> methodResults;

        /**
         * @param className     the class name
         * @param methodResults the non-null, non-empty {@link MethodResult} list
         */
        public ClassResult(String className, List<MethodResult> methodResults) {
            this.className = className;
            this.methodResults = methodResults;
        }

        public String getClassName() {
            return className;
        }

        /**
         * @return the non-null, non-empty {@link MethodResult} list
         */
        public List<MethodResult> getMethodResults() {
            return methodResults;
        }
    }

    /**
     * Groups test results by method.
     */
    protected static class MethodResult {
        private final List<ITestResult> results;

        /**
         * @param results the non-null, non-empty result list
         */
        public MethodResult(List<ITestResult> results) {
            this.results = results;
        }

        /**
         * @return the non-null, non-empty result list
         */
        public List<ITestResult> getResults() {
            return results;
        }
    }

}


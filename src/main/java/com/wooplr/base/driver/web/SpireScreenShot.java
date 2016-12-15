package com.wooplr.base.driver.web;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.testng.ITestNGMethod;
import org.testng.Reporter;

import com.san.base.util.SpireUtilConstants;

/**
 * Created by Vinaya Kumar on 01-06-2015.
 */
public class SpireScreenShot {

    /***
     * @param threadDriver
     * @param testMethod
     * @throws Exception
     */
    public static void takeScreenShoot(WebDriver webDriver , ITestNGMethod testMethod) throws Exception {
        WebDriver augmentedDriver = new Augmenter().augment(webDriver);
        File screenshot = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
        String nameScreenshot = testMethod.getMethodName();
        String path = getPath(nameScreenshot);
      //  String path = SpireUtilConstants.SCREENSHOT_PATH;
        FileUtils.copyFile(screenshot, new File(path));
        Reporter.log("<a href=\"file://&quot;\" target=\"_blank\">\" + this.getFileName(nameScreenshot) + \"</a>");
    }

    private static String getFileName(String nameTest) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy_hh.mm.ss");
        Date date = new Date();
        return dateFormat.format(date) + "_" + nameTest + ".png";
    }

    /***
     * It takes screen shot and copies the image in the specified path
     *
     * @param webDriver web driver that need to pass
     * @param path      path where the Image need to store eg:\Spire\resources\sample.jpg
     * @throws Exception generic exception .
     */
    public void getScreenShot(WebDriver webDriver, String path) throws Exception {

        File scrFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
        if (path != null) {
            //The below method will save the screen shot in d drive with name "screenshot.png"
            FileUtils.copyFile(scrFile, new File(path));
        } else {
            FileUtils.copyFile(scrFile, new File(SpireUtilConstants.SCREENSHOT_PATH));
        }

    }

    private static String getPath(String nameTest) throws IOException {
        File directory = new File(".");
        String newFileNamePath = directory.getCanonicalPath() + "/target/sure-reports/screenShots/" + getFileName(nameTest);
        return newFileNamePath;
    }


}

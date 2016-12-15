package com.wooplr.base.driver.web;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;

import com.san.base.util.SpireUtilConstants;
import com.wooplr.base.controller.ContextManager;

/**
 * Created by Vinay Kumar on 29-05-2015.
 */
public class BrowserConfig {

	/**
	 * creates profile and sets the
	 *
	 * @return default profile
	 */
	public static FirefoxProfile getFirfoxProfile() {
		String firefoxProfile = (String) ContextManager.getGlobalContext()
				.getFirefoxProfile();
		ProfilesIni profilesIni = new ProfilesIni();
		return profilesIni.getProfile(firefoxProfile);
	}

	public static ChromeOptions getChromeProfile() {
		String chromeProfilePath = (String) ContextManager.getGlobalContext()
				.getChromeProfilePath();
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("user-data-dir=" + chromeProfilePath);
		return chromeOptions;
	}

	public static FirefoxProfile downloadFxProfile() {
		ProfilesIni allProfiles = new ProfilesIni();
		FirefoxProfile fprofile = allProfiles
				.getProfile((String) ContextManager.getGlobalContext()
						.getFirefoxProfile());
		fprofile.setPreference("browser.download.folderList", 2);
		fprofile.setPreference("browser.download.dir",
				SpireUtilConstants.DOWNLOAD_PATH_PREF);
		// fprofile.setPreference("browser.helperApps.neverAsk.saveToDisk","application/ms-excel");
		fprofile.setPreference("browser.helperApps.neverAsk.saveToDisk",
				"text/csv,application/csv");
		// fprofile.setPreference("browser.helperApps.neverAsk.saveToDisk","application/msword,application/pdf,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/ms-excel,application/zip,text/csv,application/csv");
		fprofile.setAssumeUntrustedCertificateIssuer(false);
		System.out.println("Fire fox profile + " + fprofile.toString());
		return fprofile;

	}
	
	public static ChromeOptions downloadChProfile() {
		//ChromeOptions profile = getChromeProfile();
		ChromeOptions profile = new ChromeOptions();

		profile.addArguments("disable-popup-blocking", "true");
		profile.addArguments("download.default_directory", SpireUtilConstants.DOWNLOAD_PATH_PREF);
		profile.addArguments("download.directory_upgrade", "true");
		profile.addArguments("download.prompt_for_download", "false");
		return profile;
		}

}

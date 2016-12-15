package com.wooplr.base.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import com.san.base.util.FileReader;

public class SpireProperties {

	public static Properties loadEndPointProperties() {

		Properties prop = new Properties();
		InputStream input = null;

		try {

			prop = new FileReader()
					.loadPropertiesFile("./src/main/resources/properties/configEndPoints.properties");

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return prop;

	}
	
	public static Properties loadProperties(String propertiesfilePath) {

		Properties prop = new Properties();
		InputStream input = null;

		try {

			prop = new FileReader()
					.loadPropertiesFile(propertiesfilePath);

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return prop;

	}

	public static Properties loadReportProperties() {

		Properties prop = new Properties();

		try {

			File file = new File("C:\\automation_reports\\report.properties");

			FileInputStream fileInput = new FileInputStream(file);

			prop.load(fileInput);

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return prop;

	}

	public static Properties loadDbProperties() {

		Properties prop = new Properties();
		InputStream input = null;

		try {

			prop = new FileReader()
					.loadPropertiesFile("./src/test/resources/db-config.properties");

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return prop;

	}

	public static String executeQuery(String queryName) {
		Properties prop = new Properties();

		try {
			prop = new FileReader()
					.loadPropertiesFile("./src/test/resources/sqlQueries.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
		String value = prop.getProperty(queryName);

		return value;
	}

	public static void main(String[] args) {

		Properties prop = loadDbProperties();

		Enumeration enuKeys = prop.keys();

		while (enuKeys.hasMoreElements()) {

			String key = (String) enuKeys.nextElement();
			String value = prop.getProperty(key);
			System.out.println(key + ": " + value);
		}

	}

}

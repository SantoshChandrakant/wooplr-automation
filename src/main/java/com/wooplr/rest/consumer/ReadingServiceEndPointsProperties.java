package com.wooplr.rest.consumer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadingServiceEndPointsProperties {
	static Properties serviceEndPointsProp;
	String PROPFILENAME = "services-endpoints.properties";

	public ReadingServiceEndPointsProperties() {
		serviceEndPointsProp = new Properties();
		InputStream input = getClass().getClassLoader().getResourceAsStream(PROPFILENAME);
		if (input != null) {
			try {
				serviceEndPointsProp.load(input);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			try {
				throw new FileNotFoundException("property file '" + PROPFILENAME + "' not found in the classpath");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getServiceEndPoint(String name) {
		if (serviceEndPointsProp == null) {
			new ReadingServiceEndPointsProperties();
		}
		String value = serviceEndPointsProp.getProperty(name);
		return value;
	}
}

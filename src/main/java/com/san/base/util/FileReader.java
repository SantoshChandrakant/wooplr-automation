package com.san.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

public class FileReader {
	
public String getFile(String fileName) throws Exception{
		
		ClassLoader classLoader = this.getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		
		FileInputStream fis = new FileInputStream(file.getPath());
		String StringFromFile = IOUtils.toString(fis, "UTF-8");
		System.out.println(StringFromFile);

				
		return StringFromFile;
	}
	
	public Properties loadPropertiesFile(String fileName) throws IOException{
		
		Properties properties = new Properties();
		
		InputStream inputStream = new FileInputStream(new File(fileName));
		
		if (inputStream!=null) {
			properties.load(inputStream);
		}else{
			throw new FileNotFoundException("property file "+fileName+" not found in classpath");
		}
		
		return properties;
	}

}

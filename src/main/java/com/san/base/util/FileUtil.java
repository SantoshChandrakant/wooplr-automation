package com.san.base.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.wooplr.base.controller.ContextManager;
import com.wooplr.base.helper.FileHelper;
import com.wooplr.base.helper.URLHelper;


public class FileUtil {
	static Logger logger = Logger.getLogger(FileUtil.class);

	public static void copyFile(File srcPath, File dstPath) throws IOException {	
		FileHelper.copyFile(srcPath, dstPath);
	}

	public static void copyFile(String srcPath, String dstPath) throws IOException {
		copyFile(new File(srcPath), new File(dstPath));
	}

	/**
	 * Deletes a Directory with Files
	 * 
	 * @param path
	 * @return
	 */
	public static boolean deleteDirectory(File path) {
		return FileHelper.deleteDirectory(path);
	}

	/**
	 * Get content of an file and apply any changes through the properties. The template file can look like <code>
	 * <pre>
	 * <xmp>
	 * <?xml version="1.0" encoding="utf-8"?>
	 * <GetOfficialTimeRequest xmlns="urn::apis:eBLBaseComponents">
	 * <Version>547</Version>
	 * <RequesterCredentials>
	 * <Username>$username</Username>
	 * <Password>$password</Password>
	 * </RequesterCredentials>
	 * </GetOfficialTimeRequest>
	 * </xmp>
	 * </pre>
	 * </code>
	 * 
	 * Define a Properties object in your test case <code>
	 * <pre>
	 * Properties prop = new Properties();
	 * prop.put("username","apitest1");
	 * prop.put("password","");
	 * </pre>
	 * </code>
	 * 
	 * The output of this method will be <code>
	 * <pre>
	 * <xmp>
	 * <?xml version="1.0" encoding="utf-8"?>
	 * <GetOfficialTimeRequest xmlns="urn::apis:eBLBaseComponents">
	 * <Version>547</Version>
	 * <RequesterCredentials>
	 * <Username>apitest1</Username>
	 * <Password></Password>
	 * </RequesterCredentials>
	 * </GetOfficialTimeRequest>
	 * </xmp>
	 * </pre>
	 * </code>
	 * 
	 * @param TestClass
	 * @param templateName
	 * @param prop
	 * @return
	 */
	public static String getFileContent(Class<?> clazz, String templateName, Map<?, ?> prop) {
		try {
			if (prop == null || prop.isEmpty())
				return FileUtil.readFromFile(clazz.getResourceAsStream(templateName));
			else {
				VelocityEngine ve = new VelocityEngine();
				ve.setProperty("resource.loader", "class");
				ve.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
				ve.init();

				VelocityContext context = null;
				context = new VelocityContext(prop);
				StringWriter writer = new StringWriter();
				templateName = "/" + clazz.getPackage().getName().replace('.', '/') + "/" + templateName;
//				if (ve.mergeTemplate(templateName,"utf-8", context, writer)) {
//					return writer.toString();
//				} else {
//					throw new RuntimeException("Unable to get file content for " + templateName);
//				}
				InputStreamReader reader = new InputStreamReader(clazz.getResourceAsStream(templateName),"UTF-8");
				boolean success = ve.evaluate(context, writer, templateName, reader);
				if (success) {
					return writer.toString();
				} else {
					throw new RuntimeException("Unable to get file content for " + templateName);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

/**
	 * Read contents of a file
	 * 
	 * @param path
	 * @returns content
	 * @throws IOException
	 */
	public static String readFromFile(File path) throws IOException {
		return FileHelper.readFromFile(path);
	}

	/**
	 * Read contents From Stream
	 * 
	 * @param path
	 * @returns content
	 * @throws IOException
	 */
	public static String readFromFile(InputStream path) throws IOException {
		return FileHelper.readFromFile(path);
	}

	/**
	 * Read contents of a file
	 * 
	 * @param path
	 * @returns content
	 * @throws IOException
	 */
	public static String readFromFile(String path) throws IOException {
		return readFromFile(new File(path));
	}	

	/**
	 * Write content to a file. Randomizes the file prefix to avoid collision of test cases when executed in parallel mode.
	 * 
	 * @param fileNameWithExtnPostfix
	 * @param content
	 * @return relative file path
	 */
	public static String saveToFile(String fileNameWithExtnPostfix, String content) {

		String folder = null;
		String extn = fileNameWithExtnPostfix.toLowerCase().split("\\.")[1];
		if (extn.startsWith("htm"))
			folder = "/htmls/";
		else if (extn.startsWith("xml"))
			folder = "/xmls/";
		else if (extn.equals("png") || extn.equals("jpg") || extn.equals("jpeg") || extn.equals("gif"))
			folder = "/screenshots/";
		else
			folder = "/textfiles/";

		String path = folder + URLHelper.getRandomHashCode(folder) + "_" + fileNameWithExtnPostfix;
		try {
			FileUtil.writeToFile(ContextManager.getThreadContext().getOutputDirectory() + path, content);
		} catch (IOException e) {
			throw new RuntimeException("Unable to write file content to " + ContextManager.getThreadContext().getOutputDirectory() + path);
		}
		return ContextManager.getThreadContext().getTestNGContext().getSuite().getName() + path;
	}

	/**
	 * Creates Image from bytes and stores it
	 * 
	 * @param path
	 * @param screenShot
	 */
	public static synchronized void writeImage(String path, byte[] byteArray) {
		FileHelper.writeImage(path, byteArray);
	}

	/**
	 * Write to file
	 * 
	 * @param path
	 * @param is
	 * @throws IOException
	 */
	public static void writeToFile(String path, InputStream is) throws IOException {
		FileHelper.writeToFile(path, is);
	}

	/**
	 * Saves HTML Source
	 * 
	 * @param path
	 * @param selenium
	 * @throws Exception
	 */

	public static void writeToFile(String path, String content) throws IOException {
		FileHelper.writeToFile(path, content);
	}

}

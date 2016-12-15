package com.wooplr.rest.consumer;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import com.wooplr.base.helper.FileHelper;

/***
 * Class to frame all HTTP methods**
 * 
 * @author Santosh
 * 
 */
public abstract class BaseServiceConsumer extends ReadingServiceEndPointsProperties {
	public BaseServiceConsumer() {
		super();
	}

	public enum Headers {
		Cookie
	}

	Logger logger = Logger.getLogger(FileHelper.class);
	String endPointURL = null;
	Invocation.Builder invocationBuilder = null;
	public String PROPERTIES_FILE_NAME = "services-endpoints.properties";
	Properties properties = null;
	protected boolean MULTI_FORM_DATA = false;
	Client client = null;
	MultivaluedMap<String, Object> multivaluedMap;

	/**
	 * create builder instance by service endpoint URL.
	 *
	 * @param url
	 */
	protected void prepareInvocation(String url, String headerKeys[]) {
		logger.info("calling prepareInvocation !!!");
		if (multivaluedMap != null)
			client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
		else
			client = ClientBuilder.newClient();
		client.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true);
		client.register(CustomObjectMapperContextResolver.class);
		WebTarget target = client.target(url);
		invocationBuilder = target.request();
		if (headerKeys != null) {
			logger.info("Loading defaulut headers !!!");
			if (this.properties == null)
				this.properties = readingProperties();
			multivaluedMap = getHeaders(headerKeys);
			this.invocationBuilder = invocationBuilder.headers(multivaluedMap);
		} else {
			logger.info("Headers not required !!!");
			this.invocationBuilder = invocationBuilder.headers(null);
		}
	}

	/**
	 * GET operation call
	 *
	 * @return
	 */
	protected Response executeGET(String URL, String headerKeys[]) {
		prepareInvocation(URL, headerKeys);
		logger.info("calling fireGET !!!");
		return invocationBuilder.get();
	}

	/**
	 * Post operation
	 * 
	 * @param URL
	 * @param HEADERS
	 * @param FORMDATA
	 * @param entity
	 * @return response
	 */
	protected Response executePOST(String URL, String headerKeys[], HashMap<String, String> FORMDATA, Entity<?> ENTITY) {
		prepareInvocation(URL, headerKeys);
		Form form = new Form();
		if (FORMDATA != null) {
			Iterator<Entry<String, String>> it = FORMDATA.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> pair = it.next();
				form.param(pair.getKey(), pair.getValue());
				System.out.println("FormData: " + pair.getKey() + " = " + pair.getValue());
				it.remove();
			}

			return invocationBuilder.post(Entity.form(form));
		} else {
			return invocationBuilder.post(ENTITY);
		}
	}

	/**
	 * DELETE operation call
	 *
	 * @return
	 */
	protected Response executeDELETE(String URL, String[] headerKeys) {
		prepareInvocation(URL, headerKeys);
		logger.info("calling fireDELETE !!!");
		return invocationBuilder.delete();
	}

	/**
	 * PUT operation call with entity
	 *
	 * @param entity
	 * @return
	 */
	protected Response executePUT(String URL, String[] headerKeys, Entity<?> ENTITY) {
		prepareInvocation(URL, headerKeys);
		logger.info("calling firePUT !!!");
		return invocationBuilder.put(ENTITY);
	}

	/**
	 * PATCH operation call with entity
	 *
	 * @param entity
	 * @return
	 */
	protected Response executePATCH(String URL, String[] headerKeys, Entity<?> ENTITY) {
		prepareInvocation(URL, headerKeys);
		logger.info("calling firePATCH !!!");
		return invocationBuilder.method("PATCH", ENTITY);
	}

	/**
	 * Setting headers
	 */
	private MultivaluedMap<String, Object> getHeaders(String headerKeys[]) {
		multivaluedMap = new MultivaluedHashMap<String, Object>();
		System.out.println("HEADERS:");
		for (Object k : headerKeys) {
			String key = (String) k;
			String value = getPropertyValue(key);
			System.out.println(key + ":" + value);

			if (key.contains("Authorization")) {
				key = "Authorization";
				multivaluedMap.putSingle(key, value);
			} else {
				multivaluedMap.putSingle(key, value);
			}
		}
		return multivaluedMap;
	}

	/**
	 * Reading properties file from src/main/resources
	 * 
	 * @return
	 */
	protected Properties readingProperties() {
		InputStream is = null;
		properties = new Properties();
		is = ClassLoader.getSystemResourceAsStream(PROPERTIES_FILE_NAME);
		try {
			properties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	/**
	 * Reading properties file from src/main/resources
	 * 
	 * @return
	 * @throws IOException
	 */
	protected void removingProperty(String key) {
		try {
			// File file = new File("./src/main/resources/" +
			// PROPERTIES_FILE_NAME);
			properties.remove(key);
			// OutputStream out = new FileOutputStream(file);
			// properties.store(out, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Based on property name ,will get the corresponding value from properties
	 * file..
	 * 
	 * @param name
	 * @return
	 */
	private String getPropertyValue(String name) {
		if (properties == null) {
			throw new RuntimeException(" properties instance is not invoked");
		}
		String value = properties.getProperty(name);
		return value;

	}

	/**
	 * Setting properties
	 * 
	 * @param name
	 * @param value
	 */
	public void setPropertyValue(String name, String value) {
		if (properties == null) {
			throw new RuntimeException(" properties instance is not invoked");
		}
		properties.setProperty(name, value);

	}

}

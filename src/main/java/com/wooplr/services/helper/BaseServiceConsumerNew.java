package com.wooplr.services.helper;
/*package com.spire.services.helper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.spire.base.helper.FileHelper;

*//***
 * Class to frame all HTTP methods**
 * 
 * @author Pradeep
 *
 *//*
public abstract class BaseServiceConsumerNew {
	Logger logger = Logger.getLogger(FileHelper.class);
	String endPointURL = null;
	Invocation.Builder invocationBuilder = null;

	*//**
	 * create builder instance by service endpoint URL.
	 *
	 * @param url
	 *//*
	protected void prepareInvocation(String url) {
		logger.info("calling prepareInvocation !!!");
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url);
		invocationBuilder = target.request(MediaType.APPLICATION_JSON_TYPE);
	}

	*//**
	 * GET operation call
	 *
	 * @return
	 *//*
	protected Response fireGET(String URL) {
		prepareInvocation(URL);
		logger.info("calling fireGET !!!");
		return invocationBuilder.get();
	}

	*//**
	 * POST operation call with entity body
	 *
	 * @param entity
	 * @return
	 *//*
	protected Response firePOST(String URL, Entity<?> entity) {
		prepareInvocation(URL);
		logger.info("calling firePOST !!!");
		return invocationBuilder.post(entity);
	}

	*//**
	 * DELETE operation call
	 *
	 * @return
	 *//*
	protected Response fireDELETE(String URL) {
		prepareInvocation(URL);
		logger.info("calling fireDELETE !!!");
		return invocationBuilder.delete();
	}

	*//**
	 * PUT operation call with entity
	 *
	 * @param entity
	 * @return
	 *//*
	protected Response firePUT(String URL, Entity<?> entity) {
		prepareInvocation(URL);
		logger.info("calling firePUT !!!");
		return invocationBuilder.put(entity);
	}
}
*/
package com.wooplr.consumers;

import java.util.HashMap;

import javax.ws.rs.core.Response;

import org.testng.Assert;

import com.google.gson.Gson;
import com.wooplr.base.controller.Logging;
import com.wooplr.base.util.internal.entity.TestObject;
import com.wooplr.rest.consumer.BaseServiceConsumer;
import com.wooplr.rest.consumer.ReadingServiceEndPointsProperties;

/**
 * @author Santosh
 * 
 */
public class FashionLookConsumer extends BaseServiceConsumer {

	Response response = null;
	String host = ReadingServiceEndPointsProperties.getServiceEndPoint("Host");
	String version = ReadingServiceEndPointsProperties.getServiceEndPoint("Version");
	String endpoint = host + "/rest/" + version + "/look".trim();
	Gson gson = new Gson();
	String[] HEADERS = { "Authorization" };

	/**
	 * Post a look
	 * 
	 * @return fashionLookId
	 */
	public String postFashionLook(TestObject data) {

		String serviceEndPoint = this.endpoint;
		System.out.println("Post FashionLook endPointURL  >>>" + serviceEndPoint);

		String formParams[] = data.getTestData().split(",");
		HashMap<String, String> formData = new HashMap<String, String>();
		if (formParams[0] != "null") {
			formData.put("image_pid", formParams[0]);
		}
		if (formParams[1] != "null") {
			formData.put("lookText", formParams[1] + System.currentTimeMillis());
		}
		Response response = executePOST(serviceEndPoint, HEADERS, formData, null);
		System.out.println("Response: " + response.toString());

		if (response.getStatus() == 200) {
			String createdLookId = response.readEntity(String.class);
			System.out.println("RESPONSE: Created FashionLookId: " + createdLookId);
			return createdLookId;
		} else {
			Assert.fail("Failed to Post FashionLook !!! Response Code: " + response.getStatus() + " Message: "
					+ response.getStatusInfo());
			return null;
		}
	}

	/**
	 * Get FashionLookByID
	 * 
	 * @return FashionLookDetails
	 */
	public String getFashionLookById(String lookID, TestObject data) {

		String serviceEndPoint = this.endpoint + "/lookId/" + lookID;
		System.out.println("serviceEndPoint: " + serviceEndPoint);
		String looksPostedByUser = null;
		Response response = executeGET(serviceEndPoint, null);

		if (data.getException().equals("EXCEPTION")) {
			Assert.assertEquals(response.getStatus(), 204,
					"Not showing 204 status code on invalid lookID, returned status code: " + response.getStatus());
		} else {

			if (response.getStatus() == 200) {
				looksPostedByUser = response.readEntity(String.class);
				Logging.log("RESPONSE: " + looksPostedByUser.toString());

				return looksPostedByUser;
			} else {
				Assert.fail("Failed to get Looks by User,, Returned Status Code: " + response.getStatus());
				return null;
			}
		}
		return looksPostedByUser;
	}

	/**
	 * Delete a look
	 * 
	 * @return response
	 */
	public Response deleteFashionLook(String lookID) {

		String serviceEndPoint = this.endpoint + "/id/" + lookID;
		System.out.println("Delete FashionLook endPointURL  >>>" + serviceEndPoint);

		Response response = executeDELETE(serviceEndPoint, HEADERS);
		return response;
	}

	/**
	 * Love a look post and validate by getLookByLookId, Love count should be
	 * increased
	 * 
	 * @return response
	 */
	public Response loveLookPost(String lookID) {

		String serviceEndPoint = this.endpoint + "/id/" + lookID + "/love?legacy=false";
		System.out.println("Love LookPost endPointURL  >>>" + serviceEndPoint);

		Response response = executePOST(serviceEndPoint, HEADERS, null, null);
		System.out.println("STATUS: " + response.getStatus());
		return response;
	}

	public static void main(String[] args) {

	}
}

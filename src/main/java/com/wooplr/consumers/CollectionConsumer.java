//package com.wooplr.consumers;
//
//import java.util.HashMap;
//
//import javax.ws.rs.core.Response;
//
//import org.codehaus.jackson.map.DeserializationConfig.Feature;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.testng.Assert;
//
//import com.google.gson.Gson;
//import com.wooplr.base.util.internal.entity.TestObject;
//import com.wooplr.bean.ThemeCollectionDetailsJAXB;
//import com.wooplr.collection.test.CollectionHelper;
//import com.wooplr.rest.consumer.BaseServiceConsumer;
//import com.wooplr.rest.consumer.ReadingServiceEndPointsProperties;
//import com.wooplr.rest.utils.CollectionFormParams;
//import com.wooplr.rest.utils.Util;
//
//public class CollectionConsumer extends BaseServiceConsumer {
//
//	Response response = null;
//	String host = ReadingServiceEndPointsProperties.getServiceEndPoint("Host");
//	String version = ReadingServiceEndPointsProperties.getServiceEndPoint("Version");
//	String endpoint = host + "/rest/" + version + "/collection";
//	Gson gson = new Gson();
//	String[] HEADERS = { "Authorization" };
//
//	/**
//	 * Create a Collection
//	 * 
//	 * @return collectionId
//	 */
//	public String createCollection(CollectionFormParams requestParams) {
//
//		String serviceEndPoint = this.endpoint;
//		System.out.println("Create Collection endPointURL  >>>" + serviceEndPoint);
//
//		HashMap<String, String> formData = CollectionHelper.setFormData(requestParams);
//
//		Response response = executePOST(serviceEndPoint, HEADERS, formData, null);
//		Util.expliciWait(5);
//
//		if (response.getStatus() == 200) {
//			String createdLookId = response.readEntity(String.class);
//			System.out.println("RESPONSE: Created CollectionId: " + createdLookId);
//			return createdLookId;
//		} else {
//			Assert.fail("Failed to create Collection !!! Response Code: " + response.getStatus() + " Message: "
//					+ response.getStatusInfo());
//			return null;
//		}
//	}
//
//	/**
//	 * Get CollectionByID
//	 * 
//	 * @return CollectionDetails
//	 */
//	public ThemeCollectionDetailsJAXB getCollectionById(String collectionID, TestObject data) {
//
//		String serviceEndPoint = this.endpoint + "/id/" + collectionID;
//		System.out.println("getCollectionById endpointURL: " + serviceEndPoint);
//		String collectionDetails = null;
//		ThemeCollectionDetailsJAXB beanResponse = null;   
//		Response response = executeGET(serviceEndPoint, null);    
//
//		if (data.getException().equals("EXCEPTION")) {
//			Assert.assertEquals(response.getStatus(), 204,
//					"Not showing 204 status code on invalid lookID, returned status code: " + response.getStatus());
//		} else {
//			if (response.getStatus() == 200) {
//				collectionDetails = response.readEntity(String.class);
//				ObjectMapper mapper = new ObjectMapper();
//				mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//				// mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
//				// false);
//				try {
//					beanResponse = mapper.readValue(collectionDetails, ThemeCollectionDetailsJAXB.class);
//					System.out.println("JSON RESPONSE: " + gson.toJson(beanResponse));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return beanResponse;
//			} else {
//				Assert.fail("Failed to get Collection details,, Returned Status Code: " + response.getStatus());
//				return null;
//			}
//		}
//		return beanResponse;
//	}
//
//	/**
//	 * Delete Collection
//	 * 
//	 * @return deleteResponse
//	 */
//	public String deleteCollection(String collectionID) {
//
//		String serviceEndPoint = this.endpoint + "/id/" + collectionID + "?ownerType=USER";
//		System.out.println("Delete Collection endPointURL  >>>" + serviceEndPoint);
//		String deleteCollectionResponse = null;
//
//		Response response = executeDELETE(serviceEndPoint, HEADERS);
//		Util.expliciWait(5);
//
//		if (response.getStatus() == 200) {
//			deleteCollectionResponse = response.readEntity(String.class);
//			System.out.println("RESPONSE: Delete Collection: " + deleteCollectionResponse);
//			return deleteCollectionResponse;
//		} else {
//			Assert.fail("Failed to Delete Collection !!! Response Code: " + response.getStatus() + " Message: "
//					+ response.getStatusInfo());
//		}
//		return deleteCollectionResponse;
//	}
//
//	/**
//	 * Update Collection
//	 * 
//	 * @return updateCollectionResponse
//	 */
//	public String updateCollection(String collectionID, CollectionFormParams requestParams) {
//
//		String serviceEndPoint = this.endpoint + "/id/" + collectionID + "/curatorUpdateCall";
//		System.out.println("Update Collection endPointURL  >>>" + serviceEndPoint);
//		String updateCollectionResponse = null;
//
//		HashMap<String, String> formParamsToUpdate = CollectionHelper.setFormData(requestParams);
//		Response response = executePOST(serviceEndPoint, HEADERS, formParamsToUpdate, null);
//		Util.expliciWait(5);
//
//		if (response.getStatus() == 200) {
//			updateCollectionResponse = response.readEntity(String.class);
//			System.out.println("RESPONSE: Update Collection: " + updateCollectionResponse);
//			return updateCollectionResponse;
//		} else {
//			Assert.fail("Failed to Update Collection !!! Response Code: " + response.getStatus() + " Message: "
//					+ response.getStatusInfo());
//		}
//		return updateCollectionResponse;
//	}
//
//	public String getCol(String collectionID) {
//
//		String serviceEndPoint = this.endpoint + "/id/" + collectionID;
//		System.out.println("getCollectionById endpointURL: " + serviceEndPoint);
//		String collectionDetails = null;
//		ThemeCollectionDetailsJAXB beanResponse = null;
//		Response response = executeGET(serviceEndPoint, null);
//		String strResp = response.readEntity(String.class);
//		System.out.println("strResp: " + strResp);
//		return strResp;
//
//	}
//
//	public Class<?> convertStringToJavaClass(String stringResponse, Object javaBeanClass) {
//
//		ObjectMapper mapper = new ObjectMapper();
//
//		try {
//			javaBeanClass = mapper.readValue(stringResponse, javaBeanClass.getClass());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return (Class<?>) javaBeanClass;
//	}
//
//	public static void main(String[] args) {
//
//		CollectionConsumer consumer = new CollectionConsumer();
//		String stringResponse = consumer.getCol("5303398980124672");
//
//		Class<?> beanResp = consumer.convertStringToJavaClass(stringResponse, ThemeCollectionDetailsJAXB.class);
//		System.out.println("RES: " + beanResp.getName());
//	}
//}

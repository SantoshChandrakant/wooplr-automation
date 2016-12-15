//package com.wooplr.collection.test;
//
//import java.util.HashMap;
//
//import org.testng.Assert;
//
//import com.ubiquity.common.jaxb.themecollection.ThemeCollectionDetailsJAXB;
//import com.wooplr.rest.utils.CollectionFormParams;
//
///**
// * @author Santosh
// *
// */
//public class CollectionHelper {
//
//	/**
//	 * Validate created collection
//	 * 
//	 * @param requestParams
//	 * @param collectionResponse
//	 */
//	public static void validateCreatedCollection(CollectionFormParams requestParams, ThemeCollectionDetailsJAXB collectionResponse) {
//
//		if (requestParams.getName() != null) {
//			Assert.assertTrue(collectionResponse.getName().contains(requestParams.getName()), "Showing wrong CollectionName in response !!");
//		}
//		if (requestParams.getShortName() != null) {
//			Assert.assertEquals(requestParams.getShortName(), collectionResponse.getShortName(), "Showing wrong ShortName in response !!");
//		}
//		if (requestParams.getLongName() != null) {
//			Assert.assertEquals(requestParams.getLongName(), collectionResponse.getLongName(), "Showing wrong LongName in response !!");
//		}
//		if (requestParams.getCollection_type() != null) {
//			Assert.assertEquals(requestParams.getCollection_type(), collectionResponse.getCollection_type(), "Showing wrong CollectionType in response !!");
//		}
//		if (requestParams.getDescription() != null) {
//			Assert.assertEquals(requestParams.getDescription(), collectionResponse.getDescription(), "Showing wrong Description in response !!");
//		}
//		if (requestParams.getBanner_image() != null) {
//			Assert.assertEquals(requestParams.getBanner_image(), collectionResponse.getBanner_image(), "Showing wrong BannerImage in response !!");
//		}
//		if (requestParams.getOwner_type() != null) {
//			Assert.assertEquals(requestParams.getOwner_type(), collectionResponse.getOwner_type(), "Showing wrong ShortName in response !!");
//		}
//
//		Assert.assertNotNull(collectionResponse.getCreateDate(), "Create date is coming as null in Collection!!");
//		Assert.assertNotNull(collectionResponse.getWebLink(), "WebLink is coming as null in Collection!!");
//	}
//
//	/**
//	 * Set the form data in HashMap
//	 * 
//	 * @param data
//	 * @return formData
//	 */
//	public static HashMap<String, String> setFormData(CollectionFormParams data) {
//
//		HashMap<String, String> formData = new HashMap<String, String>();
//
//		if (data.getName() != null) {
//			formData.put("name", data.getName() + System.currentTimeMillis());
//		}
//		if (data.getShortName() != null) {
//			formData.put("shortName", data.getShortName());
//		}
//		if (data.getLongName() != null) {
//			formData.put("longName", data.getLongName());
//		}
//		if (data.getCollection_type() != null) {
//			formData.put("collection_type", data.getCollection_type());
//		}
//		if (data.getDescription() != null) {
//			formData.put("description", data.getDescription());
//		}
//		if (data.getBanner_image() != null) {
//			formData.put("banner_image", data.getBanner_image());
//		}
//		if (data.getOwner_type() != null) {
//			formData.put("owner_type", data.getOwner_type());
//		}
//		return formData;
//	}
//
//	/**
//	 * Set the CollectionFormParam data
//	 * 
//	 * @return collectionFormData
//	 */
//	public static CollectionFormParams getCollectionFormData() {
//		CollectionFormParams collectionFormData = new CollectionFormParams();
//		collectionFormData.setName("auto_" + System.currentTimeMillis());
//		collectionFormData.setShortName("auto_short");
//		collectionFormData.setLongName("auto_long");
//		collectionFormData.setCollection_type("TREND");
//		collectionFormData.setDescription("auto_descr");
//		collectionFormData.setBanner_image("test%2Fc%2Fbntuxdfrfuhussgbaum7");
//		collectionFormData.setOwner_type("CURATOR");
//		return collectionFormData;
//	}
//}

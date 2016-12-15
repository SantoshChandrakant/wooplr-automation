//package com.wooplr.collection.test;
//
//import java.lang.reflect.Method;
//import java.util.Iterator;
//import java.util.LinkedHashMap;
//
//import org.testng.Assert;
//import org.testng.annotations.DataProvider;
//import org.testng.annotations.Test;
//
//import com.san.base.util.WooplrCsvUtil;
//import com.ubiquity.common.jaxb.themecollection.ThemeCollectionDetailsJAXB;
//import com.wooplr.base.controller.TestPlan;
//import com.wooplr.base.controller.TestRetryAnalyzer;
//import com.wooplr.base.util.internal.entity.TestObject;
//import com.wooplr.consumers.CollectionConsumer;
//import com.wooplr.rest.utils.CollectionFormParams;
//
///**
// * @author Santosh C
// * 
// */
//@Test(groups = { "SANITY" }, retryAnalyzer = TestRetryAnalyzer.class)
//public class CollectionTestPlan extends TestPlan {
//
//	final static String DATAPROVIDER_NAME = "FASHION_LOOK";
//	final static String CSV_DIR = "./src/test/java/com/wooplr/collection/test/";
//	final static String CSV_FILENAME = "Collection_TestData.csv";
//	final static String CSV_PATH = CSV_DIR + CSV_FILENAME;
//	CollectionConsumer collectionConsumer = new CollectionConsumer();
//
//	@DataProvider(name = DATAPROVIDER_NAME)
//	public static Iterator<Object[]> getInfo(Method method) {
//		Iterator<Object[]> objectsFromCsv = null;
//		try {
//			String fileName = CSV_PATH;
//			LinkedHashMap<String, Class<?>> entityClazzMap = new LinkedHashMap<String, Class<?>>();
//			LinkedHashMap<String, String> methodFilter = new LinkedHashMap<String, String>();
//			methodFilter.put(TestObject.TEST_TITLE, method.getName());
//			entityClazzMap.put("TestObject", TestObject.class);
//			entityClazzMap.put("CollectionFormParams", CollectionFormParams.class);
//
//			objectsFromCsv = WooplrCsvUtil.getObjectsFromCsv(CollectionTestPlan.class, entityClazzMap, fileName, null,
//					methodFilter);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return objectsFromCsv;
//	}
//
//	/**
//	 * Create a Collection and validate it is returning collectionID
//	 * 
//	 * @param data
//	 */
//	@Test(groups = { "createCollection", "Sanity" }, dataProvider = DATAPROVIDER_NAME)
//	public void createCollection(TestObject testData, CollectionFormParams requestParams) {
//
//		String collectionID = collectionConsumer.createCollection(requestParams);
//
//		ThemeCollectionDetailsJAXB collectionResponse = collectionConsumer.getCollectionById(collectionID, testData);
//		CollectionHelper.validateCreatedCollection(requestParams, collectionResponse);
//
//	}
//
//	/**
//	 * Create a Collection and get Collection details by collectionID, validate
//	 * it should return collection details
//	 * 
//	 * @param data
//	 */
//	@Test(groups = { "getCollection", "Sanity" }, dataProvider = DATAPROVIDER_NAME)
//	public void getCollection(TestObject testData, CollectionFormParams requestParams) {
//
//		String collectionID = collectionConsumer.createCollection(requestParams);
//		ThemeCollectionDetailsJAXB collectionResponse = collectionConsumer.getCollectionById(collectionID, testData);
//		CollectionHelper.validateCreatedCollection(requestParams, collectionResponse);
//	}
//
//	/**
//	 * Get Collection by Invalid CollectionId, it should throw 204 status
//	 * 
//	 * @param data
//	 */
//	@Test(groups = { "getByInvalidCollectionID", "Sanity" }, dataProvider = DATAPROVIDER_NAME)
//	public void getByInvalidCollectionID(TestObject testData, CollectionFormParams requestParams) {
//
//		collectionConsumer.getCollectionById("123456789", testData);
//	}
//
//	/**
//	 * Create a Collection and delete it by collectionID, and validate it by
//	 * getByCollectionID
//	 * 
//	 * @param data
//	 */
//	@Test(groups = { "deleteCollection", "Sanity" }, dataProvider = DATAPROVIDER_NAME)
//	public void deleteCollection(TestObject testData, CollectionFormParams requestParams) {
//
//		String collectionID = collectionConsumer.createCollection(requestParams);
//		System.out.println("collectionID: " + collectionID);
//
//		String delResp = collectionConsumer.deleteCollection(collectionID);
//		System.out.println("delResp: " + delResp);
//		Assert.assertEquals(delResp, "true", "Not returning as TRUE after deleting a collection!!");
//
//		collectionConsumer.getCollectionById(collectionID, testData);
//	}
//
//	/**
//	 * Create a Collection and update the collection and verify updation by
//	 * getByCollectionID, all fields should be updated
//	 * 
//	 * @param data
//	 */
//	@Test(groups = { "updateCollection", "Sanity" }, dataProvider = DATAPROVIDER_NAME)
//	public void updateCollection(TestObject testData, CollectionFormParams requestParams) {
//
//		CollectionFormParams requestFormParams = CollectionHelper.getCollectionFormData();
//		String collectionID = collectionConsumer.createCollection(requestFormParams);
//		System.out.println("collectionID: " + collectionID);
//
//		String updateResponse = collectionConsumer.updateCollection(collectionID, requestParams);
//		Assert.assertEquals(updateResponse, "true", "Not returning as TRUE after updating a collection!!");
//
//		ThemeCollectionDetailsJAXB responseAfterUpdate = collectionConsumer.getCollectionById(collectionID, testData);
//		CollectionHelper.validateCreatedCollection(requestParams, responseAfterUpdate);
//
//		collectionConsumer.deleteCollection(collectionID);
//	}
//}
package com.wooplr.fashionlook.test;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.ws.rs.core.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.san.base.util.WooplrCsvUtil;
import com.wooplr.base.controller.TestPlan;
import com.wooplr.base.controller.TestRetryAnalyzer;
import com.wooplr.base.util.internal.entity.TestObject;
import com.wooplr.consumers.FashionLookConsumer;

/**
 * @author Santosh C
 * 
 */
@Test(groups = { "SANITY" }, retryAnalyzer = TestRetryAnalyzer.class)
public class FashionLookTestPlan extends TestPlan {

	final static String DATAPROVIDER_NAME = "FASHION_LOOK";
	final static String CSV_DIR = "./src/test/java/com/wooplr/fashionlook/test/";
	final static String CSV_FILENAME = "FashionLook_TestData.csv";
	final static String CSV_PATH = CSV_DIR + CSV_FILENAME;
	FashionLookConsumer fashionLookConsumer = new FashionLookConsumer();

	@DataProvider(name = DATAPROVIDER_NAME)
	public static Iterator<Object[]> getInfo(Method method) {
		Iterator<Object[]> objectsFromCsv = null;
		try {
			String fileName = CSV_PATH;
			LinkedHashMap<String, Class<?>> entityClazzMap = new LinkedHashMap<String, Class<?>>();
			LinkedHashMap<String, String> methodFilter = new LinkedHashMap<String, String>();
			methodFilter.put(TestObject.TEST_TITLE, method.getName());
			entityClazzMap.put("TestObject", TestObject.class);

			objectsFromCsv = WooplrCsvUtil.getObjectsFromCsv(FashionLookTestPlan.class, entityClazzMap, fileName, null,
					methodFilter);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectsFromCsv;
	}

	/**
	 * Post a FashionLook and validate it is returning LookID
	 * 
	 * @param data
	 */
	@Test(groups = { "postFashionLook", "Sanity" }, dataProvider = DATAPROVIDER_NAME)
	public void postFashionLook(TestObject data) {

		Assert.assertNotNull(fashionLookConsumer.postFashionLook(data),
				"PostLook Service is returning null value after posting a look");
	}

	/**
	 * Create a FashionLook and validate by getByLookID
	 * 
	 * @param data
	 */
	@Test(groups = { "getFashionLookById", "Sanity" }, dataProvider = DATAPROVIDER_NAME)
	public void getFashionLookById(TestObject data) {

		String lookID = fashionLookConsumer.postFashionLook(data);
		String getByLookIdResponse = fashionLookConsumer.getFashionLookById(lookID, data);

		String formParams[] = data.getTestData().split(",");

		Assert.assertTrue(getByLookIdResponse.contains(lookID), "Not showing given lookID details!!");
		Assert.assertTrue(getByLookIdResponse.contains(formParams[0]), "Not showing look_PID in response!!");
		Assert.assertTrue(getByLookIdResponse.contains(formParams[1]), "Not showing look_Text in response!!");
	}

	/**
	 * Try to get FashionLook by invalid lookID, it should throw proper
	 * exception
	 * 
	 * @param data
	 */
	@Test(groups = { "getFashionLookByInvalidId", "Sanity" }, dataProvider = DATAPROVIDER_NAME)
	public void getFashionLookByInvalidId(TestObject data) {

		fashionLookConsumer.getFashionLookById(Long.toString(System.currentTimeMillis()), data);
	}

	/**
	 * Create a FashionLook and delete it by LookId and validate deletion by
	 * getByLookID
	 * 
	 * @param data
	 */
	@Test(groups = { "deleteFashionLook", "Sanity" }, dataProvider = DATAPROVIDER_NAME)
	public void deleteFashionLook(TestObject data) {

		String fashionLookId = fashionLookConsumer.postFashionLook(data);
		Assert.assertNotNull(fashionLookId, "Failed to create FashionLook");

		fashionLookConsumer.deleteFashionLook(fashionLookId);
		fashionLookConsumer.getFashionLookById(fashionLookId, data);
	}

	/**
	 * LoveLookPost and validate by getByLookID
	 * 
	 * @param data
	 */
	@Test(groups = { "loveLookPost", "Sanity" }, dataProvider = DATAPROVIDER_NAME)
	public void loveLookPost(TestObject data) {

		String fashionLookId = fashionLookConsumer.postFashionLook(data);
		Assert.assertNotNull(fashionLookId, "Failed to create FashionLook");

		Response response = fashionLookConsumer.loveLookPost(fashionLookId);
		String loveLookPostResponse = response.readEntity(String.class);
		System.out.println("RESPONSE: " + loveLookPostResponse);

		Document doc = Jsoup.parse(loveLookPostResponse);
		doc.select("a").first();
		// link.getElementsByAttribute("");
	}
}
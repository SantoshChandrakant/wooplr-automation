package com.wooplr.challenge.test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.san.base.util.WooplrCsvUtil;
import com.wooplr.base.controller.TestPlan;
import com.wooplr.base.controller.TestRetryAnalyzer;
import com.wooplr.base.util.internal.entity.TestObject;
import com.wooplr.bean.Acknowledge;
import com.wooplr.bean.ChallengeJAXB;
import com.wooplr.bean.ChallengeResponse;
import com.wooplr.bean.ListChallenges;
import com.wooplr.bean.Response;
import com.wooplr.consumers.ChallengeConsumer;
import com.wooplr.consumers.ChallengeConsumer.ResellerActions;
  
/**
 * @author Santosh C
 * 
 */
@Test(groups = { "SANITY" }, retryAnalyzer = TestRetryAnalyzer.class)
public class ChallengeTestPlan extends TestPlan {

	final static String DATAPROVIDER_NAME = "CHALLENGE";
	final static String CSV_DIR = "./src/test/java/com/wooplr/challenge/test/";
	final static String CSV_FILENAME = "Challenge_TestData.csv";
	final static String CSV_PATH = CSV_DIR + CSV_FILENAME;
	ChallengeConsumer consumer = new ChallengeConsumer();
	Gson gson = new Gson();

	@DataProvider(name = DATAPROVIDER_NAME)
	public static Iterator<Object[]> getInfo(Method method) {
		Iterator<Object[]> objectsFromCsv = null;
		try {
			String fileName = CSV_PATH;
			LinkedHashMap<String, Class<?>> entityClazzMap = new LinkedHashMap<String, Class<?>>();
			LinkedHashMap<String, String> methodFilter = new LinkedHashMap<String, String>();
			methodFilter.put(TestObject.TEST_TITLE, method.getName());
			entityClazzMap.put("TestObject", TestObject.class);
			entityClazzMap.put("ChallengeJAXB", ChallengeJAXB.class);

			objectsFromCsv = WooplrCsvUtil.getObjectsFromCsv(ChallengeTestPlan.class, entityClazzMap, fileName, null, methodFilter);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectsFromCsv;
	}

	/**
	 * Create a Challenge
	 * 
	 * @param data
	 */
	@Test(groups = { "createChallenge", "Sanity" }, dataProvider = DATAPROVIDER_NAME)
	public void createChallenge(TestObject testData, ChallengeJAXB request) {

		ChallengeResponse response = consumer.createChallenge(request);
		String challengeId = response.getResponse().getId();
		ChallengeHelper.validatechallenge(request, response);

		consumer.deleteChallenge(challengeId);
	}

	/**
	 * Get Challenge
	 * 
	 * @param data
	 */
	@Test(groups = { "getChallenge", "Sanity" }, dataProvider = DATAPROVIDER_NAME)
	public void getChallenge(TestObject testData, ChallengeJAXB request) {
		ChallengeResponse response = consumer.createChallenge(request);
		String challengeId = response.getResponse().getId();
		consumer.getChallenge(challengeId);

		consumer.deleteChallenge(challengeId);
	}

	/**
	 * Delete Challenge
	 * 
	 * @param data
	 */
	@Test(groups = { "deleteChallenge", "Sanity" }, dataProvider = DATAPROVIDER_NAME)
	public void deleteChallenge(TestObject testData, ChallengeJAXB request) {

		ChallengeResponse response = consumer.createChallenge(request);
		String challengeId = response.getResponse().getId();
		System.out.println(challengeId);

		Acknowledge ack = consumer.deleteChallenge(challengeId);
		Assert.assertTrue(ack.getStatus(), "Response status should be TRUE");
		Assert.assertEquals("Challenge successfully deleted", ack.getReason(), "Showing wrong acknowledgement msg");

		ChallengeResponse deleted = consumer.getChallenge(challengeId);
		Assert.assertFalse(Boolean.getBoolean(deleted.getStatus()), "After delete, response is showing as true");
		Assert.assertEquals("No challenge exist for given Id", deleted.getReason(), "Showing wrong acknowledgement msg");
	}
   
	/**
	 * Make Challenge as live and not live
	 * 
	 * @param data
	 */
	@Test(groups = { "makeChallengeLive", "Sanity" }, dataProvider = DATAPROVIDER_NAME)
	public void makeChallengeLive(TestObject testData, ChallengeJAXB request) {

		ChallengeResponse response = consumer.createChallenge(request);
		String challengeId = response.getResponse().getId();
		Assert.assertEquals(response.getResponse().getIsLive(), "false", "By default challenge is not creating as isLive as false !!");

		consumer.makeChallengeLive(challengeId, "true");
		ChallengeResponse challengeResponse = consumer.getChallenge(challengeId);
		Assert.assertEquals("true", challengeResponse.getResponse().getIsLive(), "Unable to make challenge as Live !!");
		ChallengeHelper.validatechallenge(request, challengeResponse);

		consumer.makeChallengeLive(challengeId, "false");
		ChallengeResponse challengeResponse2 = consumer.getChallenge(challengeId);
		Assert.assertEquals("false", challengeResponse2.getResponse().getIsLive(), "Unable to make challenge notLive to Live !!");
		ChallengeHelper.validatechallenge(request, challengeResponse2);

		consumer.deleteChallenge(challengeId);
	}

	/**
	 * Update a Challenge
	 * 
	 * @param data
	 */
	@Test(groups = { "updateChallenge", "Sanity" }, dataProvider = DATAPROVIDER_NAME)
	public void updateChallenge(TestObject testData, ChallengeJAXB update) {

		ChallengeJAXB createChallenge = ChallengeHelper.getChallengeData();
		ChallengeResponse challenge = consumer.createChallenge(createChallenge);
		String challengeId = challenge.getResponse().getId();
		ChallengeHelper.validatechallenge(createChallenge, challenge);

		ChallengeJAXB updateBean = ChallengeHelper.getUpdateData(createChallenge, update);
		updateBean.setId(Long.parseLong(challengeId));

		System.out.println("UPDATE Request: " + gson.toJson(updateBean));

		consumer.updateChallenge(updateBean);
		ChallengeResponse afterUpdate = consumer.getChallenge(challengeId);

		ChallengeHelper.validatechallenge(updateBean, afterUpdate);

		consumer.deleteChallenge(challengeId);
	}

	/**
	 * List Challenge by giving limit
	 * 
	 * @param testData
	 * @param update
	 */
	@Test(groups = { "listChallenge", "Sanity" }, dataProvider = DATAPROVIDER_NAME)
	public void listChallenge(TestObject testData, ChallengeJAXB update) {
		ListChallenges challenegList = new ListChallenges();

		for (int i = 1; i < 15; i++) {
			challenegList = consumer.listChallenges(String.valueOf(i));
			Assert.assertEquals(challenegList.getResponse().length, i, "Not showing given number of challenges");
		}
		challenegList = consumer.listChallenges(null);
		Assert.assertEquals(challenegList.getResponse().length, 10, "10 challenges should be displayed if we dont give count!!");
	}

	/**
	 * Validate challenges are displayed in descending order and recent created
	 * challenge should be displayed at first in list
	 * 
	 * @param testData
	 * @param update
	 */
	@Test(groups = { "validateListChallenge", "Sanity" }, dataProvider = DATAPROVIDER_NAME)
	public void validateListChallenge(TestObject testData, ChallengeJAXB csv) {

		ChallengeResponse challenge = consumer.createChallenge(ChallengeHelper.getChallengeData());
		String challengeId = challenge.getResponse().getId();

		Response[] challenegList = consumer.listChallenges(null).getResponse();
		String id1 = challenegList[0].getId();

		Assert.assertEquals(id1, challengeId, "Recently created challenge not showing at first in list !!");
		consumer.deleteChallenge(challengeId);
	}

	/**
	 * Validate challenges are displayed in descending order and recent created
	 * challenge should be displayed at first in list
	 * 
	 * @param testData
	 * @param update
	 */
	@Test(groups = { "listChallengeByAction", "Sanity" }, dataProvider = DATAPROVIDER_NAME)
	public void listChallengeByAction(TestObject testData, ChallengeJAXB csv) {

		ChallengeResponse challenge = consumer.createChallenge(ChallengeHelper.getChallengeData(testData.getTestData()));
		String challengeId = challenge.getResponse().getId();
		System.out.println(challengeId);

		ListChallenges listByAction = consumer.listChallengeByAction(testData.getTestData());
		Response[] response = listByAction.getResponse();
		List<String> cids = new ArrayList<String>();
		for (Response response2 : response) {
			cids.add(response2.getId());
			ChallengeHelper.validateTimeStamp(response2.getStartDate(), response2.getEndDate(), testData.getTestData());
		}
		Assert.assertTrue(cids.contains(challengeId), "Created a challenge as PARTICIPATED, but it is not listed,, challengeId: " + challengeId);
		consumer.deleteChallenge(challengeId);
	}

	/**
	 * Create a challenge, make it live,, initiate and complete the challenge,
	 * it should list in adminReviews
	 * 
	 */
	@Test(groups = { "challengeFlowTest", "Sanity" }, dataProvider = DATAPROVIDER_NAME)
	public void challengeFlowTest(TestObject testData, ChallengeJAXB csv) {

		ChallengeResponse challenge = consumer.createChallenge(csv);
		String challengeId = challenge.getResponse().getId();
		System.out.println(challengeId);

		consumer.makeChallengeLive(challengeId, "true");
		ChallengeResponse initiateResp = consumer.resellerAction(challengeId, ResellerActions.Initiate);
		Assert.assertNotNull(initiateResp.getResponse().getStatus(), "Status coming null after challenge initiated by reseller");
		Assert.assertEquals("PENDING", initiateResp.getResponse().getStatus(), "Status not changing to PENDING after challenge is initiated by reseller");

		ChallengeResponse completeResp = consumer.resellerAction(challengeId, ResellerActions.Complete);
		Assert.assertNotNull(completeResp.getResponse().getStatus(), "Status coming null after challenge completed by reseller");
		Assert.assertEquals("INCOMPLETE", completeResp.getResponse().getStatus(), "Status not changing to INCOMPLETE after challenge is initiated by reseller");

		ListChallenges challengeList = consumer.listAdminReviews();
		Assert.assertNotNull(challengeList.getResponse(), "ListAdminReviews is giving response as null !!");

		Response[] list = challengeList.getResponse();
		List<String> challengeIds = new ArrayList<String>();
		for (Response response : list) {
			Assert.assertEquals(response.getStatus(), "INCOMPLETE", "Showing wrong status challenges for admin to review");
			challengeIds.add(response.getId());
		}
		Assert.assertTrue(challengeIds.contains(challengeId), "Reseller Completed the challenge but it is not visible to the admin,, challengeId: " + challengeId);
		consumer.deleteChallenge(challengeId);
	}

}
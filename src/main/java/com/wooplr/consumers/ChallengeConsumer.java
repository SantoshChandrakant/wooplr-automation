package com.wooplr.consumers;

import java.util.HashMap;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.gson.Gson;
import com.wooplr.bean.*;
import com.wooplr.rest.consumer.BaseServiceConsumer;
import com.wooplr.rest.consumer.ReadingServiceEndPointsProperties;

public class ChallengeConsumer extends BaseServiceConsumer {

	Response response = null;
	String host = ReadingServiceEndPointsProperties.getServiceEndPoint("Host");
	String version = ReadingServiceEndPointsProperties.getServiceEndPoint("Version");
	String endpoint = "http://sprint33.woopler100.appspot.com/rest/v2";
	Gson gson = new Gson();
	String[] HEADERS = { "Authorization" };
	String[] USER_AUTH = { "User_Authorization", "Content-Length" };
	ChallengeResponse challengeResponse = new ChallengeResponse();
	ObjectMapper mapper = new ObjectMapper();
	Acknowledge acknowledge = new Acknowledge();
   
	public enum ResellerActions {
		Initiate, Complete   
	}   
       
	/**
	 * Create a Challenge
	 * 
	 * @return challengeResponse
	 */
	public ChallengeResponse createChallenge(ChallengeJAXB challenge) {

		String serviceEndPoint = this.endpoint + "/challenge/save";
		System.out.println("Create Challenge endPointURL  >>>" + serviceEndPoint);

		challenge.setStartDate(1480530600000L);
		challenge.setEndDate(1580530600000L);
		System.out.println("Request: " + gson.toJson(challenge));
		Response response = executePOST(serviceEndPoint, USER_AUTH, null, Entity.entity(challenge, MediaType.APPLICATION_JSON));
		String strResponse = response.readEntity(String.class);
		System.out.println("RESPONSE: CreateChallenge : " + strResponse);

		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			challengeResponse = mapper.readValue(strResponse, ChallengeResponse.class);
			System.out.println("JSON RESPONSE: " + gson.toJson(challengeResponse));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return challengeResponse;
	}

	/**
	 * Update a challenge
	 * 
	 * @param challenge
	 * @return updatedChallenge
	 */
	public ChallengeResponse updateChallenge(ChallengeJAXB challenge) {
		String serviceEndPoint = this.endpoint + "/challenge/save";
		System.out.println("Update Challenge endPointURL  >>>" + serviceEndPoint);
		System.out.println("Request: " + gson.toJson(challenge));
		Response response = executePOST(serviceEndPoint, USER_AUTH, null, Entity.entity(challenge, MediaType.APPLICATION_JSON));
		String strResponse = response.readEntity(String.class);
		System.out.println("RESPONSE: UpdateChallenge : " + strResponse);

		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			challengeResponse = mapper.readValue(strResponse, ChallengeResponse.class);
			System.out.println("JSON RESPONSE: " + gson.toJson(challengeResponse));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return challengeResponse;

	}

	/**
	 * Get challenge by challengeId
	 * 
	 * @param challengeId
	 * @return challenge
	 */
	public ChallengeResponse getChallenge(String challengeId) {

		String serviceEndPoint = this.endpoint + "/challenge/" + challengeId;
		System.out.println("Get Challenge endPointURL  >>>" + serviceEndPoint);
		Response response = executeGET(serviceEndPoint, null);
		String strResponse = response.readEntity(String.class);
		System.out.println("RESPONSE: GetChallenge : " + strResponse);

		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			challengeResponse = mapper.readValue(strResponse, ChallengeResponse.class);
			System.out.println("JSON RESPONSE: " + gson.toJson(challengeResponse));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return challengeResponse;
	}

	/**
	 * Delete challenge
	 * 
	 * @param challengeId
	 * @return acknowledge
	 */
	public Acknowledge deleteChallenge(String challengeId) {

		String serviceEndPoint = this.endpoint + "/challenge/" + challengeId;
		System.out.println("Delete Challenge endPointURL  >>>" + serviceEndPoint);
		Response response = executeDELETE(serviceEndPoint, USER_AUTH);
		String strResponse = response.readEntity(String.class);

		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			acknowledge = mapper.readValue(strResponse, Acknowledge.class);
			System.out.println("JSON RESPONSE: " + gson.toJson(acknowledge));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return acknowledge;
	}

	/**
	 * Make a challenge Live
	 * 
	 * @param challengeId
	 * @param makeLive
	 * @return acknowledge
	 */
	public Acknowledge makeChallengeLive(String challengeId, String makeLive) {

		String serviceEndPoint = this.endpoint + "/challenge/" + challengeId + "/live";
		System.out.println("MakeChallengeLive endPointURL  >>>" + serviceEndPoint);

		HashMap<String, String> FORMDATA = new HashMap<String, String>();
		FORMDATA.put("status", makeLive);

		Response response = executePOST(serviceEndPoint, USER_AUTH, FORMDATA, null);
		String strResponse = response.readEntity(String.class);
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			acknowledge = mapper.readValue(strResponse, Acknowledge.class);
			System.out.println("JSON RESPONSE: " + gson.toJson(acknowledge));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return acknowledge;
	}

	/**
	 * List challenges
	 * 
	 * @return challengeList
	 */
	public ListChallenges listChallenges(String count) {

		String serviceEndPoint = this.endpoint + "/challenge/admin/list";
		System.out.println("ListChallenges endPointURL >>>" + serviceEndPoint);
		ListChallenges challengeList = new ListChallenges();

		HashMap<String, String> FORMDATA = new HashMap<String, String>();
		if (count != null) {
			FORMDATA.put("count", count);
		}
		Response response = executePOST(serviceEndPoint, USER_AUTH, FORMDATA, null);
		String strResponse = response.readEntity(String.class);
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			challengeList = mapper.readValue(strResponse, ListChallenges.class);
			System.out.println("JSON RESPONSE: " + gson.toJson(challengeList));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return challengeList;
	}

	/**
	 * List challenge by Action [CURRENTGOING, UPCOMING and PARTICIPATED]
	 * 
	 * @return challengeList
	 */
	public ListChallenges listChallengeByAction(String actionType) {

		String serviceEndPoint = this.endpoint + "/reseller/challenge/list?type=" + actionType;
		System.out.println("ListChallenges endPointURL >>>" + serviceEndPoint);

		ListChallenges challengeList = new ListChallenges();
		Response response = executeGET(serviceEndPoint, USER_AUTH);
		String strResponse = response.readEntity(String.class);
		System.out.println("STR: " + strResponse);
		try {
			challengeList = mapper.readValue(strResponse, ListChallenges.class);
			System.out.println("JSON RESPONSE: " + gson.toJson(challengeList));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return challengeList;
	}

	/**
	 * Reseller can Initiate/Complete a challenge
	 * 
	 * @return challenge
	 */
	public ChallengeResponse resellerAction(String challengeId, ResellerActions action) {

		String serviceEndPoint = this.endpoint + "/reseller/challenge/" + challengeId;
		System.out.println("ResellerAction endPointURL >>>" + serviceEndPoint);

		HashMap<String, String> FORMDATA = new HashMap<String, String>();
		if (action != null) {
			FORMDATA.put("action", action.toString());
		}
		ChallengeResponse challenge = new ChallengeResponse();
		Response response = executePOST(serviceEndPoint, USER_AUTH, FORMDATA, null);
		String strResponse = response.readEntity(String.class);
		System.out.println("STR: " + strResponse);

		try {
			challenge = mapper.readValue(strResponse, ChallengeResponse.class);
			System.out.println("JSON RESPONSE: " + gson.toJson(challenge));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return challenge;
	}

	/**
	 * List all challenges which are in status INCOMPLETE
	 * 
	 * @return challenge
	 */
	public ListChallenges listAdminReviews() {

		String serviceEndPoint = this.endpoint + "/challenge/admin/reviews";
		System.out.println("ResellerAction endPointURL >>>" + serviceEndPoint);

		ListChallenges challengeList = new ListChallenges();
		Response response = executePOST(serviceEndPoint, USER_AUTH, null, null);
		String strResponse = response.readEntity(String.class);
		System.out.println("STR: " + strResponse);

		try {
			challengeList = mapper.readValue(strResponse, ListChallenges.class);
			System.out.println("JSON RESPONSE: " + gson.toJson(challengeList));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return challengeList;
	}

	public static void main(String[] args) {

		/*ChallengeConsumer consumer = new ChallengeConsumer();
		com.ubiquity.common.jaxb.challenge.Response[] resp = consumer.listChallenges("1000").getResponse();
		for (com.ubiquity.common.jaxb.challenge.Response response : resp) {
			System.out.println(response.getId());
			consumer.deleteChallenge(response.getId());*/
		}

		// Gson gson = new Gson();
		// System.out.println(gson.toJson(consumer.listAdminReviews()));
	}


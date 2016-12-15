package com.wooplr.challenge.test;

import java.util.Calendar;

import org.testng.Assert;

import com.wooplr.bean.ChallengeJAXB;
import com.wooplr.bean.ChallengeResponse;


/**
 * @author Santosh
 *
 */
public class ChallengeHelper {

	public static void validatechallenge(ChallengeJAXB request, ChallengeResponse response) {

		Assert.assertNotNull(response.getResponse(), "Response body is coming null !!");
		Assert.assertNotNull(response.getResponse().getId(), "ChallengeID coming null in createChallenge resposne !!");
		Assert.assertNotNull(response.getResponse().getCreateDate(), "CurrentDate coming null in createChallenge resposne !!");
		Assert.assertNotNull(response.getResponse().getStartDate(), "StartDate coming null in createChallenge resposne !!");
		Assert.assertNotNull(response.getResponse().getEndDate(), "EndDate coming null in createChallenge resposne !!");

		if (request.getTitle() != null) {   
			Assert.assertNotNull(response.getResponse().getTitle(), "Title coming null in createChallenge resposne !!");
			Assert.assertEquals(request.getTitle(), response.getResponse().getTitle(), "Showing wrong title in createChallenge response !! ");
		}
		if (request.getDescription() != null) {
			Assert.assertNotNull(response.getResponse().getDescription(), "Description coming null in createChallenge resposne !!");
			Assert.assertEquals(request.getDescription(), response.getResponse().getDescription(), "Showing wrong Description in createChallenge response !! ");
		}
		if (request.getChallengeType() != null) {
			Assert.assertNotNull(response.getResponse().getChallengeType(), "ChallengeType is showing as null in response !!");
			Assert.assertEquals(request.getChallengeType(), response.getResponse().getChallengeType(), "Not showing challengeType as RESELLER !! ");
		}
		if (request.getImageUrl() != null) {
			Assert.assertNotNull(response.getResponse().getImageUrl(), "ImageUrl coming null in createChallenge resposne !!");
			Assert.assertEquals(request.getImageUrl(), response.getResponse().getImageUrl(), "Showing wrong ImageUrl in createChallenge response !! ");
		}
		if (request.getBonusKey() != null) {
			Assert.assertNotNull(response.getResponse().getBonusKey(), "BonusKey coming null in createChallenge resposne !!");
			Assert.assertEquals(request.getBonusKey(), response.getResponse().getBonusKey(), "Showing wrong BonusKey in createChallenge response !! ");
		}
		if (request.getBonusValue() != null) {
			Assert.assertNotNull(response.getResponse().getBonusValue(), "BonusValue coming null in createChallenge resposne !!");
			Assert.assertEquals(request.getBonusValue(), response.getResponse().getBonusValue(), "Showing wrong BonusValue in createChallenge response !! ");
		}
		if (request.getTnc() != null) {
			Assert.assertNotNull(response.getResponse().getTnc(), "Terms&Conditions coming null in createChallenge resposne !!");
			Assert.assertEquals(request.getTnc(), response.getResponse().getTnc(), "Showing wrong Terms&Conditions in createChallenge response !! ");
		}
	}

	public static ChallengeJAXB getChallengeData() {
		ChallengeJAXB challenge = new ChallengeJAXB();
		challenge.setTitle("Sell FlyingMachine Products");
		challenge.setDescription("Sell 100 products in 1month");
		challenge.setImageUrl("https://www.google.co.in/imgres?imgurl");
		challenge.setBonusKey("AMOUNT");
		challenge.setBonusValue("9000");
		challenge.setIsLive(true);
		return challenge;
	}

	public static ChallengeJAXB getChallengeData(String challengeType) {
		ChallengeJAXB challenge = new ChallengeJAXB();

		if (challengeType.equals("PARTICIPATED")) {
			challenge.setStartDate(ChallengeHelper.getTimeStamp(-10));
			challenge.setEndDate(ChallengeHelper.getTimeStamp(-5));
			challenge.setTitle("Participated Challenge1");
			challenge.setDescription("Participated Challenge1");
		}
		if (challengeType.equals("CURRENTGOING")) {
			challenge.setStartDate(ChallengeHelper.getTimeStamp(-10));
			challenge.setEndDate(ChallengeHelper.getTimeStamp(5));
			challenge.setTitle("CurrentGoing Challenge1");
			challenge.setDescription("CurrentGoing Challenge1");
		}
		if (challengeType.equals("UPCOMING")) {
			challenge.setStartDate(ChallengeHelper.getTimeStamp(10));
			challenge.setEndDate(ChallengeHelper.getTimeStamp(5));
			challenge.setTitle("Upcoming Challenge1");
			challenge.setDescription("Upcoming Challenge1");
		}

		challenge.setImageUrl("https://www.google.co.in/imgres?imgurl");
		challenge.setBonusKey("AMOUNT");
		challenge.setBonusValue("9000");
		challenge.setIsLive(true);
		return challenge;
	}

	/**
	 * Get timestamp after/before of given number of days
	 * 
	 * @param when
	 * @return timestamp
	 */
	public static Long getTimeStamp(int when) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, when);
		long timestamp = cal.getTimeInMillis() / 1000;
		return timestamp;
	}

	public static void validateTimeStamp(String startDate, String endDate, String actionType) {
		Calendar cal = Calendar.getInstance();
		long timestamp = cal.getTimeInMillis() / 1000;
		if (actionType.equals("PARTICIPATED")) {
			Assert.assertTrue(Long.parseLong(startDate) < timestamp, "Showing wrong entries in ListChallenges by PARTICIPATED");
			Assert.assertTrue(Long.parseLong(endDate) < timestamp, "Showing wrong entries in ListChallenges by PARTICIPATED");
		}
		if (actionType.equals("CURRENTGOING")) {
			Assert.assertTrue(Long.parseLong(startDate) < timestamp, "Showing wrong entries in ListChallenges by CURRENTGOING");
			Assert.assertTrue(Long.parseLong(endDate) > timestamp, "Showing wrong entries in ListChallenges by CURRENTGOING");
		}
		if (actionType.equals("UPCOMING")) {
			Assert.assertTrue(Long.parseLong(startDate) > timestamp, "Showing wrong entries in ListChallenges by UPCOMING");
			Assert.assertTrue(Long.parseLong(endDate) > timestamp, "Showing wrong entries in ListChallenges by UPCOMING");
		}

	}

	public static ChallengeJAXB getUpdateData(ChallengeJAXB challenge, ChallengeJAXB update) {

		if (update.getTitle() != null) {
			challenge.setTitle(update.getTitle());
		}
		if (update.getDescription() != null) {
			challenge.setDescription(update.getDescription());
		}
		if (update.getImageUrl() != null) {
			challenge.setImageUrl(update.getImageUrl());
		}
		if (update.getBonusKey() != null) {
			challenge.setBonusKey(update.getBonusKey());
		}
		if (update.getBonusValue() != null) {
			challenge.setBonusValue(update.getBonusValue());
		}
		if (update.getIsLive() != null) {
			challenge.setIsLive(update.getIsLive());
		}
		return challenge;
	}

	public static void main(String[] args) {

		System.out.println(ChallengeHelper.getTimeStamp(5));
		System.out.println(ChallengeHelper.getTimeStamp(-5));

	}
}

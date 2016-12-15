package com.wooplr.bean;

public class Response

{
	private String startDate;
   
	private String challengeType;

	private String isLive;

	private String resellerId;

	private String imageUrl;

	private String status;

	private String endDate;

	private String message;

	private String id;

	private String title;

	private String description;

	private String bonusKey;

	private String bonusValue;

	private String createDate;

	private String tnc;

	public String getTnc() {
		return tnc;
	}

	public void setTnc(String tnc) {
		this.tnc = tnc;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getChallengeType() {
		return challengeType;
	}

	public void setChallengeType(String challengeType) {
		this.challengeType = challengeType;
	}

	public String getIsLive() {
		return isLive;
	}

	public void setIsLive(String isLive) {
		this.isLive = isLive;
	}

	public String getResellerId() {
		return resellerId;
	}

	public void setResellerId(String resellerId) {
		this.resellerId = resellerId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBonusKey() {
		return bonusKey;
	}

	public void setBonusKey(String bonusKey) {
		this.bonusKey = bonusKey;
	}

	public String getBonusValue() {
		return bonusValue;
	}

	public void setBonusValue(String bonusValue) {
		this.bonusValue = bonusValue;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "ClassPojo [startDate = " + startDate + ", challengeType = " + challengeType + ", isLive = " + isLive + ", resellerId = " + resellerId + ", imageUrl = " + imageUrl + ", status = "
				+ status + ", endDate = " + endDate + ", message = " + message + ", id = " + id + ", title = " + title + ", description = " + description + ", bonusKey = " + bonusKey
				+ ", bonusValue = " + bonusValue + ", createDate = " + createDate + "]";
	}
}

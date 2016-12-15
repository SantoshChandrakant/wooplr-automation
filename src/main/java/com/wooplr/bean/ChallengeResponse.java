package com.wooplr.bean;

public class ChallengeResponse {
	private Response response;

	private String reason;

	private String status;

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ClassPojo [response = " + response + ", reason = " + reason + ", status = " + status + "]";
	}
}
package com.wooplr.bean;

public class Acknowledge {

	private Boolean status = false;
	private String reason = "";
	private String resposne = "";

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getResposne() {
		return resposne;
	}

	public void setResposne(String resposne) {
		this.resposne = resposne;
	}

}

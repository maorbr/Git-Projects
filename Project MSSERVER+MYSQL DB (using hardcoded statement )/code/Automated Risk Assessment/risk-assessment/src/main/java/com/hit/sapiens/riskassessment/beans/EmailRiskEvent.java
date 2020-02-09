package com.hit.sapiens.riskassessment.beans;

public class EmailRiskEvent {
	private String id;
	private String msg;
	private String category;
	private String userName;

	public EmailRiskEvent() {
		this("", "", "", "");
	}

	public EmailRiskEvent(String id, String msg, String category, String userName) {
		super();
		this.id = id;
		this.msg = msg;
		this.category = category;
		this.userName = userName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "EmailRiskEvent [id=" + id + ", msg=" + msg + ", category=" + category + ", userName=" + userName + "]";
	}
	
}

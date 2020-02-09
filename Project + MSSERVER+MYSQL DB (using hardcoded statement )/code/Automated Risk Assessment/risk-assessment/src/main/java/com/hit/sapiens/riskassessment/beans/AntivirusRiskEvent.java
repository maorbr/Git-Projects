package com.hit.sapiens.riskassessment.beans;

public class AntivirusRiskEvent {
	private String fileName;
	private String virusName;
	private String category;
	private String userName;

	public AntivirusRiskEvent() {
		this("", "", "", "");
	}

	public AntivirusRiskEvent(String fileName, String virusName, String category, String userName) {
		super();
		this.fileName = fileName;
		this.virusName = virusName;
		this.category = category;
		this.userName = userName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getVirusName() {
		return virusName;
	}

	public void setVirusName(String virusName) {
		this.virusName = virusName;
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
		return "RiskEvent [fileName=" + fileName + ", virusName=" + virusName + ", category=" + category + ", userName="
				+ userName + "]";
	}
}

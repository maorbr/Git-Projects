package com.hit.sapiens.riskassessment.beans;

import java.util.ArrayList;
import java.util.List;

public class Antivirus {

	private String userName;
	private List<AntivirusRiskEvent> riskEvents;

	public Antivirus() {
		this("", new ArrayList<>());
	}

	public Antivirus(String userName, List<AntivirusRiskEvent> riskEvents) {
		this.userName = userName;
		this.riskEvents = riskEvents;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<AntivirusRiskEvent> getRiskEvents() {
		return riskEvents;
	}

	public void setRiskEvents(List<AntivirusRiskEvent> riskEvents) {
		this.riskEvents = riskEvents;
	}

	public int getAmountByCategory(AntivirusCategory category) {
		int sumOfRisks = 0;
		if (riskEvents != null) {
			for (AntivirusRiskEvent riskEvent : riskEvents) {
				if (riskEvent.getCategory().equals(category.toString())) {
					sumOfRisks++;
				}
			}
		}
		return sumOfRisks;
	}

	public List<AntivirusRiskEvent> getRiskEventByCategory(AntivirusCategory category) {
		List<AntivirusRiskEvent> riskEvents = new ArrayList<>();
		for (AntivirusRiskEvent riskEvent : riskEvents) {
			if (riskEvent.getCategory().equals(category.toString())) {
				riskEvents.add(riskEvent);
			}
		}
		return riskEvents;
	}

}

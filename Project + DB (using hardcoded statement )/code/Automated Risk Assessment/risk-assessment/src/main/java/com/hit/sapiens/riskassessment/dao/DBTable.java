package com.hit.sapiens.riskassessment.dao;

public enum DBTable {
	ANTIVIRUS, EMAIL;
	
	@Override
	public String toString() {
		switch (this) {
		case ANTIVIRUS:
			return "Antivirus";
		default:
			return "Email";
		}
	}
}

package com.hit.sapiens.riskassessment.beans;

public enum AntivirusCategory {
	VIRUS, MALWARE;

	@Override
	public String toString() {
		switch (this) {
		case VIRUS:
			return "Security risk";
		case MALWARE:
			return "Malware";
		default:
			return "Security risk";
		}
	}
}

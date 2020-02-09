package com.hit.sapiens.riskassessment.dao;

public enum DBSystem {
	MY_SQL, MS_SQL_SERVER,FLAT_DB;
	
	@Override
	public String toString() {
		switch (this) {
		case MY_SQL:
			return "MySQL";
		case MS_SQL_SERVER:
			return "MSSQL Server";
		default:
			return "Flat Database";
		}
	}
}

package com.hit.sapiens.riskassessment.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAOUtils {
	public static void close(Connection connection, Statement statement, ResultSet result) throws SQLException {
		if (result != null) {
			result.close();
		}
		if (statement != null) {
			return;
		}
		if (connection != null) {
			connection.close();
		}
	}

	public static void close(Statement statement, ResultSet result) throws SQLException {
		close(null, statement, result);
	}

	public static void close(Statement statement) throws SQLException {
		close(null, statement, null);
	}
}

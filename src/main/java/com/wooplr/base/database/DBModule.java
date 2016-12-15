package com.wooplr.base.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.wooplr.base.helper.SpireProperties;

public class DBModule {

	public static Connection getConnection() throws Exception {

		Properties properties = SpireProperties.loadDbProperties();
		Properties dbProperties = new Properties();
		dbProperties.put("user", properties.getProperty("CONFIG_DB_USER_ID"));
		dbProperties.put("password",
				properties.getProperty("CONFIG_DB_PASSWORD"));
		dbProperties.put("characterEncoding", "ISO-8859-1");
		dbProperties.put("useUnicode", "true");

		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connection = DriverManager.getConnection(
				properties.getProperty("CONFIG_DB_URL"), dbProperties);

		return connection;

	}

	public static Connection getCommonsDBConnection() throws Exception {

		Properties properties = SpireProperties.loadDbProperties();
		Properties dbProperties = new Properties();
		dbProperties.put("user", properties.getProperty("COMMONS_DB_USER_ID"));
		dbProperties.put("password",
				properties.getProperty("COMMONS_DB_PASSWORD"));
		dbProperties.put("characterEncoding", "ISO-8859-1");
		dbProperties.put("useUnicode", "true");

		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection connection = DriverManager.getConnection(
				properties.getProperty("COMMONS_DB_URL"), dbProperties);

		return connection;

	}

	public static Connection getCommonsDBConnection(String dbUserId,
			String password, String dbUrl) {
		Connection connection = null;
		Properties properties = SpireProperties.loadDbProperties();
		Properties dbProperties = new Properties();
		dbProperties.put("user", dbUserId);
		dbProperties.put("password", password);
		dbProperties.put("characterEncoding", "ISO-8859-1");
		dbProperties.put("useUnicode", "true");

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			connection = DriverManager.getConnection(dbUrl, dbProperties);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return connection;

	}

	public static void main(String[] args) throws Exception {

		Properties properties = SpireProperties.loadDbProperties();
		Properties dbProperties = new Properties();
		dbProperties.put("user", properties.getProperty("CONFIG_DB_USER_ID"));
		dbProperties.put("password",
				properties.getProperty("CONFIG_DB_PASSWORD"));
		dbProperties.put("characterEncoding", "ISO-8859-1");
		dbProperties.put("useUnicode", "true");

		String url = "jdbc:mysql://localhost:3306/configs";

		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection c = DriverManager.getConnection(
				properties.getProperty("CONFIG_DB_URL"), dbProperties);

		ResultSet rs = c.createStatement()
				.executeQuery("select * from configs");

		while (rs.next()) {
			System.out.println(rs.getString("SERVICE_ID"));
			System.out.println(rs.getString("NAME"));
		}

	}

}

package com.sg.db;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.sg.client.MinaTimeClient;
import com.sg.common.BaseConfig;
import com.sg.common.Utils;

public class MysqlConnPool {
	public static String url = "jdbc:mysql://192.168.99.101:3306/pop";
	public static String userName = "root";
	public static String password = "root";

	private static final MysqlConnPool instance = new MysqlConnPool();
	private static ComboPooledDataSource comboPooledDataSource;

	static {
		try {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			if (null == MinaTimeClient.properties) {
				Utils.createDirs(BaseConfig.logPath);
				MinaTimeClient.properties = Utils.loadProperties(MinaTimeClient.popCfg);
			}
			url = MinaTimeClient.properties.getProperty("mysql_url");
			userName = MinaTimeClient.properties.getProperty("mysql_userName");
			password = MinaTimeClient.properties.getProperty("mysql_password");
			comboPooledDataSource = new ComboPooledDataSource();
			comboPooledDataSource.setDriverClass("com.mysql.jdbc.Driver");
			comboPooledDataSource.setJdbcUrl(url);
			comboPooledDataSource.setUser(userName);
			comboPooledDataSource.setPassword(password);
			comboPooledDataSource
					.setMaxPoolSize(Integer.valueOf(MinaTimeClient.properties.getProperty("MAX_POOL_SIZE")));
			comboPooledDataSource.setMinPoolSize(5);

		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	public synchronized static Connection getConnection() {
		Connection connection = null;
		try {
			connection = comboPooledDataSource.getConnection();
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			return connection;
		}
	}

	private MysqlConnPool() {
	}

	public static MysqlConnPool getInstance() {
		return instance;
	}

}
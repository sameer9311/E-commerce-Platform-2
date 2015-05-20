package com.choc.util;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public final class DBConnectionPool {
	
	private ComboPooledDataSource dataSource;
	
	protected DBConnectionPool() {
		dataSource = new ComboPooledDataSource();
		try {
			Properties prop = new Properties();
			InputStream inputstream = DBConnectionPool.class.getClassLoader().getResourceAsStream("./db.properties");
			prop.load(inputstream);
			String driver = prop.getProperty("driver");
			String url = prop.getProperty("url");
			String user = prop.getProperty("user");
			String password = prop.getProperty("password");
			
			dataSource.setDriverClass(driver);
			dataSource.setJdbcUrl(url);
			dataSource.setUser(user);
			dataSource.setPassword(password);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized ComboPooledDataSource getDataSource() {
		return dataSource;
	}
	
	public synchronized Connection getConnection() {
		Connection con = null;
		try {
			con = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
}

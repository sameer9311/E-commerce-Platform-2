package com.choc.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.mysql.jdbc.Statement;

public class DbUtils {

	public DbUtils() {
		
	}
	
	private static final DBConnectionPool connectionPool = new DBConnectionPool();
	
	public static DBConnectionPool getConnectionPool() {
		return connectionPool;
	}
	
	public static void close(final Connection con) {
		try {
			con.close();
		}
		catch(Exception ignoredException) {}
	}
	
	public static void close(final Statement st) {
		try {
			st.close();
		}
		catch(Exception ignoredException) {}
	}
	
	public static void close(final PreparedStatement st) {
		try {
			st.close();
		}
		catch(Exception ignoredException) {}
	}
	
	public static void close(final ResultSet rs) {
		try {
			rs.close();
		}
		catch(Exception ignoredException) {}
	}
}

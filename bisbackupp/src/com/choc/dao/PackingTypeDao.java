package com.choc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.choc.dao.DbSchema.*;

import com.choc.model.PackingType;
import com.choc.util.DBConnectionPool;
import com.choc.util.DbUtils;

public class PackingTypeDao {

	private static PackingTypeDao dao;
	
	public static PackingTypeDao getInstance() {
		if(dao == null)
			dao = new PackingTypeDao();
		return dao;
	}
	
	private DBConnectionPool connectionPool;
	
	private PackingTypeDao() {
		connectionPool = DbUtils.getConnectionPool();
	}
	
	public List<PackingType> getAllPackingTypes(String productType) {
		List<PackingType> packings = new ArrayList<PackingType>();
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {
			String query = "select * from " + table_types_packing + " where " + packing_category + " = ?";
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(query);
			statement.setString(1, productType);
			rs = statement.executeQuery();
			while(rs.next()) {
				PackingType ptype = getPackingTypeFromResultSet(rs);
				packings.add(ptype);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(rs);
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return packings;
	}
	
	public PackingType getPackingTypeByID(String packingID) {
		
		PackingType ptype = null;
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {
			String query = "select * from " + table_types_packing + " where " + packing_id + " = ?";
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(query);
			statement.setString(1, packingID);
			rs = statement.executeQuery();
			if(rs.next()) {
				ptype = getPackingTypeFromResultSet(rs);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(rs);
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return ptype;
	}
	
	private PackingType getPackingTypeFromResultSet(ResultSet rs) throws SQLException {
		PackingType ptype = new PackingType();
		ptype.setPackingID(rs.getString(packing_id));
		ptype.setPackingName(rs.getString(packing_name));
		ptype.setDescription(rs.getString(packing_description));
		ptype.setCost(rs.getFloat(packing_cost));
		ptype.setProductType(rs.getString(packing_category));
		return ptype;
	}

}

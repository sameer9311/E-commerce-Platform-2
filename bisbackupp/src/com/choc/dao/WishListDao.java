package com.choc.dao;

import static com.choc.dao.DbSchema.product_id;
import static com.choc.dao.DbSchema.table_users_wishlists;
import static com.choc.dao.DbSchema.user_id;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.choc.model.Product;
import com.choc.util.DBConnectionPool;
import com.choc.util.DbUtils;

public class WishListDao {

	private static WishListDao dao;
	
	private DBConnectionPool connectionPool;
	
	public static WishListDao getInstance() {
		if(dao == null) {
			dao = new WishListDao();
		}
		return dao;
	}
	
	private WishListDao() {
		connectionPool = DbUtils.getConnectionPool();
	}
	
	public boolean wishListContainsProduct(String userID, String productID) {
		boolean present = false;
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {
			String query = "select exists(select * from " + table_users_wishlists + " where " + user_id + " = ? and " + product_id + " = ?)";
			
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(query);
			statement.setString(1, userID);
			statement.setString(2, productID);
			rs = statement.executeQuery();
			if(rs.next()) {
				present = rs.getBoolean(1);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(rs);
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return present;
	}
	
	public boolean removeProductFromWishList(String userID, String productID) {
		boolean success = false;
		
		Connection conn = null;
		PreparedStatement statement = null;
		
		try {
			String query = "delete from " + table_users_wishlists + " where " + user_id + " = ? and " + product_id + " = ?" ;
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(query);
			statement.setString(1, userID);
			statement.setString(2, productID);
			int count = statement.executeUpdate();
			if(count == 1) {
				success = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return success;
	}
	
	
	public boolean addProductToWishList(String userID, String productID) {
		boolean success = false;
		
		Connection conn = null;
		PreparedStatement statement = null;
		
		try {
			String query = "insert into " + table_users_wishlists + "("
								+ user_id + ", "
								+ product_id + ") "
								+ "values(?, ?)";
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(query);
			statement.setString(1, userID);
			statement.setString(2, productID);
			int count = statement.executeUpdate();
			if(count == 1) {
				success = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return success;
	}
	
	public List<String> getProductIDsFromWishlist(String userID) {
		List<String> productIDs = new ArrayList<String>();
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {
			String query = "select " + product_id + " from " + table_users_wishlists + " where " + user_id + " = ?";
			
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(query);
			statement.setString(1, userID);
			rs = statement.executeQuery();
			
			while(rs.next()) {
				String pid = rs.getString(product_id);
				productIDs.add(pid);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(rs);
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return productIDs;
	}
	
	public List<Product> getProductsFromWishlist(String userID) {
		List<String> productIDs = getProductIDsFromWishlist(userID);
		List<Product> products = new ArrayList<Product>();
		
		for(String productID : productIDs) {
			products.add(ProductDao.getInstance().getProductByProductID(productID));
		}
		return products;
	}
	
	public int getWishListSize(String userID) {
		int size = 0;
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {
			String query = "select count(" + product_id + ") from " + table_users_wishlists + " where " + user_id + " = ?";
			
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(query);
			statement.setString(1, userID);
			rs = statement.executeQuery();
			
			while(rs.next()) {
				size = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(rs);
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return size;
	}
}

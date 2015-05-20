package com.choc.dao;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.choc.util.DBConnectionPool;
import com.mysql.jdbc.Statement;
import static com.choc.dao.DbSchema.*;
import com.choc.util.DbUtils;
import com.choc.model.Product;

public class CartDao {

	private static CartDao dao;

	private DBConnectionPool connectionPool;

	private CartDao() {
		connectionPool = DbUtils.getConnectionPool();
	}

	public static CartDao getInstance() {
		if (dao == null) {
			dao = new CartDao();
		}
		return dao;
	}

	public boolean insertIntoCart(String userid, String productid,
			String sellerid, String pkgid, int quantity) {
		
		boolean success = false;
		PreparedStatement statement = null;
		Connection conn = null;

		try {
			String query = "Insert into " + table_users_carts
					+ " values(?,?,?,?,?)";
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(query);
			statement.setString(1, userid);
			statement.setString(2, productid);
			statement.setString(3, sellerid);
			statement.setString(4, pkgid);
			statement.setInt(5, quantity);
			int count = statement.executeUpdate();
			success = (count == 1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return success;
	}

	public boolean removeFromCart(String userID, String productID, String sellerID) {
		boolean success = false;
		PreparedStatement statement = null;
		Connection conn = null;

		try {
			String query = "delete from " + table_users_carts + " where " 
					+ user_id + " = ? and " 
					+ product_id + " = ? and " 
					+ seller_id + " = ?";
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(query);
			statement.setString(1, userID);
			statement.setString(2, productID);
			statement.setString(3, sellerID);
			System.out.println(statement);
			int count = statement.executeUpdate();
			success = (count == 1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return success;
	}
	
	public boolean emptyCart(String userID) {
		boolean success = false;
		PreparedStatement statement = null;
		Connection conn = null;

		try {
			String query = "delete from " + table_users_carts + " where " 
					+ user_id + " = ?";
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(query);
			statement.setString(1, userID);
			System.out.println(statement);
			int count = statement.executeUpdate();
			success = (count > 0);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return success;
	}

	public List<Product> retrieveFromCart(String userid) {
		PreparedStatement statement = null;
		ResultSet rs = null;
		Connection conn = null;

		List<Product> productList = new ArrayList<Product>();
		try {
			String query1 = "select " + "uc." + product_id + "," + "uc."
					+ seller_id + "," + "sp." + product_seller_quantity + ","
					+ "sp." + product_base_cost + "sp." + product_discount
					+ "s." + seller_name + "p," + product_name + "p."
					+ product_brand_name + "p." + product_category + "from "
					+ table_users_carts + " uc," + table_seller_products
					+ " sp," + table_sellers + " s," + table_products
					+ " p where uc." + product_id + "= sp." + product_id
					+ "AND uc." + seller_id + "= sp." + seller_id + "AND sp."
					+ seller_id + "= s." + seller_id + "AND uc." + product_id
					+ "= p." + product_id + "AND uc." + user_id + "= ?";

			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(query1);
			statement.setString(1, userid);
			rs = statement.executeQuery();

			while (rs.next()) {
				Product prod = new Product();
				prod.setProductID(rs.getString(product_id));
				prod.setBrandName(rs.getString(product_brand_name));
				prod.setProductName(rs.getString(product_name));
				prod.setCategory(rs.getString(product_category));
				prod.setBaseCost(rs.getInt(product_base_cost));
				prod.setDiscount(rs.getInt(product_discount));
				prod.setSellerID(rs.getString(seller_id));
				prod.setSellerName(rs.getString(seller_name));
				productList.add(prod);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(rs);
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return productList;
	}
}

package com.choc.dao;

import static com.choc.dao.DbSchema.*;
import static com.choc.dao.DaoQueries.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.taglibs.standard.extra.spath.Step;

import com.choc.model.ProductSeller;
import com.choc.util.DBConnectionPool;
import com.choc.util.DbUtils;

import static com.choc.dao.ProductDao.*;

public class ProductSellerDAO {

	private static ProductSellerDAO dao;
	
	public static ProductSellerDAO getInstance() {
		if(dao == null)
			dao = new ProductSellerDAO();
		return dao;
	}
	
	
	private DBConnectionPool connectionPool;
	
	private ProductSellerDAO() {
		connectionPool = DbUtils.getConnectionPool();
	}
	
	public List<ProductSeller> getProductSellers(String productID) {
		List<ProductSeller> productSellers = new ArrayList<ProductSeller>();
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(PRODUCT_SELLERS_BY_PRODUCT_ID);
			rs = statement.executeQuery();
			while(rs.next()) {
				productSellers.add(getProductSellerFromResultSet(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(rs);
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return productSellers;
	}
	
	public boolean sellerSellsProduct(String sellerID, String productID) {
		boolean exists = false;
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {
			String query = "select exists(select * from " + table_seller_products 
					+ " where " + seller_id + " = ? and " + product_id + " = ?)";
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(query);
			statement.setString(1, sellerID);
			statement.setString(2, productID);
			rs = statement.executeQuery();
			if(rs.next()) {
				exists = rs.getBoolean(1);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(rs);
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return exists;
	}
	
	
	
	private ProductSeller getProductSellerFromResultSet(ResultSet rs) throws SQLException {
		ProductSeller pseller = new ProductSeller();
		pseller.setSellerID(rs.getString(seller_id));
		pseller.setSellerName(rs.getString(seller_name));
		pseller.setSellerQuantity(rs.getInt(product_seller_quantity));
		pseller.setBaseCost(rs.getFloat(product_base_cost));
		pseller.setDiscount(rs.getFloat(product_discount));
		pseller.setPrice(rs.getFloat(product_price));
		pseller.setProductDescriprion(rs.getString(product_description));
		return pseller;
	}
}

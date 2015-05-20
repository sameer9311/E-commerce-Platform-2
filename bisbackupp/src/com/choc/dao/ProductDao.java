package com.choc.dao;

import static com.choc.dao.DaoQueries.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale.Category;
import java.util.Map;

import org.apache.tomcat.jdbc.pool.ConnectionPool;

import static com.choc.dao.DbSchema.*;

import com.choc.model.Product;
import com.choc.model.ProductCategory;
import com.choc.model.ProductSeller;
import com.choc.model.Seller;
import com.choc.model.SellerAddress;
import com.choc.util.DBConnectionPool;
import com.choc.util.DbUtils;
import com.mysql.jdbc.Statement;

public class ProductDao {
	private static ProductDao dao;
	
	private DBConnectionPool connectionPool;

	private ProductDao() {
		connectionPool = DbUtils.getConnectionPool();
	}

	public static ProductDao getInstance() {
		if (dao == null) {
			dao = new ProductDao();
		}
		return dao;
	}

	public List<String> getAllBrands() {
		List<String> brands = new ArrayList<String>();
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(ALL_BRANDS);
			System.out.println(statement);
			rs = statement.executeQuery();
			while(rs.next()) {
				brands.add(rs.getString(product_brand_name));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(rs);
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return brands;
	}
	
	public List<ProductCategory> getAllCategories() {
		List<ProductCategory> categories = new ArrayList<ProductCategory>();
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(ALL_CATEGORIES);
			System.out.println(statement);
			rs = statement.executeQuery();
			while(rs.next()) {
				ProductCategory cat = new ProductCategory();
				cat.setCategoryID(rs.getString(category_id));
				cat.setCaregoryName(rs.getString(category_name));
				categories.add(cat);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(rs);
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return categories;
	}
	
	public Product getProductByProductID(String productID) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		Product product = null;
		try {
			
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(PRODUCT_BY_PRODUCT_ID);
			statement.setString(1, productID);
			System.out.println(statement);
			rs = statement.executeQuery();

			if(rs.next()) {
				product = getProductFromResultSet(rs);
				PreparedStatement st1 = null;
				ResultSet rs1 = null;
				try {
					st1 = conn.prepareStatement(PRODUCT_RATINGS);
					st1.setString(1, product.getProductID());
					rs1 = st1.executeQuery();
					if(rs1.next())
						product.setRating(rs1.getFloat(product_ratings));
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DbUtils.close(rs1);
					DbUtils.close(st1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(rs);
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return product;
	}
	
	public List<Product> getAllProducts() {
		List<Product> products = new ArrayList<Product>();

		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(DISTINCT_PRODUCTS_DETAILS);
			System.out.println(statement);
			rs = statement.executeQuery();

			while (rs.next()) {
				Product product = getProductFromResultSet(rs);
				PreparedStatement st1 = null;
				ResultSet rs1 = null;
				try {
					st1 = conn.prepareStatement(PRODUCT_RATINGS);
					st1.setString(1, product.getProductID());
					rs1 = st1.executeQuery();
					if(rs1.next())
						product.setRating(rs1.getFloat(product_ratings));
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DbUtils.close(rs1);
					DbUtils.close(st1);
				}
				products.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(rs);
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return products;
	}
	
	public List<Product> getProductsByCategory(String category) {
		List<Product> products = new ArrayList<Product>();

		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
			
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(DISTINCT_PRODUCTS_BY_CATEGORY);
			statement.setString(1, category);
			System.out.println(statement);
			rs = statement.executeQuery();

			while (rs.next()) {
				Product product = getProductFromResultSet(rs);
				PreparedStatement st1 = null;
				ResultSet rs1 = null;
				try {
					st1 = conn.prepareStatement(PRODUCT_RATINGS);
					st1.setString(1, product.getProductID());
					rs1 = st1.executeQuery();
					if(rs1.next())
						product.setRating(rs1.getFloat(product_ratings));
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DbUtils.close(rs1);
					DbUtils.close(st1);
				}
				products.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(rs);
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return products;
	}

	public List<Product> getProductsByBrand(String brandName) {
		List<Product> products = new ArrayList<Product>();
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(DISTINCT_PRODUCTS_BY_BRAND);
			statement.setString(1, brandName);
			rs = statement.executeQuery();

			while (rs.next()) {
				Product product = getProductFromResultSet(rs);
				PreparedStatement st1 = null;
				ResultSet rs1 = null;
				try {
					st1 = conn.prepareStatement(PRODUCT_RATINGS);
					st1.setString(1, product.getProductID());
					rs1 = st1.executeQuery();
					if(rs1.next())
						product.setRating(rs1.getFloat(product_ratings));
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DbUtils.close(rs1);
					DbUtils.close(st1);
				}
				products.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(rs);
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return products;
	}
	
	// to be edited
	public List<Product> getProductsOfSeller(String sellerID) {
		List<Product> products = new ArrayList<Product>();
		
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(PRODUCTS_BY_SELLER_ID);
			statement.setString(1, sellerID);
			rs = statement.executeQuery();
			while (rs.next()) {
				Product product = getProductFromResultSet(rs);
				PreparedStatement st1 = null;
				ResultSet rs1 = null;
				try {
					st1 = conn.prepareStatement(PRODUCT_RATINGS);
					st1.setString(1, product.getProductID());
					rs1 = st1.executeQuery();
					if(rs1.next())
						product.setRating(rs1.getFloat(product_ratings));
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					DbUtils.close(rs1);
					DbUtils.close(st1);
				}
				products.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(rs);
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return products;
	}
	
	//to be implemented
	public List<Product> getProductsAfterMultipleFilters(List<String> category, List<String> brands, float priceLow, float priceHigh) {
		return null;
	}
	
	public boolean insertNewProduct(Product product) {
		boolean success = false;
		
		Connection conn = null;
		PreparedStatement statement = null;
		try {
			String querry = "insert into " + table_products + "("
					+ product_name + ", "
					+ product_brand_name + ", "
					+ product_category 
					+ ") values(?, ?, ?)";
			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(querry);
			statement.setString(1, product.getProductName());
			statement.setString(2, product.getBrandName());
			statement.setString(3, product.getCategory());
			int count = statement.executeUpdate();
			if(count == 1)
				success = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return success;
	}
	
	
	public boolean deleteProductByProductId(String productId, int quantity,
			String sellerId) {
		boolean ret_val = false;
		Connection conn = null;
		PreparedStatement statement = null;
		try {
			conn = connectionPool.getConnection();
			String querry = "Delete from " + table_seller_products + "where "
					+ product_id + "=? AND " + seller_id + " = ?";
			statement = conn.prepareStatement(querry);
			statement.setString(1, productId);
			statement.setString(2, sellerId);
			statement.executeQuery();
			ret_val = true;
		} catch (SQLException e) {
			e.printStackTrace();
			ret_val = false;
		} finally {
			DbUtils.close(statement);
		}

		/*
		 * try{ String querry = "Delete from "+ table_products_tags + "where " +
		 * product_id + "=?"; PreparedStatement statement =
		 * connection.prepareStatement(querry); statement.setString(1,
		 * productId); statement.executeQuery(); ret_val= true; }catch
		 * (SQLException e) { e.printStackTrace(); ret_val=false; }
		 */

		try {
			String query = "update " + table_products + "set "
					+ product_total_quantity + "=" + product_total_quantity
					+ "- ?" + "where" + product_id + "=? AND " + seller_id
					+ " = ?";

			statement = conn.prepareStatement(query);
			statement.setInt(1, quantity);
			statement.setString(2, productId);
			statement.setString(3, sellerId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(statement);
		}

		return ret_val;
	}

	public boolean updateProductByProductId(String productId, int quantity,
			float base_cost, float discount, String sellerId) {
		boolean ret_val = false;
		Connection conn = null;
		PreparedStatement statement = null;
		try {
			String query = "update " + table_seller_products + "set "
					+ product_seller_quantity + "=?, " + product_base_cost + "=?, "
					+ product_discount + "=? " + "where" + product_id
					+ "=? AND " + seller_id + " = ?";

			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(query);
			statement.setInt(1, quantity);
			statement.setString(2, productId);
			statement.setFloat(3, base_cost);
			statement.setFloat(4, discount);
			statement.setString(5, sellerId);
			statement.executeUpdate();
			ret_val = true;
		} catch (SQLException e) {
			e.printStackTrace();
			ret_val = false;
		} finally {
			DbUtils.close(statement);
			DbUtils.close(conn);
		}
		return ret_val;
	}

	public void insertProductsDetails(Product product) {

		Connection conn = null;
		PreparedStatement statement = null;
		try {

			/*
			 * ..................NEED TO INSERT PRODUCT_ID n
			 * DATE.........................
			 */

			String query = "insert into " + table_products + "("
					+ product_brand_name + ", " + product_name + ", "
					+ product_category + ") values (?, ?, ?)";

			conn = connectionPool.getConnection();
			statement = conn.prepareStatement(query);
			statement.setString(1, product.getBrandName());
			statement.setString(2, product.getProductName());
			statement.setString(3, product.getCategory());
			System.out.println(statement);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(statement);
		}

		try {

			/*
			 * ..................NEED TO INSERT PRODUCT_ID n
			 * DATE.........................
			 */

			String query = "update " + table_products + "set "
					+ product_total_quantity + "=" + product_total_quantity
					+ "+ ?" + "where" + product_id + "=? AND " + seller_id
					+ " = ?";

			statement = conn.prepareStatement(query);
			statement.setInt(1, product.getTotalQuantity());
			statement.setString(2, product.getProductID());
			statement.setString(3, product.getSellerID());
			System.out.println(statement);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(statement);
		}

		try {

			/*
			 * ..................NEED TO INSERT PRODUCT_ID n SELLER_ID n
			 * DATE.........................
			 */

			String query = "insert into " + table_seller_products + "("
					+ product_seller_quantity + ", " + product_base_cost + ", "
					+ product_discount + ") values (?, ?, ?)";

			statement = conn.prepareStatement(query);
			statement.setInt(1, product.getTotalQuantity());
			statement.setFloat(2, product.getBaseCost());
			statement.setFloat(3, product.getDiscount());
			System.out.println(statement);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.close(statement);
			DbUtils.close(conn);
		}

		/*
		 * try {
		 * 
		 * //..................NEED TO INSERT
		 * PRODUCT_ID.........................
		 * 
		 * String query = "insert into " + table_products_tags + "(" +
		 * product_tag_id + ") values (?)";
		 * 
		 * PreparedStatement statement = connection.prepareStatement(query);
		 * statement.setString(1, product.getTagID());
		 * System.out.println(statement); statement.executeUpdate(); }
		 * catch(SQLException e) { e.printStackTrace(); }
		 */
	}
	
	private Product getProductFromResultSet(ResultSet rs) throws SQLException {
		Product product = new Product();
		product.setProductID(rs.getString(product_id));
		product.setCategory(rs.getString(product_category));
		product.setBrandName(rs.getString(product_brand_name));
		product.setProductName(rs.getString(product_name));
		product.setDescription(rs.getString(product_description));
		product.setRating(0);
		product.setTotalQuantity(rs.getInt(product_total_quantity));
		product.setBaseCost(rs.getFloat(product_base_cost));
		product.setDiscount(rs.getFloat(product_discount));
		product.setPrice(rs.getFloat(product_price));
		product.setSellerID(rs.getString(product_seller_id));
		product.setSellerName(rs.getString(product_seller_name));
		product.setSellerQuantity(rs.getInt(product_seller_quantity));
		return product;
	}

}

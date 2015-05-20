package com.choc.dao;

import static com.choc.dao.DbSchema.*;
import static com.choc.dao.DbSchema.product_base_cost;
import static com.choc.dao.DbSchema.product_brand_name;
import static com.choc.dao.DbSchema.product_category;
import static com.choc.dao.DbSchema.product_description;
import static com.choc.dao.DbSchema.product_discount;
import static com.choc.dao.DbSchema.product_id;
import static com.choc.dao.DbSchema.product_name;
import static com.choc.dao.DbSchema.product_price;
import static com.choc.dao.DbSchema.product_ratings;
import static com.choc.dao.DbSchema.product_seller_quantity;
import static com.choc.dao.DbSchema.product_total_quantity;
import static com.choc.dao.DbSchema.seller_id;
import static com.choc.dao.DbSchema.seller_name;
import static com.choc.dao.DbSchema.table_feedbacks;
import static com.choc.dao.DbSchema.table_products;
import static com.choc.dao.DbSchema.table_seller_products;
import static com.choc.dao.DbSchema.table_sellers;

public class DaoQueries {
	protected static final String PRICE_FUNC = "(" + product_base_cost + " * " + "(1 - " + product_discount + "/100))";
	
	protected static final String ALL_BRANDS = "select distinct(" + product_brand_name + ") from " + table_products;
	
	protected static final String ALL_CATEGORIES = "select * from " + table_categories;
	
	protected static final String ALL_PRODUCT_IDS = "select " + product_id + " from " + table_products; 
	
	protected static final String ALL_PRODUCT_RATINGS = 
			"select " + product_id + ", " + "AVG(" + feedbacks_ratings +") as " + product_ratings
			+ " from " + table_feedbacks
			+ " group by " + product_id;
	
	protected static final String PRODUCT_RATINGS = 
			"select " + product_ratings
			+ " from (" + ALL_PRODUCT_RATINGS + ") as T"
			+ " where " + product_id + " = ?";
			                       
	protected static final String ALL_SELLERS_PRODUCTS_DETAILS_QUERY = 
			"select " + table_sellers + "." + seller_id + ", "
				+ seller_name + ", "
				+ table_seller_products + "." + product_id + ", "
				+ product_category + ", "
				+ product_brand_name + ", "
				+ product_name + ", "
				+ product_description + ", "
				+ product_seller_quantity + ", "
				+ product_total_quantity + ", "
				+ product_base_cost + ", "
				+ product_discount + ", "
				+ PRICE_FUNC + " as " + product_price
			
			+ " from " + table_sellers + ", "
					+ table_seller_products + ", "
					+ table_products
			+ " where " + table_sellers + "." + seller_id + " = " + table_seller_products + "." + seller_id + " and "
						+ table_products + "." + product_id + " = " + table_seller_products + "." + product_id
			+ " order by " + product_price;
			
	protected static final String SELECT_PRODUCT_ATTRIBS = 
			"select " + product_id + ", "
					+ product_brand_name + ", "
					+ product_name + ", "
					+ product_category + ", "
					+ product_description + ", "
					+ product_seller_quantity + ", "
					+ product_total_quantity + ", "
					+ product_base_cost + ", "
					+ product_discount + ", "
					+ product_price + ", "
					+ seller_id + ", "
					+ seller_name + ", "
					+ product_seller_quantity;
	
	protected static final String PRODUCT_BY_PRODUCT_ID = 
			SELECT_PRODUCT_ATTRIBS
			+ " from (" + ALL_SELLERS_PRODUCTS_DETAILS_QUERY + ") as T1"
			+ " where " + product_id + " = ?"
			+ " limit 1";
	
	protected static final String DISTINCT_PRODUCTS_DETAILS = 
			"select "  + seller_id + ", "
					+ seller_name + ", "
					+ product_id + ", "
					+ product_brand_name + ", "
					+ product_name + ", "
					+ product_category + ", "
					+ product_description + ", "
					+ product_seller_quantity + ", "
					+ product_total_quantity + ", "
					+ product_base_cost + ", "
					+ product_discount + ", "
					+ "MIN(" + product_price + ") as " + product_price
			+ " from (" + ALL_SELLERS_PRODUCTS_DETAILS_QUERY + ") as T1"
			+ " group by " + product_id;
	
	protected static final String DISTINCT_PRODUCTS_BY_CATEGORY = 
			SELECT_PRODUCT_ATTRIBS
			+ " from (" + DISTINCT_PRODUCTS_DETAILS + ") as T2"
			+ " where " + product_category + " = ?";
	
	protected static final String DISTINCT_PRODUCTS_BY_BRAND = 
			SELECT_PRODUCT_ATTRIBS
			+ " from (" + DISTINCT_PRODUCTS_DETAILS + ") as T2"
			+ " where " + product_brand_name + " = ?";
	
	protected static final String PRODUCT_SELLERS_BY_PRODUCT_ID = 
			"select " + table_sellers + "." + seller_id + ", "
					+ seller_name + ", "
					+ product_description + ", "
					+ product_seller_quantity + ", "
					+ product_base_cost + ", "
					+ product_discount + ", "
					+ PRICE_FUNC + " as " + product_price
			+ " from "  + table_sellers + ", "
						+ table_seller_products
			+ " where " + table_sellers + "." + seller_id + " = " + table_seller_products + "." + seller_id 
						+ " and " + product_id + " = ?"
			+ "order by" + product_price + ", "
						 + table_sellers + "." + seller_id;
	
	protected static final String PRODUCTS_BY_SELLER_ID = 
			SELECT_PRODUCT_ATTRIBS 
			+ " from (" + ALL_SELLERS_PRODUCTS_DETAILS_QUERY + ") as T1"
			+ "where " + seller_id + " = ?"
			+ " order by " + product_id;
}

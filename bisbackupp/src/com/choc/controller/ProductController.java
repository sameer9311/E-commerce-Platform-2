package com.choc.controller;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.choc.dao.ProductDao;
import com.choc.model.Product;
import com.choc.model.ProductCategory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

@Path("/products")
public class ProductController {
	
	@GET
	@Path("/all")
	@Produces({MediaType.APPLICATION_JSON})
	public String fetchProducts() {
		System.out.println("fetching all products...");
		List<Product> products = ProductDao.getInstance().getAllProducts();
		JsonArray jarray = new JsonArray();
		JsonParser parser = new JsonParser();
		Gson gson = new Gson();
		for(Product product : products) {
			jarray.add(parser.parse(gson.toJson(product)));
		}
		return jarray.toString();
	}
	
	@GET
	@Path("/all_brands")
	@Produces({MediaType.APPLICATION_JSON})
	public String fetchBrands() {
		System.out.println("fetching all brands...");
		List<String> brands = ProductDao.getInstance().getAllBrands();
		JSONArray jarray = new JSONArray();
		for(String brand : brands) {
			JSONObject json = new JSONObject();
			try {
				json.put("name", brand);
			} catch (JSONException e) {
				System.out.println("Cannot add brand: " + brand + "\nmessage: " + e.getMessage());
			}
			jarray.put(json);
		}
		System.out.println(jarray);
		return jarray.toString();
	}
	
	@GET
	@Path("/all_cat")
	@Produces({MediaType.APPLICATION_JSON})
	public String fetchCategories() {
		System.out.println("fetching all categories...");
		List<ProductCategory> categories = ProductDao.getInstance().getAllCategories();
		JsonArray jarray = new JsonArray();
		JsonParser parser = new JsonParser();
		Gson gson = new Gson();
		for(ProductCategory category : categories) {
			jarray.add(parser.parse(gson.toJson(category)));
		}
		return jarray.toString();
	}
	
	

}

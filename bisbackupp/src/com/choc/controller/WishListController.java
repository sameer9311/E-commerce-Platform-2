package com.choc.controller;

import java.util.List;

import static com.choc.dao.DbSchema.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.choc.dao.ProductDao;
import com.choc.dao.UserDao;
import com.choc.dao.WishListDao;
import com.choc.exceptions.ObjectNotFoundException;
import com.choc.model.Product;
import com.choc.util.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Path("/wishlist")
public class WishListController {
	
	@GET
	@Path("/view/{sID}")
	@Produces({MediaType.APPLICATION_JSON})
	public String fetchProductsFromWishList(@PathParam("sID") String sessionID) {
		SessionManager sessionManager = SessionManager.getInstance();
		String emailID = sessionManager.getEmailID(sessionID);
		String userID = null;
		String jsonResp = null;
		try {
			userID = UserDao.getInstance().getUserIDByEmail(emailID);
			List<Product> products = WishListDao.getInstance().getProductsFromWishlist(userID);
			JsonArray jarray = new JsonArray();
			JsonParser parser = new JsonParser();
			Gson gson = new Gson();
			for(Product product : products) {
				jarray.add(parser.parse(gson.toJson(product)));
			}
			jsonResp = jarray.toString();
		} catch (ObjectNotFoundException e) {
			log(e.getMessage());
		}
		return jsonResp;
	}
	
	@GET
	@Path("/add/{sID}/{pID}")
	@Produces({MediaType.APPLICATION_JSON})
	public String addToWishlist(@PathParam("sID") String sessionID, @PathParam("pID") String productID) {
		SessionManager sessionManager = SessionManager.getInstance();
		String emailID = sessionManager.getEmailID(sessionID);
		String userID = null;
		String jsonResp = null;
		try {
			userID = UserDao.getInstance().getUserIDByEmail(emailID);
			boolean success = WishListDao.getInstance().addProductToWishList(userID, productID);
			JsonObject json = new JsonObject();
			json.addProperty("success", success);
			jsonResp = json.toString();
		} catch (ObjectNotFoundException e) {
			log(e.getMessage());
		}
		return jsonResp;
	}
	
	@GET
	@Path("/remove/{sID}/{pID}")
	@Produces({MediaType.APPLICATION_JSON})
	public String removeWishlist(@PathParam("sID") String sessionID, @PathParam("pID") String productID) {
		SessionManager sessionManager = SessionManager.getInstance();
		String emailID = sessionManager.getEmailID(sessionID);
		String userID = null;
		String jsonResp = null;
		try {
			userID = UserDao.getInstance().getUserIDByEmail(emailID);
			boolean success = WishListDao.getInstance().removeProductFromWishList(userID, productID);
			JsonObject json = new JsonObject();
			json.addProperty("success", success);
			jsonResp = json.toString();
		} catch (ObjectNotFoundException e) {
			log(e.getMessage());
		}
		return jsonResp;
	}
}

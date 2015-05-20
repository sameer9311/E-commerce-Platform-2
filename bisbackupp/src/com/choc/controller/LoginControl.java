package com.choc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import test.MailTest;

import com.choc.dao.ProductDao;
import com.choc.dao.UserDao;
import com.choc.exceptions.BadJsonException;
import com.choc.exceptions.UserAlreadyExistsException;
import com.choc.model.Product;
import com.choc.model.ProductCategory;
import com.choc.model.User;
import com.choc.util.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Path("/api")
public class LoginControl {

	private Thread autoExpirationThread;
	
	@GET
	@Path("/multiply/{a}/{b}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Long multiply(@PathParam("a") Long a, @PathParam("b") Long b) {
		System.out.println("multiply:A: " + a + " B:" + b);
		return a * b;
	}
	
	@GET
	@Path("/verify/{hash}")
	public void validateUser(@PathParam("hash") String hash) {
		try {
			UserDao dao = UserDao.getInstance();
			dao.verifyUser(hash);
			System.out.println("user verified!!");
		} catch (Exception e) {
			System.out.println("Unknown Hash value recieved!!");
		}
	}

	@POST
	@Path("login")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String loginUser(String jsonReq) {
		String emailID = "";
		String password = "";
		HashMap<String, String> map = new HashMap<String, String>();
		boolean badJSON = false;
		try {
			JSONObject jobj = new JSONObject(jsonReq);
			System.out.println(jobj);
			emailID = jobj.getString("username");
			password = jobj.getString("password");
		} catch (Exception e) {
			map.put("status", "failure");
			map.put("message", "Bad Json");
			badJSON = true;
		}
		if (!badJSON) {
			SessionManager sessionManager = SessionManager.getInstance();
			UserDao dao = UserDao.getInstance();
			boolean success = dao.loginUser(emailID, password);
			if (success) {
				User user = dao.getUserByEmailId(emailID);
				map.put("status", "success");
				map.put("message", "User login successful");
				map.put("session_id", sessionManager.createSession(emailID));
				map.put("name", user.getFirstname() + " " + user.getLastname());
				System.out.println(map.get("session_id"));
				autoExpirationThread = new Thread(new Runnable() {
					
					public void run() {
						try {
							Thread.sleep(100000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if(Thread.interrupted()) {
							System.out.println("cancelling auto expiration of sessionID");
						} else {
							System.out.println("destroying session...");
							String email = sessionManager.getEmailID(map.get("session_id"));
							dao.logoutUser(email);
							sessionManager.destroySession(map.get("session_id"));
						}
						
					}
				});
			//	autoExpirationThread.start();
				
			} else {
				map.put("status", "failure");
				map.put("message", "invalid credentials");
			}
		}
		return new JSONObject(map).toString();
	}
	
	@POST
	@Path("/checkSession")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String checkSession(String jsonReq) {
		String sessionID = "";
		HashMap<String, Object> map = new HashMap<String, Object>();
		boolean badJSON = false;
		try {
			JSONObject jobj = new JSONObject(jsonReq);
			System.out.println(jobj);
			sessionID = jobj.getString("sessionID");
		} catch (Exception e) {
			map.put("status", "failure");
			map.put("message", "Bad Json");
			badJSON = true;
		}
		if (!badJSON) {
			
			SessionManager sessionManager = SessionManager.getInstance();
			map.put("status", "success");
			if (sessionManager.getEmailID(sessionID) != null) {
				map.put("exists", Boolean.TRUE);
			} else {
				map.put("exists", Boolean.FALSE);
			}
		}
		return new JSONObject(map).toString();
	}
	
	@POST
	@Path("/logout")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public String logutUser(String jsonReq) {
		String emailID = "";
		String sessionID = "";
		HashMap<String, String> map = new HashMap<String, String>();
		boolean badJSON = false;
		try {
			JSONObject jobj = new JSONObject(jsonReq);
			System.out.println(jobj);
			sessionID = jobj.getString("sessionID");
		} catch (Exception e) {
			map.put("status", "failure");
			map.put("message", "Bad Json");
			badJSON = true;
		}
		if (!badJSON) {
//			if(autoExpirationThread != null && autoExpirationThread.isAlive())
//				autoExpirationThread.interrupt();
//			System.out.println("thread interupted");
			SessionManager sessionManager = SessionManager.getInstance();
			UserDao dao = UserDao.getInstance();
			emailID = sessionManager.getEmailID(sessionID);
			if (emailID != null) {
				map.put("status", "success");
				map.put("message", "User logout successful");
				sessionManager.destroySession(sessionID);
				dao.logoutUser(emailID);
			} else {
				map.put("status", "success");
				map.put("message", "sessionID expired");
			}
		}
		return new JSONObject(map).toString();
	}
	
	@POST
	@Path("/signup")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public void signupUser(String jsonReq){
		boolean badJSON = false;
		User user = new User();
		try {
			JSONObject json = new JSONObject(jsonReq);
			user.setEmail(json.getString("email_id"));
			user.setPassword(json.getString("password"));
			user.setFirstName(json.getString("firstname"));
			user.setLastName(json.getString("lastname"));
			user.setContact(json.getString("contact_no"));
		}
		catch (Exception e) {
			System.out.println("BAD JSON!!");
			badJSON = true;
		}
		
		if(!badJSON) {
			System.out.println("JSON Accepted");
			UserDao dao = UserDao.getInstance();
			boolean userInserted = true;
			try {
				dao.insertUser(user);
			} catch (UserAlreadyExistsException e) {
				System.out.println("EMail id already used!!");
				userInserted  = false;
			}
			if(userInserted) {
				String hash = dao.getUserHash(user.getEmailID());
				String link = "http://localhost:8080/choc/rest/api/verify/" + hash;
				MailTest.sendVerificationMail(user.getEmailID(), link);
			}
		}
	}

}

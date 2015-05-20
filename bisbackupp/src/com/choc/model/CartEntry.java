package com.choc.model;

public class CartEntry {

	private String userID;
	private Product product;
	private PackingType packing;
	private float amount;
	
	public CartEntry() {
		
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public PackingType getPacking() {
		return packing;
	}

	public void setPacking(PackingType packing) {
		this.packing = packing;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

}

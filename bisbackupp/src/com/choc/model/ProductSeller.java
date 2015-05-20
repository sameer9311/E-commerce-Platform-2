package com.choc.model;

public class ProductSeller {

	private String sellerID;
	private String sellerName;
	private String productDescriprion;
	private int sellerQuantity;
	private float baseCost;
	private float discount;
	private float price;
	
	public ProductSeller() {
		// TODO Auto-generated constructor stub
	}

	public String getSellerID() {
		return sellerID;
	}

	public void setSellerID(String sellerID) {
		this.sellerID = sellerID;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public String getProductDescriprion() {
		return productDescriprion;
	}

	public void setProductDescriprion(String productDescriprion) {
		this.productDescriprion = productDescriprion;
	}

	public int getSellerQuantity() {
		return sellerQuantity;
	}

	public void setSellerQuantity(int sellerQuantity) {
		this.sellerQuantity = sellerQuantity;
	}

	public float getBaseCost() {
		return baseCost;
	}

	public void setBaseCost(float baseCost) {
		this.baseCost = baseCost;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

}

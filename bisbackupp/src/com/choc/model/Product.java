package com.choc.model;

import java.util.ArrayList;

public class Product {
	private String productID;
	private String brandName;
	private String productName;
	private String category;
	private String description;
	private int totalQuantity;

	private float baseCost;
	private float discount;
	private float price;
	private float avgRating;
	
	private String sellerID;
	private String sellerName;
	private int sellerQuantity;

	public Product() {
		
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public float getBaseCost() {
		return baseCost;
	}

	public void setBaseCost(float base_cost) {
		this.baseCost = base_cost;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float minPrice) {
		this.price = minPrice;
	}

	public int getSellerQuantity() {
		return sellerQuantity;
	}

	public void setSellerQuantity(int sellerQuantity) {
		this.sellerQuantity = sellerQuantity;
	}
	
	@Override
	public String toString() {
		return getProductID() + "\n"
				+ getBrandName() + "\n"
				+ getProductName() + "\n"
				+ getCategory() + "\n"
				+ getDescription() + "\n"
				+ "ratings: " + getRating() + "\n"
				+ "cost: " + getBaseCost() + "\n"
				+ "discount" + getDiscount() + "\n"
				+ "minprice" + getPrice() + "\n"
				+ "totalQuantity" + getTotalQuantity() + "\n"
				+ getSellerID() + "\n"
				+ getSellerName() + "\n"
				+ getSellerQuantity() + "\n";
	}

	public float getRating() {
		return avgRating;
	}

	public void setRating(float rating) {
		this.avgRating = rating;
	}

}

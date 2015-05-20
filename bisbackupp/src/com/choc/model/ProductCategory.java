package com.choc.model;

public class ProductCategory {
	
	public static final String CAKE = "CAKE";
	public static final String CHOCOLATE = "CHOC";
	
	private String categoryID;
	private String categoryName;
	
	public String getCategoryID() {
		return categoryID;
	}
	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}
	public String getCaregoryName() {
		return categoryName;
	}
	public void setCaregoryName(String caregoryName) {
		this.categoryName = caregoryName;
	}
	
	@Override
	public String toString() {
		return categoryID + " " + categoryName;
	}
	
}

package com.choc.model;

public class PackingType {

	private String packingID;
	private String packingName;
	private String description;
	private float cost;
	private String productType;
	
	public PackingType() {
		// TODO Auto-generated constructor stub
	}

	public String getPackingID() {
		return packingID;
	}

	public void setPackingID(String packingID) {
		this.packingID = packingID;
	}

	public String getPackingName() {
		return packingName;
	}

	public void setPackingName(String packingName) {
		this.packingName = packingName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	@Override
	public String toString() {
		return packingID + "\n"
				+ packingName + "\n"
				+ description + "\n"
				+ productType + "\n"
				+ cost + "\n";
	}
}

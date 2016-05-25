package com.example.Entity;

public class HomeServiceFunction 
{
	private String funtionName;
	private int imageId;
	public HomeServiceFunction(String funtionName, int imageId) {
		super();
		this.funtionName = funtionName;
		this.imageId = imageId;
	}
	public String getFuntionName() {
		return funtionName;
	}
	public int getImageId() {
		return imageId;
	}
	
}

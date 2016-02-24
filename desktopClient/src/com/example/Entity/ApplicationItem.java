package com.example.Entity;

public class ApplicationItem 
{
	private String applicationName;
	private int imageId;
	public ApplicationItem(String applicationName, int imageId) {
		super();
		this.applicationName = applicationName;
		this.imageId = imageId;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public int getImageId() {
		return imageId;
	}
	
}

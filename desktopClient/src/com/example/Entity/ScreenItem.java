package com.example.Entity;

public class ScreenItem
{
	private String screenName;
	private int imageId;
	public ScreenItem(String screenName, int imageId)
	{
		super();
		this.screenName = screenName;
		this.imageId = imageId;
	}
	public String getscreenName() {
		return screenName;
	}
	public int getImageId() {
		return imageId;
	}
}

package com.example.Entity;

public class MediaItem 
{
	private String mediaName;
	private int imageId;
	public MediaItem(String mediaName, int imageId) {
		super();
		this.mediaName = mediaName;
		this.imageId = imageId;
	}
	public String getMediaName() {
		return mediaName;
	}
	public int getImageId() {
		return imageId;
	}
}

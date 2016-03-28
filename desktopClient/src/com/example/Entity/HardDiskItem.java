package com.example.Entity;

public class HardDiskItem
{
	private String hardDiskName;
	private int imageId;
	private boolean checked;
	public HardDiskItem(String hardDiskName, int imageId,boolean checked)
	{
		super();
		this.hardDiskName = hardDiskName;
		this.imageId = imageId;
		this.checked=checked;
	}
	public String gethardDiskName() {
		return hardDiskName;
	}
	public int getImageId() {
		return imageId;
	}
	public boolean isChecked()
	{
		return checked;
	}
	public void setChecked(boolean checked)
	{
		this.checked = checked;
	}
	
}

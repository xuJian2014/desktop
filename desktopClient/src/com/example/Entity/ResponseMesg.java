package com.example.Entity;

import java.io.Serializable;

public class ResponseMesg implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String responserMesg;
	private int mesgType;
	/*
	 * 	0:��ʾ�����δ������������ȴ�
	 *  1����ʾ������ϢΪ�����IP��ַ
	 */
	public String getResponserMesg() {
		return responserMesg;
	}
	public void setResponserMesg(String responserMesg) {
		this.responserMesg = responserMesg;
	}
	public int getMesgType() {
		return mesgType;
	}
	public void setMesgType(int mesgType) {
		this.mesgType = mesgType;
	}
	public ResponseMesg(String responserMesg,int mesgType)
	{
		this.responserMesg = responserMesg;
		this.mesgType = mesgType;
	}
}

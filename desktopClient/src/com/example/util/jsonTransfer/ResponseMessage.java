package com.example.util.jsonTransfer;

public class ResponseMessage
{
	private int errNum;
	private String responseMessage;
	
	public int getErrNum()
	{
		return errNum;
	}
	public void setErrNum(int errNum)
	{
		this.errNum = errNum;
	}
	public String getResponseMessage()
	{
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage)
	{
		this.responseMessage = responseMessage;
	}
	public ResponseMessage(int errNum, String responseMessage)
	{
		super();
		this.errNum = errNum;
		this.responseMessage = responseMessage;
	}
}

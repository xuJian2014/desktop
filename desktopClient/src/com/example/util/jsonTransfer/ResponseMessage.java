package com.example.util.jsonTransfer;

public class ResponseMessage
{
	private int errNum;
	private String responseMessage;
	
	private String responseMessage2;
	
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
	
	public String getResponseMessage2()
	{
		return responseMessage2;
	}
	public void setResponseMessage(String responseMessage)
	{
		this.responseMessage = responseMessage;
	}
	
	public void setResponseMessage2(String responseMessage2)
	{
		this.responseMessage2 = responseMessage2;
	}
	public ResponseMessage(int errNum, String responseMessage ,String responseMessage2)
	{
		super();
		this.errNum = errNum;
		this.responseMessage = responseMessage;
		this.responseMessage2=responseMessage2;
	}
}

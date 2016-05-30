package com.example.util.jsonTransfer;

public class Response2Message
{
	private int errNum;
	private String responseMessage1;
	private String responseMessage2;
	public int getErrNum()
	{
		return errNum;
	}
	public void setErrNum(int errNum)
	{
		this.errNum = errNum;
	}
	public String getResponseMessage1()
	{
		return responseMessage1;
	}
	public void setResponseMessage1(String responseMessage1)
	{
		this.responseMessage1 = responseMessage1;
	}
	public String getResponseMessage2()
	{
		return responseMessage2;
	}
	public void setResponseMessage2(String responseMessage2)
	{
		this.responseMessage2 = responseMessage2;
	}
	public Response2Message(int errNum, String responseMessage1,
			String responseMessage2)
	{
		super();
		this.errNum = errNum;
		this.responseMessage1 = responseMessage1;
		this.responseMessage2 = responseMessage2;
	}
	
	
	
}

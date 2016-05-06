package com.example.util.jsonTransfer;

public class Parameter3Option
{
	private String parameter1;
	private String parameter2;
	private String parameter3;
	public String getParameter1()
	{
		return parameter1;
	}
	public void setParameter1(String parameter1)
	{
		this.parameter1 = parameter1;
	}
	public String getParameter2()
	{
		return parameter2;
	}
	public void setParameter2(String parameter2)
	{
		this.parameter2 = parameter2;
	}
	public String getParameter3()
	{
		return parameter3;
	}
	public void setParameter3(String parameter3)
	{
		this.parameter3 = parameter3;
	}
	public Parameter3Option(String parameter1, String parameter2,String parameter3)
	{
		super();
		this.parameter1 = parameter1;
		this.parameter2 = parameter2;
		this.parameter3 = parameter3;
	}
}

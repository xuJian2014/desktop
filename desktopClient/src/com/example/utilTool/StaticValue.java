package com.example.utilTool;

public class StaticValue
{
	public static String vMachineIp=null;
	public static String getvMachineIp() 
	{
		return vMachineIp;
	}
	public static void setvMachineIp(String vMachineIp)
	{
		StaticValue.vMachineIp = vMachineIp;
	}
}

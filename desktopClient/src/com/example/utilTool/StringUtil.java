package com.example.utilTool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil 
{
	/*
	 * 判断字符串是否为空
	 */
    public static boolean isNullString(String str) 
    {  
        if(str == null || str.length() <= 0)
        {
        	return true;
        }
        else
        	return false;
    } 
    
    //判断IP字符串是否合法
    public static boolean isIPAddress(String ipaddr)
	{
		boolean flag = false;
		Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher m = pattern.matcher(ipaddr);
		flag = m.matches();
		return flag;
	}
}

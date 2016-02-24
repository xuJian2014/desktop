package com.example.utilTool;

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
}

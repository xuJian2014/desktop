package com.example.utilTool;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.org.apache.xml.internal.security.Init;

public class StringUtil
{
	private static HashSet<String> documentType=new HashSet<String>();
	/*
	 * 判断字符串是否为空
	 */
	public static boolean isNullString(String str)
	{
		if (str == null || str.length() <= 0)
		{
			return true;
		} else
			return false;
	}

	// 判断IP字符串是否合法
	public static boolean isIPAddress(String ipaddr)
	{
		boolean flag = false;
		Pattern pattern = Pattern
				.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher m = pattern.matcher(ipaddr);
		flag = m.matches();
		return flag;
	}
	
	//判断是否包含中文
	public static boolean isContainsChinese(String str)
	{
		String regEx = "[\u4e00-\u9fa5]";
		Pattern pat = Pattern.compile(regEx);
		Matcher matcher = pat.matcher(str);
		boolean flg = false;
		if (matcher.find())
		{
			flg = true;
		}
		return flg;
	}
	private static void initData()
	{
		documentType.add("txt");
		documentType.add("docx");
		documentType.add("doc");
		documentType.add("pptx");
	}
	
	//是否包含documentType中的元素
	public static boolean isFileForProjection(String str)
	{
		initData();
		boolean flag=false;
		if(documentType.contains(str))
		{
			flag=true;
		}
		return flag;
	}
	
	//获取文件的后缀名
	public static String getExtensionName(String filename) 
	{   
			
	     if ((filename != null) && (filename.length() > 0)) 
	       {   
	            int dot = filename.lastIndexOf('.');   
	            if ((dot >-1) && (dot < (filename.length() - 1))) 
	            {   
	                return filename.substring(dot + 1);   
	            }   
	       }   
	        return filename;   
	   }   
}

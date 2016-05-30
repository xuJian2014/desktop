package com.example.util.jsonTransfer;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonParse
{	
	private static Gson gson;
	private static JsonParser parse;
	static 
	{
		gson = new Gson();
		parse=new JsonParser(); 
	}
	public static String Json2String(int optionId,Object object)
	{
		JsonObject jsonObject=new JsonObject();
		jsonObject.addProperty("id", optionId);
		
		JsonElement optionMessage=gson.toJsonTree(object);
		jsonObject.add("optionMessage", optionMessage);
		return gson.toJson(jsonObject);
	}
	public static ResponseMessage Json2Object(String jsonStr)
	{
		if(jsonStr==null)
			return null;
		ResponseMessage responseMessage=null;
		JsonObject object=(JsonObject) parse.parse(jsonStr);
		responseMessage=gson.fromJson(object.toString(), ResponseMessage.class);
		return responseMessage;
	}
	
	public static Response2Message Json3Object(String jsonStr)
	{
		if(jsonStr==null)
			return null;
		Response2Message responseMessage=null;
		JsonObject object=(JsonObject) parse.parse(jsonStr);
		responseMessage=gson.fromJson(object.toString(), Response2Message.class);
		return responseMessage;
	}
}

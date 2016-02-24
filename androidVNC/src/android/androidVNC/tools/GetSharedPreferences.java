package android.androidVNC.tools;

import android.content.Context;
import android.content.SharedPreferences;

public class GetSharedPreferences {
	private static final String FILE_NAME_SHAREDPREF="VNCConnect";
	
	public static String getData(Context context,String key, Object defaultObject){
		String type=defaultObject.getClass().getSimpleName();
		SharedPreferences getPreferences=context.getSharedPreferences(FILE_NAME_SHAREDPREF, Context.MODE_WORLD_READABLE);
		
		if("String".equals(type)){
			return getPreferences.getString(key, (String)defaultObject);
		}
		return null;
	}
}
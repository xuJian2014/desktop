package com.example.util.crashHandler;

import android.app.Application;

public class MyApplication extends Application
{
	private static MyApplication mApplication;
	
	public synchronized static MyApplication getInstance()
	{
		return mApplication;
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		initData();
	}

	private void initData()
	{
		//当程序发生Uncaught异常的时候,由该类来接管程序,一定要在这里初始化
		CrashHandler.getInstance().init(this);
	}

}

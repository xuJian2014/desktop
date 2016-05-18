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
		//��������Uncaught�쳣��ʱ��,�ɸ������ӹܳ���,һ��Ҫ�������ʼ��
		CrashHandler.getInstance().init(this);
	}

}

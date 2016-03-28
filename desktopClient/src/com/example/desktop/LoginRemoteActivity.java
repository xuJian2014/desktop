package com.example.desktop;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class LoginRemoteActivity extends Activity 
{
	private Button button;
	private TextView textView;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_remote);
		textView=(TextView) findViewById(R.id.textView2);
		button=(Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				 Intent intent=new Intent();
	              //前名一个参数是应用程序的包名,后一个是这个应用程序的主Activity名
	             intent.setComponent(new ComponentName("com.microsoft.rdc.android", 
	                                                   "com.microsoft.rdc.ui.activities.HomeActivity"));
	             startActivity(intent);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_remote, menu);
		return true;
	}

}

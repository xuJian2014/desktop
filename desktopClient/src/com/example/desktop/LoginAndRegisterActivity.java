package com.example.desktop;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginAndRegisterActivity extends Activity
{
	private Button loginButton;
	private Button registerButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_register);
		
		getActionBar().setDisplayUseLogoEnabled(true);
		getActionBar().setIcon(null);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
		
		loginButton=(Button) findViewById(R.id.loginButton);
		registerButton=(Button) findViewById(R.id.registerButton);
		loginButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent intent=new Intent(LoginAndRegisterActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});
		
		registerButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				Intent intent=new Intent(LoginAndRegisterActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});
	}
}

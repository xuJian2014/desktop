package com.example.touch;

import android.androidVNC.VncCanvasActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.desktop.MainActivity;
import com.example.desktop.R;

public class ChoiceActivity extends Activity implements OnClickListener {

	private Button btnRemote;
	private Button btnFamily;
	private Button btnCancle;
	private SharedPreferences getRemotePreferences;
	private SharedPreferences getFamilyPreferences;
	private String connectionIP;
	private String connectionPwd;
	private SharedPreferences getVNCPreferences;
	private SharedPreferences.Editor vncEditor;
	private String chooseFlag;
	private String gameString = "game";
	private String mouseString = "mouse";
	private String keyboardString = "keyboard";
	private String vncString = "vnc";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.chioce_remote_family_activity);     //骞虫澘
		setContentView(R.layout.chioce_remote_family_activity_phone);  //鎵嬫満

		View viewChoice = findViewById(R.id.choice_pop_layout);
		viewChoice.getBackground().setAlpha(200);

		btnRemote = (Button) findViewById(R.id.btn_choice_remote);
		btnFamily = (Button) findViewById(R.id.btn_choice_family);
		btnCancle = (Button) findViewById(R.id.btn_choice_cancel);

		btnRemote.setOnClickListener(this);
		btnFamily.setOnClickListener(this);
		btnCancle.setOnClickListener(this);
		
		getVNCPreferences = this.getSharedPreferences("VNCConnect", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
		vncEditor = getVNCPreferences.edit();
		
		Intent choiceIntent = getIntent();
		chooseFlag = choiceIntent.getStringExtra("choiceFlag");
	}

	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_choice_remote:
			
			getRemotePreferences = ChoiceActivity.this.getSharedPreferences("Remote", Context.MODE_PRIVATE);
			connectionIP = getRemotePreferences.getString("remoteIP", "0000");
			connectionPwd = getRemotePreferences.getString("remotePassword", "0000");
			vncEditor.putString("IP", connectionIP);
			vncEditor.putString("password", connectionPwd);
			vncEditor.commit();
			
			System.out.println("connectionIP:" + connectionIP + ",connectionPwd:" + connectionPwd);
			
			if(connectionIP=="0000" || connectionPwd=="0000"){
				finish();
				Intent iMain = new Intent(this, MainActivity.class);
				startActivity(iMain);       
			}else{
				finish();
				startChoseActivity();
			}
			
			break;

		case R.id.btn_choice_family:
		
			getFamilyPreferences = ChoiceActivity.this.getSharedPreferences("configInfo", Context.MODE_PRIVATE);
			connectionIP = getFamilyPreferences.getString("homeServiceIp", "0000");
			connectionPwd = getFamilyPreferences.getString("homeServicePwd", "0000");
			vncEditor.putString("IP", connectionIP);
			vncEditor.putString("password", connectionPwd);
			vncEditor.commit();
			System.out.println("connectionIP:" + connectionIP + ",connectionPwd:" + connectionPwd);
			if(connectionIP == "0000" || connectionPwd == "0000"){
				finish();
				Intent iMain = new Intent(this, MainActivity.class);
				startActivity(iMain);  
			}else{
				finish();
				startChoseActivity();
			}
			break;

		case R.id.btn_choice_cancel:
			finish();
			break;
		}
	}
	
	private void startChoseActivity(){
		
		if(chooseFlag.equals(mouseString)){
			startActivity(new Intent(this, MouseActivity.class));   
		}
		else if(chooseFlag.equals(keyboardString)){
			startActivity(new Intent(ChoiceActivity.this, KeyboardActivity.class));   
		}
		else if(chooseFlag.equals(gameString)){
			startActivity(new Intent(this, GameActivity.class));  
		}
		else{
			startActivity(new Intent(this, VncCanvasActivity.class));   
		}
	}
}

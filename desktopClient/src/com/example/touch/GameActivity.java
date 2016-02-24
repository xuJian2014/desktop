package com.example.touch;


import java.awt.event.KeyEvent;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.action.AuthentificationAction;
import com.example.action.AuthentificationResponseAction;
import com.example.connection.ConnectServer;
import com.example.desktop.R;


public class GameActivity extends Activity {

	
	private String ip = TouchFlag.getInstance().getIp();
	private String password = TouchFlag.getInstance().getPwd();
	private  boolean isValid = false;
	private Vibrator vibrator;
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game);
        vibrator = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
        preferences = this.getApplicationContext().getSharedPreferences("configInfo", Context.MODE_PRIVATE);
        if(!preferences.getBoolean("flag", false))
        {
        	//绗竴娆¤鍙� 璁剧疆鍒濊瘯閰嶇疆
        	editor = preferences.edit();
        	editor.putBoolean("flag", true);
        	editor.putInt("button1",KeyEvent.VK_W);
        	editor.putInt("button2",KeyEvent.VK_A);
        	editor.putInt("button3",KeyEvent.VK_D);
        	editor.putInt("button4",KeyEvent.VK_S);
        	
        	
        	
        	editor.putInt("buttonR1C3",KeyEvent.VK_O);
        	editor.putInt("buttonR1C2",KeyEvent.VK_I);
        	editor.putInt("buttonR1C1",KeyEvent.VK_U);
        	editor.putInt("buttonR2C1",KeyEvent.VK_J);
        	editor.putInt("buttonR2C2",KeyEvent.VK_K);
        	editor.putInt("buttonR2C3",KeyEvent.VK_L);
        	editor.commit();
        }  
	        Log.d("debug", "load");
	      Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {	
						ConnectServer.getInstance().close();
						ConnectServer.getInstance().connect(ip);
						ConnectServer.getInstance().sendAction(new AuthentificationAction(password));
						AuthentificationResponseAction response = ConnectServer.getInstance().recvAction();
						isValid  = response.authentificated;
						Log.d("debug", "RUN");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Thread.currentThread().interrupt();
					}			
				}
			});
		thread.start();	
		try
		{
			thread.join();
			Log.d("debug", "join");
			if(isValid)
			{
				 Log.d("debug", "vaild");
				Toast.makeText(GameActivity.this, "服务器连接成功", Toast.LENGTH_SHORT).show();
				KeyListener listener = new KeyListener(vibrator,preferences);
				Button forward = (Button) findViewById(R.id.button3);
				forward.setOnTouchListener(listener);
				Button back = (Button) findViewById(R.id.button2);
				back.setOnTouchListener(listener);
				Button up = (Button) findViewById(R.id.button1);
				up.setOnTouchListener(listener);
				Button down = (Button) findViewById(R.id.button4);
				down.setOnTouchListener(listener);
				
				
				
				
				Button red = (Button) findViewById(R.id.buttonR2C1);
				red.setOnTouchListener(listener);
				Button blue = (Button) findViewById(R.id.buttonR2C2);
				blue.setOnTouchListener(listener);
				Button green = (Button) findViewById(R.id.buttonR1C2);
				green.setOnTouchListener(listener);
				Button yellow = (Button) findViewById(R.id.buttonR2C3);
				yellow.setOnTouchListener(listener);
				Button black = (Button) findViewById(R.id.buttonR1C3);
				black.setOnTouchListener(listener);
				Button purple = (Button) findViewById(R.id.buttonR1C1);
				purple.setOnTouchListener(listener);
				
				Button set = (Button) findViewById(R.id.button5);
				
				set.setOnClickListener(new OnClickListener() {			
					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(GameActivity.this, SetActivity.class);
						startActivity(intent);
					}
				});
				
			}
        }catch(Exception e)
		{
			e.printStackTrace();
		}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    
   /* protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	try {
	    	ConnectServer.getInstance().connect(ip);
	    	ConnectServer.getInstance().sendAction(new AuthentificationAction(password));
    	} catch (Exception e) {
    		Toast.makeText(MainActivity.this, "start鏈嶅姟鍣ㄨ繛鎺ュけ璐�", Toast.LENGTH_SHORT).show();
    	}
    }*/
    	    
  @Override
  protected void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
    	ConnectServer.getInstance().setSocket(null);
    }
    	    
  @Override
  protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	ConnectServer.getInstance().close();
  }
    
}

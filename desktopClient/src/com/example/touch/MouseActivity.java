package com.example.touch;

import android.app.Activity;
import android.app.Service;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.action.AuthentificationAction;
import com.example.action.AuthentificationResponseAction;
import com.example.action.MouseClickAction;
import com.example.connection.ConnectServer;
import com.example.desktop.R;


public class MouseActivity extends Activity {

	private float screenDensity;
	private Vibrator vibrator;
	
	private String ip = TouchFlag.getInstance().getIp();
	private String password = TouchFlag.getInstance().getPwd();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //鍒濆鍖栧繀瑕佸弬鏁�
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mouse);
        screenDensity = getResources().getDisplayMetrics().density;
        SharedPreferences preferences  = PreferenceManager.getDefaultSharedPreferences(this);
		PreferenceManager.setDefaultValues(this, R.layout.mouse_settings, true);	
		vibrator = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
       
		 //寮�鍚嚎绋嬭繛鎺ユ湇鍔″櫒
		Thread thread = new Thread(new Runnable() {		
			@Override
			public void run() {
				// TODO Auto-generated method stub
					try {
						ConnectServer.getInstance().close();
						ConnectServer.getInstance().connect(ip);
						ConnectServer.getInstance().sendAction(new AuthentificationAction(password));
						AuthentificationResponseAction response = ConnectServer.getInstance().recvAction();
						TouchFlag.getInstance().setValid(response.authentificated);
						
					} catch (Exception e) {
						Thread.currentThread().interrupt();
					}
			}
		});
		thread.start();
		try
		{
			thread.join();
			if(TouchFlag.getInstance().isValid())
			{
				Toast.makeText(MouseActivity.this, "服务器连接成功", Toast.LENGTH_SHORT).show();
				Button clickLeft = (Button) findViewById(R.id.button1);
			    clickLeft.setOnTouchListener(new MouseTouchListener(MouseClickAction.BUTTON_LEFT,vibrator));
			    Button clickRight = (Button) findViewById(R.id.button2);
			    clickRight.setOnTouchListener(new MouseTouchListener(MouseClickAction.BUTTON_RIGHT,vibrator));
			    ImageView clickMove = (ImageView) findViewById(R.id.imageview);
			    clickMove.setOnTouchListener(new ToucherTouchListener(screenDensity,preferences,vibrator));
			    
			    Button scoll = (Button) findViewById(R.id.scoll);
			    scoll.setOnTouchListener(new ScollListener(screenDensity,preferences));
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
    
    

	/*protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		try {
			ConnectServer.getInstance().connect(ip);
			ConnectServer.getInstance().sendAction(new AuthentificationAction(password));
			isValid = ConnectServer.getInstance().recvAction();
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

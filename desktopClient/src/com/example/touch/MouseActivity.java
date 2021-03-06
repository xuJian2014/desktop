package com.example.touch;

import java.io.IOException;

import android.app.Activity;
import android.app.Service;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.InputDevice;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.action.MouseClickAction;
import com.example.connection.DeviceConnection;
import com.example.desktop.R;



public class MouseActivity extends Activity {

	private ImageView clickMove;
	private float screenDensity;
	private Vibrator vibrator;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //初始化必要参数
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mouse);
        screenDensity = getResources().getDisplayMetrics().density;
        SharedPreferences preferences  = PreferenceManager.getDefaultSharedPreferences(this);
		PreferenceManager.setDefaultValues(this, R.xml.mouse_settings, true);	
		vibrator = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
	
		 //开启线程连接服务器
			try
			{
				DeviceConnection.getInstance().changeDestination();
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
			
			if(DeviceConnection.getInstance().isAuthentificated())
			{
				Toast.makeText(MouseActivity.this, "服务器连接成功", Toast.LENGTH_SHORT).show();
				Button clickLeft = (Button) findViewById(R.id.button1);
			    clickLeft.setOnTouchListener(new MouseTouchListener(MouseClickAction.BUTTON_LEFT,vibrator));
			    Button clickRight = (Button) findViewById(R.id.button2);
			    clickRight.setOnTouchListener(new MouseTouchListener(MouseClickAction.BUTTON_RIGHT,vibrator));
			    clickMove = (ImageView) findViewById(R.id.imageview);
			    clickMove.setOnTouchListener(new ToucherTouchListener(screenDensity,preferences,vibrator));
			    clickMove.setOnGenericMotionListener(new BlueMousetListener());
			    Button scoll = (Button) findViewById(R.id.scoll);
			    scoll.setOnTouchListener(new ScollListener(screenDensity,preferences));
			    
			    
			}else
			{
				Toast.makeText(MouseActivity.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
				this.finish();
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
			Toast.makeText(MainActivity.this, "start服务器连接失败", Toast.LENGTH_SHORT).show();
		}
	}*/
 
    
    
  public boolean onGenericMotionEvent(MotionEvent event) {
    	
    	//获得输入源
    	
    	   if( DeviceConnection.getInstance().isAuthentificated() && (InputDevice.SOURCE_MOUSE == event.getSource()))
    	   {
    		   switch (event.getAction()) {
    	     	            
    	       case MotionEvent.ACTION_HOVER_MOVE:
    	    	   
    	    	   if(event.getRawX()<=clickMove.getLeft() || event.getRawX()>=clickMove.getRight() ||event.getRawY() <= clickMove.getTop() || event.getRawY()>= clickMove.getBottom())
    	    	   {
    	    		   Toast.makeText(MouseActivity.this, "请在指定位置滑动", Toast.LENGTH_SHORT).show();
    	    		   return true;
    	    	   }
    	    	       	    		   
    		   }
    	   }
    	   return false;
    }
   
    @Override
    protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		DeviceConnection.getInstance().close();
	}
    
    @Override
    protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		DeviceConnection.getInstance().close();
	}
}

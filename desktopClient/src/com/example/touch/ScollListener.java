package com.example.touch;



import android.content.SharedPreferences;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.example.action.MouseWheelAction;
import com.example.connection.ConnectServer;

public class ScollListener implements OnTouchListener{


	private SharedPreferences preferences;
	private float screenDensity;
	
	private float moveSensitivity;
	private float moveAcceleration;
	
	private float wheelPrevious;
	private float wheelResult;
	private float wheelSensitivity;
	private float wheelAcceleration;
	
	public ScollListener(float screenDensity, SharedPreferences preferences)
	{
		this.preferences = preferences;
		this.screenDensity = screenDensity;
		
		this.reloadPreferences();
	}
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		switch (event.getAction())
		{
			case MotionEvent.ACTION_MOVE:
			{
				this.onTouchMove(event);
				break;
			}			
			case MotionEvent.ACTION_DOWN:
			{
				this.onTouchDown(event);
				break;
			}			
			
			default:
				break;
		}	
		return true;
		
	}
	
	private void onTouchMove(MotionEvent event) {
		// TODO Auto-generated method stub
		float wheelRaw = event.getRawY() - this.wheelPrevious;
		wheelRaw *= this.wheelSensitivity;
		wheelRaw = (float) ((Math.pow(Math.abs(wheelRaw), this.wheelAcceleration) * Math.signum(wheelRaw)));
		wheelRaw += this.wheelResult;
		int wheelFinal = Math.round(wheelRaw);
		
		if (wheelFinal != 0)
		{
			Log.d("debug", wheelFinal+"");
			MouseWheelAction mouseWheel = new MouseWheelAction((byte)wheelFinal);
			ConnectServer.getInstance().sendAction(mouseWheel);
		}
		
		this.wheelResult = wheelRaw - wheelFinal;
		this.wheelPrevious = event.getRawY();
	}
	private void onTouchDown(MotionEvent event) {
		// TODO Auto-generated method stub
		this.wheelPrevious = event.getRawY();
		this.wheelResult = 0;
		return;
	}
	private void reloadPreferences()
	{
			
		this.moveSensitivity = Float.parseFloat(preferences.getString("control_sensitivity", null));
		this.moveSensitivity /= screenDensity;
		this.moveAcceleration = Float.parseFloat(preferences.getString("control_acceleration", null));
		this.wheelSensitivity = this.moveSensitivity / 10f;
		this.wheelAcceleration = this.moveAcceleration;
		
	}
}



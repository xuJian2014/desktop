package com.example.touch;




import java.io.IOException;

import android.content.SharedPreferences;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.example.action.MouseClickAction;
import com.example.action.MouseMoveAction;
import com.example.connection.DeviceConnection;

public class ToucherTouchListener implements OnTouchListener{

	private Vibrator vibrator;
	private SharedPreferences preferences;
	private float screenDensity;
	private float immobileDistance;
	
	private float moveSensitivity;
	private float moveAcceleration;
	private float moveDownX;
	private float moveDownY;
	private float movePreviousX;
	private float movePreviousY;
	private float moveResultX;
	private float moveResultY;
	
	
	private float wheelPrevious;
	private float wheelResult;
	private float wheelSensitivity;
	private float wheelAcceleration;
	private boolean clickAtMove = false;
	
	public ToucherTouchListener(float screenDensity,SharedPreferences preferences,Vibrator vibrator)
	{
		this.preferences = preferences;
		this.screenDensity = screenDensity;
		this.vibrator = vibrator;
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
			case MotionEvent.ACTION_UP:
			{
				this.onTouchUp(event);
				// Commented out since we're going to be in a loop
				// this.screenCaptureRequest();
				break;
			}		
			default:
				break;
		}	
		return true;
		
	}

	private void onTouchDown(MotionEvent event)
	{
		this.moveDownX = this.movePreviousX = event.getRawX();
		this.moveDownY = this.movePreviousY = event.getRawY();
		
		this.moveResultX = 0;
		this.moveResultY = 0;
		
		this.clickAtMove = true;
	}
	
	/*private void onTouchDownMouseWheel(MotionEvent event)
	{
		this.wheelPrevious = event.getRawY();
		this.wheelResult = 0;
	}*/
	
	private void onTouchMove(MotionEvent event)
	{
			this.clickAtMove = false;
			
			float moveRawX = event.getRawX() - this.movePreviousX;
			float moveRawY = event.getRawY() - this.movePreviousY;
			
			moveRawX *= this.moveSensitivity;
			moveRawY *= this.moveSensitivity;
			
			moveRawX = (float) ((Math.pow(Math.abs(moveRawX), this.moveAcceleration) * Math.signum(moveRawX)));
			moveRawY = (float) ((Math.pow(Math.abs(moveRawY), this.moveAcceleration) * Math.signum(moveRawY)));
			
			moveRawX += this.moveResultX;
			moveRawY += this.moveResultY;
			
			int moveXFinal = Math.round(moveRawX);
			int moveYFinal = Math.round(moveRawY);
			
			if (moveXFinal != 0 || moveYFinal != 0)
			{
				MouseMoveAction mouseMoveAction = new MouseMoveAction((short) moveXFinal, (short) moveYFinal);
				try
				{
					DeviceConnection.getInstance().sendAction(
							mouseMoveAction);
				} catch (IOException e)
				{
					DeviceConnection.getInstance().close();
					try
					{
						DeviceConnection.getInstance().connect();
					} catch (IOException e1)
					{
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
				this.moveResultX = moveRawX - moveXFinal;
				this.moveResultY = moveRawY - moveYFinal;		
				this.movePreviousX = event.getRawX();
				this.movePreviousY = event.getRawY();
			}
		
	}
	
	private void onTouchUp(MotionEvent event)
	{
		if(this.clickAtMove == true)
		{
			vibrator.vibrate(new long[]{0,100}, -1);
			MouseClickAction mouseDown = new MouseClickAction(MouseClickAction.BUTTON_LEFT, MouseClickAction.STATE_DOWN);
			try
			{
				DeviceConnection.getInstance().sendAction(
						mouseDown);
			} catch (IOException e)
			{
				DeviceConnection.getInstance().close();
				try
				{
					DeviceConnection.getInstance().connect();
				} catch (IOException e1)
				{
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
			MouseClickAction mouseUp = new MouseClickAction(MouseClickAction.BUTTON_LEFT, MouseClickAction.STATE_UP);
			try
			{
				DeviceConnection.getInstance().sendAction(
						mouseUp);
			} catch (IOException e)
			{
				DeviceConnection.getInstance().close();
				try
				{
					DeviceConnection.getInstance().connect();
				} catch (IOException e1)
				{
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}	
	}
	private double getDistanceFromDown(MotionEvent event)
	{
		return Math.sqrt(Math.pow(event.getRawX() - this.moveDownX, 2) + Math.pow(event.getRawY() - this.moveDownY, 2));
	}
	
	private void reloadPreferences()
	{
		
		
		this.immobileDistance = Float.parseFloat(preferences.getString("control_immobile_distance", null));
		this.immobileDistance *= screenDensity;
		
		this.moveSensitivity = Float.parseFloat(preferences.getString("control_sensitivity", null));
		this.moveSensitivity /= screenDensity;
		this.moveAcceleration = Float.parseFloat(preferences.getString("control_acceleration", null));
		this.wheelSensitivity = this.moveSensitivity / 10f;
		this.wheelAcceleration = this.moveAcceleration;
		
	}
	
	
	
}

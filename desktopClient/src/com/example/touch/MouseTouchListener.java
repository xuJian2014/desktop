package com.example.touch;

import java.io.IOException;

import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.example.action.MouseClickAction;
import com.example.connection.DeviceConnection;



public class MouseTouchListener implements OnTouchListener{

	private byte button;
	private Vibrator vibrator;
	
	public MouseTouchListener(byte button,Vibrator vibrator)
	{
		
		this.button = button;
		this.vibrator = vibrator;
	}
	
	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		switch (event.getAction())
		{
			case MotionEvent.ACTION_MOVE:
			{
				//this.onTouchMove(event);
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
				break;
			}				
			default:
				break;
		}		
		return true;		
	}
	private void onTouchDown(MotionEvent event)
	{
		vibrator.vibrate(new long[]{0,100}, -1);
		MouseClickAction mouseCliekAction = new MouseClickAction(this.button, MouseClickAction.STATE_DOWN);
		try
		{
			DeviceConnection.getInstance().sendAction(
					mouseCliekAction);
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
	
	/*private void onTouchMove(MotionEvent event)
	{
		if (!this.hold && event.getEventTime() - event.getDownTime() >= this.holdDelay)
		{
			this.hold = true;
			
			this.application.vibrate(100);
		}
	}*/
	
	private void onTouchUp(MotionEvent event)
	{
		
		MouseClickAction mouseCliekAction = new MouseClickAction(this.button, MouseClickAction.STATE_UP);
		try
		{
			DeviceConnection.getInstance().sendAction(mouseCliekAction);
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

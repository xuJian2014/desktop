package com.example.touch;

import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.example.action.MouseClickAction;
import com.example.connection.ConnectServer;



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
		ConnectServer.getInstance().sendAction(mouseCliekAction);
		
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
		ConnectServer.getInstance().sendAction(mouseCliekAction);
	}

}

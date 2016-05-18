package com.example.touch;

import java.io.IOException;

import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnGenericMotionListener;

import com.example.action.MouseMoveAction;
import com.example.action.MouseWheelAction;
import com.example.connection.DeviceConnection;

public class BlueMousetListener implements OnGenericMotionListener{

	private float moveSensitivity =0.7f;
	private float moveAcceleration= 1.4f;
	private float moveDownX;
	private float moveDownY;
	private float movePreviousX;
	private float movePreviousY;
	private float moveResultX;
	private float moveResultY;
	
	@Override
	public boolean onGenericMotion(View arg0, MotionEvent event) {

		 if(InputDevice.SOURCE_MOUSE == event.getSource())
   	   {
   		   switch (event.getAction()) {
   		   
   		   //滚轮可以用
   	        case MotionEvent.ACTION_SCROLL:
   	        	if( event.getAxisValue(MotionEvent.AXIS_VSCROLL) < 0.0f){	
   	        		MouseWheelAction mouseWheel = new MouseWheelAction((byte)1);
   	        		try
   					{
   						DeviceConnection.getInstance().sendAction(mouseWheel);
   					} catch (IOException e)
   					{
   						e.printStackTrace();
   					}
       				event.setLocation(50f, 50f);
   	        	}
   	        	else if(event.getAxisValue(MotionEvent.AXIS_VSCROLL) > 0.0f)
   	        	{
   	        		MouseWheelAction mouseWheel = new MouseWheelAction((byte)-1);
   	        		try
   					{
   						DeviceConnection.getInstance().sendAction(mouseWheel);
   					} catch (IOException e)
   					{
   						e.printStackTrace();
   					}
   	        	}		    	        		    	        	
   	            return true;
   	            
   	       case MotionEvent.ACTION_HOVER_MOVE:
				try {
					Thread.sleep(25);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
   	    	   	float moveRawX = event.getX() - this.movePreviousX;
   				float moveRawY = event.getY() - this.movePreviousY;
   				
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
   						DeviceConnection.getInstance().sendAction(mouseMoveAction);
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
   					this.movePreviousX = event.getX();
   					this.movePreviousY = event.getY();
   				}
   	            return true;
   	       
   		   }
   		   
   	   }
		return false;
	}
}


package com.example.controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.action.PowerOffAction;
import com.example.connection.ConnectServer;
import com.example.desktop.R;

public class PowerActivity extends Activity {
	private OnBtnClickListenerImpl listener;

	private Button powerOff;
	private Button powerSleep;
	private Button powerRestart;
	private Button powerLock;
	private Button powerSleepOff;
	private Button powerCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_power);

		listener = new OnBtnClickListenerImpl();

		powerOff = (Button) findViewById(R.id.ButtonPowerOff);
		powerOff.setOnClickListener(listener);

		powerSleep = (Button) findViewById(R.id.ButtonPowerSleep);
		powerSleep.setOnClickListener(listener);

		powerRestart = (Button) findViewById(R.id.ButtonPowerRestart);
		powerRestart.setOnClickListener(listener);

		powerLock = (Button) findViewById(R.id.ButtonPowerLock);
		powerLock.setOnClickListener(listener);

		powerSleepOff = (Button) findViewById(R.id.ButtonPowerSleepOff);
		powerSleepOff.setOnClickListener(listener);

		powerCancel = (Button) findViewById(R.id.ButtonPowerCancel);
		powerCancel.setOnClickListener(listener);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.power, menu);
		return true;
	}

	protected class OnBtnClickListenerImpl implements OnClickListener {

		@Override
		public void onClick(View view) {
			PowerOffAction action = null;
			switch (view.getId()) {
				case R.id.ButtonPowerOff :
					action = new PowerOffAction(PowerOffAction.POWER_OFF);
					break;
				case R.id.ButtonPowerSleep :
					action = new PowerOffAction(PowerOffAction.POWER_SLEEP);
					break;
				case R.id.ButtonPowerRestart :
					action = new PowerOffAction(PowerOffAction.POWER_RESTART);
					break;
				case R.id.ButtonPowerLock :
					action = new PowerOffAction(PowerOffAction.POWER_LOCK);
					break;
				case R.id.ButtonPowerSleepOff :
					action = new PowerOffAction(PowerOffAction.POWER_SLEPP_OFF);
					break;
				case R.id.ButtonPowerCancel :
					action = new PowerOffAction(PowerOffAction.POWER_OFF_CANCEL);
					break;
				default :
					break;
			}
			if (action != null) {
				ConnectServer.getInstance().sendAction(action);
			}
		}
	}

}

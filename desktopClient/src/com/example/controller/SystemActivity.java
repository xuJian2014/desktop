package com.example.controller;

import java.util.logging.Logger;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.action.SystemControlAction;
import com.example.connection.ConnectServer;
import com.example.desktop.R;

public class SystemActivity extends Activity
{
	private OnBtnClickListenerImpl listener;

	private Button powerOff;
	private Button powerSleep;
	private Button powerRestart;
	private Button powerLock;
	private Button powerSleepOff;
	private Button powerCancel;

	private Button sysVolumeUp;
	private Button sysVolumeDown;
	private Button sysVolumeMute;

	Logger log = Logger.getAnonymousLogger();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_systemcontrol);
		getActionBar().setDisplayUseLogoEnabled(true);
		getActionBar().setIcon(null);
		getActionBar().setDisplayOptions(
				ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME
						| ActionBar.DISPLAY_HOME_AS_UP);
		getActionBar().setDisplayUseLogoEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);

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

		sysVolumeMute = (Button) findViewById(R.id.buttonSysVolumeMute);
		sysVolumeMute.setOnClickListener(listener);

		sysVolumeUp = (Button) findViewById(R.id.buttonSysVolumeUp);
		sysVolumeUp.setOnClickListener(listener);

		sysVolumeDown = (Button) findViewById(R.id.buttonSysVolumeDown);
		sysVolumeDown.setOnClickListener(listener);
	}



	protected class OnBtnClickListenerImpl implements OnClickListener
	{

		@Override
		public void onClick(View view)
		{
			SystemControlAction action = null;
			switch (view.getId())
			{
			case R.id.ButtonPowerOff:
				action = new SystemControlAction(SystemControlAction.POWER_OFF);
				break;
			case R.id.ButtonPowerSleep:
				action = new SystemControlAction(
						SystemControlAction.POWER_SLEEP);
				break;
			case R.id.ButtonPowerRestart:
				action = new SystemControlAction(
						SystemControlAction.POWER_RESTART);
				break;
			case R.id.ButtonPowerLock:
				action = new SystemControlAction(SystemControlAction.POWER_LOCK);
				break;
			case R.id.ButtonPowerSleepOff:
				action = new SystemControlAction(
						SystemControlAction.POWER_SLEPP_OFF);
				break;
			case R.id.ButtonPowerCancel:
				action = new SystemControlAction(
						SystemControlAction.POWER_OFF_CANCEL);
				break;

			case R.id.buttonSysVolumeMute:
				log.info("---------------------" + "mute");
				action = new SystemControlAction(
						SystemControlAction.VOLUME_MUTE);
				break;
			case R.id.buttonSysVolumeUp:
				log.info("---------------------" + "up");
				action = new SystemControlAction(SystemControlAction.VOLUME_UP);
				break;
			case R.id.buttonSysVolumeDown:
				log.info("---------------------" + "down");
				action = new SystemControlAction(
						SystemControlAction.VOLUME_DOWN);
				break;
			default:
				break;
			}
			if (action != null)
			{
				ConnectServer.getInstance().sendAction(action);
			}
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}

package com.example.controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.action.WindowControlAction;
import com.example.connection.ConnectServer;
import com.example.desktop.R;

public class WindowActivity extends Activity {

	private Button windowMax;
	private Button windowMin;
	private Button windowSwitchRight;
	private Button windowSwitchLeft;
	private Button windowClose;
	private Button windowDesktop;

	private OnBtnClickListenerImpl listener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_window);

		listener = new OnBtnClickListenerImpl();

		windowMax = (Button) findViewById(R.id.ButtonWindowMax);
		windowMax.setOnClickListener(listener);

		windowMin = (Button) findViewById(R.id.ButtonWindowMin);
		windowMin.setOnClickListener(listener);

		windowSwitchRight = (Button) findViewById(R.id.ButtonWindowSwitchRight);
		windowSwitchRight.setOnClickListener(listener);

		windowSwitchLeft = (Button) findViewById(R.id.ButtonWindowSwitchLeft);
		windowSwitchLeft.setOnClickListener(listener);

		windowClose = (Button) findViewById(R.id.ButtonWindowClose);
		windowClose.setOnClickListener(listener);

		windowDesktop = (Button) findViewById(R.id.ButtonWindowDesktop);
		windowDesktop.setOnClickListener(listener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.window, menu);
		return true;
	}

	protected class OnBtnClickListenerImpl implements OnClickListener {

		@Override
		public void onClick(View view) {
			WindowControlAction action = null;
			switch (view.getId()) {
				case R.id.ButtonWindowMax :
					action = new WindowControlAction(
							WindowControlAction.WIN_MAX);
					break;
				case R.id.ButtonWindowMin :
					action = new WindowControlAction(
							WindowControlAction.WIN_MIN);
					break;
				case R.id.ButtonWindowSwitchRight :
					action = new WindowControlAction(
							WindowControlAction.WIN_SWITCH_RIGHT);
					break;
				case R.id.ButtonWindowSwitchLeft :
					action = new WindowControlAction(
							WindowControlAction.WIN_SWITCH_LEFT);
					break;
				case R.id.ButtonWindowClose :
					action = new WindowControlAction(
							WindowControlAction.WIN_CLOSE);
					break;
				case R.id.ButtonWindowDesktop :
					action = new WindowControlAction(
							WindowControlAction.WIN_DESKTOP);
					break;
				default :
					break;
			}
			if (action != null) {
				if (ConnectServer.getInstance() != null) {
					ConnectServer.getInstance().sendAction(action);
				}
			}
		}
	}

}
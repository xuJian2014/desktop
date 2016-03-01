package com.example.controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.action.ProjectionAction;
import com.example.connection.ConnectServer;
import com.example.desktop.R;

public class ProjectionActivity extends Activity {

	private Button play;
	private Button playCurrent;
	private Button exit;
	private Button previous;
	private Button next;

	private OnBtnClickListenerImpl listener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_projection);

		listener = new OnBtnClickListenerImpl();

		play = (Button) findViewById(R.id.ButtonProjectionPlay);
		play.setOnClickListener(listener);

		playCurrent = (Button) findViewById(R.id.buttonProjectionPlayCurrent);
		playCurrent.setOnClickListener(listener);

		exit = (Button) findViewById(R.id.buttonProjectionExit);
		exit.setOnClickListener(listener);

		previous = (Button) findViewById(R.id.buttonProjectionPrevious);
		previous.setOnClickListener(listener);

		next = (Button) findViewById(R.id.buttonProjectionNext);
		next.setOnClickListener(listener);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.projection, menu);
		return true;
	}

	protected class OnBtnClickListenerImpl implements OnClickListener {

		@Override
		public void onClick(View view) {
			ProjectionAction action = null;
			switch (view.getId()) {
				case R.id.ButtonProjectionPlay :
					action = new ProjectionAction(ProjectionAction.PLAY);
					break;
				case R.id.buttonProjectionPlayCurrent :
					action = new ProjectionAction(ProjectionAction.PLAY_CURRENT);
					break;
				case R.id.buttonProjectionExit :
					action = new ProjectionAction(ProjectionAction.EXIT);
					break;
				case R.id.buttonProjectionPrevious :
					action = new ProjectionAction(ProjectionAction.PREV);
					break;
				case R.id.buttonProjectionNext :
					action = new ProjectionAction(ProjectionAction.NEXT);
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

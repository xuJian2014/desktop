package com.example.controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.action.SystemControlAction;
import com.example.connection.ConnectServer;
import com.example.desktop.R;

public class MediaCtlActivity extends Activity {

	private ImageButton prev;
	private ImageButton next;
	private ImageButton play;
	private ImageButton stop;

	private Button sysVolumeUp;
	private Button sysVolumeDown;
	private Button sysVolumeMute;

	private Button mediaVolumeUp;
	private Button mediaVolumeDown;
	private Button mediaVolumeMute;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mediactl);

		OnClickListener listener = new OnBtnClickListenerImpl();
		prev = (ImageButton) findViewById(R.id.imageButtonPrev);
		prev.setOnClickListener(listener);
		next = (ImageButton) findViewById(R.id.imageButtonNext);
		next.setOnClickListener(listener);
		play = (ImageButton) findViewById(R.id.imageButtonPlay);
		play.setOnClickListener(listener);
		stop = (ImageButton) findViewById(R.id.imageButtonStop);
		stop.setOnClickListener(listener);

		sysVolumeMute = (Button) findViewById(R.id.buttonSysVolumeMute);
		sysVolumeMute.setOnClickListener(listener);
		sysVolumeUp = (Button) findViewById(R.id.buttonSysVolumeUp);
		sysVolumeUp.setOnClickListener(listener);
		sysVolumeDown = (Button) findViewById(R.id.buttonSysVolumeDown);
		sysVolumeDown.setOnClickListener(listener);

		mediaVolumeMute = (Button) findViewById(R.id.buttonMediaVolumeMute);
		mediaVolumeMute.setOnClickListener(listener);
		mediaVolumeUp = (Button) findViewById(R.id.buttonMediaVolumeUp);
		mediaVolumeUp.setOnClickListener(listener);
		mediaVolumeDown = (Button) findViewById(R.id.buttonMediaVolumeDown);
		mediaVolumeDown.setOnClickListener(listener);

	}

	private class OnBtnClickListenerImpl implements OnClickListener {

		@Override
		public void onClick(View view) {
			SystemControlAction action = null;
			switch (view.getId()) {
				case R.id.imageButtonPrev :
					action = new SystemControlAction(
							SystemControlAction.MEDIA_PREV);
					break;
				case R.id.imageButtonNext :
					action = new SystemControlAction(
							SystemControlAction.MEDIA_NEXT);
					break;
				case R.id.imageButtonPlay :
					action = new SystemControlAction(
							SystemControlAction.MEDIA_PAUSE);
					break;
				case R.id.imageButtonStop :
					action = new SystemControlAction(
							SystemControlAction.MEDIA_STOP);
					break;
				case R.id.buttonSysVolumeMute :
					action = new SystemControlAction(
							SystemControlAction.VOLUME_MUTE);
					break;
				case R.id.buttonSysVolumeUp :
					action = new SystemControlAction(
							SystemControlAction.VOLUME_UP);
					break;
				case R.id.buttonSysVolumeDown :
					action = new SystemControlAction(
							SystemControlAction.VOLUME_DOWN);
					break;
				case R.id.buttonMediaVolumeMute :
					// action = new SystemControlAction(
					// SystemControlAction.MEDIA_VOLUME_MUTE);
					break;
				case R.id.buttonMediaVolumeUp :
					action = new SystemControlAction(
							SystemControlAction.MEDIA_VOLUME_UP);
					break;
				case R.id.buttonMediaVolumeDown :
					action = new SystemControlAction(
							SystemControlAction.MEDIA_VOLUME_DOWN);
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

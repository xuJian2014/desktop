package com.example.controller;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.example.action.AuthentificationAction;
import com.example.action.AuthentificationResponseAction;
import com.example.connection.ConnectServer;
import com.example.desktop.R;
import com.example.touch.TouchFlag;

public class Control_MainActivity extends Activity {

	private final boolean debug = true;
	private GridView gridView;
	private GridAdapter mAdapter;

	private ArrayList<Integer> mDrawableList;
	private ArrayList<String> mNameList;
	
	//protected String ip = TouchFlag.getInstance().getIp();
	//protected String password = TouchFlag.getInstance().getPwd();
	String ip="192.168.1.110";
	String password="1234";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control_activity_main);

		mDrawableList = new ArrayList<Integer>();
		mNameList = new ArrayList<String>();
		mNameList.add("媒体控制");
		mNameList.add("浏览器控制");
		mNameList.add("演示控制");
		mNameList.add("窗口控制");
		// mNameList.add("远程应用");
		mNameList.add("关机");

		for (int i = 0; i < mNameList.size(); i++) {
			mDrawableList.add(R.drawable.control_ic_launcher);
		}

		gridView = (GridView) findViewById(R.id.grid);
		mAdapter = new GridAdapter(this, mNameList, mDrawableList);
		gridView.setAdapter(mAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String text = (String) mAdapter.getItem(position);
				if (debug) {
					Toast.makeText(Control_MainActivity.this, text, Toast.LENGTH_SHORT)
							.show();
				}
				if ("媒体控制".equals(text)) {
					mediaControl();
				} else if ("浏览器控制".equals(text)) {
					browserControl();
				} else if ("演示控制".equals(text)) {
					projectionControl();
				} else if ("窗口控制".equals(text)) {
					windowControl();
				} else if ("远程应用".equals(text)) {
					remoteApplication();
				} else if ("关机".equals(text)) {
					powerOff();
				}
			}
		});
		
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					ConnectServer.getInstance().close();
					ConnectServer.getInstance().connect(ip);
					ConnectServer.getInstance().sendAction(
							new AuthentificationAction(password));
					AuthentificationResponseAction response = ConnectServer
							.getInstance().recvAction();
					TouchFlag.getInstance().setAuthentificated(
							response.authentificated);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread.start();
		
		try {
			thread.join();
			if (TouchFlag.getInstance().isAuthentificated()) {
				Toast.makeText(Control_MainActivity.this, "服务器连接成功",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(Control_MainActivity.this, "服务器连接失败",
						Toast.LENGTH_SHORT).show();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.control_main, menu);
		return true;
	}
	
	protected void browserControl() {
		Intent i = new Intent();
		i.setClass(Control_MainActivity.this, BrowserActivity.class);
		startActivity(i);
	}

	protected void mediaControl() {
		Intent i = new Intent();
		i.setClass(Control_MainActivity.this, MediaCtlActivity.class);
		startActivity(i);
	}

	protected void powerOff() {
		Intent i = new Intent();
		i.setClass(Control_MainActivity.this, PowerActivity.class);
		startActivity(i);
	}

	protected void projectionControl() {
		Intent i = new Intent();
		i.setClass(Control_MainActivity.this, ProjectionActivity.class);
		startActivity(i);
	}

	protected void remoteApplication() {

	}

	protected void windowControl() {
		Intent i = new Intent();
		i.setClass(Control_MainActivity.this, WindowActivity.class);
		startActivity(i);
	}

	@Override
	protected void onDestroy() {
		ConnectServer.getInstance().close();
		ConnectServer.getInstance().setSocket(null);
		super.onDestroy();
	}
}

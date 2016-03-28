package com.example.controller;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class Control_MainActivity extends Activity
{

	private final boolean debug = true;
	private GridView gridView;
	private GridAdapter mAdapter;

	private ArrayList<Integer> mDrawableList;
	private ArrayList<String> mNameList;

	// protected String ip = TouchFlag.getInstance().getIp();
	// protected String password = TouchFlag.getInstance().getPwd();
	String ip = "192.168.1.110";
	String password = "1234";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control_activity_main);

		getActionBar().setDisplayUseLogoEnabled(true);
		getActionBar().setIcon(null);
		getActionBar().setDisplayOptions(
				ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME
						| ActionBar.DISPLAY_HOME_AS_UP);
		getActionBar().setDisplayUseLogoEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		mDrawableList = new ArrayList<Integer>();
		mNameList = new ArrayList<String>();
		mNameList.add("���������");
		mNameList.add("��ʾ����");
		mNameList.add("���ڿ���");
		mNameList.add("ϵͳ����");
		
		mDrawableList.add(R.drawable.control_browse);
		mDrawableList.add(R.drawable.control_projection);
		mDrawableList.add(R.drawable.control_change);
		mDrawableList.add(R.drawable.control_setting);
			
		gridView = (GridView) findViewById(R.id.grid);
		mAdapter = new GridAdapter(this, mNameList, mDrawableList);
		gridView.setAdapter(mAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				String text = (String) mAdapter.getItem(position);
				if (debug)
				{
					Toast.makeText(Control_MainActivity.this, text,
							Toast.LENGTH_SHORT).show();
				}
				if ("���������".equals(text))
				{
					browserControl();
				} else if ("��ʾ����".equals(text))
				{
					projectionControl();
				} else if ("���ڿ���".equals(text))
				{
					windowControl();
				} else if ("ϵͳ����".equals(text))
				{
					powerOff();
				}
			}
		});

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run()
			{
				try
				{
					ConnectServer.getInstance().close();
					ConnectServer.getInstance().connect(ip);
					ConnectServer.getInstance().sendAction(
							new AuthentificationAction(password));
					AuthentificationResponseAction response = (AuthentificationResponseAction) ConnectServer
							.getInstance().recvAction();
					TouchFlag.getInstance().setAuthentificated(
							response.authentificated);
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread.start();

		try
		{
			thread.join();
			if (TouchFlag.getInstance().isAuthentificated())
			{
				Toast.makeText(Control_MainActivity.this, "���������ӳɹ�",
						Toast.LENGTH_LONG).show();
			} else
			{
				Toast.makeText(Control_MainActivity.this, "����������ʧ��",
						Toast.LENGTH_SHORT).show();
			}
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}



	protected void browserControl()
	{
		Intent i = new Intent();
		i.setClass(Control_MainActivity.this, BrowserActivity.class);
		startActivity(i);
	}

	protected void powerOff()
	{
		Intent i = new Intent();
		i.setClass(Control_MainActivity.this, SystemActivity.class);
		startActivity(i);
	}

	protected void projectionControl()
	{
		Intent i = new Intent();
		i.setClass(Control_MainActivity.this, ProjectionActivity.class);
		startActivity(i);
	}

	protected void remoteApplication()
	{

	}

	protected void windowControl()
	{
		Intent i = new Intent();
		i.setClass(Control_MainActivity.this, WindowActivity.class);
		startActivity(i);
	}

	@Override
	protected void onDestroy()
	{
		ConnectServer.getInstance().close();
		ConnectServer.getInstance().setSocket(null);
		super.onDestroy();
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

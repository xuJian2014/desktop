package com.example.controller;

import java.io.IOException;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.action.SystemControlAction;
import com.example.action.WindowControlAction;
import com.example.connection.DeviceConnection;
import com.example.desktop.R;

public class Control_MainActivity extends Activity
{

	private final boolean debug = true;
	private GridView gridViewApp;
	private GridAdapter mAdapterApp;

	private GridView gridViewSystem;
	private GridAdapter mAdapterSystem;

	private GridView gridViewWindows;
	private GridAdapter mAdapterWindows;

	private GridView gridViewVolumn;
	private GridAdapter mAdapterVolumn;

	private ArrayList<Integer> mDrawableList;
	private ArrayList<String> mNameList;

	
	protected boolean longPress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control_activity_main);

		getActionBar().setDisplayUseLogoEnabled(true);
		getActionBar().setIcon(null);
		getActionBar().setDisplayOptions(
				ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME
						| ActionBar.DISPLAY_HOME_AS_UP);
		getActionBar().setDisplayUseLogoEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		initGridViewApp();
		setGrideViewHeightBasedOnChildren(gridViewApp, 2);

		initGridViewVolumn();
		setGrideViewHeightBasedOnChildren(gridViewVolumn, 3);

		initGridViewSystem();
		setGrideViewHeightBasedOnChildren(gridViewSystem, 2);

		initGridViewWindows();
		setGrideViewHeightBasedOnChildren(gridViewWindows, 2);
		
		try
		{
			DeviceConnection.getInstance().changeDestination();
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

			if (DeviceConnection.getInstance().isAuthentificated())
			{
				Toast.makeText(Control_MainActivity.this, "服务器连接成功",
						Toast.LENGTH_LONG).show();
			} else
			{
				Toast.makeText(Control_MainActivity.this, "服务器连接失败",
						Toast.LENGTH_SHORT).show();
			}

	}
	private void initGridViewVolumn() {
		gridViewVolumn = (GridView) findViewById(R.id.gridVolumn);

		mDrawableList = new ArrayList<Integer>();
		mNameList = new ArrayList<String>();
		mNameList.add("音量减");
		mNameList.add("静音");
		mNameList.add("音量加");

		mDrawableList.add(R.drawable.control_minus100);
		mDrawableList.add(R.drawable.control_mute100);
		mDrawableList.add(R.drawable.control_plus100);

		mAdapterVolumn = new GridAdapter(this, mNameList, mDrawableList);
		gridViewVolumn.setAdapter(mAdapterVolumn);

		gridViewVolumn.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SystemControlAction action = null;
				switch (position) {
					case 0 :
						action = new SystemControlAction(
								SystemControlAction.VOLUME_DOWN);
						break;
					case 1 :
						action = new SystemControlAction(
								SystemControlAction.VOLUME_MUTE);
						break;
					case 2 :
						action = new SystemControlAction(
								SystemControlAction.VOLUME_UP);
						break;
					default :
						break;
				}

				if (action != null) {
					try
					{
						DeviceConnection.getInstance().sendAction(action);
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
					}				}
			}
		});
	}
	private void initGridViewWindows() {
		gridViewWindows = (GridView) findViewById(R.id.gridWindows);

		mDrawableList = new ArrayList<Integer>();
		mNameList = new ArrayList<String>();
		mNameList.add("最大化");
		mNameList.add("最小化");
		mNameList.add("向左切换");
		mNameList.add("向右切换");
		mNameList.add("关闭窗口");
		mNameList.add("显示桌面");

		mDrawableList.add(R.drawable.control_maxwindow100);
		mDrawableList.add(R.drawable.control_minwindow100);
		mDrawableList.add(R.drawable.control_switchleft100);
		mDrawableList.add(R.drawable.control_switchright100);
		mDrawableList.add(R.drawable.control_closewindow100);
		mDrawableList.add(R.drawable.control_windows100);

		mAdapterWindows = new GridAdapter(this, mNameList, mDrawableList);
		gridViewWindows.setAdapter(mAdapterWindows);
		gridViewWindows.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				WindowControlAction action = null;
				switch (position) {
					case 0 :
						action = new WindowControlAction(
								WindowControlAction.WIN_MAX);
						break;
					case 1 :
						action = new WindowControlAction(
								WindowControlAction.WIN_MIN);
						break;
					case 2 :
						action = new WindowControlAction(
								WindowControlAction.WIN_SWITCH_LEFT);
						break;
					case 3 :
						action = new WindowControlAction(
								WindowControlAction.WIN_SWITCH_RIGHT);
						break;
					case 4 :
						action = new WindowControlAction(
								WindowControlAction.WIN_CLOSE);
						break;
					case 5 :
						action = new WindowControlAction(
								WindowControlAction.WIN_DESKTOP);
						break;
					default :
						break;
				}
				if (action != null) {
					try
					{
						DeviceConnection.getInstance().sendAction(action);
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
		});
	}

	private void initGridViewSystem() {
		gridViewSystem = (GridView) findViewById(R.id.gridSystem);

		mDrawableList = new ArrayList<Integer>();
		mNameList = new ArrayList<String>();
		mNameList.add("关机");
		mNameList.add("重启");
		mNameList.add("睡眠");
		mNameList.add("休眠");
		mNameList.add("锁定");
		mNameList.add("取消");

		mDrawableList.add(R.drawable.control_shutdown100);
		mDrawableList.add(R.drawable.control_restart100);
		mDrawableList.add(R.drawable.control_sleep100);
		mDrawableList.add(R.drawable.control_hibernate100);
		mDrawableList.add(R.drawable.control_lock100);
		mDrawableList.add(R.drawable.control_cancel100);

		mAdapterSystem = new GridAdapter(this, mNameList, mDrawableList);
		gridViewSystem.setAdapter(mAdapterSystem);
		gridViewSystem.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SystemControlAction action = null;

				switch (position) {
					case 0 :
						action = new SystemControlAction(
								SystemControlAction.POWER_OFF);
						break;
					case 1 :
						action = new SystemControlAction(
								SystemControlAction.POWER_RESTART);
						break;
					case 2 :
						AlertDialog.Builder build = new AlertDialog.Builder(
								Control_MainActivity.this);
						final Dialog dialog = build
								.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
												SystemControlAction action = new SystemControlAction(
														SystemControlAction.POWER_SLEEP);
												if (action != null) {
													try
													{
														DeviceConnection.getInstance().sendAction(action);
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

										}).setNegativeButton("取消", null)
								.setTitle("确定吗？").create();
						dialog.show();
						break;
					case 3 :
						AlertDialog.Builder build1 = new AlertDialog.Builder(
								Control_MainActivity.this);
						final Dialog dialog1 = build1
								.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
												SystemControlAction action = new SystemControlAction(
														SystemControlAction.POWER_SLEPP_OFF);
												if (action != null) {
													try
													{
														DeviceConnection.getInstance().sendAction(action);
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

										}).setNegativeButton("取消", null)
								.setTitle("确定吗？").create();
						dialog1.show();
						break;
					case 4 :
						AlertDialog.Builder build11 = new AlertDialog.Builder(
								Control_MainActivity.this);
						final Dialog dialog11 = build11
								.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
												SystemControlAction action = new SystemControlAction(
														SystemControlAction.POWER_LOCK);
												if (action != null) {
													try
													{
														DeviceConnection.getInstance().sendAction(action);
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

										}).setNegativeButton("取消", null)
								.setTitle("确定吗？").create();
						dialog11.show();
						break;
					case 5 :
						action = new SystemControlAction(
								SystemControlAction.POWER_OFF_CANCEL);
						break;
					default :
						break;
				}
				if (action != null) {
					try
					{
						DeviceConnection.getInstance().sendAction(action);
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
		});
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
	private void initGridViewApp() {
		gridViewApp = (GridView) findViewById(R.id.gridApp);

		mDrawableList = new ArrayList<Integer>();
		mNameList = new ArrayList<String>();
		mNameList.add("浏览器控制");
		mNameList.add("演示控制");

		mDrawableList.add(R.drawable.control_browse100);
		mDrawableList.add(R.drawable.control_projection100);

		mAdapterApp = new GridAdapter(this.getApplicationContext(), mNameList, mDrawableList);
		gridViewApp.setAdapter(mAdapterApp);
		gridViewApp.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String text = (String) mAdapterApp.getItem(position);
				if (debug) {
					Toast.makeText(Control_MainActivity.this, text, Toast.LENGTH_SHORT)
							.show();
				}

				if ("浏览器控制".equals(text)) {
					browserControl();
				} else if ("演示控制".equals(text)) {
					projectionControl();
				}
			}
		});
	}

	protected void projectionControl() {
		Intent i = new Intent();
		i.setClass(Control_MainActivity.this, ProjectionActivity.class);
		startActivity(i);
	}

	protected void browserControl() {
		Intent i = new Intent();
		i.setClass(Control_MainActivity.this, BrowserActivity.class);
		startActivity(i);
	}

	public void setGrideViewHeightBasedOnChildren(GridView grid, int column) {
		ListAdapter adapter = grid.getAdapter();
		if (adapter == null) {
			return;
		}

		int totalHeight = 0;

		View listItem = adapter.getView(0, null, grid);
		listItem.measure(0, 0);
		if (adapter.getCount() - 1 < 0) {
			totalHeight = listItem.getMeasuredHeight();
		} else {
			int line = adapter.getCount() / column;
			if (adapter.getCount() % column != 0)
				line = line + 1;
			totalHeight = (listItem.getMeasuredHeight()) * line;
		}

		ViewGroup.LayoutParams params = grid.getLayoutParams();
		params.height = totalHeight + 5;
		((MarginLayoutParams) params).setMargins(0, 0, 0, 0);
		grid.setLayoutParams(params);
	}

	@Override
	protected void onDestroy() {
		DeviceConnection.getInstance().close();
		super.onDestroy();
	}
}

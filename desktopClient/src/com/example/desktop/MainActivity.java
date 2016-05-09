package com.example.desktop;
import android.androidVNC.VncCanvasActivity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.fragment.ApplicationManagerFragment;
import com.example.fragment.HomeControlFragment;
import com.example.fragment.HomeFragment;
import com.example.fragment.SettingFragment;
import com.example.touch.AuxiliaryService;

public class MainActivity extends FragmentActivity
{
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] serviceTitles;
	SharedPreferences sharedConfigInfo;
	
	String connectionIP;
	String connectionPwd;
	SharedPreferences getVNCPreferences;
	Editor vncEditor;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,GravityCompat.START);
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_list_item, serviceTitles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		getActionBar().setDisplayUseLogoEnabled(true);
		getActionBar().setIcon(null);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_HOME_AS_UP);
		
		getActionBar().setDisplayUseLogoEnabled(false);
		getActionBar().setHomeButtonEnabled(true);//设置返回键可用
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.drawable.ic_drawer, R.string.drawer_open,R.string.drawer_close) 
		{
			public void onDrawerClosed(View view) 
			{
					getActionBar().setTitle(mTitle);
					invalidateOptionsMenu();
			}
			public void onDrawerOpened(View drawerView) 
			{
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		if (savedInstanceState == null)
		{
			selectItem(0);
		}
		
	}
	
	private void init() 
	{
		mTitle = mDrawerTitle = getTitle();
		serviceTitles = getResources().getStringArray(R.array.service_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		getVNCPreferences = getSharedPreferences("VNCConnect", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
		vncEditor = getVNCPreferences.edit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		if (mDrawerToggle.onOptionsItemSelected(item)) 
		{
			return true;
		}
		switch (item.getItemId())
		{
			case R.id.action_websearch:
				
				startAuxiliaryService(null);    
				
				return true;
			default:
				return super.onOptionsItemSelected(item);
				
		}
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener
	{
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) 
		{
			selectItem(position);
		}
	}

	private void selectItem(int position)
	{
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		switch (position)
		{
			case 0:
				HomeFragment homeFragment = new HomeFragment();
				fragmentManager.beginTransaction().replace(R.id.content_frame, homeFragment).commit();
				break;
			case 1:
				 SharedPreferences getRemotePreferences = getSharedPreferences("Remote", Context.MODE_PRIVATE);
				 connectionIP = getRemotePreferences.getString("remoteIP", "0000");
				 connectionPwd = getRemotePreferences.getString("remotePassword", "0000");
				 
				vncEditor.putString("IP", connectionIP);
				vncEditor.putString("password", connectionPwd);
				vncEditor.commit();
				startActivity(new Intent(MainActivity.this, VncCanvasActivity.class));
				break;
			case 2:
				HomeControlFragment controlFragment = new HomeControlFragment();
				fragmentManager.beginTransaction().replace(R.id.content_frame, controlFragment).commit();
				break;
			case 3:
				SettingFragment settingFragment = new SettingFragment();
				fragmentManager.beginTransaction().replace(R.id.content_frame, settingFragment).commit();
				break;
			case 4:
				ApplicationManagerFragment applicationManagerFragment=new ApplicationManagerFragment();
				fragmentManager.beginTransaction().replace(R.id.content_frame, applicationManagerFragment).commit();
				break;
			case 5:
				qiut(); //退出
				break;
		default:
			break;
		}
		mDrawerList.setItemChecked(position, true);
		setTitle(serviceTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}
	@Override
	public void setTitle(CharSequence title)
	{
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}
	@Override
	protected void onPostCreate(Bundle savedInstanceState) 
	{
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	public void startAuxiliaryService(View v) 
	{
		startService(new Intent(MainActivity.this, AuxiliaryService.class));
		finish();
	}
	
	private void qiut()
	{
		AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("是否清空当前账号？");
		builder.setPositiveButton("否", new DialogInterface.OnClickListener() 
		{ 
            public void onClick(DialogInterface dialog, int i)
            { 
            	dialog.dismiss();
            	MainActivity.this.finish();
            } 
        }); 
		builder.setNegativeButton("是", new DialogInterface.OnClickListener()
		{ 
            public void onClick(DialogInterface dialog, int i) 
            {       
            	SharedPreferences sharedConfigInfo=getSharedPreferences("configInfo",Context.MODE_PRIVATE);;
            	SharedPreferences.Editor editor=sharedConfigInfo.edit();
            	editor.remove("proxyUserName");
            	editor.remove("proxyPassWord");
            	editor.commit();
            	dialog.dismiss();
            	MainActivity.this.finish();
            }
        }); 
		builder.show();
		
	}
}

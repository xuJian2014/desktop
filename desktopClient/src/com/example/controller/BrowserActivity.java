package com.example.controller;

import java.io.IOException;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;

import com.example.action.BrowserControlAction;
import com.example.connection.DeviceConnection;
import com.example.desktop.R;

public class BrowserActivity extends Activity
{

	private OnBtnClickListenerImpl listener;
	private OnTouchListener touchListener;
	private Button nextPage;
	private Button prevPage;
	private Button homePage;
	private Button search;
	private Button refresh;
	private Button refreshStop;
	private Button close;
	private Button browseDown;
	private Button browseUp;
	private Button fullscreen;
	private Button bookmark;
	protected boolean longPress = false;

	// Logger logger = Logger.getAnonymousLogger();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browser);
		getActionBar().setDisplayUseLogoEnabled(true);
		getActionBar().setIcon(null);
		getActionBar().setDisplayOptions(
				ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME
						| ActionBar.DISPLAY_HOME_AS_UP);
		getActionBar().setDisplayUseLogoEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		listener = new OnBtnClickListenerImpl();
		touchListener = new OnBtnTouchListenerImpl();

		nextPage = (Button) findViewById(R.id.ButtonBrowserNextPage);
		nextPage.setOnClickListener(listener);

		prevPage = (Button) findViewById(R.id.ButtonBrowserPrevPage);
		prevPage.setOnClickListener(listener);

		homePage = (Button) findViewById(R.id.ButtonBrowserHomePage);
		homePage.setOnClickListener(listener);

		refresh = (Button) findViewById(R.id.ButtonBrowserRefresh);
		refresh.setOnClickListener(listener);

		refreshStop = (Button) findViewById(R.id.ButtonBrowserStopRefresh);
		refreshStop.setOnClickListener(listener);

		close = (Button) findViewById(R.id.ButtonBrowserClose);
		close.setOnClickListener(listener);

		browseDown = (Button) findViewById(R.id.ButtonBrowserDown);
		browseUp = (Button) findViewById(R.id.ButtonBrowserUp);

		fullscreen = (Button) findViewById(R.id.ButtonBrowserFullscreen);
		fullscreen.setOnClickListener(listener);

		bookmark = (Button) findViewById(R.id.ButtonBrowserBookmark);
		bookmark.setOnClickListener(listener);

		search = (Button) findViewById(R.id.ButtonBrowserSearch);
		search.setOnClickListener(listener);

		browseDown = (Button) findViewById(R.id.ButtonBrowserDown);
		// browseDown.setOnClickListener(listener);

		browseUp = (Button) findViewById(R.id.ButtonBrowserUp);
		// browseUp.setOnClickListener(listener);

		browseDown.setOnTouchListener(touchListener);
		browseUp.setOnTouchListener(touchListener);
	}

	protected class OnBtnClickListenerImpl implements OnClickListener
	{

		@Override
		public void onClick(View view)
		{
			BrowserControlAction action = null;
			switch (view.getId())
			{
			case R.id.ButtonBrowserNextPage:
				action = new BrowserControlAction(
						BrowserControlAction.NEXT_PAGE);
				break;
			case R.id.ButtonBrowserPrevPage:
				action = new BrowserControlAction(
						BrowserControlAction.PREV_PAGE);
				break;
			case R.id.ButtonBrowserHomePage:
				action = new BrowserControlAction(
						BrowserControlAction.HOME_PAGE);
				break;
			case R.id.ButtonBrowserRefresh:
				action = new BrowserControlAction(BrowserControlAction.REFRESH);
				break;
			case R.id.ButtonBrowserStopRefresh:
				action = new BrowserControlAction(
						BrowserControlAction.STOP_REFRESH);
				break;
			case R.id.ButtonBrowserClose:
				action = new BrowserControlAction(
						BrowserControlAction.CLOSE_CURRENT);
				break;
			case R.id.ButtonBrowserDown:
				action = new BrowserControlAction(
						BrowserControlAction.BROWSE_DOWN);
				break;
			case R.id.ButtonBrowserUp:
				action = new BrowserControlAction(
						BrowserControlAction.BROWSE_UP);
				break;
			case R.id.ButtonBrowserFullscreen:
				action = new BrowserControlAction(
						BrowserControlAction.FULL_SCREEN);
				break;
			case R.id.ButtonBrowserBookmark:
				action = new BrowserControlAction(BrowserControlAction.BOOKMARK);
				break;
			case R.id.ButtonBrowserSearch:
				action = new BrowserControlAction(BrowserControlAction.SEARCH);
				break;
			default:
				break;
			}
			if (action != null)
			{
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

	protected class OnBtnTouchListenerImpl implements OnTouchListener
	{

		@Override
		public boolean onTouch(final View view, MotionEvent event)
		{

			if (event.getAction() == MotionEvent.ACTION_DOWN)
			{
				longPress = true;
				new Thread(new Runnable() {

					@Override
					public void run()
					{
						BrowserControlAction action = null;
						if (view.getId() == R.id.ButtonBrowserDown)
						{
							action = new BrowserControlAction(
									BrowserControlAction.BROWSE_DOWN);
						} else if (view.getId() == R.id.ButtonBrowserUp)
						{
							action = new BrowserControlAction(
									BrowserControlAction.BROWSE_UP);
						} else
						{
							return;
						}
						while (longPress)
						{
							try
							{
								Thread.sleep(120);
							} catch (InterruptedException e)
							{
								e.printStackTrace();
							}
							if (longPress)
							{
								try
								{
									DeviceConnection.getInstance().sendAction(
											action);
								} catch (IOException e)
								{
									DeviceConnection.getInstance().close();
									try
									{
										DeviceConnection.getInstance()
												.connect();
									} catch (IOException e1)
									{
										e1.printStackTrace();
									}
									e.printStackTrace();
								}
							}
						}
					}
				}).start();
			} else if (event.getAction() == MotionEvent.ACTION_UP)
			{
				longPress = false;
			}

			return false;
		}
	}
}

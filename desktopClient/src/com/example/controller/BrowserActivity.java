package com.example.controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.action.BrowserControlAction;
import com.example.connection.ConnectServer;
import com.example.desktop.R;

public class BrowserActivity extends Activity {

	private OnBtnClickListenerImpl listener;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browser);

		listener = new OnBtnClickListenerImpl();

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
		browseDown.setOnClickListener(listener);

		browseUp = (Button) findViewById(R.id.ButtonBrowserUp);
		browseUp.setOnClickListener(listener);

		fullscreen = (Button) findViewById(R.id.ButtonBrowserFullscreen);
		fullscreen.setOnClickListener(listener);

		bookmark = (Button) findViewById(R.id.ButtonBrowserBookmark);
		bookmark.setOnClickListener(listener);

		search = (Button) findViewById(R.id.ButtonBrowserSearch);
		search.setOnClickListener(listener);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.browser, menu);
		return true;
	}

	protected class OnBtnClickListenerImpl implements OnClickListener {

		@Override
		public void onClick(View view) {
			BrowserControlAction action = null;
			switch (view.getId()) {
				case R.id.ButtonBrowserNextPage :
					action = new BrowserControlAction(
							BrowserControlAction.NEXT_PAGE);
					break;
				case R.id.ButtonBrowserPrevPage :
					action = new BrowserControlAction(
							BrowserControlAction.PREV_PAGE);
					break;
				case R.id.ButtonBrowserHomePage :
					action = new BrowserControlAction(
							BrowserControlAction.HOME_PAGE);
					break;
				case R.id.ButtonBrowserRefresh :
					action = new BrowserControlAction(
							BrowserControlAction.REFRESH);
					break;
				case R.id.ButtonBrowserStopRefresh :
					action = new BrowserControlAction(
							BrowserControlAction.STOP_REFRESH);
					break;
				case R.id.ButtonBrowserClose :
					action = new BrowserControlAction(
							BrowserControlAction.CLOSE_CURRENT);
					break;
				case R.id.ButtonBrowserDown :
					action = new BrowserControlAction(
							BrowserControlAction.BROWSE_DOWN);
					break;
				case R.id.ButtonBrowserUp :
					action = new BrowserControlAction(
							BrowserControlAction.BROWSE_UP);
					break;
				case R.id.ButtonBrowserFullscreen :
					action = new BrowserControlAction(
							BrowserControlAction.FULL_SCREEN);
					break;
				case R.id.ButtonBrowserBookmark :
					action = new BrowserControlAction(
							BrowserControlAction.BOOKMARK);
					break;
				case R.id.ButtonBrowserSearch :
					action = new BrowserControlAction(
							BrowserControlAction.SEARCH);
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

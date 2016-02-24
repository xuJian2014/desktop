/* 
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this software; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
 * USA.
 */

//
// androidVNC is the Activity for setting VNC server IP and port.
//

package android.androidVNC;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.app.ActivityManager.MemoryInfo;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;



public class androidVNC extends Activity {
	private EditText ipText;
	private EditText portText;
	private EditText passwordText;
	private Button goButton;
	private TextView repeaterText;
	private RadioGroup groupForceFullScreen;
	private Spinner colorSpinner;
	private Spinner spinnerConnection;
	private VncDatabase database;
	private ConnectionBean selected;
	private EditText textNickname;
	private EditText textUsername;
	private CheckBox checkboxKeepPassword;
	private CheckBox checkboxLocalCursor;
	private boolean repeaterTextSet;
	
	/*private SharedPreferences getPreferences;    //获取VNC连接的IP和密码
	private String serverIP;
	private String serverPwd;*/
	
	
	@Override
	public void onCreate(Bundle icicle) {

		super.onCreate(icicle);
		setContentView(R.layout.main);

		ipText = (EditText) findViewById(R.id.textIP);
		portText = (EditText) findViewById(R.id.textPORT);
		passwordText = (EditText) findViewById(R.id.textPASSWORD);
		textNickname = (EditText) findViewById(R.id.textNickname);
		textUsername = (EditText) findViewById(R.id.textUsername);
		goButton = (Button) findViewById(R.id.buttonGO);
		
		((Button) findViewById(R.id.buttonRepeater)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(R.layout.repeater_dialog);
			}
		});
		((Button)findViewById(R.id.buttonImportExport)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(R.layout.importexport);
			}
		});
		colorSpinner = (Spinner)findViewById(R.id.colorformat);
		COLORMODEL[] models=COLORMODEL.values();
		ArrayAdapter<COLORMODEL> colorSpinnerAdapter = new ArrayAdapter<COLORMODEL>(this, android.R.layout.simple_spinner_item, models);
		groupForceFullScreen = (RadioGroup)findViewById(R.id.groupForceFullScreen);
		checkboxKeepPassword = (CheckBox)findViewById(R.id.checkboxKeepPassword);
		checkboxLocalCursor = (CheckBox)findViewById(R.id.checkboxUseLocalCursor);
		colorSpinner.setAdapter(colorSpinnerAdapter);
		colorSpinner.setSelection(0);
		spinnerConnection = (Spinner)findViewById(R.id.spinnerConnection);
		spinnerConnection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> ad, View view, int itemIndex, long id) {
				selected = (ConnectionBean)ad.getSelectedItem();
				updateViewFromSelected();
			}
			@Override
			public void onNothingSelected(AdapterView<?> ad) {
				selected = null;
			}
		});
		spinnerConnection.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			/* (non-Javadoc)
			 * @see android.widget.AdapterView.OnItemLongClickListener#onItemLongClick(android.widget.AdapterView, android.view.View, int, long)
			 */
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				spinnerConnection.setSelection(arg2);
				selected = (ConnectionBean)spinnerConnection.getItemAtPosition(arg2);
				canvasStart();
				return true;
			}
			
		});
		repeaterText = (TextView)findViewById(R.id.textRepeaterId);
		goButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				canvasStart();
			}
		});
		
		database = new VncDatabase(this);
	}
	
	protected void onDestroy() {
		database.close();
		super.onDestroy();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == R.layout.importexport)
			return new ImportExportDialog(this);
		else
			return new RepeaterDialog(this);
	}


	private void updateViewFromSelected() {
		
		//prefConnect=getSharedPreferences("com.example.VNCConnect", Context.CONTEXT_IGNORE_SECURITY);
		
		/*//获取VNC连接所需的IP和密码
		Context context;
		try {
			context = createPackageContext("com.example.desktop", Context.CONTEXT_IGNORE_SECURITY);
			getPreferences = context.getSharedPreferences("VNCContect", MODE_WORLD_READABLE);
			serverIP = getPreferences.getString("IP", "");
			serverPwd = getPreferences.getString("password", "");
			Toast.makeText(androidVNC.this, "服务器信息如下：IP地址：" + serverIP + "; 密码：" + serverPwd, Toast.LENGTH_LONG).show();
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		
		if (selected==null)
			return;
		ipText.setText(selected.getAddress());         //设置IP地址
		//ipText.setText("192.168.1.102");
		//ipText.setText(serverIP);
		portText.setText(Integer.toString(selected.getPort()));
		if (selected.getKeepPassword() || selected.getPassword().length()>0) {
			passwordText.setText(selected.getPassword());      //设置密码
			//passwordText.setText("123456");
			//passwordText.setText(serverPwd);
		}
		groupForceFullScreen.check(selected.getForceFull()==BitmapImplHint.AUTO ? R.id.radioForceFullScreenAuto : (selected.getForceFull() == BitmapImplHint.FULL ? R.id.radioForceFullScreenOn : R.id.radioForceFullScreenOff));
		checkboxKeepPassword.setChecked(selected.getKeepPassword());
		checkboxLocalCursor.setChecked(selected.getUseLocalCursor());
		textNickname.setText(selected.getNickname());
		textUsername.setText(selected.getUserName());
		COLORMODEL cm = COLORMODEL.valueOf(selected.getColorModel());
		COLORMODEL[] colors=COLORMODEL.values();
		for (int i=0; i<colors.length; ++i)
		{
			if (colors[i] == cm) {
				colorSpinner.setSelection(i);
				break;
			}
		}
		updateRepeaterInfo(selected.getUseRepeater(), selected.getRepeaterId());
	}
	
	/**
	 * Called when changing view to match selected connection or from
	 * Repeater dialog to update the repeater information shown.
	 * @param repeaterId If null or empty, show text for not using repeater
	 */
	void updateRepeaterInfo(boolean useRepeater, String repeaterId)
	{
		if (useRepeater)
		{
			repeaterText.setText(repeaterId);
			repeaterTextSet = true;
		}
		else
		{
			repeaterText.setText(getText(R.string.repeater_empty_text));
			repeaterTextSet = false;
		}
	}
	
	
	protected void onStart() {
		super.onStart();
		arriveOnPage();
	}
	
	/**
	 * Return the object representing the app global state in the database, or null
	 * if the object hasn't been set up yet
	 * @param db App's database -- only needs to be readable
	 * @return Object representing the single persistent instance of MostRecentBean, which
	 * is the app's global state
	 */
	static MostRecentBean getMostRecent(SQLiteDatabase db)
	{
		ArrayList<MostRecentBean> recents = new ArrayList<MostRecentBean>(1);
		MostRecentBean.getAll(db, MostRecentBean.GEN_TABLE_NAME, recents, MostRecentBean.GEN_NEW);
		if (recents.size() == 0)
			return null;
		return recents.get(0);
	}
	
	void arriveOnPage() {
		ArrayList<ConnectionBean> connections=new ArrayList<ConnectionBean>();
		ConnectionBean.getAll(database.getReadableDatabase(), ConnectionBean.GEN_TABLE_NAME, connections, ConnectionBean.newInstance);
		Collections.sort(connections);
		connections.add(0, new ConnectionBean());
		int connectionIndex=0;
		if ( connections.size()>1)
		{
			MostRecentBean mostRecent = getMostRecent(database.getReadableDatabase());
			if (mostRecent != null)
			{
				for ( int i=1; i<connections.size(); ++i)
				{
					if (connections.get(i).get_Id() == mostRecent.getConnectionId())
					{
						connectionIndex=i;
						break;
					}
				}
			}
		}
		spinnerConnection.setAdapter(new ArrayAdapter<ConnectionBean>(this,android.R.layout.simple_spinner_item,
				connections.toArray(new ConnectionBean[connections.size()])));
		spinnerConnection.setSelection(connectionIndex,false);
		selected=connections.get(connectionIndex);
		updateViewFromSelected();
		IntroTextDialog.showIntroTextIfNecessary(this, database);
	}
	
	protected void onStop() {
		super.onStop();
		if ( selected == null ) {
			return;
		}
		updateSelectedFromView();
		selected.save(database.getWritableDatabase());
	}
	
	VncDatabase getDatabaseHelper()
	{
		return database;
	}
	
	private void canvasStart() {
		if (selected == null) return;
		MemoryInfo info = Utils.getMemoryInfo(this);
		if (info.lowMemory) {
			// Low Memory situation.  Prompt.
			Utils.showYesNoPrompt(this, "Continue?", "Android reports low system memory.\nContinue with VNC connection?", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					vnc();
				}
			}, null);
		} else
			vnc();
	}
	
		
	private void vnc() {
		updateSelectedFromView();   //从List中获取以前登录并且保存的信息
		saveAndWriteRecent();       //写入和获取最近登录信息
		Intent intent = new Intent(this, VncCanvasActivity.class);
		intent.putExtra(VncConstants.CONNECTION,selected.Gen_getValues());      //private ConnectionBean selected;
		startActivity(intent);
	}
	
	private void updateSelectedFromView() {
		if (selected==null) {
			return;
		}
		selected.setAddress(ipText.getText().toString());     //设置IP地址
		//selected.setAddress("192.168.1.102");
		//selected.setAddress(serverIP);
		try
		{
			selected.setPort(Integer.parseInt(portText.getText().toString()));
		}
		catch (NumberFormatException nfe)
		{
			
		}
		selected.setNickname(textNickname.getText().toString());
		selected.setUserName(textUsername.getText().toString());
		selected.setForceFull(groupForceFullScreen.getCheckedRadioButtonId()==R.id.radioForceFullScreenAuto ? BitmapImplHint.AUTO : (groupForceFullScreen.getCheckedRadioButtonId()==R.id.radioForceFullScreenOn ? BitmapImplHint.FULL : BitmapImplHint.TILE));
		
		selected.setPassword(passwordText.getText().toString());    //设置密码
		//selected.setPassword("123456");
		//selected.setPassword(serverPwd);
		
		
		selected.setKeepPassword(checkboxKeepPassword.isChecked());
		selected.setUseLocalCursor(checkboxLocalCursor.isChecked());
		selected.setColorModel(((COLORMODEL)colorSpinner.getSelectedItem()).nameString());
		if (repeaterTextSet)
		{
			selected.setRepeaterId(repeaterText.getText().toString());
			selected.setUseRepeater(true);
		}
		else
		{
			selected.setUseRepeater(false);
		}
	}
	

	private void saveAndWriteRecent()
	{
		SQLiteDatabase db = database.getWritableDatabase();
		db.beginTransaction();
		try
		{
			selected.save(db);
			MostRecentBean mostRecent = getMostRecent(db);
			if (mostRecent == null)
			{
				mostRecent = new MostRecentBean();
				mostRecent.setConnectionId(selected.get_Id());
				mostRecent.Gen_insert(db);
			}
			else
			{
				mostRecent.setConnectionId(selected.get_Id());
				mostRecent.Gen_update(db);
			}
			db.setTransactionSuccessful();
		}
		finally
		{
			db.endTransaction();
		}
	}
	/* 
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.androidvncmenu,menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.androidvncmenu,menu);
		return true;
	}
	
	 (non-Javadoc)
	 * @see android.app.Activity#onMenuOpened(int, android.view.Menu)
	 
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		menu.findItem(R.id.itemDeleteConnection).setEnabled(selected!=null && ! selected.isNew());
		menu.findItem(R.id.itemSaveAsCopy).setEnabled(selected!=null && ! selected.isNew());
		return true;
	}

	 (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.itemSaveAsCopy) {
			if (selected.getNickname().equals(textNickname.getText().toString()))
				textNickname.setText("Copy of "+selected.getNickname());
			updateSelectedFromView();
			selected.set_Id(0);
			saveAndWriteRecent();
			arriveOnPage();
		} else if (itemId == R.id.itemDeleteConnection) {
			Utils.showYesNoPrompt(this, "Delete?", "Delete " + selected.getNickname() + "?",
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int i)
				{
					selected.Gen_delete(database.getWritableDatabase());
					arriveOnPage();
				}
			}, null);
		} else if (itemId == R.id.itemOpenDoc) {
			Utils.showDocumentation(this);
		}
		return true;
	}
	*/
}

package com.example.touch;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.action.AuthentificationAction;
import com.example.action.AuthentificationResponseAction;
import com.example.action.KeyboardAction;
import com.example.connection.ConnectServer;
import com.example.desktop.R;

public class KeyboardActivity extends Activity {
	private Activity act;
	private Context ctx;
	protected String ip = TouchFlag.getInstance().getIp();

	protected String password = TouchFlag.getInstance().getPwd();
	private KeyboardUtil keyboard;
	private int lastCode = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_keyboard);

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

		ctx = this;
		act = this;
		keyboard = new KeyboardUtil(act, ctx);
		keyboard.showKeyboard();
		try {
			thread.join();
			if (TouchFlag.getInstance().isAuthentificated()) {
				Toast.makeText(KeyboardActivity.this, "服务器连接成功",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(KeyboardActivity.this, "服务器连接失败",
						Toast.LENGTH_SHORT).show();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Integer code;
		if ((code = KeyCode.getJavaAwtKeyCode(keyCode)) != -1) {
			if (TouchFlag.getInstance().isAuthentificated()) {
				if (lastCode != keyCode) {
					ConnectServer.getInstance().sendAction(
							new KeyboardAction(code, true));
				}
			} else {
				Toast.makeText(this, "未连接", Toast.LENGTH_SHORT).show();
				ConnectServer.getInstance().close();
			}
			return false;
		} else {
			Toast.makeText(this, "不支持的按键", Toast.LENGTH_SHORT).show();
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		Integer code;
		if ((code = KeyCode.getJavaAwtKeyCode(keyCode)) != 0 && TouchFlag.getInstance().isAuthentificated()) {
			ConnectServer.getInstance().sendAction(
					new KeyboardAction(code, false));
			return false;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	@Override
	protected void onResume() {
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
		ConnectServer.getInstance().setSocket(null);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ConnectServer.getInstance().close();
	}
}

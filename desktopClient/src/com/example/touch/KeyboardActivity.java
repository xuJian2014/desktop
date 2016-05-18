package com.example.touch;

import java.io.IOException;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.action.KeyboardAction;
import com.example.connection.DeviceConnection;
import com.example.desktop.R;

public class KeyboardActivity extends Activity
{
	private Activity act;

	private Context ctx;

	private KeyboardUtil keyboard;
	private int lastCode = 0;
	private BluetoothAdapter ba = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_keyboard);

		try
		{
			DeviceConnection.getInstance().changeDestination();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		if (DeviceConnection.getInstance().isAuthentificated())
		{
			Toast.makeText(KeyboardActivity.this, "服务器连接成功", Toast.LENGTH_LONG)
					.show();
		} else
		{
			Toast.makeText(KeyboardActivity.this, "服务器连接失败", Toast.LENGTH_LONG)
					.show();
			finish();
		}

		ctx = this;
		act = this;
		keyboard = new KeyboardUtil(act, ctx);
		keyboard.showKeyboard();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (ba != null)
		{
			if (!ba.enable())
			{
				Set<BluetoothDevice> devices = ba.getBondedDevices();
				if (devices.size() > 0)
				{
					Integer code = null;
					if ((code = KeyCode.getJavaAwtKeyCode(keyCode)) != -1)
					{
						if (DeviceConnection.getInstance().isAuthentificated())
						{
							if (lastCode != keyCode)
							{
								try
								{
									DeviceConnection.getInstance().sendAction(
											new KeyboardAction(code, true));
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
						} else
						{
							Toast.makeText(this, "未连接", Toast.LENGTH_SHORT)
									.show();
							DeviceConnection.getInstance().close();
						}
						return false;
					} else
					{
						Toast.makeText(this, "不支持的按键", Toast.LENGTH_SHORT)
								.show();
					}
				}
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		Integer code;
		if ((code = KeyCode.getJavaAwtKeyCode(keyCode)) != 0
				&& DeviceConnection.getInstance().isAuthentificated())
		{
			try
			{
				DeviceConnection.getInstance().sendAction(
						new KeyboardAction(code, true));
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
			return false;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onResume()
	{
//		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
//		{
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//		}
		super.onResume();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		DeviceConnection.getInstance().close();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		DeviceConnection.getInstance().close();
	}
}

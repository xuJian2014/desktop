package com.example.touch;

import java.awt.event.KeyEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.SharedPreferences;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.example.action.KeyboardAction;
import com.example.desktop.R;

public class KeyListener implements OnTouchListener {

	private static boolean isPressing = false;
	private static int lastcode;
	private Vibrator vibrator;
	private SharedPreferences preferences;
	private final Lock lock = new ReentrantLock();
	private ExecutorService singleThread;

	public KeyListener(Vibrator vibrator, SharedPreferences preferences) {
		this.preferences = preferences;
		this.vibrator = vibrator;

		singleThread = Executors.newSingleThreadExecutor();
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {

		switch (view.getId()) {
		case R.id.button3:
			if (event.getAction() == MotionEvent.ACTION_DOWN)
				keyDown(event, preferences.getInt("button3", KeyEvent.VK_D));
			else if (event.getAction() == MotionEvent.ACTION_UP)
				keyUp(event, preferences.getInt("button3", KeyEvent.VK_D));
			break;
		case R.id.button2:
			if (event.getAction() == MotionEvent.ACTION_DOWN)
				keyDown(event, preferences.getInt("button2", KeyEvent.VK_A));
			else if (event.getAction() == MotionEvent.ACTION_UP)
				keyUp(event, preferences.getInt("button2", KeyEvent.VK_A));
			break;
		case R.id.button1:
			if (event.getAction() == MotionEvent.ACTION_DOWN)
				keyDown(event, preferences.getInt("button1", KeyEvent.VK_W));
			else if (event.getAction() == MotionEvent.ACTION_UP)
				keyUp(event, preferences.getInt("button1", KeyEvent.VK_W));
			break;
		case R.id.button4:
			if (event.getAction() == MotionEvent.ACTION_DOWN)
				keyDown(event, preferences.getInt("button4", KeyEvent.VK_S));
			else if (event.getAction() == MotionEvent.ACTION_UP)
				keyUp(event, preferences.getInt("button4", KeyEvent.VK_S));
			break;
		case R.id.buttonR2C1:
			if (event.getAction() == MotionEvent.ACTION_DOWN)
				keyDown(event, preferences.getInt("buttonR2C1", KeyEvent.VK_J));
			else if (event.getAction() == MotionEvent.ACTION_UP)
				keyUp(event, preferences.getInt("buttonR2C1", KeyEvent.VK_J));
			break;
		case R.id.buttonR1C2:
			if (event.getAction() == MotionEvent.ACTION_DOWN)
				keyDown(event, preferences.getInt("buttonR1C2", KeyEvent.VK_I));
			else if (event.getAction() == MotionEvent.ACTION_UP)
				keyUp(event, preferences.getInt("buttonR1C2", KeyEvent.VK_I));
			break;
		case R.id.buttonR1C3:
			if (event.getAction() == MotionEvent.ACTION_DOWN)
				keyDown(event, preferences.getInt("buttonR1C3", KeyEvent.VK_O));
			else if (event.getAction() == MotionEvent.ACTION_UP)
				keyUp(event, preferences.getInt("buttonR1C3", KeyEvent.VK_O));
			break;
		case R.id.buttonR1C1:
			if (event.getAction() == MotionEvent.ACTION_DOWN)
				keyDown(event, preferences.getInt("buttonR1C1", KeyEvent.VK_U));
			else if (event.getAction() == MotionEvent.ACTION_UP)
				keyUp(event, preferences.getInt("buttonR1C1", KeyEvent.VK_U));
			break;
		case R.id.buttonR2C2:
			if (event.getAction() == MotionEvent.ACTION_DOWN)
				keyDown(event, preferences.getInt("buttonR2C2", KeyEvent.VK_K));
			else if (event.getAction() == MotionEvent.ACTION_UP)
				keyUp(event, preferences.getInt("buttonR2C2", KeyEvent.VK_K));
			break;
		case R.id.buttonR2C3:
			if (event.getAction() == MotionEvent.ACTION_DOWN)
				keyDown(event, preferences.getInt("buttonR2C3", KeyEvent.VK_L));
			else if (event.getAction() == MotionEvent.ACTION_UP)
				keyUp(event, preferences.getInt("buttonR2C3", KeyEvent.VK_L));
			break;
		}
		return true;
	}

	private void keyDown(MotionEvent event, final int code) {
		vibrator.vibrate(new long[] { 0, 100 }, -1);
		final KeyboardAction action = new KeyboardAction(code, true);
		lock.lock();
		isPressing = true;
		ConnectServer.getInstance().sendAction(
				action);
		lock.unlock();
		lastcode = code;

		singleThread.execute(new Runnable() {

			@Override
			public void run() {
				try {
					if (isPressing && lastcode == code) {
						Thread.sleep(800);
					}
					while (isPressing && lastcode == code) {
						ConnectServer.getInstance().sendAction(
								action);
						Thread.sleep(80);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void keyUp(MotionEvent event, int code) {
		KeyboardAction keyboardAction = new KeyboardAction(code, false);
		lock.lock();
		isPressing = false;
		ConnectServer.getInstance().sendAction(
				keyboardAction);
		lock.unlock();
	}
}

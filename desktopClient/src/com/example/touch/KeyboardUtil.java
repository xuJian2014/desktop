package com.example.touch;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.View;

import com.example.action.ControllerDroidAction;
import com.example.action.KeyboardAction;
import com.example.action.SystemControlAction;
import com.example.connection.ConnectServer;
import com.example.desktop.R;

public class KeyboardUtil {
	private static boolean keyboardMain = true;
	private static boolean isPressing = false;
	private static int lastcode;
	private final Activity act;
	private final Context ctx;
	public boolean isAlt = false;
	public boolean isCapsLock = false;
	public boolean isCtrl = false;
	public boolean isShift = false;
	public boolean isWindows = false;
	private final Keyboard k1;
	private final Keyboard k2;

	private final KeyboardView keyboardView;
	private KeyboardView.OnKeyboardActionListener listener;
	private final Lock lock = new ReentrantLock();

	private final ExecutorService singleThread;
	List<Keyboard.Key> keyList;
	protected int count = 0;

	public KeyboardUtil(Activity act, Context ctx) {
		this.ctx = ctx;
		this.act = act;

		k1 = new Keyboard(this.ctx, R.layout.qwerty);
		k2 = new Keyboard(this.ctx, R.layout.symbols);
		keyList = k1.getKeys();
		keyboardView = (KeyboardView) this.act.findViewById(R.id.keyboard_view);
		keyboardView.setKeyboard(k1);
		keyboardView.setEnabled(true);
		keyboardView.setPreviewEnabled(false);

		initKeyboardViewListener();
		keyboardView.setOnKeyboardActionListener(listener);

		singleThread = Executors.newSingleThreadExecutor();
	}
	public void hideKeyboard() {
		int visibility = keyboardView.getVisibility();
		if (visibility == View.VISIBLE) {
			keyboardView.setVisibility(View.INVISIBLE);
		}
	}

	private void initKeyboardViewListener() {
		listener = new KeyboardView.OnKeyboardActionListener() {
			@Override
			public void onKey(int primaryCode, int[] keyCodes) {
				ControllerDroidAction action = null;
				switch (primaryCode) {
					case KeyboardAction.VK_CAPS_LOCK :
						if (isCapsLock) {
							isCapsLock = false;
							action = new KeyboardAction(primaryCode);
							for (Keyboard.Key key : keyList) {
								if (key.label != null
										&& isWord(key.label.toString())) {
									key.label = key.label.toString()
											.toLowerCase();
								}
							}
						} else {
							isCapsLock = true;
							action = new KeyboardAction(primaryCode);
							for (Keyboard.Key key : keyList) {
								if (key.label != null
										&& isWord(key.label.toString())) {
									key.label = key.label.toString()
											.toUpperCase(Locale.getDefault());
								}
							}
						}
						keyboardView.setKeyboard(k1);
						break;
					case KeyboardAction.VK_SHIFT :
						if (isShift) {
							action = new KeyboardAction(primaryCode, false);
							isShift = false;
						} else {
							action = new KeyboardAction(primaryCode, true);
							isShift = true;
						}
						break;
					case KeyboardAction.VK_CONTROL :
						if (isCtrl) {
							action = new KeyboardAction(primaryCode, false);
							isCtrl = false;
						} else {
							action = new KeyboardAction(primaryCode, true);
							isCtrl = true;
						}
						break;
					case KeyboardAction.VK_ALT :
						if (isAlt) {
							action = new KeyboardAction(primaryCode, false);
							isAlt = false;
						} else {
							action = new KeyboardAction(primaryCode, true);
							isAlt = true;
						}
						break;
					case KeyboardAction.VK_WINDOWS :
						if (isWindows) {
							action = new KeyboardAction(primaryCode, false);
							isWindows = false;
						} else {
							action = new KeyboardAction(primaryCode, true);
							isWindows = true;
						}
						break;
					case Keyboard.KEYCODE_CANCEL :
						keyboardView.setKeyboard(k1);
						keyboardMain = true;
						break;
					case Keyboard.KEYCODE_MODE_CHANGE :
						keyboardView.setKeyboard(k2);
						keyboardMain = false;
						break;
					
					case KeyboardAction.VK_PRINTSCREEN :
						/*action = new SystemControlAction(
								SystemControlAction.SAVE_SCREEN);*/
						act.finish();
						break;
					default :
						break;
				}

				if (action != null) {
					sendAction2Remote(action);
				}
			}

			@Override
			public void onPress(final int primaryCode) {
				if (isPressing && lastcode == primaryCode) {
					return;
				}

				switch (primaryCode) {
					case KeyboardAction.VK_CAPS_LOCK :
					case KeyboardAction.VK_SHIFT :
					case KeyboardAction.VK_CONTROL :
					case KeyboardAction.VK_ALT :
					case KeyboardAction.VK_WINDOWS :
					case Keyboard.KEYCODE_CANCEL :
					case Keyboard.KEYCODE_MODE_CHANGE :
					case KeyboardAction.VK_PRINTSCREEN :
						return;
					default :
						break;
				}

				final KeyboardAction action = new KeyboardAction(primaryCode,
						true);
				lock.lock();
				isPressing = true;
				sendAction2Remote(action);
				lastcode = primaryCode;
				if ((count & 0xa) == 0xa) {
					count = 0;
				}
				final int c = ++count;
				lock.unlock();

				try {
					Thread.sleep(50);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				if (isPressing && lastcode == primaryCode) {
					singleThread.execute(new Runnable() {

						@Override
						public void run() {
							try {
								if (isPressing && lastcode == primaryCode
										&& c == count) {
									Thread.sleep(700);
								} else {
									return;
								}
								while (isPressing && lastcode == primaryCode
										&& c == count) {
									sendAction2Remote(action);
									Thread.sleep(80);
								}
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					});
				}
			}

			@Override
			public void onRelease(int primaryCode) {
				switch (primaryCode) {
					case KeyboardAction.VK_CAPS_LOCK :
					case KeyboardAction.VK_SHIFT :
					case KeyboardAction.VK_CONTROL :
					case KeyboardAction.VK_ALT :
					case KeyboardAction.VK_WINDOWS :
					case Keyboard.KEYCODE_CANCEL :
					case Keyboard.KEYCODE_MODE_CHANGE :
					case KeyboardAction.VK_PRINTSCREEN :
						return;
					default :
						KeyboardAction keyboardAction = new KeyboardAction(
								primaryCode, false);
						lock.lock();
						isPressing = false;
						sendAction2Remote(keyboardAction);
						lastcode = -1;
						lock.unlock();
				}
			}

			@Override
			public void onText(CharSequence text) {
			}

			@Override
			public void swipeDown() {
			}

			@Override
			public void swipeLeft() {
			}

			@Override
			public void swipeRight() {
			}

			@Override
			public void swipeUp() {
			}
		};
	}
	private boolean isWord(String str) {
		String wordStr = "abcdefghijklmnopqrstuvwxyz";
		return wordStr.contains(str.toLowerCase());
	}
	private void sendAction2Remote(final ControllerDroidAction action) {
		ConnectServer.getInstance().sendAction(action);
	}
	public void showKeyboard() {
		int visibility = keyboardView.getVisibility();
		if (visibility == View.GONE || visibility == View.INVISIBLE) {
			keyboardView.setVisibility(View.VISIBLE);
		}
	}
	
	public void backToKeyboard() {
		keyboardView.setKeyboard(k1);
		keyboardMain = true;
	}

	public boolean getKeyboardType() {
		return keyboardMain;
	}

}

package com.example.touch;

import java.util.Timer;
import java.util.TimerTask;

import android.androidVNC.VncCanvasActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.controller.Control_MainActivity;
import com.example.desktop.MainActivity;
import com.example.desktop.R;

public class EasyTouchView extends View {
    private Context mContext;
    private WindowManager mWManager;
    private WindowManager.LayoutParams mWMParams;
    private View mTouchView;
    private ImageView mIconImageView = null;
    private PopupWindow mPopuWin;
    private ServiceListener mSerLisrener;
    private View mSettingTable;
    private int mTag = 0;
    private int midX;
    private int midY;
    private int mOldOffsetX;
    private int mOldOffsetY;

    private Toast mToast;
    private Timer mTimer = null;
    private TimerTask mTask = null;

    public EasyTouchView(Context context, AttributeSet attrs) {
    	super(context, attrs);
		mContext = context;
		
	}
    
    public EasyTouchView(Context context, ServiceListener listener) {
        super(context);
        mContext = context;
        mSerLisrener = listener;
        
    }

    public void initTouchViewEvent() {
        initEasyTouchViewEvent();
        
        initSettingTableView();
    }
    
    //鍒濆鍖朤ouch鎸夐挳
    private void initEasyTouchViewEvent() {
    	// 璁剧疆杞藉叆view WindowManager鍙傛暟
        mWManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        midX = mWManager.getDefaultDisplay().getWidth() / 2 - 70;
        midY = mWManager.getDefaultDisplay().getHeight()  / 2 ;
        
        mTouchView = LayoutInflater.from(mContext).inflate(R.layout.easy_touch_view, null);        
        mIconImageView = (ImageView) mTouchView.findViewById(R.id.easy_touch_view_imageview);
        mTouchView.setBackgroundColor(Color.TRANSPARENT);
               
        mTouchView.setOnTouchListener(mTouchListener);
        WindowManager wm = mWManager;
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        //wmParams.windowAnimations=0;
        //wmParams.format=PixelFormat.TRANSLUCENT | WindowManager.LayoutParams.FIRST_SYSTEM_WINDOW;
        mWMParams = wmParams;
        wmParams.type = 2003; // 2002琛ㄧず绯荤粺绾х獥鍙�
        wmParams.flags = 40;  // 璁剧疆妗岄潰鍙帶
        
		wmParams.width = 160;  //Touch宽度
		wmParams.height = 160; //Touch高度
        wmParams.format = -3;  // 閫忔槑
        wm.addView(mTouchView, wmParams);
    }
    
    //鍒濆鍖栬缃潰鏉�
    private void initSettingTableView() {
        mSettingTable = LayoutInflater.from(mContext).inflate(R.layout.phone_show_setting_table, null);     //手机
        // mSettingTable = LayoutInflater.from(mContext).inflate(R.layout.pad_show_setting_table, null);     //平板
        
        View mSettingTableView=mSettingTable.findViewById(R.id.touch_setting_table);
        mSettingTableView.getBackground().setAlpha(200);    //璁剧疆鑳屾櫙閫忔槑搴�
        
        ImageView mouseButton =  (ImageView) mSettingTable.findViewById(R.id.show_setting_table_item_mouse_button);       
        ImageView keyboardButton = (ImageView) mSettingTable.findViewById(R.id.show_setting_table_item_virturlkeyboard_button);
        ImageView keymouseButton = (ImageView) mSettingTable.findViewById(R.id.show_setting_table_item_keymouse_button);
        
        ImageView miracastButton = (ImageView) mSettingTable.findViewById(R.id.show_setting_table_item_miracast_button);
        ImageView mainButton = (ImageView) mSettingTable.findViewById(R.id.familyterminal_button);
        ImageView settingButton = (ImageView) mSettingTable.findViewById(R.id.show_setting_table_item_setting_button);
        
        ImageView backButton = (ImageView) mSettingTable.findViewById(R.id.show_setting_table_item_back_button);
        ImageView homeButton = (ImageView) mSettingTable.findViewById(R.id.show_setting_table_item_home_button);
        ImageView exitTouchButton = (ImageView) mSettingTable.findViewById(R.id.show_setting_table_item_exit_touch_button);
        
        mouseButton.setOnClickListener(mClickListener);
        keyboardButton.setOnClickListener(mClickListener);
        keymouseButton.setOnClickListener(mClickListener);
        
        miracastButton.setOnClickListener(mClickListener);
        mainButton.setOnClickListener(mClickListener);
        settingButton.setOnClickListener(mClickListener);
        
        backButton.setOnClickListener(mClickListener);
        homeButton.setOnClickListener(mClickListener);
        exitTouchButton.setOnClickListener(mClickListener);
    }

    private boolean chooseFlag = false;
    
    private void startChoiceRemoteFamilyActivity(String chooseFlag){
    	mPopuWin.dismiss();
    	Intent iChoice = new Intent();
		iChoice.setClass(mContext, ChoiceActivity.class);
		iChoice.putExtra("choiceFlag", chooseFlag);
		iChoice.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.getApplicationContext().startActivity(iChoice);
    }
    
    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            
            case R.id.show_setting_table_item_mouse_button:
				/*if (!TouchFlag.getInstance().isValid() && !chooseFlag) {
					startChoiceRemoteFamilyActivity("mouse");
					chooseFlag = true;
				}else{
					hideSettingTable("虚拟鼠标");
					Intent iMouse = new Intent();
					iMouse.setClass(mContext, MouseActivity.class);
					iMouse.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.getApplicationContext().startActivity(iMouse);
				}*/
				hideSettingTable("虚拟鼠标");
				Intent iMouse = new Intent();
				iMouse.setClass(mContext, MouseActivity.class);
				iMouse.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.getApplicationContext().startActivity(iMouse);
                break;
                
            case R.id.show_setting_table_item_virturlkeyboard_button:
				/*if (!TouchFlag.getInstance().isAuthentificated() && !chooseFlag) {
					startChoiceRemoteFamilyActivity("keyboard");
					chooseFlag = true;
				} else {
					hideSettingTable("虚拟键盘");
					Intent iKeyboard = new Intent();
	                iKeyboard.setClass(mContext, KeyboardActivity.class);
	                iKeyboard.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	                mContext.getApplicationContext().startActivity(iKeyboard);
				}*/
            	hideSettingTable("虚拟键盘");
				Intent iKeyboard = new Intent();
                iKeyboard.setClass(mContext, KeyboardActivity.class);
                iKeyboard.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.getApplicationContext().startActivity(iKeyboard);
                break;
                
            case R.id.show_setting_table_item_keymouse_button:
			
            	//startChoiceRemoteFamilyActivity("vnc");
            	
				hideSettingTable("VNC");

				Intent iVNCDesktop = new Intent();
				iVNCDesktop.setClass(mContext, VncCanvasActivity.class);// 直接跳转到“画布”界面
					// iKeyMouse.setClass(mContext, androidVNC.class);      //跳转到“连接信息”界面
				iVNCDesktop.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.getApplicationContext().startActivity(iVNCDesktop);
				
            	/*hideSettingTable("VNC");

				Intent iVNCDesktop = new Intent();
				iVNCDesktop.setClass(mContext, VncCanvasActivity.class); // 直接跳转到“画布”界面
				// iKeyMouse.setClass(mContext, androidVNC.class);       // 跳转到“连接信息”界面
				iVNCDesktop.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.getApplicationContext().startActivity(iVNCDesktop);*/
                break;
            
            case R.id.show_setting_table_item_miracast_button:
            	hideSettingTable("无线投影");
                
                Intent iMira = new Intent();
				iMira.setClass(mContext, MiracastActivity.class);
				iMira.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.getApplicationContext().startActivity(iMira);
                break;
                
            case R.id.familyterminal_button:
            	//hideSettingTable("家庭终端");
            	/*mouseActivity.finishMouse();
            	keyboardActivity.finishKeyboard();*/
            	mPopuWin.dismiss();
				Intent iMain = new Intent();
				iMain.setClass(mContext, MainActivity.class);
				iMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.getApplicationContext().startActivity(iMain);
                break;
                
            case R.id.show_setting_table_item_setting_button:
            	hideSettingTable("设置");
            	mPopuWin.dismiss();
				Intent iCtrl = new Intent();
				iCtrl.setClass(mContext, Control_MainActivity.class);
				iCtrl.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.getApplicationContext().startActivity(iCtrl);
                /*Intent iSeting = new Intent();
                iSeting.setClass(mContext, MainActivity.class);
                iSeting.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(iSeting);*/
                break;
                
            case R.id.show_setting_table_item_back_button:
            	/*if (!chooseFlag) {         //还需要一个连接成功的标识
					startChoiceRemoteFamilyActivity("game");
					chooseFlag = true;
				} else {
					hideSettingTable("虚拟手柄");
					Intent iGame = new Intent();
					iGame.setClass(mContext, GameActivity.class);
					iGame.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.getApplicationContext().startActivity(iGame);
				}*/
            	
            	hideSettingTable("虚拟手柄");
            	Intent iSeting = new Intent();
                iSeting.setClass(mContext, GameActivity.class);
                iSeting.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(iSeting);
                break;
                
            case R.id.show_setting_table_item_home_button:
                //hideSettingTable("主页");
            	mPopuWin.dismiss();
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);	
				intent.addCategory(Intent.CATEGORY_HOME);
				mContext.getApplicationContext().startActivity(intent);
                break;
                
            case R.id.show_setting_table_item_exit_touch_button:
                quitTouchView();
                break;
            }

        }
    };
    
    private void quitTouchView() {
        hideSettingTable("已退出");
        
        mWManager.removeView(mTouchView);
        mSerLisrener.OnCloseService(true);
        
        clearTimerThead();
    }
    
    private OnTouchListener mTouchListener = new OnTouchListener() {
        float lastX, lastY;
        int paramX, paramY;

        public boolean onTouch(View v, MotionEvent event) {
            final int action = event.getAction();

            float x = event.getRawX();
            float y = event.getRawY();

            if (mTag == 0) {
                mOldOffsetX = mWMParams.x; // 鍋忕Щ閲�
                mOldOffsetY = mWMParams.y; // 鍋忕Щ閲�
            }

            switch (action) {
            case MotionEvent.ACTION_DOWN:
                motionActionDownEvent(x, y);
                break;
                
            case MotionEvent.ACTION_MOVE:
                motionActionMoveEvent(x, y);
                break;
                
            case MotionEvent.ACTION_UP:
                motionActionUpEvent(x, y);
                break;

            default:
                break;
            }
            
            return true;
        }
        
        private void motionActionDownEvent(float x, float y) {
            lastX = x;
            lastY = y;
            paramX = mWMParams.x;
            paramY = mWMParams.y;
        }
        
        private void motionActionMoveEvent(float x, float y) {
            int dx = (int) (x - lastX);
            int dy = (int) (y - lastY);
            mWMParams.x = paramX + dx;
            mWMParams.y = paramY + dy;
            mTag = 1;
            
            // 鏇存柊Touch鎸夐挳鐨勪綅缃�
            mWManager.updateViewLayout(mTouchView, mWMParams);
        }
        
        private void motionActionUpEvent(float x, float y) {
            int newOffsetX = mWMParams.x;
            int newOffsetY = mWMParams.y;
            if (mOldOffsetX == newOffsetX && mOldOffsetY == newOffsetY) {
                mPopuWin = new PopupWindow(mSettingTable, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                mPopuWin.setTouchInterceptor(new OnTouchListener() {

                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                            hideSettingTable();
                            return true;
                        }
                        return false;
                    }
                });
                
                mPopuWin.setBackgroundDrawable(new BitmapDrawable());     // 杩欎釜鏄负浜嗙偣鍑烩�滆繑鍥濨ack鈥濅篃鑳戒娇鍏舵秷澶憋紝骞朵笖涓嶄細褰卞搷浣犵殑鑳屾櫙
                mPopuWin.setTouchable(true);
                mPopuWin.setFocusable(true);
                mPopuWin.setOutsideTouchable(true);
                mPopuWin.setContentView(mSettingTable);
                
                if (Math.abs(mOldOffsetX) > midX) {
                    if (mOldOffsetX > 0) {
                        mOldOffsetX = midX;
                    } else {
                        mOldOffsetX = -midX;
                    }
                }
                
                if (Math.abs(mOldOffsetY) > midY) {
                    if (mOldOffsetY > 0) {
                        mOldOffsetY = midY;
                    } else {
                        mOldOffsetY = -midY;
                    }
                }
                
                mPopuWin.setAnimationStyle(R.style.AnimationPreview);      //璁剧疆PopupWindow鐨勮繘鍑哄姩鐢�
                mPopuWin.setFocusable(true);
                mPopuWin.update();
                
                /*int popuWinX = (mWManager.getDefaultDisplay().getWidth() - mSettingTable
						.getWidth()) / 2;
				int popuWinY = (mWManager.getDefaultDisplay().getHeight() - mSettingTable
						.getHeight()) / 2;*/
                
                mPopuWin.showAtLocation(mTouchView, Gravity.CENTER, -mOldOffsetX, -mOldOffsetY);    //閲嶇粯TouchSettingTable鐨勪綅缃�
                // TODO
                mIconImageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent));
                
                catchSettingTableDismiss();
            } else {
                mTag = 0;
            }
        }
    };
    
    private void catchSettingTableDismiss() {
        mTimer = new Timer();
        mTask = new TimerTask() {
            
            @Override
            public void run() {
                if (mPopuWin == null || !mPopuWin.isShowing()) {
                    handler.sendEmptyMessage(0x0);
                }
            }
        };
        
        mTimer.schedule(mTask, 0, 100);
    }
    
    private void clearTimerThead() {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            mIconImageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.touch_ic4));
        };
    };
    
    public void showToast(Context context, String text) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    private void hideSettingTable(String content) {
        hideSettingTable();
        showToast(mContext, content);
    }
    
    private void hideSettingTable() {
        if (null != mPopuWin) {
            mPopuWin.dismiss();
        }
    }

    public interface ServiceListener {
        public void OnCloseService(boolean isClose);
    }
}

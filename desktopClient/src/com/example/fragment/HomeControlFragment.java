package com.example.fragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.desktop.R;
public class HomeControlFragment extends Fragment implements OnClickListener
{
	private LinearLayout mTaApplication;
	private LinearLayout mTabMdia;
	private LinearLayout mTabScreen;

	private ImageButton mImgApplication;
	private ImageButton mImgMedia;
	private ImageButton mImgScreen;
	
	private TextView mTextApplication;
	private TextView mTextMedia;
	private TextView mTextScreen;
	
	private Application_List_Fragment applicationFragment;
	private Media_List_Fragment mediaFragment;
	private Screen_List_Fragment screenFragment;
	
	View logonView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		logonView = inflater.inflate(R.layout.fragment_home_control, null);
		initView();
		initEvent();
		setSelect(0);
		return logonView;
	}

	private void initEvent()
	{
		mTaApplication.setOnClickListener(this);
		mTabMdia.setOnClickListener(this);
		mTabScreen.setOnClickListener(this);
		
	}
	private void initView()
	{
		mTaApplication = (LinearLayout)logonView.findViewById(R.id.id_tab_application);
		mTabMdia = (LinearLayout) logonView.findViewById(R.id.id_tab_media);
		mTabScreen = (LinearLayout) logonView.findViewById(R.id.id_tab_screen);
	
		mImgApplication = (ImageButton) logonView.findViewById(R.id.id_tab_application_img);
		mImgMedia = (ImageButton) logonView.findViewById(R.id.id_tab_media_img);
		mImgScreen = (ImageButton) logonView.findViewById(R.id.id_tab_screen_img);
		
		mTextApplication=(TextView) logonView.findViewById(R.id.id_tab_application_text);
		mTextMedia=(TextView) logonView.findViewById(R.id.id_tab_media_text);
		mTextScreen=(TextView) logonView.findViewById(R.id.id_tab_screen_text);
		
	}
	//底部按钮响应事件
	@Override
	public void onClick(View v)
	{
		resetImgs();
		switch (v.getId())
		{
			case R.id.id_tab_application:
				setSelect(0);
				break;
			case R.id.id_tab_media:
				setSelect(1);
				break;
			case R.id.id_tab_screen:
				setSelect(2);
				break;
			default:
				break;
		}
	}
	private void setSelect(int i)
	{
		FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		hideFragment(transaction);
		switch (i)
		{
			case 0:
				if (applicationFragment == null)
				{
					applicationFragment = new Application_List_Fragment();
					transaction.add(R.id.homeService_content, applicationFragment);
				} else
				{
					transaction.show(applicationFragment);
				}
				mImgApplication.setImageResource(R.drawable.application_list);
				mTextApplication.setTextColor(android.graphics.Color.parseColor("#ffffff"));
				break;
			case 1:
				System.out.println("按下媒体键");
				if (mediaFragment == null)
				{
					mediaFragment = new Media_List_Fragment();
					transaction.add(R.id.homeService_content, mediaFragment);
				} 
				else
				{
					transaction.show(mediaFragment);	
				}
				mImgMedia.setImageResource(R.drawable.media_list);
				mTextMedia.setTextColor(android.graphics.Color.parseColor("#ffffff"));
				break;
			case 2:
				if (screenFragment == null)
				{
					screenFragment = new Screen_List_Fragment();
					transaction.add(R.id.homeService_content, screenFragment);
				} else
				{
					transaction.show(screenFragment);
				}
				mImgScreen.setImageResource(R.drawable.screen_list);
				mTextScreen.setTextColor(android.graphics.Color.parseColor("#ffffff"));
				break;	
			default:
				break;
		}
		transaction.commit();
	}
	//隐藏Fragment
	private void hideFragment(FragmentTransaction transaction)
	{
		if (applicationFragment != null)
		{
			transaction.hide(applicationFragment);
		}
		if (mediaFragment != null)
		{
			transaction.hide(mediaFragment);
		}
		if (screenFragment != null)
		{
			transaction.hide(screenFragment);
		}
	}
	/**
	 * 切换图片至暗色
	 */
	private void resetImgs()
	{
		mImgApplication.setImageResource(R.drawable.tab_application_normal);
		mImgMedia.setImageResource(R.drawable.tab_media_normal);
		mImgScreen.setImageResource(R.drawable.tab_screen_normal);
		mTextApplication.setTextColor(android.graphics.Color.parseColor("#2B2B2B"));
		mTextMedia.setTextColor(android.graphics.Color.parseColor("#2B2B2B"));
		mTextScreen.setTextColor(android.graphics.Color.parseColor("#2B2B2B"));
	}
}

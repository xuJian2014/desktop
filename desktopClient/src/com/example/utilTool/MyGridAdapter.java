package com.example.utilTool;

import com.example.desktop.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyGridAdapter extends BaseAdapter 
{
	private Context mContext;

	public String[] img_text = { "小区服务中心", "远程桌面", "PC应用平台", "可选设备","游戏模式","监视模式","鼠标","键盘","个人设置","应用助手"};
	public int[] imgs = { R.drawable.computer, R.drawable.vmachine,
			R.drawable.homedevice, R.drawable.usb,R.drawable.game, R.drawable.control,
			R.drawable.mouse, R.drawable.keybord,R.drawable.applicationhelper,R.drawable.setting};
	
	public int[] imgs_init=
		{
			R.drawable.computer_failed,
			R.drawable.vmachine_failed,
			R.drawable.homedevice_failed,
			R.drawable.usb_failed,
			R.drawable.game_failed,
			R.drawable.computer_failed,
			R.drawable.mouse_failed,
			R.drawable.keybord_failed
			};

	public MyGridAdapter(Context mContext)
	{
		super();
		this.mContext = mContext;
	}

	@Override
	public int getCount()
	{
		return img_text.length;
	}

	@Override
	public Object getItem(int position) 
	{
		return img_text[position];
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.phone_grid_item, parent, false);
		}
		TextView tv = BaseViewHolder.get(convertView, R.id.tv_item);
		ImageView iv = BaseViewHolder.get(convertView, R.id.iv_item);
		iv.setBackgroundResource(imgs[position]);
		tv.setText(img_text[position]);
		return convertView;
	}

}

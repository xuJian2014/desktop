package com.example.media_file;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.desktop.R;
import com.example.utilTool.BaseViewHolder;

public class LocalMediaGridAdapter extends BaseAdapter 
{
	private Context mContext;

	private String[] img_text = { " ”∆µ", "“Ù¿÷", "Œƒµµ", "ÕºœÒ"};
	private int[] imgs = { R.drawable.local_mediafile_moive,R.drawable.local_mediafile_music,
			R.drawable.local_mediafile_office,R.drawable.local_mediafile_image};
	

	public LocalMediaGridAdapter(Context mContext)
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

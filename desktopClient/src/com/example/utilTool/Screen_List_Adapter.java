package com.example.utilTool;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Entity.ScreenItem;
import com.example.desktop.R;


public class Screen_List_Adapter extends ArrayAdapter<ScreenItem>
{
	private int resourceId;
	public Screen_List_Adapter(Context context,int textViewResourceId, List<ScreenItem> objects)
	{
		super(context, textViewResourceId, objects);
		resourceId=textViewResourceId;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ScreenItem screenItem=getItem(position);
		View view=LayoutInflater.from(getContext()).inflate(resourceId, null);
		ImageView screenImageView=(ImageView) view.findViewById(R.id.imageView1);
		TextView screenTextView=(TextView) view.findViewById(R.id.textView1);
		screenImageView.setImageResource(screenItem.getImageId());
		screenTextView.setText(screenItem.getscreenName());
		return view;
	
	}
}

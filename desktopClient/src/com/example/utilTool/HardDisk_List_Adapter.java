package com.example.utilTool;

import java.util.List;

import com.example.Entity.HardDiskItem;
import com.example.desktop.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


public class HardDisk_List_Adapter extends ArrayAdapter<HardDiskItem>
{
	private int resourceId;
	public HardDisk_List_Adapter(Context context,int textViewResourceId, List<HardDiskItem> objects)
	{
		super(context, textViewResourceId, objects);
		resourceId=textViewResourceId;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		HardDiskItem hardDiskItem=getItem(position);
		View view=LayoutInflater.from(getContext()).inflate(resourceId, null);
		ImageView hardDiskImageView=(ImageView) view.findViewById(R.id.imageView1);
		TextView screenTextView=(TextView) view.findViewById(R.id.textView1);
		hardDiskImageView.setImageResource(hardDiskItem.getImageId());
		screenTextView.setText(hardDiskItem.gethardDiskName());
		
		return view;
	}
}

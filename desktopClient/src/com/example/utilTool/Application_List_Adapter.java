package com.example.utilTool;

import java.util.List;

import com.example.Entity.ApplicationItem;
import com.example.desktop.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Application_List_Adapter extends ArrayAdapter<ApplicationItem>
{
	private int resourceId;
	public Application_List_Adapter(Context context, int textViewResourceId,List<ApplicationItem> objects) 
	{
		super(context, textViewResourceId, objects);
		resourceId=textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ApplicationItem applicationItem=getItem(position);
		View view=LayoutInflater.from(getContext()).inflate(resourceId, null);
		ImageView applicationView=(ImageView) view.findViewById(R.id.imageView1);
		TextView applicationTextView=(TextView) view.findViewById(R.id.textView1);
		applicationView.setImageResource(applicationItem.getImageId());
		applicationTextView.setText(applicationItem.getApplicationName());
		return view;
	}
}

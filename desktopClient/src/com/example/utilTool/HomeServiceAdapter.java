package com.example.utilTool;

import java.util.List;

import com.example.Entity.HomeServiceFunction;
import com.example.desktop.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeServiceAdapter extends ArrayAdapter<HomeServiceFunction>
{
	private int resourceId;
	public HomeServiceAdapter(Context context, int resource,List<HomeServiceFunction> objects) 
	{
		super(context, resource, objects);
		resourceId=resource;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		HomeServiceFunction homeServiceFunction=getItem(position);
		View view;
		ViewHolder viewHolder;
		if(convertView==null)
		{
			view=LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder=new ViewHolder();
			viewHolder.imageView=(ImageView) view.findViewById(R.id.function_image);
			viewHolder.textView=(TextView) view.findViewById(R.id.funcation_name);
			view.setTag(viewHolder);
		}
		else
		{
			view=convertView;
			viewHolder=(ViewHolder) view.getTag();
		}
		viewHolder.imageView.setImageResource(homeServiceFunction.getImageId());
		viewHolder.textView.setText(homeServiceFunction.getFuntionName());
		return view;
	}
	class ViewHolder
	{
		ImageView imageView;
		TextView textView;
	}
}

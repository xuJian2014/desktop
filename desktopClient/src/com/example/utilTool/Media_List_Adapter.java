package com.example.utilTool;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Entity.MediaItem;
import com.example.desktop.R;

public class Media_List_Adapter  extends ArrayAdapter<MediaItem>
{
	private int resourceId;
	public Media_List_Adapter(Context context,int textViewResourceId, List<MediaItem> objects) 
	{
		super(context, textViewResourceId, objects);
		resourceId=textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		MediaItem mediaItem=getItem(position);
		View view=LayoutInflater.from(getContext()).inflate(resourceId, null);
		ImageView mediaImageView=(ImageView) view.findViewById(R.id.imageView1);
		TextView mediaTextView=(TextView) view.findViewById(R.id.textView1);
		mediaImageView.setImageResource(mediaItem.getImageId());
		mediaTextView.setText(mediaItem.getMediaName());
		return view;
	
	}
}


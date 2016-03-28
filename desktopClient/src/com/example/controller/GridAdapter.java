package com.example.controller;

import java.util.ArrayList;

import com.example.desktop.R;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {

	private ArrayList<String> mNameList = new ArrayList<String>();
	private ArrayList<Integer> mDrawableList = new ArrayList<Integer>();
	private final LayoutInflater mInflater;
	// private final Context mContext;
	LinearLayout.LayoutParams params;
	
	public int[] imgs_init={
			R.drawable.control_browse,
			R.drawable.control_projection,
			R.drawable.control_change,
			R.drawable.control_setting,
			};

	public GridAdapter(Context context, ArrayList<String> nameList,
			ArrayList<Integer> mDrawableList2) {
		mNameList = nameList;
		mDrawableList = mDrawableList2;
		// mContext = context;
		mInflater = LayoutInflater.from(context);

		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
	}

	@Override
	public int getCount() {
		return mNameList.size();
	}

	@Override
	public Object getItem(int position) {
		return mNameList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Holder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.control_grid_item, null);
			convertView.setPadding(30, 80, 30, 80);

			// construct an item tag
			ImageView image = (ImageView) convertView
					.findViewById(R.id.grid_icon);
			TextView text = (TextView) convertView.findViewById(R.id.grid_name);
			holder = new Holder(image, text);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		// set name
		holder.mName.setText(mNameList.get(position));

		// set icon
		holder.mIcon.setBackgroundResource(mDrawableList.get(position));
		holder.mIcon.setLayoutParams(params);
		return convertView;
	}
	private class Holder {
		protected ImageView mIcon;
		protected TextView mName;

		public Holder(ImageView icon, TextView name) {
			this.mName = name;
			this.mIcon = icon;
		}
	}
}

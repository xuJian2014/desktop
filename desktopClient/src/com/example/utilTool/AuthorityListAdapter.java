package com.example.utilTool;

import java.util.List;
import com.example.Entity.HardDiskItem;
import com.example.desktop.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class AuthorityListAdapter extends BaseAdapter
{
	private List<HardDiskItem> mList;
	private Context mContext;
	private LayoutInflater mInflater;

	public AuthorityListAdapter(Context context, List<HardDiskItem> list)
	{
		mList = list;
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.check_list_item, null);
			holder.mTitle = (TextView) convertView.findViewById(R.id.title);
			holder.imageView = (ImageView) convertView.findViewById(R.id.imageView1);
			holder.checkBox=(CheckBox) convertView.findViewById(R.id.checkBox);
			convertView.setTag(holder);

		} else
		{

			holder = (ViewHolder) convertView.getTag();
		}

		holder.mTitle.setText(mList.get(position).gethardDiskName());
		holder.imageView.setImageResource(mList.get(position).getImageId());
		holder.checkBox.setChecked(mList.get(position).isChecked());
		return convertView;
	}

	public class ViewHolder
	{
		public TextView mTitle;
		public ImageView imageView;
		public CheckBox checkBox;
	};

}

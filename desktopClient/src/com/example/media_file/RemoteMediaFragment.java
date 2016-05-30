package com.example.media_file;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.desktop.R;
import com.example.utilTool.MyGridView;


public class RemoteMediaFragment extends Fragment
{
	private MyGridView gridview;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		 View rootView = inflater.inflate(R.layout.remotemediafragmentlayout, container, false);//关联布局文件
		 gridview=(MyGridView)rootView.findViewById(R.id.gridview);
		 gridview.setAdapter(new RemoteMediaGridAdapter(getActivity()));
		 gridview.setOnItemClickListener(new OnItemClickListener() 
		 {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id)
			{
				
				Intent intent=new Intent(getActivity(),RemoteMediaActivity.class);
				intent.putExtra("position", position);
				RemoteMediaFragment.this.startActivity(intent);
			}
		});
		
		return rootView;
	}
}

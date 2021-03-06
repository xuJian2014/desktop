package com.example.fragment;

import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.Entity.ScreenItem;
import com.example.desktop.R;
import com.example.util.jsonTransfer.JsonParse;
import com.example.util.jsonTransfer.OptionEnum;
import com.example.util.jsonTransfer.ResponseMessage;
import com.example.util.jsonTransfer.ResponseMessageEnum;
import com.example.utilTool.ReFlashListView;
import com.example.utilTool.ReFlashListView.IReflashListener;
import com.example.utilTool.Screen_List_Adapter;
import com.example.utilTool.SendMsgThread;
import com.example.utilTool.StringUtil;

public class Screen_List_Fragment extends Fragment implements IReflashListener
{
	private String screenStr;
	private String[] content_Screen=new String[]{};
	ReFlashListView listView;
	View view;
	private List<ScreenItem> screenList=new ArrayList<ScreenItem>();
	String responseMesgScreen;
	Screen_List_Adapter adapter;
	
	SharedPreferences sharedPreferences;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		view=inflater.inflate(R.layout.screen_list, container, false);
		//SendMsgThread sendMsgThread=new SendMsgThread(handler, getActivity(),"screen");
		SendMsgThread sendMsgThread=new SendMsgThread(handler, getActivity(),JsonParse.Json2String(OptionEnum.SCREEN.ordinal(), null));
		listView=(ReFlashListView) view.findViewById(R.id.listView1);
		sharedPreferences=getActivity().getSharedPreferences("configInfo",Context.MODE_PRIVATE);
		listView.setInterface(this);
		
		Thread thread =new Thread(sendMsgThread);
		thread.start();
		return view;
	}
	@Override
	public void onPause()
	{
		super.onPause();
		handler.removeCallbacks(null);
	}
	private Handler handler=new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			switch(msg.what)
			{
				case 0:
					cleanListView();
					Toast.makeText(getActivity(), "家庭服务中心拒绝连接", Toast.LENGTH_LONG).show();
					break;
				case 1:
					cleanListView();
					Toast.makeText(getActivity(), "家庭服务中心已关闭", Toast.LENGTH_LONG).show();
					break;
				case 2:
					screenStr=msg.getData().getString("msg");
					ResponseMessage responseMessage=JsonParse.Json2Object(screenStr);
					if(responseMessage==null)
					{
						Toast.makeText(getActivity(), "对不起,没有找到屏幕", Toast.LENGTH_LONG).show();
					}
					else
					{
						if(responseMessage.getErrNum()==ResponseMessageEnum.ERROR.ordinal())
						{
							Toast.makeText(getActivity(), "获取屏幕失败", Toast.LENGTH_LONG).show();
						}
						else
						{
							content_Screen=responseMessage.getResponseMessage().split(",");
							binderListData(content_Screen);
						}
					}
					/*if(StringUtil.isNullString(screenStr) || "error".equals(screenStr))
					{
						Toast.makeText(getActivity(), "获取屏幕失败", Toast.LENGTH_LONG).show();
					}
					else
					{
						content_Screen=screenStr.split(",");
						binderListData(content_Screen);
					}*/
					break;
				case 5:
					cleanListView();
					Toast.makeText(getActivity(), "连接家庭服务中心发生错误", Toast.LENGTH_LONG).show();
					break;
				default:
					break;
			}
			return ;
		}
	};
	private void binderListData(String[] str)
	{
		ScreenItem screenItem;
		cleanListView();
		
		for(int i=0;i<str.length;i++)
		{
			String screenName=str[i];
			if(sharedPreferences.contains(screenName))
			{
				screenName=sharedPreferences.getString(screenName, "...");
			}
			screenItem=new ScreenItem(screenName, R.drawable.screen_item);
			screenList.add(screenItem);
		}
		adapter=new Screen_List_Adapter(getActivity(), R.layout.content_list_item, screenList);
		listView.setAdapter(adapter);
		
		
		//点击修改屏幕名称响应事件
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			AlertDialog.Builder builder = new Builder(getActivity());

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
				View screenNameView = layoutInflater.inflate(R.layout.screen_name_dialog, null);
				final TextView originScreenNameTextView = (TextView) screenNameView.findViewById(R.id.originScreenNameId);
				final EditText alterScreenNameEdit = (EditText) screenNameView.findViewById(R.id.editText1);
				//final String originScreenName = screenList.get(position - 1).getscreenName();
				final String originScreenName=content_Screen[position-1];
				originScreenNameTextView.setText(screenList.get(position - 1).getscreenName());
				builder.setTitle("修改屏幕名称");
				builder.setView(screenNameView);
				builder.setPositiveButton("取消", new OnClickListener() 
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});
				builder.setNegativeButton("保存", new OnClickListener() 
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						String alterScreenName = alterScreenNameEdit.getText().toString();
						if (StringUtil.isNullString(alterScreenName))
						{
							Toast.makeText(getActivity(), "保存失败，输入不能为空",Toast.LENGTH_LONG).show();
						}
						else
						{
							SharedPreferences.Editor editor = sharedPreferences.edit();
							editor.putString(originScreenName, alterScreenName);
							if (editor.commit())
							{
								Toast.makeText(getActivity(), "保存成功",Toast.LENGTH_LONG).show();
							}
							else
							{
								Toast.makeText(getActivity(), "保存失败，请重新配置",Toast.LENGTH_LONG).show();
							}
						}
					}
				});
				builder.show();
			} 
		});

	}
	private void cleanListView() 
	{
		int size=screenList.size();
		if(size>0)
		{
			screenList.removeAll(screenList);
			adapter.notifyDataSetChanged();
			//listView.setAdapter(adapter);
		}
	}
	private void setReflashData() 
	{
		SendMsgThread sendMsgThread=new SendMsgThread(handler, getActivity(),JsonParse.Json2String(OptionEnum.SCREEN.ordinal(), null));
		Thread thread =new Thread(sendMsgThread);
		thread.start(); 
	}
	@Override
	public void onReflash() 
	{
		setReflashData();
		listView.reflashComplete();
		
	}
}

package com.example.fragment;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.desktop.R;

public class SettingFragment extends Fragment
{
	private TextView myInfo;
	private TextView proxyServer;
	private TextView homeService;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		View settingView=inflater.inflate(R.layout.fragment_setting, null);
		myInfo=(TextView) settingView.findViewById(R.id.personInfo);
		proxyServer=(TextView) settingView.findViewById(R.id.proxyServer);
		homeService=(TextView) settingView.findViewById(R.id.homeService);
		return settingView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		//锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟矫帮拷钮锟斤拷应
		myInfo.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				Toast.makeText(getActivity(), "你被点击 了个人资料设置...", Toast.LENGTH_SHORT).show();
				
			}
		});
		
		//锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟接�
		proxyServer.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				showProxyServerSetting();
			}
		});
		
		//锟斤拷庭锟秸讹拷锟借备锟斤拷锟矫帮拷钮锟斤拷应
		homeService.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0) 
			{
				showHomeServiceSetting();
			}
		});
		
	}
	private void showProxyServerSetting()
	{
		LayoutInflater layoutInflater=LayoutInflater.from(getActivity());
		 View textEntryView=layoutInflater.inflate(R.layout.proxyserver_setting_dialog, null);
		  final EditText mEtProxyServerIp=(EditText) textEntryView.findViewById(R.id.proxyServerIp);
		 final EditText mEtproxyServerPort=(EditText) textEntryView.findViewById(R.id.proxyServerPort);
		
		AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
		builder.setTitle("配置代理服务器：");
		//builder.setIcon(R.drawable.feed);
		
		
		SharedPreferences sharedPreferences=getActivity().getSharedPreferences("configInfo",Context.MODE_PRIVATE);
		String proxyIpAdress=sharedPreferences.getString("proxyIpAdress",null);
		int proxyPortNumber=sharedPreferences.getInt("proxyPortNumber",0);
		if(proxyIpAdress!=null)
		{	
			mEtProxyServerIp.setText(proxyIpAdress);
			mEtproxyServerPort.setText(String.valueOf(proxyPortNumber));
		}
		
		builder.setView(textEntryView);
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() 
		{ 
            public void onClick(DialogInterface dialog, int i)
            { 
            	dialog.dismiss();	
            } 
        }); 
		builder.setNegativeButton("保存", new DialogInterface.OnClickListener()
		{ 
            public void onClick(DialogInterface dialog, int i) 
            { 
                   
               String proxyServerIp=mEtProxyServerIp.getText().toString();
               String proxyServerPort=mEtproxyServerPort.getText().toString();
               if(SettingFragment.isStringNull(proxyServerIp)||SettingFragment.isStringNull(proxyServerPort))
               {
            	   Toast.makeText(getActivity(), "保存失败，输入不能为空", Toast.LENGTH_LONG).show();
               }
               else
               {
            	   SharedPreferences sharedConfigInfo=getActivity().getSharedPreferences("configInfo",Context.MODE_PRIVATE);
            	   SharedPreferences.Editor editor=sharedConfigInfo.edit();
            	   editor.putString("proxyIpAdress", proxyServerIp);
            	   editor.putInt("proxyPortNumber", Integer.valueOf(proxyServerPort));
            	   if(editor.commit())
            	   {
            		   Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_LONG).show();
            	   }
            	   else
            	   {
            		   Toast.makeText(getActivity(), "保存失败，请重新配置", Toast.LENGTH_LONG).show();
            	   }
               }
            } 
        }); 
	
		builder.show();
	}
	
	protected void showHomeServiceSetting() 
	{
		LayoutInflater layoutInflater=LayoutInflater.from(getActivity());
		final View textEntryView=layoutInflater.inflate(R.layout.homeservice_setting_dialog, null);
		final EditText mEtHomeServiceIp=(EditText) textEntryView.findViewById(R.id.homeServiceIp);
		final EditText mEtHomeServicePwd=(EditText) textEntryView.findViewById(R.id.homeServicePwd);
		final EditText mEtHomeServicePort=(EditText) textEntryView.findViewById(R.id.homeServicePort);

		SharedPreferences sharedPreferences=getActivity().getSharedPreferences("configInfo",Context.MODE_PRIVATE);
		String homeIpAdress=sharedPreferences.getString("homeServiceIp",null);
		int homePortNumber=sharedPreferences.getInt("homeServicePortNumber",0);
		if(homeIpAdress!=null)
		{	
			mEtHomeServiceIp.setText(homeIpAdress);
			mEtHomeServicePort.setText(String.valueOf(homePortNumber));
		}
		
		AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
		builder.setTitle("配置家庭终端：");
		//builder.setIcon(R.drawable.feed);
		builder.setView(textEntryView);
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() 
		{ 
            public void onClick(DialogInterface dialog, int i)
            { 
            	dialog.dismiss();	
            } 
        }); 
		builder.setNegativeButton("保存", new DialogInterface.OnClickListener()
		{ 
            public void onClick(DialogInterface dialog, int i) 
            { 
                   
               String homeServiceIp=mEtHomeServiceIp.getText().toString();
               String homeServicePwd=mEtHomeServicePwd.getText().toString();
               String homeServicePort=mEtHomeServicePort.getText().toString();
               if(SettingFragment.isStringNull(homeServiceIp)||SettingFragment.isStringNull(homeServicePort))
               {
            	   Toast.makeText(getActivity(), "保存失败，输入不能为空", Toast.LENGTH_LONG).show();
               }
               else
               {
            	   SharedPreferences sharedConfigInfo=getActivity().getSharedPreferences("configInfo",Context.MODE_PRIVATE);
            	   SharedPreferences.Editor editor=sharedConfigInfo.edit();
            	   editor.putString("homeServiceIp", homeServiceIp);
            	   editor.putString("homeServicePwd", homeServicePwd);
            	   editor.putInt("homeServicePortNumber", Integer.valueOf(homeServicePort));
            	   if(editor.commit())
            	   {
            		   Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_LONG).show();
            	   }
            	   else
            	   {
            		   Toast.makeText(getActivity(), "保存失败，请重新配置", Toast.LENGTH_LONG).show();
            	   }
               }
            } 
        }); 
	
		builder.show();
		
	}
	private static boolean isStringNull(String str)
	{
		if(str==null||"".equals(str))
			return true;
		else
			return false;
	}
}

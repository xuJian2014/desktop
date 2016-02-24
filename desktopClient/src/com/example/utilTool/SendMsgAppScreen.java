package com.example.utilTool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SendMsgAppScreen implements Runnable
{
	private final int TIME_OUT=30*1000;//最大响应时间，超时设置
		Socket socketClient=null;
		Handler handler=null;
		Activity activity;
		private String requestStr;
		public SendMsgAppScreen(Handler handler,Activity activity,String requestStr) 
		{
			super();
			this.handler = handler;
			this.activity = activity;
			this.requestStr=requestStr;
		}
		@Override
		public void run()
		{
			Message message=handler.obtainMessage();
			SharedPreferences sharedPreferences=activity.getSharedPreferences("configInfo",Context.MODE_PRIVATE);
			String dstAddress=sharedPreferences.getString("homeServiceIp","192.168.1.112");
			int dstPort=sharedPreferences.getInt("homeServicePortNumber",8888);
			socketClient=new Socket();
			SocketAddress socAddress = new InetSocketAddress(dstAddress, dstPort);
			try
			{
				socketClient.connect(socAddress, TIME_OUT);
				PrintWriter out=new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream())),true);
				out.println(requestStr);
				InputStream inputStream=socketClient.getInputStream();
				socketClient.setSoTimeout(TIME_OUT);
				BufferedReader buffer=new BufferedReader(new InputStreamReader(inputStream));
				String responseInfo=buffer.readLine();
				Bundle bundle=new Bundle();
				bundle.putString("msg", responseInfo);
				message.setData(bundle);
				message.what=3;  //成功发送请求消息
				handler.sendMessage(message);
			}
			catch(ConnectException e)
			{
				message.what=0;// Connection refused
				handler.sendMessage(message);
			}
			catch (SocketException e) 
			{
				message.what=1;//Socket is closed
				handler.sendMessage(message);
			}
			catch (IOException e)
			{
				message.what=5;  // 连接发生错误
				handler.sendMessage(message);
			}
			finally
			{
				if(socketClient!=null)
				{
					try
					{
						socketClient.close();
					} 
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
}


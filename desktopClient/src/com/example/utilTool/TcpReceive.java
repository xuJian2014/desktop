package com.example.utilTool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import android.os.Handler;
import android.os.Message;

public class TcpReceive implements Runnable 
{
	private Handler handler=null;
	private Message message;
	public TcpReceive(Handler handler) 
	{
		this.handler=handler;
		message=handler.obtainMessage();
	}
	@Override
	public void run() 
	{
		Socket socket = null;
		ServerSocket ss = null;
		BufferedReader in = null;
		boolean flag=true;
		String line = null;
		try
		{
			ss = new ServerSocket(9999);
			ss.setSoTimeout(15*1000);
			
			while (flag) 
			{
				socket = ss.accept();
				flag=false;
				if (socket != null) 
				{
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					line = in.readLine();
					if(line!=null)
					{
						String[] temp=line.split(",");
						String portNum=temp[1];
						message.what=7;  //扫描到家庭服务中心IP
						message.obj=socket.getInetAddress().getHostAddress()+","+portNum;
						System.out.println("---"+message.obj);
						handler.sendMessage(message);
					}
					else 
					{
						message.what=8;
						handler.sendMessage(message);
					}
				}
		    } 
		}
			catch (Exception e) 
			{
				//扫描失败
				message.what=8;
				handler.sendMessage(message);
			}
			finally 
			{
				try 
				{
					if (in != null)
						in.close();
					if (socket != null)
						socket.close();
					if (ss != null)
						ss.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
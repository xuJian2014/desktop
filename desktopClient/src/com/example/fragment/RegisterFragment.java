package com.example.fragment;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.Entity.RequestMesg;
import com.example.Entity.ResponseMesg;
import com.example.desktop.R;

public class RegisterFragment extends Fragment
{
	private EditText mEtUserName;
	private EditText mEtPassWord;
	private EditText mEtVmName;
	private EditText mEtHomeServiceIp;
	private Button mBtnRegister;
	private RequestMesg requestMesg;
	private ProgressDialog mDialog; 
	private final int TIME_OUT=10*1000;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		View registerView = inflater.inflate(R.layout.fragment_register, null);
		mEtUserName=(EditText) registerView.findViewById(R.id.register_edit_account);
		mEtPassWord=(EditText) registerView.findViewById(R.id.register_edit_pwd);
		mEtVmName=(EditText) registerView.findViewById(R.id.register_edit_vName);
		mEtHomeServiceIp=(EditText) registerView.findViewById(R.id.register_edit_homeIpAdress);
		mBtnRegister=(Button) registerView.findViewById(R.id.btn_register);
		return registerView;
	}
	
	private Handler handler=new Handler()
	{
		public void handleMessage(Message msg) 
		{
			switch (msg.what)
			{
			case 0:
				mDialog.cancel();
				Toast.makeText(getActivity(), "未知主机错误，重新配置服务器IP", Toast.LENGTH_LONG).show();
				break;
			case 1:
				mDialog.cancel();
				Toast.makeText(getActivity(), "连接代理服务器超时", Toast.LENGTH_LONG).show();
				break;
			case 2:
				 //启动接收返回信息线程
				 ClientReceive clientReceive=new ClientReceive();
	             Thread response_thread=new Thread(clientReceive);
	             response_thread.start();
	             break;
			case 3:
				mDialog.cancel();
				Toast.makeText(getActivity(), "对不起，不能重复注册", Toast.LENGTH_LONG).show();
				break;
			case 4:
				mDialog.cancel();
				Toast.makeText(getActivity(), "恭喜您，注册成功", Toast.LENGTH_LONG).show();
				break;
			case 5:
				mDialog.cancel();
				Toast.makeText(getActivity(), "对不起，注册失败,返回硬盘IP地址错误", Toast.LENGTH_LONG).show();
				break;
			case 6:
				mDialog.cancel();
				Toast.makeText(getActivity(), "对不起，代理服务器返回信息出现未知错误", Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		}
	};
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		mBtnRegister.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View v)
			{
				 mDialog = new ProgressDialog(getActivity());  
	             mDialog.setTitle("注册");  
	             mDialog.setMessage("正在注册，请稍后...");  
	             mDialog.show(); 
	             String userName=mEtUserName.getText().toString().trim();
	             String passWord=mEtPassWord.getText().toString().trim();
	             String vmName=mEtVmName.getText().toString().trim();
	             String homeServiceIp=mEtHomeServiceIp.getText().toString().trim();
	             requestMesg=new RequestMesg(userName, passWord, vmName, homeServiceIp, 1, 7000, 0);
	             Client client=new Client(requestMesg);
	             Thread request_thread=new Thread(client);
	             request_thread.start();
			}
		});
	}
	
	//发送注册信息线程
		class Client implements Runnable
		{
			private RequestMesg mesg;
			Socket socketClient=null;
			SharedPreferences sharedPreferences=getActivity().getSharedPreferences("configInfo",Context.MODE_PRIVATE);
			String dstAddress=sharedPreferences.getString("proxyIpAdress", "192.168.1.102");
			int dstPort=sharedPreferences.getInt("proxyPortNumber", 8000);
			Message msg=handler.obtainMessage();
			public Client(RequestMesg mesg)
			{
				this.mesg=mesg;
			}
			@Override
			public void run() 
			{
				try
				{
					socketClient=new Socket();
					SocketAddress socAddress = new InetSocketAddress(dstAddress, dstPort); 
					socketClient.connect(socAddress, TIME_OUT);
					ObjectOutputStream objOutStream=getOutObjectStream(socketClient);
					objOutStream.writeObject(mesg);
					msg.what=2;  //成功发送请求消息
					handler.sendMessage(msg);
					socketClient.close();
				}
				catch (UnknownHostException e)
				{
					msg.what=0;  //代理服务器IP地址格式错误，未知主机
					handler.sendMessage(msg);
				} 
				catch (IOException e)
				{
					msg.what=1;  //连接代理服务器超时
					handler.sendMessage(msg);
				}
			}
			 private ObjectOutputStream getOutObjectStream(Socket socket) throws IOException
			{
				 return new ObjectOutputStream(socket.getOutputStream());  
			}

		}
	//接收注册消息线程
	class ClientReceive implements Runnable
	{
		ServerSocket clientReceive;
		Socket socket;
		int listenPort=7000;
		ResponseMesg responseMesg;
		Message msg = handler.obtainMessage();
		public void run() 
		{
			try
			{
			 	clientReceive=new ServerSocket(listenPort);
			 	boolean falg=true;
				while(falg)
				{
					 socket=clientReceive.accept();
					 clientReceive.setSoTimeout(TIME_OUT);
					 ObjectInputStream objectInStream=getInObjectStream(socket);
					 responseMesg=(ResponseMesg) objectInStream.readObject();
					 int command=responseMesg.getMesgType();
					 if(command==6)
					 {
						 msg.what=3; //不能重复注册
						 falg=false;
						 handler.sendMessage(msg);
					 }
					 else if(command==7)
					 {
						 msg.what=4; //注册成功
						 falg=false;
						 handler.sendMessage(msg);
					 }
					 else //command=8
					 {
						 msg.what=5; //注册失败
						 falg=false;
						 handler.sendMessage(msg);
					 }
						 
				 }
			}
			catch(Exception e)
			{
				msg.what=6; //未知错误;
				handler.sendMessage(msg);
			}
			finally
			{
				if(clientReceive!=null)
					try 
					{
						clientReceive.close();
					}
					catch (IOException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
		private ObjectInputStream getInObjectStream(Socket socket) throws IOException 
		{
			BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
			return new ObjectInputStream(bis);
		}
	}
	
}

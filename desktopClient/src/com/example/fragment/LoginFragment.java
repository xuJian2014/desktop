/*package com.example.fragment;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import com.example.Entity.RequestMesg;
import com.example.Entity.ResponseMesg;
import com.example.desktop.R;
import com.example.utilTool.StringUtil;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
public class LoginFragment extends Fragment
{
	private EditText mEtLogon;
	private EditText mEtPwd;
	private Button mBtnLogon;
	private Button mBtnReset;
	private ProgressDialog mDialog;  
	private TextView textViewIp;  
	private RequestMesg requestMesg;
	private SharedPreferences pref, prefVNC;
	private SharedPreferences.Editor editor, editorVNC;
	private CheckBox rememberPass;
	private final int TIME_OUT=5*1000;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		View logonView = inflater.inflate(R.layout.test, null);
		mEtLogon = (EditText) logonView.findViewById(R.id.login_edit_account);
		mEtPwd = (EditText) logonView.findViewById(R.id.login_edit_pwd);
		mBtnLogon = (Button) logonView.findViewById(R.id.login_btn_login);
		mBtnReset = (Button) logonView.findViewById(R.id.login_btn_register);
		textViewIp=(TextView) logonView.findViewById(R.id.textView2);
		rememberPass = (CheckBox)logonView.findViewById(R.id.remember_pass);
		pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		boolean isRemember = pref.getBoolean("remember_password", false);
		if (isRemember) 
		{
			String userName = pref.getString("userName", "");
			String passWord = pref.getString("passWord", "");
			mEtLogon.setText(userName);
			mEtPwd.setText(passWord);
			rememberPass.setChecked(true);
		}
		return logonView;
	}
	@Override
	public void onPause()
	{
		super.onPause();
		handler.removeCallbacks(null);
	}
	
	public Handler handler= new Handler()
	{
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
				case 0:
					if(mDialog!=null)
						mDialog.cancel();
					Toast.makeText(getActivity(), "未知主机错误，重新配置服务器IP", Toast.LENGTH_LONG).show();
					break;
				case 1:
					if(mDialog!=null)
						mDialog.cancel();
					Toast.makeText(getActivity(), "连接代理服务器超时", Toast.LENGTH_LONG).show();
					break;
				case 2:
		             ClientReceive clientReceive=new ClientReceive();
		             Thread response_thread=new Thread(clientReceive);
		             response_thread.start();
		             break;
				case 3:
					 Toast.makeText(getActivity(), "正在开机，请耐心等待...", Toast.LENGTH_LONG).show(); 
					 break;
				case 4:
					if(mDialog!=null)
						mDialog.cancel();
					Bundle bundle=msg.getData();
					ResponseMesg responseMesg=(ResponseMesg) bundle.getSerializable("message");
					
					//写入远程连接所需的IP
					prefVNC = getActivity().getSharedPreferences("Remote",Context.MODE_PRIVATE);
					editorVNC=prefVNC.edit();
					editorVNC.putString("remoteIP", responseMesg.getResponserMesg());
					editorVNC.commit();
					
					Toast.makeText(getActivity(), "您的登录IP地址为:"+responseMesg.getResponserMesg(), Toast.LENGTH_LONG).show();
					break;
				case 5:
					if(mDialog!=null)
						mDialog.cancel();
					Toast.makeText(getActivity(), "对不起，您的登录信息有误，请确认后重新登录", Toast.LENGTH_LONG).show(); 
					break;
				case 6:
					if(mDialog!=null)
						mDialog.cancel();
					Toast.makeText(getActivity(), "对不起，代理服务器返回信息出现未知错误", Toast.LENGTH_LONG).show(); 
					break;
				case 7:
					if(mDialog!=null)
						mDialog.cancel();
					Toast.makeText(getActivity(), "对不起，您是新用户，请注册后再登陆", Toast.LENGTH_LONG).show(); 
					break;
				case 8:
					if(mDialog!=null)
						mDialog.cancel();
					Toast.makeText(getActivity(), "对不起，代理服务器返回信息出现未知错误", Toast.LENGTH_LONG).show(); 
				default:
					break;
			}
		}
	};
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		mBtnLogon.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				 String userName=mEtLogon.getText().toString();
	             String passWord=mEtPwd.getText().toString();
	             
	             if(StringUtil.isNullString(userName)||StringUtil.isNullString(passWord))
	             {
	            	 Toast.makeText(getActivity(), "用户名和密码不能为空!", Toast.LENGTH_SHORT).show();
	             }
	             else
	             {
		             //写入远程连接所需的Password
					 prefVNC = getActivity().getSharedPreferences("Remote",Context.MODE_PRIVATE);
					 editorVNC = prefVNC.edit();
					 editorVNC.putString("remotePassword", passWord);
					 editorVNC.commit();
					
		            
		             editor = pref.edit();
		             if(rememberPass.isChecked())
		             {
						editor.putBoolean("remember_password", true);
						editor.putString("userName", userName);
						editor.putString("passWord", passWord);
					 }
		             else 
		             {
						editor.clear();
		             }
					 editor.commit();
					 mDialog = new ProgressDialog(getActivity());  
		             mDialog.setTitle("登陆");  
		             mDialog.setMessage("正在登陆服务器，请稍后...");  
		             mDialog.show();  
					 SharedPreferences sharedPreferences=getActivity().getSharedPreferences("configInfo",Context.MODE_PRIVATE);
		             String homeServiceIp=sharedPreferences.getString("homeServiceIp", "192.168.1.103");
		             requestMesg=new RequestMesg(userName, passWord,homeServiceIp,0,6000);
		             
		             Client client=new Client(requestMesg);
		             Thread request_thread=new Thread(client);
		             request_thread.start();  
	             }
			}
		});
		 mBtnReset.setOnClickListener(new OnClickListener()
	        {	
				@Override
				public void onClick(View v)
				{
					mEtLogon.setText("");
					mEtPwd.setText("");
				}
			});
	}
	
		class Client implements Runnable
		{
			Message msg=Message.obtain();
			RequestMesg mesg;
			Socket socketClient=null;
			SharedPreferences sharedPreferences=getActivity().getSharedPreferences("configInfo",Context.MODE_PRIVATE);
			String dstAddress=sharedPreferences.getString("proxyIpAdress","192.168.1.103");
			int dstPort=sharedPreferences.getInt("proxyPortNumber",8001);
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
					msg.what=2;  
					handler.sendMessage(msg);
					socketClient.close();
				}
				catch (UnknownHostException e)
				{
					msg.what=0;  
					handler.sendMessage(msg);
				} 
				catch (IOException e)
				{
					msg.what=1;  
					handler.sendMessage(msg);
				}
			}
			 private ObjectOutputStream getOutObjectStream(Socket socket) throws IOException
			{
				 return new ObjectOutputStream(socket.getOutputStream());  
			}

		}

	class ClientReceive implements Runnable
	{
		ServerSocket clientReceive;
		Socket socket;
		private int listenPort=6000;
		ResponseMesg responseMesg;
		boolean flag=true;
		Message msg=Message.obtain();
		public void run() 
		{
			try
			{
			 	clientReceive=new ServerSocket(listenPort);
			 	int i=0;
				while(flag)
				{
					System.out.println(i);
					 socket=clientReceive.accept();
					 ObjectInputStream objectInStream=getInObjectStream(socket);
					 responseMesg=(ResponseMesg) objectInStream.readObject();
					 i++;
					 int command=responseMesg.getMesgType();
					 if(command==3)
					 {
						 msg.what=3; 
						 handler.sendMessage(msg);
					 }
					 else if(command==4)
					 {
						 Bundle bundle=new Bundle();
						 bundle.putSerializable("message", responseMesg);
						 Message message=Message.obtain();
						 message.setData(bundle);
						 message.what=4;
						 flag=false;
						 handler.sendMessage(message); 
					 }
					 else if(command==1)
					 {
						 msg.what=5; //锟斤拷录锟斤拷息锟斤拷锟斤拷
						 flag=false;
						 handler.sendMessage(msg);
					 }
					 else if(command==2)
					 {
						 msg.what=7; //锟斤拷锟矫伙拷锟斤拷锟斤拷要注锟斤拷
						 flag=false;
						 handler.sendMessage(msg);
					 }
					 else  //command=5
					 {
						 msg.what=6; //锟斤拷锟斤拷失锟杰ｏ拷锟斤拷锟斤拷原锟斤拷未知
						 flag=false;
						 handler.sendMessage(msg);
					 }
					 socket.close();
				 }
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("登录出现异常！");
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
*/
package com.example.desktop;

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
import com.example.utilTool.StringUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity
{
	private EditText mEtLogon;
	private EditText mEtPwd;
	private EditText mEtIp;
	private Button mBtnLogon;
	private Button mBtnRegister;
	private ProgressDialog mDialog;   
	private RequestMesg requestMesg;
	private SharedPreferences pref, prefVNC;
	private SharedPreferences.Editor editor, editorVNC;
	private CheckBox rememberPass;
	private final int TIME_OUT=10*1000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		mEtLogon = (EditText) findViewById(R.id.login_edit_account);
		mEtPwd = (EditText) findViewById(R.id.login_edit_pwd);
		mEtIp=(EditText) findViewById(R.id.login_edit_ip); 
		mBtnLogon = (Button) findViewById(R.id.login_btn_login);
		mBtnRegister = (Button) findViewById(R.id.login_btn_register);
		rememberPass = (CheckBox)findViewById(R.id.remember_pass);
		pref = getSharedPreferences("configInfo",Context.MODE_PRIVATE);
		
		mBtnLogon.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				 String userName=mEtLogon.getText().toString();
	             String passWord=mEtPwd.getText().toString();
	             String ip=mEtIp.getText().toString();
	             
	             if(StringUtil.isNullString(userName)||StringUtil.isNullString(passWord)||StringUtil.isNullString(ip))
	             {
	            	 Toast.makeText(LoginActivity.this, "上述输入均不能为空!", Toast.LENGTH_SHORT).show();
	             }
	             else if(!StringUtil.isIPAddress(ip))
	             {
	            	 Toast.makeText(LoginActivity.this, "IP地址不正确，请重新输入！", Toast.LENGTH_SHORT).show();
	             }
	             else
	             {
		             //写入远程连接所需的Password
					 prefVNC = getSharedPreferences("Remote",Context.MODE_PRIVATE);
					 editorVNC = prefVNC.edit();
					 editorVNC.putString("remotePassword", passWord);
					 editorVNC.commit();       
		         
					 editor = pref.edit();
		             if(rememberPass.isChecked())
		             {
						editor.putString("proxyUserName", userName);
						editor.putString("proxyPassWord", passWord);
						editor.putString("proxyIpAdress", ip);
						editor.commit();
					 }
		            
					 mDialog = new ProgressDialog(LoginActivity.this);  
		             mDialog.setTitle("登录");  
		             mDialog.setMessage("正在登录服务器，请稍候...");  
		             mDialog.show();  
		             
		             String homeServiceIp=pref.getString("homeServiceIp", "192.168.1.103");
		             requestMesg=new RequestMesg(userName, passWord,homeServiceIp,0,6000);
		             
		             Client client=new Client(requestMesg,ip,8000);
		             Thread request_thread=new Thread(client);
		             request_thread.start();  
	             }
			}
		});
		mBtnRegister.setOnClickListener(new OnClickListener()
	        {	
				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
					startActivity(intent);
				}
			});
	}
	@Override
	protected void onStart()
	{
		super.onStart();
		
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
					Toast.makeText(LoginActivity.this, "未知主机错误，重新配置服务器IP", Toast.LENGTH_LONG).show();
					break;
				case 1:
					if(mDialog!=null)
						mDialog.cancel();
					Toast.makeText(LoginActivity.this, "连接代理服务器发生错误，请检查IP地址是否正确或代理服务是否开启！", Toast.LENGTH_LONG).show();
					break;
				case 2:
		             ClientReceive clientReceive=new ClientReceive();
		             Thread response_thread=new Thread(clientReceive);
		             response_thread.start();
		             break;
				case 3:
					 Toast.makeText(LoginActivity.this, "正在开机，请耐心等待...", Toast.LENGTH_LONG).show(); 
					 break;
				case 4:
					if(mDialog!=null)
						mDialog.cancel();
					Bundle bundle=msg.getData();
					ResponseMesg responseMesg=(ResponseMesg) bundle.getSerializable("message");
					
					//写入远程连接所需的IP
					prefVNC = getSharedPreferences("Remote",Context.MODE_PRIVATE);
					editorVNC=prefVNC.edit();
					editorVNC.putString("remoteIP", responseMesg.getResponserMesg());
					editorVNC.commit();
					
					//Toast.makeText(LoginActivity.this, "您的登录IP地址为:"+responseMesg.getResponserMesg(), Toast.LENGTH_LONG).show();
					Toast.makeText(LoginActivity.this, "登录成功!"+responseMesg.getResponserMesg(), Toast.LENGTH_LONG).show();
					
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					startActivity(intent);
					LoginActivity.this.finish();
					
					break;
				case 5:
					if(mDialog!=null)
						mDialog.cancel();
					Toast.makeText(LoginActivity.this, "对不起，您的登录信息有误，请确认后重新登录", Toast.LENGTH_LONG).show(); 
					break;
				case 6:
					if(mDialog!=null)
						mDialog.cancel();
					Toast.makeText(LoginActivity.this, "对不起，代理服务器返回信息出现未知错误", Toast.LENGTH_LONG).show(); 
					break;
				case 7:
					if(mDialog!=null)
						mDialog.cancel();
					Toast.makeText(LoginActivity.this, "对不起，您是新用户，请注册后再登录", Toast.LENGTH_LONG).show(); 
					
					
					break;
				case 8:
					if(mDialog!=null)
						mDialog.cancel();
					Toast.makeText(LoginActivity.this, "对不起，代理服务器返回信息出现未知错误", Toast.LENGTH_LONG).show(); 
				default:
					break;
			}
		}
	};
	
	class Client implements Runnable
	{
		Message msg=Message.obtain();
		RequestMesg mesg;
		Socket socketClient=null;
		/*SharedPreferences sharedPreferences=getSharedPreferences("configInfo",Context.MODE_PRIVATE);
		String dstAddress=sharedPreferences.getString("proxyIpAdress","192.168.1.103");
		int dstPort=sharedPreferences.getInt("proxyPortNumber",8001);*/
		String dstAddress;
		int dstPort;
		
		
		public Client(RequestMesg mesg,String dstAddress,int dstPort)
		{
			this.mesg=mesg;
			this.dstAddress=dstAddress;
			this.dstPort=dstPort;
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
			catch (Exception e)
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
			while(flag)
			{	
				 socket=clientReceive.accept();
				 ObjectInputStream objectInStream=getInObjectStream(socket);
				 responseMesg=(ResponseMesg) objectInStream.readObject();
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

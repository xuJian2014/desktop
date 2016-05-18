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

import com.example.Entity.RequestMesg;
import com.example.Entity.ResponseMesg;
import com.example.connection.DeviceConnection;
import com.example.utilTool.StringUtil;

public class LoginActivity extends Activity
{
	private EditText mEtLogon;
	private EditText mEtPwd;
	private EditText mEtIp;
	private Button mBtnLogon;
	private Button mBtnReset;
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
		mBtnReset = (Button) findViewById(R.id.login_btn_reset);
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
	            	 Toast.makeText(LoginActivity.this, "�������������Ϊ��!", Toast.LENGTH_SHORT).show();
	             }
	             else if(!StringUtil.isIPAddress(ip))
	             {
	            	 Toast.makeText(LoginActivity.this, "IP��ַ����ȷ�����������룡", Toast.LENGTH_SHORT).show();
	             }
	             else if(StringUtil.isContainsChinese(userName))
	             {
	            	 Toast.makeText(LoginActivity.this, "�û�������Ϊ�����ַ�", Toast.LENGTH_SHORT).show();
	             }
	             else
	             {
		             //д��Զ�����������Password
					 prefVNC = getSharedPreferences("Remote",Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
					 editorVNC = prefVNC.edit();
					 editorVNC.putString("remotePassword", passWord);
					 editorVNC.commit();       
		         
					 
					 String connectionPwd = prefVNC.getString("remotePassword", "0000");
					 DeviceConnection.getInstance().setPassword(connectionPwd);
					 
						
					 editor = pref.edit();
		             if(rememberPass.isChecked())
		             {
						editor.putString("proxyUserName", userName);
						editor.putString("proxyPassWord", passWord);
						editor.putString("proxyIpAdress", ip);
						editor.commit();
					 }
		            
					 mDialog = new ProgressDialog(LoginActivity.this);  
		             mDialog.setTitle("��¼");  
		             mDialog.setMessage("���ڵ�¼�����������Ժ�...");  
		             mDialog.show();  
		             
		             String homeServiceIp=pref.getString("homeServiceIp", "192.168.1.103");
		             requestMesg=new RequestMesg(userName, passWord,homeServiceIp,0,6000);
		             
		             Client client=new Client(requestMesg,ip,8000);
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
					mEtIp.setText("");
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
				case 1:
					if(mDialog!=null)
						mDialog.cancel();
					Toast.makeText(LoginActivity.this, "�Բ������Ӵ���������ʧ�ܣ����Ժ�����", Toast.LENGTH_LONG).show();
					break;
				case 2:
		             ClientReceive clientReceive=new ClientReceive();
		             Thread response_thread=new Thread(clientReceive);
		             response_thread.start();
		             break;
				case 3:
					 Toast.makeText(LoginActivity.this, "���ڿ����������ĵȴ�...", Toast.LENGTH_LONG).show(); 
					 break;
				case 4:
					if(mDialog!=null)
						mDialog.cancel();
					Bundle bundle=msg.getData();
					ResponseMesg responseMesg=(ResponseMesg) bundle.getSerializable("message");	
					//д��Զ�����������IP
					prefVNC = getSharedPreferences("Remote",Context.MODE_PRIVATE);
					editorVNC=prefVNC.edit();
					editorVNC.putString("remoteIP", responseMesg.getResponserMesg());
					editorVNC.commit();	
					
					String connectionIP = prefVNC.getString("remoteIP", "0000");
					DeviceConnection.getInstance().setIp(connectionIP);
					 
					Toast.makeText(LoginActivity.this, "��¼�ɹ�!"+responseMesg.getResponserMesg(), Toast.LENGTH_LONG).show();	
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					startActivity(intent);
					LoginActivity.this.finish();
					break;
				case 5:
					if(mDialog!=null)
						mDialog.cancel();
					Toast.makeText(LoginActivity.this, "��¼ʧ�ܣ������û����������Ƿ�������ȷ", Toast.LENGTH_LONG).show(); 
					break;
				case 7:
					if(mDialog!=null)
						mDialog.cancel();
					Toast.makeText(LoginActivity.this, "�Բ����������û�����ע����ٵ�¼", Toast.LENGTH_LONG).show(); 
					Intent intent2=new Intent(LoginActivity.this, LoginAndRegisterActivity.class);
					startActivity(intent2);
					break;
				case 8:
					if(mDialog!=null)
						mDialog.cancel();
					Toast.makeText(LoginActivity.this, "�Բ��𣬴���������������Ϣ����δ֪����", Toast.LENGTH_LONG).show(); 
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
				 if(command==3)  //�ȴ����������
				 {
					 msg.what=3; 
					 handler.sendMessage(msg);
				 }
				 else if(command==4) //��ȷ����IP
				 {
					 Bundle bundle=new Bundle();
					 bundle.putSerializable("message", responseMesg);
					 Message message=Message.obtain();
					 message.setData(bundle);
					 message.what=4;
					 flag=false;
					 handler.sendMessage(message); 
				 }
				 else if(command==1) //�û����������
				 {
					 msg.what=5; 
					 flag=false;
					 handler.sendMessage(msg);
				 }
				 else if(command==2) //���û�����ע��
				 {
					 msg.what=7;
					 flag=false;
					 handler.sendMessage(msg);
				 }
				 socket.close();
			 }
		}
		catch(Exception e)
		{
			 msg.what=8;
			 flag=false;
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
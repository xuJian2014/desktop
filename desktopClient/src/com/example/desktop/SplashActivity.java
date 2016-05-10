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
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
public class SplashActivity extends Activity 
{
	private SharedPreferences prefVNC; //���������������ص�IP��ַ
	private SharedPreferences sharedConfigInfo; //�����ʼ����Ϣ����
	private Editor editor;
	private Editor editorVNC;
		
	private ProgressDialog mDialog;   
	private RequestMesg requestMesg;
	private final int TIME_OUT=10*1000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		init(); //��ʼ������
		new Handler().postDelayed(new Runnable()
		{
			@Override
			public void run() 
			{
				String userName = sharedConfigInfo.getString("proxyUserName", "");
				String passWord = sharedConfigInfo.getString("proxyPassWord", "");
				if(StringUtil.isNullString(userName)||StringUtil.isNullString(passWord))
				{
					Intent intent = new Intent(SplashActivity.this, LoginAndRegisterActivity.class);
					startActivity(intent);
					SplashActivity.this.finish();
				}
				else
				{
					 mDialog = new ProgressDialog(SplashActivity.this);  
		             mDialog.setTitle("��¼");  
		             mDialog.setMessage("���ڵ�¼�����������Ժ�...");  
		             mDialog.show();  
					
		             String homeServiceIp=sharedConfigInfo.getString("homeServiceIp", "192.168.1.103");
		             requestMesg=new RequestMesg(userName, passWord,homeServiceIp,0,6000);
		             
		             Client client=new Client(requestMesg);
		             Thread request_thread=new Thread(client);
		             request_thread.start();  
				}
			}
		}, 4000);
	}
	private void init()
	{
		sharedConfigInfo=getSharedPreferences("configInfo",Context.MODE_PRIVATE);
		editor=sharedConfigInfo.edit();
		
		if(!sharedConfigInfo.contains("proxyIpAdress"))
		{
			editor.putString("proxyIpAdress", "");
			editor.putInt("proxyPortNumber", 8000);
		}
		if(!sharedConfigInfo.contains("homeServiceIp"))
		{
			editor.putString("homeServiceIp", "192.168.1.118");
			editor.putInt("homeServicePortNumber", 8888);
		}
		if(!sharedConfigInfo.contains("userName"))
		{
			editor.putString("userName", "��ʼ�û���������������");
			editor.putString("passWord", "��ʼ���룬����������");
		}
		if(!sharedConfigInfo.contains("initHardDiskStr"))
		{
			editor.putString("initHardDiskStr", "Ĭ����1-0,Ĭ����2-0");
		}
		
		if(editor.commit())  
		{
			Toast.makeText(SplashActivity.this, "������ʼ���ɹ�", Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(SplashActivity.this, "δ���в�����ʼ��",Toast.LENGTH_SHORT).show();
		}
		
	}
	public Handler handler= new Handler()
	{
		public void handleMessage(Message msg) 
		{
			Intent intent=null;
			switch (msg.what) 
			{
				case 0:
				case 1:  //����0��1��˵��δ�ɹ�����������Ϣ��ֱ��ת���¼����
					if(mDialog!=null)
						mDialog.cancel();
					intent = new Intent(SplashActivity.this, LoginAndRegisterActivity.class);
					startActivity(intent);
					SplashActivity.this.finish();
					break;
				case 2:  //����������Ϣ�ɹ��������̵߳ȴ�������Ϣ
		             ClientReceive clientReceive=new ClientReceive();
		             Thread response_thread=new Thread(clientReceive);
		             response_thread.start();
		             break;
				case 3:
					 Toast.makeText(SplashActivity.this, "���ڿ����������ĵȴ�...", Toast.LENGTH_LONG).show(); 
					 break;
				case 4://��¼�ɹ�
					if(mDialog!=null)
						mDialog.cancel();
					Bundle bundle=msg.getData();
					ResponseMesg responseMesg=(ResponseMesg) bundle.getSerializable("message");
					//д��Զ�����������IP
					prefVNC = getSharedPreferences("Remote",Context.MODE_PRIVATE);
					editorVNC=prefVNC.edit();
					editorVNC.putString("remoteIP", responseMesg.getResponserMesg());
					editorVNC.commit();
					intent = new Intent(SplashActivity.this, MainActivity.class);
					startActivity(intent);
					SplashActivity.this.finish();
					break;
				case 5://��¼��Ϣ����
					if(mDialog!=null)
						mDialog.cancel();
					intent = new Intent(SplashActivity.this, LoginAndRegisterActivity.class);
					startActivity(intent);
					SplashActivity.this.finish();
					break;
				case 6://������񷵻���Ϣ����
					if(mDialog!=null)
						mDialog.cancel();
					//Toast.makeText(SplashActivity.this, "�Բ��𣬴��������������Ϣ����δ֪����", Toast.LENGTH_LONG).show(); 
					intent = new Intent(SplashActivity.this, LoginAndRegisterActivity.class);
					startActivity(intent);
					SplashActivity.this.finish();
					break;
				case 7:
					if(mDialog!=null)
						mDialog.cancel();
					//Toast.makeText(SplashActivity.this, "�Բ����������û�����ע����ٵ�½", Toast.LENGTH_LONG).show(); 
					intent = new Intent(SplashActivity.this, LoginAndRegisterActivity.class);
					startActivity(intent);
					SplashActivity.this.finish();
					break;
				case 8:
					if(mDialog!=null)
						mDialog.cancel();
					//Toast.makeText(SplashActivity.this, "�Բ��𣬴��������������Ϣ����δ֪����", Toast.LENGTH_LONG).show(); 
					intent = new Intent(SplashActivity.this, LoginAndRegisterActivity.class);
					startActivity(intent);
					SplashActivity.this.finish();
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
		SharedPreferences sharedPreferences=getSharedPreferences("configInfo",Context.MODE_PRIVATE);
		String dstAddress=sharedPreferences.getString("proxyIpAdress","192.168.1.103");
		int dstPort=8000;
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
					 msg.what=5; 
					 flag=false;
					 handler.sendMessage(msg);
				 }
				 else if(command==2)
				 {
					 msg.what=7; 
					 flag=false;
					 handler.sendMessage(msg);
				 }
				 else  //command=5
				 {
					 msg.what=6; 
					 flag=false;
					 handler.sendMessage(msg);
				 }
				 socket.close();
			 }
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("��¼�����쳣��");
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

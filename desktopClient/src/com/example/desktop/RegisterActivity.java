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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Entity.RequestMesg;
import com.example.Entity.ResponseMesg;
import com.example.utilTool.StringUtil;

public class RegisterActivity extends Activity
{
	private EditText mEtUserName;
	private EditText mEtPassWord;
	private EditText mEtVmName;
	private Button mBtnRegister;
	private TextView showSystemSetting;
	private LinearLayout settingLayout;
	private ImageView is_showView;
	private Spinner cpuNumSpinner;
	private Spinner memorySizeSpinner;
	private RequestMesg requestMesg;
	private ProgressDialog mDialog; 
	private String[] cpuNumDatas;
	private String[] memorySizeDatas;
	String cpuNum="2";
	String memorySize="4G";
	private final int TIME_OUT=10*1000;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_register);
		initData();
		
		//����һ��ArrayAdapter����������Դ��֮��������
		ArrayAdapter<String> cpuNumAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,cpuNumDatas);
		ArrayAdapter<String> memorySizeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,memorySizeDatas);
		//���õ��������б�ķ��
		cpuNumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		memorySizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	
		//��arrayAdapter������ӽ�Spinnerȥ
		cpuNumSpinner.setAdapter(cpuNumAdapter);
		memorySizeSpinner.setAdapter(memorySizeAdapter);
		
		cpuNumSpinner.setSelection(1, true);
		memorySizeSpinner.setSelection(1, true);
		//��Ӽ�����
		cpuNumSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id)
			{
				cpuNum=cpuNumDatas[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
		
				//��Ӽ�����
		memorySizeSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id)
			{
				memorySize=memorySizeDatas[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		});
		showSystemSetting.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(settingLayout.getVisibility()==View.VISIBLE)
				{
					is_showView.setImageResource(R.drawable.list_right);
					settingLayout.setVisibility(View.GONE);
				}
				else
				{
					is_showView.setImageResource(R.drawable.list_dowm);
					settingLayout.setVisibility(View.VISIBLE);
				}
				
			}
		});
		
		
		mBtnRegister.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View v)
			{
	             String userName=mEtUserName.getText().toString().trim();
	             String passWord=mEtPassWord.getText().toString().trim();
	             String vmName=mEtVmName.getText().toString().trim();
	             if(StringUtil.isNullString(userName)||StringUtil.isNullString(passWord)||StringUtil.isNullString(vmName))
	             {
	            	 Toast.makeText(RegisterActivity.this, "�Բ������������Ϣ������", Toast.LENGTH_SHORT).show();
	             }
	             else if(StringUtil.isContainsChinese(userName))
	             {
	            	 Toast.makeText(RegisterActivity.this, "�û�������Ϊ�����ַ�", Toast.LENGTH_SHORT).show();
	             }
	             else
	             {
	            	 mDialog = new ProgressDialog(RegisterActivity.this);  
		             mDialog.setTitle("ע��");  
		             mDialog.setMessage("����ע�ᣬ���Ժ�...");  
		             mDialog.show(); 
	            	 requestMesg=new RequestMesg(userName, passWord, vmName, 1, 7000, cpuNum,memorySize);
	 	             Client client=new Client(requestMesg);
	 	             Thread request_thread=new Thread(client);
	 	             request_thread.start();
	             }
			}
		});
	}
	private void initData()
	{
		mEtUserName=(EditText) findViewById(R.id.register_edit_account);
		mEtPassWord=(EditText)findViewById(R.id.register_edit_pwd);
		mEtVmName=(EditText) findViewById(R.id.register_edit_vName);
		mBtnRegister=(Button) findViewById(R.id.btn_register);	
		showSystemSetting=(TextView) findViewById(R.id.systemSettingView);
		settingLayout=(LinearLayout) findViewById(R.id.spinnerLayout);
		is_showView=(ImageView) findViewById(R.id.is_showView);
		cpuNumSpinner=(Spinner) findViewById(R.id.spinner1);
		memorySizeSpinner=(Spinner) findViewById(R.id.spinner2);
		cpuNumDatas=new String[]{"1","2","4","6","8"};
		memorySizeDatas=new String[]{"2G","4G","6G","8G"};
	}
	
	private Handler handler=new Handler()
	{
		public void handleMessage(Message msg) 
		{
			switch (msg.what)
			{
			case 0:
			case 1:
				if(mDialog!=null)
					mDialog.cancel();
				Toast.makeText(RegisterActivity.this, "�Բ������Ӵ��������ʧ�ܣ����Ժ�����", Toast.LENGTH_LONG).show();	
				break;
			case 2:
				 //�������շ�����Ϣ�߳�
				 ClientReceive clientReceive=new ClientReceive();
	             Thread response_thread=new Thread(clientReceive);
	             response_thread.start();
	             break;
			case 3:
				if(mDialog!=null)
					mDialog.cancel();
				Toast.makeText(RegisterActivity.this, "�Բ��𣬲����ظ�ע��", Toast.LENGTH_LONG).show();
				break;
			case 4:
				if(mDialog!=null)
					mDialog.cancel();
				Toast.makeText(RegisterActivity.this, "��ϲ����ע��ɹ�", Toast.LENGTH_LONG).show();
				break;
			case 5:
				if(mDialog!=null)
					mDialog.cancel();
				Toast.makeText(RegisterActivity.this, "�Բ���ע��ʧ��", Toast.LENGTH_LONG).show();
				break;
			case 6:
				if(mDialog!=null)
					mDialog.cancel();
				Toast.makeText(RegisterActivity.this, "�Բ��𣬴��������������Ϣ����δ֪����", Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		}
	};
	
	class Client implements Runnable
	{
		private RequestMesg mesg;
		Socket socketClient=null;
		SharedPreferences sharedPreferences=getSharedPreferences("configInfo",Context.MODE_PRIVATE);
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
				msg.what=2;  //�ɹ�����������Ϣ
				handler.sendMessage(msg);
				socketClient.close();
			}
			catch (UnknownHostException e)
			{
				msg.what=0;  //���������IP��ַ��ʽ����δ֪����
				handler.sendMessage(msg);
			} 
			catch (IOException e)
			{
				msg.what=1;  //���Ӵ����������ʱ
				handler.sendMessage(msg);
			}
		}
		 private ObjectOutputStream getOutObjectStream(Socket socket) throws IOException
		{
			 return new ObjectOutputStream(socket.getOutputStream());  
		}

	}
//����ע����Ϣ�߳�
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
					 msg.what=3; //�����ظ�ע��
					 falg=false;
					 handler.sendMessage(msg);
				 }
				 else if(command==7)
				 {
					 msg.what=4; //ע��ɹ�
					 falg=false;
					 handler.sendMessage(msg);
				 }
				 else //command=8
				 {
					 msg.what=5; //ע��ʧ��
					 falg=false;
					 handler.sendMessage(msg);
				 }
					 
			 }
		}
		catch(Exception e)
		{
			msg.what=6; //δ֪����;
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

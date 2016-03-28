package com.example.fragment;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import android.androidVNC.VncCanvasActivity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Entity.RequestMesg;
import com.example.Entity.ResponseMesg;
import com.example.controller.Control_MainActivity;
import com.example.desktop.R;
import com.example.touch.ChoiceActivity;
import com.example.touch.GameActivity;
import com.example.touch.KeyboardActivity;
import com.example.touch.MouseActivity;
import com.example.utilTool.HomeForScreenThread;
import com.example.utilTool.MyGridAdapter;
import com.example.utilTool.MyGridView;
import com.example.utilTool.SendMsgThread;
import com.example.utilTool.SendMultiUdpMessage;
import com.example.utilTool.TcpReceive;
public class HomeFragment extends Fragment 
{
	private MyGridView gridview;
	private ProgressDialog mDialog;
	private ProgressBar progressBar;
	private  TextView textView;//显示扫描过程和结果
	private ImageButton image_sync;
	private Thread request_thread;
	private Thread response_thread;
	private boolean is_Scan_falg=false;  //当前操作是否为扫描操作
	private ImageView image;
	
	String connectionIP;
	String connectionPwd;
	SharedPreferences getVNCPreferences;
	Editor vncEditor;
	private String[] content_Screen=new String[]{};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		View logonView = inflater.inflate(R.layout.fragment_home_test, null);
		
		getVNCPreferences = getActivity().getSharedPreferences("VNCConnect", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
		vncEditor = getVNCPreferences.edit();
		progressBar=(ProgressBar)logonView.findViewById(R.id.progressBar1);
		textView=(TextView)logonView.findViewById(R.id.textView1);
		gridview=(MyGridView)logonView.findViewById(R.id.gridview);
	
		image_sync=(ImageButton) logonView.findViewById(R.id.imageButton1);
		gridview.setAdapter(new MyGridAdapter(getActivity()));
		
		progressBar.setVisibility(View.GONE);
		textView.setVisibility(View.GONE);
		return logonView;
	}
	@Override
	public void onPause()
	{
		super.onPause();
		handler.removeCallbacks(null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		gridview.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3)
			{
				switch (arg2)
				{
					case 0:
						connectProxyServer();
						break;
					case 1:
						 SharedPreferences getRemotePreferences = getActivity().getSharedPreferences("Remote", Context.MODE_PRIVATE);
						 connectionIP = getRemotePreferences.getString("remoteIP", "0000");
						 connectionPwd = getRemotePreferences.getString("remotePassword", "0000");
						 
						vncEditor.putString("IP", connectionIP);
						vncEditor.putString("password", connectionPwd);
						vncEditor.commit();
						startActivity(new Intent(getActivity(), VncCanvasActivity.class));
						break;
					case 2:
						SharedPreferences getFamilyPreferences = getActivity().getSharedPreferences("configInfo", Context.MODE_PRIVATE);
						connectionIP = getFamilyPreferences.getString("homeServiceIp", "0000");
						connectionPwd = getFamilyPreferences.getString("homeServicePwd", "0000");
						vncEditor.putString("IP", connectionIP);
						vncEditor.putString("password", connectionPwd);
						vncEditor.commit();
						startActivity(new Intent(getActivity(), VncCanvasActivity.class));
						break;
					case 3:
						HomeForScreenThread sendMsgThread=new HomeForScreenThread(handler, getActivity(),"screen");
						Thread thread =new Thread(sendMsgThread);
						thread.start();
						
						
						
						
						/*final String screenStr[]=new String[]{"小米TV","乐视TV","屏幕三"};
						AlertDialog.Builder builder=new Builder(getActivity());
						builder.setItems(screenStr, new DialogInterface.OnClickListener() 
						{
							
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								Toast.makeText(getActivity(), "您选择的是"+screenStr[which], Toast.LENGTH_SHORT).show();
								
							}
						});
						builder.create().show();*/
						break;
					case 4:
						startActivity(new Intent(getActivity(), GameActivity.class));
						break;
					case 5:
						startActivity(new Intent(getActivity(), Control_MainActivity.class));
						break;
					case 6:
						startActivity(new Intent(getActivity(), MouseActivity.class));
						break;
					case 7:
						startActivity(new Intent(getActivity(), KeyboardActivity.class));
						break;
					default:
						break;
				}
			}
		});
		image_sync.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				is_Scan_falg=true;
				progressBar.setVisibility(View.VISIBLE);
				textView.setText("正在检测代理服务器...");
				//TextView textview2=(TextView)gridview.getChildAt(0).findViewById(R.id.tv_item);
				textView.setVisibility(View.VISIBLE);
				check_Proxy();
			}
		});
	}
	
	/**
	 * 启动"终端"选择页面
	 */
	private void startChoiceRemoteFamilyActivity(String chooseFlag){
    	Intent iChoice = new Intent();
		iChoice.setClass(getActivity(), ChoiceActivity.class);
		iChoice.putExtra("choiceFlag", chooseFlag);
		startActivity(iChoice);
    }
	/*
	 * 检查代理服务器IP地址是否连通
	 */
	private void check_Proxy() 
	{
		RequestMesg requestMesg=new RequestMesg(null, null,null,2,6000);
		Client client=new Client(requestMesg);
		request_thread=new Thread(client);
		request_thread.start();
	}
	/*
	 * 扫描家庭服务中心IP地址
	 */
	private void scan_HomeServiceIp()
	{
		is_Scan_falg=false;
		textView.setText("正在扫描家庭服务中心IP地址");
		WifiManager wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
		MulticastLock multicastLock = wifiManager.createMulticastLock("multicast.test");
		multicastLock.acquire();
		TcpReceive tcpReceiveRunnable=new TcpReceive(handler);
		new Thread(tcpReceiveRunnable).start();
		SendMultiUdpMessage sendUdpMessage=new SendMultiUdpMessage(handler);
		Thread thread=new Thread(sendUdpMessage);
		thread.start();
	}
	private void connectProxyServer()
	{
		 mDialog = new ProgressDialog(getActivity());  
         mDialog.setTitle("测试连接代理服务器");  
         mDialog.setMessage("正在连接代理服务器，请稍后...");  
         mDialog.show();
         check_Proxy();
	}
	public Handler handler=new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			switch (msg.what) 
			{
				case 0:
				case 1:
					if(mDialog!=null)
						mDialog.cancel();
					if(!is_Scan_falg)
					{
						Toast.makeText(getActivity(), "连接代理服务器发生错误，请重新配置", Toast.LENGTH_LONG).show();
						image=(ImageView)gridview.getChildAt(0).findViewById(R.id.iv_item);
						image.setBackgroundResource(R.drawable.computer_failed);
					}
					else
					{
						Toast.makeText(getActivity(), "连接代理服务器发生错误，请重新配置", Toast.LENGTH_LONG).show();
						textView.setText("连接代理服务器发生错误，请重新配置...");
						image=(ImageView)gridview.getChildAt(0).findViewById(R.id.iv_item);
						image.setBackgroundResource(R.drawable.computer_failed);
						scan_HomeServiceIp();
					}
					break;
				case 2:
						//Toast.makeText(getActivity(), "连接代理服务器超时，请重新连接测试", Toast.LENGTH_LONG).show();
			        ClientReceive clientReceive=new ClientReceive();
			        response_thread=new Thread(clientReceive);
			        response_thread.start();
					break;
				case 3:
					if(mDialog!=null)
						mDialog.cancel();
					Toast.makeText(getActivity(), "连接代理服务器成功", Toast.LENGTH_SHORT).show();
					
					if(progressBar.getVisibility()==View.VISIBLE)
					{
						textView.setText("连接代理服务器成功");
						
						scan_HomeServiceIp();
					}
					break;
				case 4:
					if(mDialog!=null)
						mDialog.cancel();
					Toast.makeText(getActivity(), "默认IP不是代理服务器地址，请重新配置", Toast.LENGTH_LONG).show();
					break;
				case 5:
					if(mDialog!=null)
						mDialog.cancel();
					Toast.makeText(getActivity(), "接收返回信息发生错误，请重新连接测试", Toast.LENGTH_LONG).show();
					break;
				case 6:
					Toast.makeText(getActivity(), "发送多播信息失败", Toast.LENGTH_LONG).show();
					break;
				case 7:
					textView.setText("成功获取家庭服务器IP地址");
					Toast.makeText(getActivity(), "成功获取家庭服务器IP地址"+(String)msg.obj, Toast.LENGTH_LONG).show();
					
					//将扫描得到的家庭服务中心IP地址和端口保存到系统文件中
					SharedPreferences sharedConfigInfo=getActivity().getSharedPreferences("configInfo",Context.MODE_PRIVATE);
					SharedPreferences.Editor editor=sharedConfigInfo.edit();
					String ip_port=(String)msg.obj;;
					String[] temp_data=ip_port.split(",");
					editor.putString("homeServiceIp",temp_data[0]);
					editor.putInt("homeServicePortNumber", Integer.parseInt(temp_data[1]));
					if(!editor.commit())  
					{
						Toast.makeText(getActivity(), "保存家庭服务中心IP失败", Toast.LENGTH_SHORT).show();
					}
					progressBar.setVisibility(View.GONE);
					textView.setVisibility(View.GONE);
					break;
				case 8:
					textView.setText("扫描家庭服务中心IP地址失败");
					Toast.makeText(getActivity(), "扫描家庭服务中心IP地址失败", Toast.LENGTH_SHORT).show();
					image=(ImageView)gridview.getChildAt(1).findViewById(R.id.iv_item);
					image.setBackgroundResource(R.drawable.vmachine_failed);
					progressBar.setVisibility(View.GONE);
					textView.setVisibility(View.GONE);
					break;
				case 9:
					Toast.makeText(getActivity(), "家庭服务中心拒绝连接", Toast.LENGTH_LONG).show();
					break;
				case 10:
					Toast.makeText(getActivity(), "家庭服务中心已关闭", Toast.LENGTH_LONG).show();
					break;
				case 11:
					String screenStr=msg.getData().getString("msg");
					if(screenStr.equals("error")||null==screenStr||"".equals(screenStr))
					{
						Toast.makeText(getActivity(), "获取屏幕失败", Toast.LENGTH_LONG).show();
					}
					else
					{
						content_Screen=screenStr.split(",");
						AlertDialog.Builder builder=new Builder(getActivity());
						builder.setItems(content_Screen, new DialogInterface.OnClickListener() 
						{
							
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								Toast.makeText(getActivity(), "您选择的是"+content_Screen[which], Toast.LENGTH_SHORT).show();
								
							}
						});
						builder.create().show();
					}
					break;
				case 12:
					Toast.makeText(getActivity(), "连接家庭服务中心发生错误", Toast.LENGTH_LONG).show();
					break;
				default:
					break;
				}
			}
		};
	//发送请求消息线程
	class Client implements Runnable
	{
		RequestMesg requestMesg;
		Socket socketClient=null;
		SharedPreferences sharedPreferences=getActivity().getSharedPreferences("configInfo",Context.MODE_PRIVATE);
		String dstAddress=sharedPreferences.getString("proxyIpAdress","192.168.1.103");
		int dstPort=sharedPreferences.getInt("proxyPortNumber",8001);
		Message msg=handler.obtainMessage();
		public Client(RequestMesg requestMesg)
		{
			super();
			this.requestMesg = requestMesg;
		}
		@Override
		public void run()
		{
			try
			{
				socketClient=new Socket();
				SocketAddress socAddress = new InetSocketAddress(dstAddress, dstPort); 
				socketClient.connect(socAddress, 15*1000);
				ObjectOutputStream objOutStream=getOutObjectStream(socketClient);
				objOutStream.writeObject(requestMesg);
				msg.what=2;
				handler.sendMessage(msg);
				socketClient.close();
			} 
			catch (Exception e)
			{
				msg.what=0;
				handler.sendMessage(msg);
			} 
		}
		private ObjectOutputStream getOutObjectStream(Socket socket) throws IOException
		{
			 return new ObjectOutputStream(socket.getOutputStream());  
		}
	}
	//接收消息线程
	class ClientReceive implements Runnable
	{
		ServerSocket clientReceive;
		Socket socket;
		private int listenPort=6000;
		ResponseMesg responseMesg;
		Message msg=handler.obtainMessage();
		public void run() 
		{
			try
			{
				clientReceive=new ServerSocket(listenPort);
				clientReceive.setSoTimeout(10*1000);
				socket=clientReceive.accept();
				ObjectInputStream objectInStream=getInObjectStream(socket);
				responseMesg=(ResponseMesg) objectInStream.readObject();
				if(responseMesg==null)
				{ 
					msg.what=4;
					handler.sendMessage(msg);
				}
				else if(responseMesg.getMesgType()==0)
				{
					msg.what=3;
					handler.sendMessage(msg);
				}  
					socket.close(); 
				}
				catch (Exception e)
				{
					msg.what=5;
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

package com.example.utilTool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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
import java.util.Arrays;

import com.example.util.jsonTransfer.JsonParse;
import com.example.util.jsonTransfer.OptionEnum;
import com.example.util.jsonTransfer.Parameter2Option;
import com.example.util.jsonTransfer.ResponseMessage;
import com.example.util.jsonTransfer.ResponseMessageEnum;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

public class Send_AppImage_Msg implements Runnable
{
	private final int TIME_OUT = 10 * 1000;
	Socket socketClient = null;
	Handler handler = null;
	Activity activity;
	private String requestStr;
	private String application_arrs=null;
	MyCallBack callBack;
	public Send_AppImage_Msg(Handler handler, Activity activity,String requestStr, MyCallBack callBack)
	{
		super();
		this.handler = handler;
		this.activity = activity;
		this.requestStr = requestStr;
		this.callBack = callBack;
	}
	 @Override
	public void run()
	{
			SharedPreferences sharedPreferences=activity.getSharedPreferences("configInfo",Context.MODE_PRIVATE);
			String dstAddress=sharedPreferences.getString("homeServiceIp","192.168.1.112");
			int dstPort=sharedPreferences.getInt("homeServicePortNumber",8888);
			SocketAddress socAddress = new InetSocketAddress(dstAddress, dstPort);
			String image_sizes=null;
			String[] image_size_arr={};
			int length=0;
			try
			{
				socketClient = new Socket();
				socketClient.connect(socAddress, TIME_OUT);
				PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream())),true);
				out.println(requestStr);
				BufferedReader buffer=new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
				
				String responseInfo=buffer.readLine();
				//System.out.println("应用列表"+responseInfo);	
				ResponseMessage responseMessage=JsonParse.Json2Object(responseInfo);	
				if(responseMessage.getErrNum()==ResponseMessageEnum.ERROR.ordinal())
				{
					handler.sendEmptyMessage(5);
				}
				else
				{
					//String[] temp=responseInfo.split(":::");//截取响应字符串，temp[0]图片大小，temp[1]应用字符串
					String[] temp=responseMessage.getResponseMessage().split(":::");
					image_sizes=temp[0];
					application_arrs=temp[1];
				   //System.out.println("send_Thread:"+application_arrs);
					image_size_arr=image_sizes.split(",");
				}
			}
			catch (ConnectException e)
			{
				handler.sendEmptyMessage(0);
			} 
			catch (SocketException e)
			{
				handler.sendEmptyMessage(1);
			} 
			catch (Exception e)
			{
				handler.sendEmptyMessage(5);
			}
			finally
			{
				try
				{
					if(socketClient!=null)
						socketClient.close();					
				} 
				catch (IOException e)
				{
					e.printStackTrace();
				}	
			}
			if( null!=image_sizes)
			{
				try
				{   
					 
					Bitmap[] bmp=new Bitmap[image_size_arr.length];
					socketClient = new Socket();			
					socketClient.connect(socAddress, TIME_OUT);					
					PrintWriter out2 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream())),true);
					out2.println(JsonParse.Json2String(OptionEnum.APP_LIST.ordinal(), new Parameter2Option("image", "192.168.1.111")));
							
					byte[] imageByteArrs=read_images(socketClient.getInputStream(),length);//获取图片字节
					/*System.out.println("图片字节长度:"+imageByteArrs.length);
					System.out.println("图片字节数组长度:"+image_size_arr.length);*/
					int index=0;
					for(int i=0;i<image_size_arr.length;i++)
					{
						//System.out.println("inter:"+i+" index:"+index+" length:"+(Integer.valueOf(image_size_arr[i])));
						byte[] data=Arrays.copyOfRange(imageByteArrs, index, index+Integer.valueOf(image_size_arr[i]));
						//System.out.println("data length:"+data.length);
						bmp[i]= BitmapFactory.decodeByteArray(data, 0, data.length);
						//saveBitmap(bmp[i],"xujian"+String.valueOf(i)+".png");
						index+=Integer.valueOf(image_size_arr[i]);
					}
					callBack.getResult(application_arrs,bmp);
				}
				catch (ConnectException e)
				{
					handler.sendEmptyMessage(0);
				} 
				catch (SocketException e)
				{
					handler.sendEmptyMessage(1);
				} 
				catch (Exception e)
				{
					handler.sendEmptyMessage(5);
				}
				finally
				{
					try
					{
						if(socketClient!=null)
							socketClient.close();
					} catch (IOException e)
					{
						e.printStackTrace();
					}	
				}
			}
			else 
			{
				callBack.getResult(null, null);
			}
	}

	private byte[] read_images(InputStream inStream, int length)
	{
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		int len=0;
		byte[] buffer = new byte[1024];
		try
		{
			//int len = inStream.read(buffer);
			//outputStream.write(buffer, 0, length);
			  while((len=inStream.read(buffer))!=-1)
			  {
			  //System.out.println("--"+len); 
			  outputStream.write(buffer, 0, len);
			  }
		
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		byte[] result = outputStream.toByteArray();
		return result;
	}
	public interface MyCallBack
	{
		public void getResult(String application_arr,Bitmap[] bitmap);
	}
}


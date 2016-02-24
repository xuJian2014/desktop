package com.example.utilTool;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import android.os.Handler;
import android.os.Message;

public class SendMultiUdpMessage implements Runnable
{
	Handler handler;
	private MulticastSocket ms=null;
	private int port=8899;
	DatagramPacket dataPacket = null;
	Message message;
	public SendMultiUdpMessage(Handler handler)
	{
		this.handler=handler;
		message=handler.obtainMessage();
	}
	@Override
	public void run()
	{
            try 
            {  
            	ms = new MulticastSocket(); 
                ms.setTimeToLive(32);  
                byte[] data = "hello".getBytes();   
                InetAddress address = InetAddress.getByName("239.0.0.2");   
                dataPacket = new DatagramPacket(data, data.length, address,port); 
                ms.send(dataPacket);  
                System.out.println("组播执行了");
            } 
            catch (Exception e) 
            {  
            	message.what=6;
            	handler.sendMessage(message);
            } 
            finally
            {
            	if(ms!=null)
            	{
            		ms.close();  
            	}
            }
		}
}
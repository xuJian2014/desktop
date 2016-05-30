package com.example.media_file;

import java.util.ArrayList;
import java.util.List;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Toast;
import com.example.Entity.MediaItem;
import com.example.desktop.R;
import com.example.util.jsonTransfer.JsonParse;
import com.example.util.jsonTransfer.OptionEnum;
import com.example.util.jsonTransfer.Parameter2Option;
import com.example.util.jsonTransfer.ResponseMessage;
import com.example.util.jsonTransfer.ResponseMessageEnum;
import com.example.utilTool.Media_List_Adapter;
import com.example.utilTool.ReFlashListView;
import com.example.utilTool.ReFlashListView.IReflashListener;
import com.example.utilTool.ScreenResponseMsg;
import com.example.utilTool.SendMsgAppScreen;
import com.example.utilTool.SendMsgThread;

public class RemoteMediaActivity extends Activity implements IReflashListener
{
	private ReFlashListView listView;
	private ProgressDialog mDialog;
	private String[] content_Media=new String[]{};
	private String[] screenList=new String[]{};
	private List<MediaItem> mediaList=new ArrayList<MediaItem>();
	private Media_List_Adapter adapter;
	private AdapterContextMenuInfo info;
	private int position;
	private SendMsgThread sendMsgThread;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_videos);
		
		getActionBar().setDisplayUseLogoEnabled(true);
		getActionBar().setIcon(null);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
		
		listView=(ReFlashListView) findViewById(R.id.reFlashListView1);
		listView.setInterface(RemoteMediaActivity.this);
		position=getIntent().getIntExtra("position", 0);
		
		switch (position)
		{
		case 0:
			sendMsgThread=new SendMsgThread(handler, RemoteMediaActivity.this,JsonParse.Json2String(OptionEnum.Remote_File_Videos.ordinal(), null));
			break;
		case 1:
			sendMsgThread=new SendMsgThread(handler, RemoteMediaActivity.this,JsonParse.Json2String(OptionEnum.Remote_File_Music.ordinal(), null));
			break;
		case 2:
			sendMsgThread=new SendMsgThread(handler, RemoteMediaActivity.this,JsonParse.Json2String(OptionEnum.Remote_File_Office.ordinal(), null));
			break;
		case 3:
			sendMsgThread=new SendMsgThread(handler, RemoteMediaActivity.this,JsonParse.Json2String(OptionEnum.Remote_File_Photos.ordinal(), null));
			break;
		default:
			break;
		}
		 
		Thread thread =new Thread(sendMsgThread);
		thread.start(); 
	}

	public Handler handler=new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			switch(msg.what)
			{
				case 0:
				case 1:
				case 5:
					if(mDialog!=null)
						mDialog.cancel();
					cleanListView();
					Toast.makeText(RemoteMediaActivity.this, "对不起，连接家庭服务终端失败，请稍候重试", Toast.LENGTH_LONG).show();
					break;
				case 2://获取媒体库文件字符串
					String mediaStr=msg.getData().getString("msg");
					ResponseMessage responseMessage=JsonParse.Json2Object(mediaStr);
					if(responseMessage==null)
					{
						Toast.makeText(RemoteMediaActivity.this, "获取媒体库文件失败", Toast.LENGTH_LONG).show();
					}
					else
					{
						if(responseMessage.getErrNum()==ResponseMessageEnum.ERROR.ordinal())
						{
							Toast.makeText(RemoteMediaActivity.this, "获取媒体库文件失败", Toast.LENGTH_LONG).show();
						}
						else
						{
							content_Media=responseMessage.getResponseMessage().split(",");
							binderListData(content_Media);
						}
					}
					break;
				case 3: //获取屏幕字符串
					if(mDialog!=null)
						mDialog.cancel();
					String screenStr=msg.getData().getString("msg");
					ResponseMessage responseMessage2=JsonParse.Json2Object(screenStr);
					if(responseMessage2==null)
					{
						Toast.makeText(RemoteMediaActivity.this, "对不起,没有找到屏幕", Toast.LENGTH_LONG).show();
					}
					else
					{
						if(responseMessage2.getErrNum()==ResponseMessageEnum.ERROR.ordinal())
						{
							Toast.makeText(RemoteMediaActivity.this, "对不起,没有找到屏幕", Toast.LENGTH_LONG).show();
						}
						else
						{
							screenList=responseMessage2.getResponseMessage().split(",");
							SharedPreferences sharedPreferences=getSharedPreferences("configInfo",Context.MODE_PRIVATE);
							for (int i = 0; i <screenList.length; i++)
							{
								if(sharedPreferences.contains(screenList[i]))
								{
									screenList[i]=sharedPreferences.getString(screenList[i], null);
								}
							}
							showScreen(screenList);
						}
					}
					break;
				case 4:
					if(mDialog!=null)
						mDialog.cancel();
					String screenIsSuccess=msg.getData().getString("msg");
					ResponseMessage responseMessage3=JsonParse.Json2Object(screenIsSuccess);
					if(responseMessage3==null)
					{
						Toast.makeText(RemoteMediaActivity.this, "对不起，投影失败，请稍候重试...", Toast.LENGTH_LONG).show();
					}
					else
					{
						int errNum=responseMessage3.getErrNum();
						if(errNum==ResponseMessageEnum.SUCCESS.ordinal())
						{
							//Toast.makeText(getActivity(), "投影"+adapter.getItem(info.position-1).getMediaName()+"成功！", Toast.LENGTH_LONG).show();
							Toast.makeText(RemoteMediaActivity.this, "投影成功！", Toast.LENGTH_LONG).show();
						}
						else if(errNum==ResponseMessageEnum.WAIT.ordinal())
						{
							Toast.makeText(RemoteMediaActivity.this, "正在连接中，请稍后请重试...", Toast.LENGTH_LONG).show();
						}
						else if(errNum==ResponseMessageEnum.UNCONNECTED.ordinal())
						{
							Toast.makeText(RemoteMediaActivity.this, "对不起，未能成功连接，请重试...", Toast.LENGTH_LONG).show();
						}
						else //error
						{
							//Toast.makeText(getActivity(), "投影"+adapter.getItem(info.position-1).getMediaName()+"失败！", Toast.LENGTH_LONG).show();
							Toast.makeText(RemoteMediaActivity.this, "对不起，投影失败！", Toast.LENGTH_LONG).show();
						}
					}
					break;
				default:
					break;
			}
		}
	};
	private void binderListData(String[] str)
	{
		cleanListView();
		MediaItem mediaItem=null;
		for(int i=0;i<str.length;i++)
		{
			int k=str[i].lastIndexOf(".");
			String suffixName=str[i].substring(k+1);
			if(suffixName.equals("pdf"))
			{
				 mediaItem=new MediaItem(str[i], R.drawable.media_pdf);
			}
			else if(suffixName.equals("docx")||suffixName.equals("doc"))
			{
				mediaItem=new MediaItem(str[i], R.drawable.media_word);
			}
			else if(suffixName.equals("avi")||suffixName.equals("mp4"))
			{
				mediaItem=new MediaItem(str[i], R.drawable.media_avi);
			}
			else if(suffixName.equals("mp3"))
			{
				mediaItem=new MediaItem(str[i], R.drawable.media_music);
			}
			else if(suffixName.equals("xlsx"))
			{
				mediaItem=new MediaItem(str[i], R.drawable.media_excel);
			}
			else if(suffixName.equals("ppt")||suffixName.equals("pptx"))
			{
				mediaItem=new MediaItem(str[i], R.drawable.media_ppt);
			}
			else if(suffixName.equals("jpg")||suffixName.equals("png"))
			{
				mediaItem=new MediaItem(str[i], R.drawable.media_photo);
			}
			else
			{
				 mediaItem=new MediaItem(str[i], R.drawable.media_item);
			}
			mediaList.add(mediaItem);
		}
		adapter=new Media_List_Adapter(RemoteMediaActivity.this, R.layout.content_list_item, mediaList);
		listView.setAdapter(adapter);
		registerForContextMenu(listView);
	}
	private void cleanListView() 
	{
		int size=mediaList.size();
		if(size>0)
		{
			mediaList.removeAll(mediaList);
			adapter.notifyDataSetChanged();
			listView.setAdapter(adapter);
		}
	}
	private void showScreen(String[] str)
	{
		AlertDialog.Builder builder=new AlertDialog.Builder(RemoteMediaActivity.this);
		builder.setTitle("屏幕列表");
		builder.setItems(screenList,new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				 if(info!=null)
				 {
					 mDialog = new ProgressDialog(RemoteMediaActivity.this);  
			         mDialog.setTitle("投影");  
			         mDialog.setMessage("正在进行投影，请稍等...");  
			         mDialog.show();
			         ScreenResponseMsg msgAppScreen=new ScreenResponseMsg(handler, RemoteMediaActivity.this, 
				    		 JsonParse.Json2String(OptionEnum.PROJECTION_FILE.ordinal(), new Parameter2Option(String.valueOf(info.id), String.valueOf(which))));
					 Thread thread=new Thread(msgAppScreen);
					 thread.start();
				 }
			}
			});
		builder.create().show();
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = RemoteMediaActivity.this.getMenuInflater();
		inflater.inflate(R.menu.application_menu, menu);	 
		
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) 
	{ 
		if(item.getMenuInfo() instanceof AdapterContextMenuInfo)
		{
			info = (AdapterContextMenuInfo) item.getMenuInfo();
			switch (item.getItemId()) 
			{
				case R.id.screen: //投影屏幕
					 mDialog = new ProgressDialog(RemoteMediaActivity.this);  
				     mDialog.setTitle("屏幕");  
				     mDialog.setMessage("正在获取屏幕，请稍等...");  
				     mDialog.show();
					 SendMsgAppScreen sendMsgScreen=new SendMsgAppScreen(handler, RemoteMediaActivity.this,JsonParse.Json2String(OptionEnum.DISPLAY.ordinal(), null));
					 Thread threadScreen =new Thread(sendMsgScreen);
					threadScreen.start(); 	
					break;
			}
			return true; 
		}
		else
			 return false;
    } 
	
	private void setReflashData() 
	{
		SendMsgThread sendMsgThread=new SendMsgThread(handler, RemoteMediaActivity.this,JsonParse.Json2String(OptionEnum.FILE.ordinal(), null));
		Thread thread =new Thread(sendMsgThread);
		thread.start(); 
	}
	@Override
	public void onReflash()
	{
		setReflashData();
		listView.reflashComplete();
	}	
}

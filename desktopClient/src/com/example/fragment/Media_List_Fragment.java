package com.example.fragment;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;
import com.example.Entity.MediaItem;
import com.example.desktop.R;
import com.example.tree_view.FileBean;
import com.example.tree_view.Node;
import com.example.tree_view.SimpleTreeAdapter;
import com.example.tree_view.TreeListViewAdapter.OnTreeNodeClickListener;
import com.example.util.jsonTransfer.JsonParse;
import com.example.util.jsonTransfer.OptionEnum;
import com.example.util.jsonTransfer.Parameter1Option;
import com.example.util.jsonTransfer.Parameter2Option;
import com.example.util.jsonTransfer.ResponseMessage;
import com.example.util.jsonTransfer.ResponseMessageEnum;
import com.example.utilTool.Media_List_Adapter;
import com.example.utilTool.ReFlashListView;
import com.example.utilTool.ReFlashListView.IReflashListener;
import com.example.utilTool.ScreenResponseMsg;
import com.example.utilTool.SendMsgAppScreen;
import com.example.utilTool.SendMsgThread;
import com.example.utilTool.Send_FileSystem_MsgThread;
import com.example.utilTool.StringUtil;
public class Media_List_Fragment extends Fragment implements IReflashListener
{
	private String[] content_Media=new String[]{};
	private ReFlashListView listView;
	private String[] screenList=new String[]{};
	private AdapterContextMenuInfo info;
	private View view;
	private List<MediaItem> mediaList=new ArrayList<MediaItem>();
	private Media_List_Adapter adapter;
	private ProgressDialog mDialog; 
	private List<FileBean> mDatas = new ArrayList<FileBean>();
	private ListView mTree;
	private SimpleTreeAdapter<FileBean> mAdapter;
	private int currentId=1;
	private static String selectedFileDir=null;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		view=inflater.inflate(R.layout.media_list, container,false);
		listView=(ReFlashListView) view.findViewById(R.id.listView1);
		listView.setInterface(this);
		SendMsgThread sendMsgThread=new SendMsgThread(handler, getActivity(),JsonParse.Json2String(OptionEnum.FILE.ordinal(), null));
		Thread thread =new Thread(sendMsgThread);
		thread.start(); 
		initDatas();//初始化数据mDatas
		mTree = (ListView) view.findViewById(R.id.id_tree);
		try
		{
			mAdapter = new SimpleTreeAdapter<FileBean>(mTree, getActivity(), mDatas, 1);
			mAdapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener()
			{
				@Override
				public void onClick(Node node, int position)
				{
					String nodeName;
					if(node.getChildren().size()==0)
					{
						currentId=position;//使用当前目录id作为父ID
						if(node.getId()==1)
						{
							nodeName="fileSystem";
							Send_FileSystem_MsgThread send_FileSystem_MsgThread=new Send_FileSystem_MsgThread(handler, getActivity(),JsonParse.Json2String(OptionEnum.FILE_SYSTEM.ordinal(), null));
							Thread thread =new Thread(send_FileSystem_MsgThread);
							thread.start();
						}
						else
						{
							nodeName=node.getName();
							Node pNode=node;
							while(pNode.getParent()!=null&&(!("文件系统".equals(pNode.getParent().getName()))))
							{
								nodeName=pNode.getParent().getName()+"\\"+nodeName;
								pNode=pNode.getParent();
							}
							selectedFileDir=nodeName;
							Send_FileSystem_MsgThread send_FileSystem_MsgThread=new Send_FileSystem_MsgThread(handler, getActivity(), 
									JsonParse.Json2String(OptionEnum.DRIVE.ordinal(), new Parameter1Option(nodeName)));
							Thread thread =new Thread(send_FileSystem_MsgThread);
							thread.start();
						}
					}
				}
			});
			mTree.setAdapter(mAdapter);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return view;
	}
	private void initDatas()
	{
		mDatas.add(new FileBean(1, 0, "文件系统"));
	}
	@Override
	public void onPause() 
	{
		super.onPause();
		handler.removeCallbacks(null);
	}
	@SuppressLint("HandlerLeak") 
	Handler handler=new Handler()
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
					Toast.makeText(getActivity(), "对不起，连接家庭服务终端失败，请稍候重试", Toast.LENGTH_LONG).show();
					break;
				case 2://获取媒体库文件字符串
					String mediaStr=msg.getData().getString("msg");
					ResponseMessage responseMessage=JsonParse.Json2Object(mediaStr);
					if(responseMessage==null)
					{
						Toast.makeText(getActivity(), "获取媒体库文件失败", Toast.LENGTH_LONG).show();
					}
					else
					{
						if(responseMessage.getErrNum()==ResponseMessageEnum.ERROR.ordinal())
						{
							Toast.makeText(getActivity(), "获取媒体库文件失败", Toast.LENGTH_LONG).show();
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
						Toast.makeText(getActivity(), "对不起,没有找到屏幕", Toast.LENGTH_LONG).show();
					}
					else
					{
						if(responseMessage2.getErrNum()==ResponseMessageEnum.ERROR.ordinal())
						{
							Toast.makeText(getActivity(), "对不起,没有找到屏幕", Toast.LENGTH_LONG).show();
						}
						else
						{
							screenList=responseMessage2.getResponseMessage().split(",");
							SharedPreferences sharedPreferences=getActivity().getSharedPreferences("configInfo",Context.MODE_PRIVATE);
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
						Toast.makeText(getActivity(), "对不起，投影失败，请稍候重试...", Toast.LENGTH_LONG).show();
					}
					else
					{
						int errNum=responseMessage3.getErrNum();
						if(errNum==ResponseMessageEnum.SUCCESS.ordinal())
						{
							//Toast.makeText(getActivity(), "投影"+adapter.getItem(info.position-1).getMediaName()+"成功！", Toast.LENGTH_LONG).show();
							Toast.makeText(getActivity(), "投影成功！", Toast.LENGTH_LONG).show();
						}
						else if(errNum==ResponseMessageEnum.WAIT.ordinal())
						{
							Toast.makeText(getActivity(), "正在连接中，请稍后请重试...", Toast.LENGTH_LONG).show();
						}
						else if(errNum==ResponseMessageEnum.UNCONNECTED.ordinal())
						{
							Toast.makeText(getActivity(), "对不起，未能成功连接，请重试...", Toast.LENGTH_LONG).show();
						}
						else //error
						{
							//Toast.makeText(getActivity(), "投影"+adapter.getItem(info.position-1).getMediaName()+"失败！", Toast.LENGTH_LONG).show();
							Toast.makeText(getActivity(), "对不起，投影失败！", Toast.LENGTH_LONG).show();
						}
					}
					break;
				case 6:
					String file_drc=msg.getData().getString("msg");
					ResponseMessage responseMessage4=JsonParse.Json2Object(file_drc);
					if(responseMessage4==null)
					{
						Toast.makeText(getActivity(), "获取文件路径失败", Toast.LENGTH_LONG).show();
					}
					else
					{
						if(responseMessage4.getErrNum()==ResponseMessageEnum.ERROR.ordinal())
						{
							Toast.makeText(getActivity(), "对不起，您没有权限访问该文件！", Toast.LENGTH_LONG).show();
						}
						else if("".equals(responseMessage4.getResponseMessage()))
						{
							Toast.makeText(getActivity(), "这是一个空文件夹，不能展开！", Toast.LENGTH_SHORT).show();
						}
						else if("projectionFile".equals(responseMessage4.getResponseMessage()))
						{
							if(StringUtil.isFileForProjection(StringUtil.getExtensionName(selectedFileDir)))
							{
								mDialog = new ProgressDialog(getActivity());  
					            mDialog.setTitle("屏幕");  
					            mDialog.setMessage("正在获取屏幕，请稍等...");  
					            mDialog.show();
								SendMsgAppScreen sendMsgScreen=new SendMsgAppScreen(handler, getActivity(),JsonParse.Json2String(OptionEnum.DISPLAY.ordinal(), null));
								Thread threadScreen =new Thread(sendMsgScreen);
								threadScreen.start(); 	
							}
							else
							{
								Toast.makeText(getActivity(), "这是一个文件，并不能投影！", Toast.LENGTH_SHORT).show();
							}
						}
						else
						{
							setFileDir(responseMessage4.getResponseMessage());
						}
					}
					break;
				case 7:
					Toast.makeText(getActivity(), "获取文件路径错误", Toast.LENGTH_SHORT).show();
					break;
					
				case 8:
					if(mDialog!=null)
						mDialog.cancel();
					String screenStr2=msg.getData().getString("msg");
					ResponseMessage responseMessage5=JsonParse.Json2Object(screenStr2);
					if(responseMessage5==null)
					{
						Toast.makeText(getActivity(), "对不起,没有找到屏幕", Toast.LENGTH_LONG).show();
					}
					else
					{
						if(responseMessage5.getErrNum()==ResponseMessageEnum.ERROR.ordinal())
						{
							Toast.makeText(getActivity(), "对不起,没有找到屏幕", Toast.LENGTH_LONG).show();
						}
						else
						{
							screenList=responseMessage5.getResponseMessage().split(",");
							SharedPreferences sharedPreferences=getActivity().getSharedPreferences("configInfo",Context.MODE_PRIVATE);
							for (int i = 0; i <screenList.length; i++)
							{
								if(sharedPreferences.contains(screenList[i]))
								{
									screenList[i]=sharedPreferences.getString(screenList[i], null);
								}
							}
							showScreenforFileSystem(screenList);
						}
					}
					break;
				default:
					break;
			}
		}
		private void showScreenforFileSystem(String[] screenList)
		{
			AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
			builder.setTitle("屏幕列表");
			builder.setItems(screenList,new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					 mDialog = new ProgressDialog(getActivity());  
			         mDialog.setTitle("投影");  
			         mDialog.setMessage("正在进行投影，请稍等...");  
			         mDialog.show();
			         ScreenResponseMsg msgAppScreen=new ScreenResponseMsg(handler, getActivity(), 
				    		 JsonParse.Json2String(OptionEnum.PROJECTION_FILE_SYSTEM.ordinal(), new Parameter2Option(selectedFileDir, String.valueOf(which))));
					 Thread thread=new Thread(msgAppScreen);
					 thread.start();
					}
				});
			builder.create().show();
			
		}
		private void setFileDir(String file_drc)
		{
			String temp[]=file_drc.split(";");
			if(!" ".equals(temp[0]))
			{
				String[] files=temp[0].split(",");
				for(int i=files.length-1;i>=0;i--)
				{
					mAdapter.addExtraNode(currentId, files[i],1);
				}
			}
			if(!" ".equals(temp[1]))
			{
				String[] file=temp[1].split(",");
				for(int i=file.length-1;i>=0;i--)
				{
					mAdapter.addExtraNode(currentId, file[i],2);
				}
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
		adapter=new Media_List_Adapter(getActivity(), R.layout.content_list_item, mediaList);
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
		AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
		builder.setTitle("屏幕列表");
		builder.setItems(screenList,new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				 mDialog = new ProgressDialog(getActivity());  
		         mDialog.setTitle("投影");  
		         mDialog.setMessage("正在进行投影，请稍等...");  
		         mDialog.show();
		         ScreenResponseMsg msgAppScreen=new ScreenResponseMsg(handler, getActivity(), 
			    		 JsonParse.Json2String(OptionEnum.PROJECTION_FILE.ordinal(), new Parameter2Option(String.valueOf(info.id), String.valueOf(which))));
				 Thread thread=new Thread(msgAppScreen);
				 thread.start();
				}
			});
		builder.create().show();
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.application_menu, menu);	 
		
	}
	public boolean onContextItemSelected(MenuItem item) 
	{ 
			if (getUserVisibleHint())
		 	{  
				info = (AdapterContextMenuInfo)item.getMenuInfo();
				switch (item.getItemId()) 
				{
					case R.id.screen: //投影屏幕
						 mDialog = new ProgressDialog(getActivity());  
			             mDialog.setTitle("屏幕");  
			             mDialog.setMessage("正在获取屏幕，请稍等...");  
			             mDialog.show();
						SendMsgAppScreen sendMsgScreen=new SendMsgAppScreen(handler, getActivity(),JsonParse.Json2String(OptionEnum.DISPLAY.ordinal(), null));
						Thread threadScreen =new Thread(sendMsgScreen);
						threadScreen.start(); 	
						break;
				}
		        return true;  
		    }
			return false;
    } 
	
	@Override
	public void onDestroy()
	{
		handler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}
	private void setReflashData() 
	{
		SendMsgThread sendMsgThread=new SendMsgThread(handler, getActivity(),JsonParse.Json2String(OptionEnum.FILE.ordinal(), null));
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
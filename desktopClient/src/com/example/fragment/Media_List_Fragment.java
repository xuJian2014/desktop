package com.example.fragment;

import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.example.utilTool.Media_List_Adapter;
import com.example.utilTool.ReFlashListView;
import com.example.utilTool.ReFlashListView.IReflashListener;
import com.example.utilTool.ScreenResponseMsg;
import com.example.utilTool.SendMsgAppScreen;
import com.example.utilTool.SendMsgThread;
import com.example.utilTool.Send_FileSystem_MsgThread;
import com.example.utilTool.StaticValue;
public class Media_List_Fragment extends Fragment implements IReflashListener
{
	private static String mediaStr;
	private static String[] content_Media=new String[]{};
	ReFlashListView listView;
	String screenStr;
	static String[] screenList=new String[]{};
	private AdapterContextMenuInfo info;
	private String screenIsSuccess;
	View view;
	private List<MediaItem> mediaList=new ArrayList<MediaItem>();
	Media_List_Adapter adapter;
	private ProgressDialog mDialog; 
	private String MachineIp;
	private List<FileBean> mDatas = new ArrayList<FileBean>();
	private ListView mTree;
	private SimpleTreeAdapter<FileBean> mAdapter;
	private  int currentId=1;
	private String file_drc=null;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		MachineIp =StaticValue.getvMachineIp();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		view=inflater.inflate(R.layout.media_list, container,false);
		listView=(ReFlashListView) view.findViewById(R.id.listView1);
		listView.setInterface(this);
		SendMsgThread sendMsgThread=new SendMsgThread(handler, getActivity(),"file,"+"192.168.1.109");
		Thread thread =new Thread(sendMsgThread);
		thread.start(); 
		initDatas();//��ʼ������mDatas
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
						currentId=position;//ʹ�õ�ǰĿ¼id��Ϊ��ID
						if(node.getId()==1)
						{
							nodeName="fileSystem";
							Send_FileSystem_MsgThread send_FileSystem_MsgThread=new Send_FileSystem_MsgThread(handler, getActivity(),nodeName+",192.168.1.109");
							Thread thread =new Thread(send_FileSystem_MsgThread);
							thread.start();
						}
						else
						{
							nodeName=node.getName();
							Node pNode=node;
							while(pNode.getParent()!=null&&(!("�ļ�ϵͳ".equals(pNode.getParent().getName()))))
							{
								nodeName=pNode.getParent().getName()+"\\"+nodeName;
								pNode=pNode.getParent();
							}
							Send_FileSystem_MsgThread send_FileSystem_MsgThread=new Send_FileSystem_MsgThread(handler, getActivity(), "drive,"+nodeName+",192.168.1.109");
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
		mDatas.add(new FileBean(1, 0, "�ļ�ϵͳ"));
	}
	@Override
	public void onPause() 
	{
		super.onPause();
		handler.removeCallbacks(null);
	}
	Handler handler=new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			switch(msg.what)
			{
				case 0:
					if(mDialog!=null)
						mDialog.cancel();
					cleanListView();
					Toast.makeText(getActivity(), "��ͥ�������ľܾ�����", Toast.LENGTH_LONG).show();
					break;
				case 1:
					if(mDialog!=null)
						mDialog.cancel();
					cleanListView();
					Toast.makeText(getActivity(), "��ͥ���������ѹر�", Toast.LENGTH_LONG).show();
					break;
				case 2://��ȡ�ļ��ַ���
					mediaStr=msg.getData().getString("msg");
					if("error".equals(mediaStr)||null==mediaStr)
					{
						Toast.makeText(getActivity(), "��ȡý����ļ�ʧ��", Toast.LENGTH_LONG).show();
					}
					else
					{
						content_Media=mediaStr.split(",");
						binderListData(content_Media);
					}
					break;
				case 3: //��ȡ��Ļ�ַ���
					if(mDialog!=null)
						mDialog.cancel();
					screenStr=msg.getData().getString("msg");
					if("".equals(screenStr)||screenStr==null || "error".equals(screenStr))
					{
						 Toast.makeText(getActivity(), "�Բ���,û���ҵ���Ļ", Toast.LENGTH_LONG).show();
					}
					else
					{
						screenList=screenStr.split(",");
						showScreen(screenList);
					}
					break;
				case 4:
					screenIsSuccess=msg.getData().getString("msg");
					if(mDialog!=null)
						mDialog.cancel();
					if("success".equals(screenIsSuccess))
					{
						Toast.makeText(getActivity(), adapter.getItem(info.position-1).getMediaName()+"�ɹ���", Toast.LENGTH_LONG).show();
					}
					else if("wait".equals(screenIsSuccess))
					{
						Toast.makeText(getActivity(), "���������У��Ժ�������...", Toast.LENGTH_LONG).show();
					}
					else if("ignore".equals(screenIsSuccess))
					{
						Toast.makeText(getActivity(), screenIsSuccess, Toast.LENGTH_LONG).show();
					}
					else
					{
						Toast.makeText(getActivity(), adapter.getItem(info.position-1).getMediaName()+"ʧ�ܣ�", Toast.LENGTH_LONG).show();
					}
					break;
				case 5:
					if(mDialog!=null)
						mDialog.cancel();
					cleanListView();
					Toast.makeText(getActivity(), "���Ӽ�ͥ�������ķ�������", Toast.LENGTH_SHORT).show();
					break;
				case 6:
					file_drc=msg.getData().getString("msg");
					//Toast.makeText(getActivity(), file_drc, Toast.LENGTH_SHORT).show();
					if(null==file_drc)
						Toast.makeText(getActivity(), "��ȡ�ļ�·��ʧ��", Toast.LENGTH_SHORT).show();
					else if("".equals(file_drc))
					{
						Toast.makeText(getActivity(), "����һ���ļ�����ļ��У�����չ����", Toast.LENGTH_SHORT).show();
					}
					else if("error".equals(file_drc))
					{
						Toast.makeText(getActivity(), "�Բ�����û��Ȩ�޷��ʸ��ļ���", Toast.LENGTH_SHORT).show();
					}
					else
					{
						setFileDir(file_drc);
					}
					break;
				case 7:
					Toast.makeText(getActivity(), "��ȡ�ļ�·������", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
			}
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
			/*mAdapter.notifyDataSetInvalidated();
			System.out.println("ִ��ListViewˢ�²���");*/
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
		builder.setTitle("��Ļ�б�");
		builder.setItems(screenList,new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				 mDialog = new ProgressDialog(getActivity());  
		         mDialog.setTitle("ͶӰ");  
		         mDialog.setMessage("���ڽ���ͶӰ�����Ե�...");  
		         mDialog.show();
			     ScreenResponseMsg msgAppScreen=new ScreenResponseMsg(handler, getActivity(), info.id+",open,"+String.valueOf(which));
			     System.out.println("----"+info.id+",open,"+String.valueOf(which));
				 Thread thread=new Thread(msgAppScreen);
				 thread.start();
				}
			});
		builder.create().show();
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo)
	{
		 MenuInflater inflater = getActivity().getMenuInflater();
		 inflater.inflate(R.menu.application_menu, menu);
		 super.onCreateContextMenu(menu, v, menuInfo);
	}
	public boolean onContextItemSelected(MenuItem item) 
	{ 
		info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) 
		{
			case R.id.screen: //ͶӰ��Ļ
				 mDialog = new ProgressDialog(getActivity());  
	             mDialog.setTitle("��Ļ");  
	             mDialog.setMessage("���ڻ�ȡ��Ļ�����Ե�...");  
	             mDialog.show();
				SendMsgAppScreen sendMsgScreen=new SendMsgAppScreen(handler, getActivity(),"display");
				Thread threadScreen =new Thread(sendMsgScreen);
				threadScreen.start(); 	
				break;
			case R.id.close: //�ر�
				ScreenResponseMsg closeMsg=new ScreenResponseMsg(handler, getActivity(),info.position+",close");
				Thread closeMsgThread =new Thread(closeMsg);
				closeMsgThread.start();
				break;
			case R.id.max:  //���
				ScreenResponseMsg maxMsg=new ScreenResponseMsg(handler, getActivity(),info.position+",max");
				Thread maxMsgThread =new Thread(maxMsg);
				maxMsgThread.start();
				break;
			case R.id.min: //��С��
				ScreenResponseMsg minMsg=new ScreenResponseMsg(handler, getActivity(),info.position+",min");
				Thread minMsgThread =new Thread(minMsg);
				minMsgThread.start();
				break;
			default:
				break;
		}
        return super.onContextItemSelected(item);  
    } 
	
	@Override
	public void onDestroy()
	{
		handler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}
	private void setReflashData() 
	{
		SendMsgThread sendMsgThread=new SendMsgThread(handler, getActivity(),"file,"+"192.168.1.109");
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
package com.example.fragment;

import java.util.ArrayList;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.desktop.R;
public class Media_List_Fragment extends Fragment implements OnPageChangeListener,TabListener
{
	private ViewPager mPager;
	private ArrayList<Fragment> mfragmentList;
	// 标题列表
	ArrayList<String> titleList = new ArrayList<String>();
	View view;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		view=inflater.inflate(R.layout.activity_view_pager, container,false);
		initViewPager();
		return view;
	}
	
	private void initViewPager() 
	{
		mPager = (ViewPager) view.findViewById(R.id.viewpager);

		mfragmentList = new ArrayList<Fragment>();
		mfragmentList.add(new com.example.media_file.LocalMediaFragment());
		mfragmentList.add(new com.example.media_file.RemoteMediaFragment());

		mPager.setAdapter(new MyViewPagerAdapter(getChildFragmentManager(),mfragmentList));
		mPager.setCurrentItem(0);   
		mPager.setOnPageChangeListener(this);

		/*getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar_shape));
		// 初始化TAB属性
		String[] tabName = null;
		String[] temTabName = { "本地 媒体库", "远程媒体库" };
		tabName = temTabName;

		for (int i = 0; i < tabName.length; i++) {
			ActionBar.Tab tab = getActivity().getActionBar().newTab();
			tab.setText(tabName[i]);
			tab.setTabListener(this);
			tab.setTag(i);
			getActivity().getActionBar().addTab(tab);
		}*/
	}

	//三个页面选项卡Fragment适配器
	public class MyViewPagerAdapter extends FragmentPagerAdapter 
	{
		ArrayList<Fragment> list;
		public MyViewPagerAdapter(FragmentManager fManager,ArrayList<Fragment> arrayList)
		{
			super(fManager);
			this.list = arrayList;
		}

		@Override
		public int getCount()
		{
			return list == null ? 0 : list.size();
		}

		@Override
		public Fragment getItem(int arg0)
		{

			return list.get(arg0);
		}

		@Override
		public int getItemPosition(Object object)
		{
			return POSITION_NONE;
		}

		@Override
		public boolean isViewFromObject(View view, Object obj)
		{
			return view == ((Fragment) obj).getView();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
				
		}
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2)
	{
		// TODO Auto-generated method stub

	}
	@Override
	public void onPageSelected(int arg0) 
	{
		//滑动ViewPager的时候设置相对应的ActionBar Tab被选中  
		//getActivity().getActionBar().getTabAt(arg0).select();
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) 
	{
		if (tab.getTag() == null)
			return;
		//选中tab,滑动选项卡
		int index = ((Integer) tab.getTag()).intValue();
		if (mPager != null && mPager.getChildCount() > 0 && mfragmentList.size() > index)
			mPager.setCurrentItem(index);
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1)
	{
		// TODO Auto-generated method stub

	}
	
}



//2016.05.27日版本
/*import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.desktop.R;
import com.example.media_file.VideosActivity;
import com.example.touch.GameActivity;
import com.example.utilTool.ReFlashExpandableListView;
import com.example.utilTool.ReFlashExpandableListView.IReflashListener;
	
public class Media_List_Fragment extends Fragment implements IReflashListener
{

	private View view;
	private ReFlashExpandableListView listView;
	private String[] armTypes = new String[]{"本地媒体库","远程媒体库"};
	private String[][] arms=new String[][]{} ;// 二维数组，表示ExpandableListview数据
	private MyAdapter adapter;
	private LinearLayout childView;
	private int[] images=new int[]
	{
		R.drawable.mediafile_moive,R.drawable.mediafile_music,
		R.drawable.mediafile_office,R.drawable.mediafile_image
	};
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.media_file_view,container,false);
		listView=(ReFlashExpandableListView) view.findViewById(R.id.mediafilelistview);
		listView.setInterface(this);
		initdata();
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
		
		listView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id)
			{
				//Toast.makeText(getActivity(), armTypes[groupPosition]+"的"+arms[groupPosition][childPosition], Toast.LENGTH_SHORT).show();
				startActivity(new Intent(getActivity(), VideosActivity.class));
				return true;
			}
		});
		
		
		return view;
	}
	private void initdata()
	{
		arms=new String[][]{new String[]{"视频","音乐","文档","图像"},new String[]{"视频","音乐","文档","图像"}};
	}
	class MyAdapter extends BaseExpandableListAdapter
	{

		@Override
		public int getGroupCount()
		{
			return armTypes.length;
		}

		@Override
		public int getChildrenCount(int groupPosition)
		{
			return arms[groupPosition].length;
		}

		@Override
		public Object getGroup(int groupPosition)
		{
			return armTypes[groupPosition];
		}

		@Override
		public Object getChild(int groupPosition, int childPosition)
		{
			return arms[groupPosition][childPosition];
		}

		@Override
		public long getGroupId(int groupPosition)
		{
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition)
		{
			return childPosition;
		}

		@Override
		public boolean hasStableIds()
		{
			return true;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent)
		{
			TextView textView = getTextView();// 调用定义的getTextView()方法
			textView.setText("     " + getGroup(groupPosition).toString());// 添加数据
			textView.setPadding(0, 20, 0, 20);
			textView.setBackgroundColor(android.graphics.Color.parseColor("#d9d9d9"));
			textView.setTextSize(25);
			textView.setWidth(parent.getWidth());
			return textView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent)
		{
			childView = new LinearLayout(getActivity());
			childView.setOrientation(LinearLayout.VERTICAL);
			ImageView logo = new ImageView(getActivity()); 
			logo.setImageResource(images[childPosition]);
			logo.setPadding(0, 30, 0, 20);
			logo.invalidate();
			childView.addView(logo);
			TextView textView = getTextView();// 调用定义的getTextView()方法
			textView.setText(getChild(groupPosition, childPosition).toString());// 添加数据
			childView.addView(textView);
			textView.setGravity(Gravity.CENTER_HORIZONTAL);
			childView.invalidate();
			return childView;
		}
		
		// 定义一个TextView
		private TextView getTextView()
		{
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
			TextView textView = new TextView(getActivity());
			textView.setLayoutParams(lp);
			textView.setPadding(20, 40, 0, 40);
			textView.setTextSize(20);
			textView.setSingleLine();
			textView.setEllipsize(TruncateAt.END);
			return textView;
		}
		
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition)
		{
			return true;
		}
		
	}
	
	@Override
	public void onReflash()
	{
		
		Toast.makeText(getActivity(), "刷新了一下", Toast.LENGTH_SHORT).show();
		listView.reflashComplete();
	}
}*/

/*package com.example.fragment;

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
import com.example.utilTool.SendMsgAppScreenFileSystem;
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
	private String selectedFileDir=null;
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
					            SendMsgAppScreenFileSystem sendMsgScreen=new SendMsgAppScreenFileSystem(handler, getActivity(),JsonParse.Json2String(OptionEnum.DISPLAY.ordinal(), null));
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
				 if(info!=null)
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
	@Override
	public boolean onContextItemSelected(MenuItem item) 
	{ 
		if( getUserVisibleHint() == false ) 
	    {
	        return false;
	    }
		if(item.getMenuInfo() instanceof AdapterContextMenuInfo)
		{
			info = (AdapterContextMenuInfo) item.getMenuInfo();
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
		else
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
}*/
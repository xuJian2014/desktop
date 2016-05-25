package com.example.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils.TruncateAt;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connection.DeviceConnection;
import com.example.desktop.R;
import com.example.util.jsonTransfer.JsonParse;
import com.example.util.jsonTransfer.OptionEnum;
import com.example.util.jsonTransfer.Parameter2Option;
import com.example.util.jsonTransfer.ResponseMessage;
import com.example.util.jsonTransfer.ResponseMessageEnum;
import com.example.utilTool.ReFlashExpandableListView;
import com.example.utilTool.ReFlashExpandableListView.IReflashListener;
import com.example.utilTool.ScreenResponseMsg;
import com.example.utilTool.SendMsgAppScreen;
import com.example.utilTool.Send_AppImage_Msg;
import com.example.utilTool.Send_AppImage_Msg.MyCallBack;
import com.example.utilTool.StringUtil;

public class Application_List_Fragment extends Fragment implements IReflashListener
{
	private String homeApplicationStr;// 家庭服务中心应用字符串
	private String vmApplicationStr;// 远端虚拟机应用字符串
	private String[] content_Application;// 家庭服务中心应用列表
	private String[] content_VmApp;// 远端虚拟机应用列表
	private String[] screenList = new String[]{};// 屏幕列表
	private String responseStr = null;;
	private String[] armTypes = new String[]{ "正在运行", "已安装" };
	private String[][] arms = new String[][]{};// 二维数组，表示ExpandableListview数据
	private ReFlashExpandableListView listView;
	private View view;
	private String screenStr;
	private ProgressDialog mDialog;
	private String screenIsSuccess;
	

	private ExpandableListContextMenuInfo info;
	
	private MyAdapter adapter;
	private LinearLayout childView;
	private SharedPreferences remoteIpInfo;
	private String remoteIp;;
	private SharedPreferences getFamilyPreferences;
	private String connectionIP;
	private String connectionPwd;
	private SharedPreferences getVNCPreferences;
	private SharedPreferences.Editor vncEditor;

	private int[] images = new int[]
	{ 		
			R.drawable.application_explorer, R.drawable.application_item,
			R.drawable.application_visualstudio, R.drawable.application_weixin,
			R.drawable.application_xunlei, R.drawable.application_baiduyun,
			R.drawable.application_kankan, R.drawable.application_youku,R.drawable.vmachine_48
	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		remoteIpInfo = getActivity().getSharedPreferences("Remote",Context.MODE_PRIVATE);
		getFamilyPreferences = getActivity().getSharedPreferences("configInfo", Context.MODE_PRIVATE);
		getVNCPreferences = getActivity().getSharedPreferences("VNCConnect", Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
		vncEditor = getVNCPreferences.edit();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.application_list, container, false);
		listView = (ReFlashExpandableListView) view.findViewById(R.id.listView1);
		listView.setInterface(this);
		remoteIp = getRemoteIp();
		if (StringUtil.isNullString(remoteIp))
		{
			Toast.makeText(getActivity(), "获取远程虚拟机应用失败，请登录代理服务器获取IP地址",
					Toast.LENGTH_SHORT).show();
		} 
		else
		{
			Send_AppImage_Msg sendMsgThread = new Send_AppImage_Msg(handler,getActivity(), 
					JsonParse.Json2String(OptionEnum.APP_LIST.ordinal(),new Parameter2Option("app", remoteIp)),
					new MyCallBack() 
				{
						@Override
						public void getResult(String application_arrs,Bitmap[] bitmapArrs)
						{
							Message message = handler.obtainMessage();
							if (null == application_arrs || null == bitmapArrs)
							{
								message.what = 6;
								handler.sendMessage(message);
							} 
							else
							{
								message.obj = bitmapArrs;// 图片
								Bundle bundle = new Bundle();
								bundle.putString("appListMsg", application_arrs);// 应用列表字符串
								message.setData(bundle);
								message.what = 2;
								handler.sendMessage(message);
							}
						}
					});
			Thread thread = new Thread(sendMsgThread);// 请求应用列表字符串
			thread.start();
		}
		return view;
	}

	private String getRemoteIp()
	{
		remoteIp = remoteIpInfo.getString("remoteIP", "");
		return remoteIp;
	}

	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg)
		{
			switch (msg.what)
			{
				case 0:
				case 1:
				case 5:
					if (mDialog != null)
						mDialog.cancel();
					Toast.makeText(getActivity(), "对不起，连接家庭服务终端失败，请稍候重试",Toast.LENGTH_LONG).show();
					break;
				case 2: // 获取应用字符串
					responseStr = msg.getData().getString("appListMsg");
					if (responseStr == null)
					{
						Toast.makeText(getActivity(), "对不起，未能获取应用列表字符串",Toast.LENGTH_LONG).show();
					} 
					else
					{
						if (!"error".equals(responseStr))
						{
							homeApplicationStr = responseStr.split(";;;")[0];
							vmApplicationStr = responseStr.split(";;;")[1];
							content_Application = homeApplicationStr
									.split("\\|\\*\\^");// 以"\*^"为分割符
							content_VmApp = vmApplicationStr.split("\\|\\*\\^");
							Bitmap[] bitmapArrs = (Bitmap[]) msg.obj;
							binderListData(content_Application, content_VmApp,bitmapArrs);
						} 
						else
						{
							Toast.makeText(getActivity(), "对不起，获取应用列表字符串发生错误",Toast.LENGTH_LONG).show();
						}
					}
					break;
				case 3: // 获取屏幕字符串
					if (mDialog != null)
						mDialog.cancel();
					screenStr = msg.getData().getString("msg");
					ResponseMessage responseMessage = JsonParse.Json2Object(screenStr);
					if (responseMessage == null)
					{
						Toast.makeText(getActivity(), "对不起,没有找到屏幕",Toast.LENGTH_LONG).show();
					} 
					else
					{
						if (responseMessage.getErrNum() == ResponseMessageEnum.ERROR.ordinal())
						{
							Toast.makeText(getActivity(), "对不起,没有找到屏幕",Toast.LENGTH_LONG).show();
						}
						else
						{
							screenList = responseMessage.getResponseMessage().split(",");
							SharedPreferences sharedPreferences = getActivity().getSharedPreferences("configInfo",Context.MODE_PRIVATE);
							for (int i = 0; i < screenList.length; i++)
							{
								if (sharedPreferences.contains(screenList[i]))
								{
									screenList[i] = sharedPreferences.getString(screenList[i], "...");
								}
							}
							showScreen(screenList);
						}
					}
					break;
				case 4:
					if (mDialog != null)
						mDialog.cancel();
					screenIsSuccess = msg.getData().getString("msg");
					ResponseMessage responseMessage2 = JsonParse.Json2Object(screenIsSuccess);
					if (responseMessage2 == null)
					{
						Toast.makeText(getActivity(), "对不起，投影失败，请重试...",Toast.LENGTH_LONG).show();
					} 
					else
					{
						int errNum = responseMessage2.getErrNum();
						if (errNum == ResponseMessageEnum.SUCCESS.ordinal())
						{
							Toast.makeText(getActivity(),"投影"+ adapter.getTextView().getText().toString() + "成功！",Toast.LENGTH_LONG).show();
						} else if (errNum == ResponseMessageEnum.WAIT.ordinal())
						{
							Toast.makeText(getActivity(), "正在连接中，稍后请重试...",Toast.LENGTH_LONG).show();
						} 
						else if (errNum == ResponseMessageEnum.UNCONNECTED.ordinal())
						{
							Toast.makeText(getActivity(), "对不起，未能成功连接，请重试...",Toast.LENGTH_LONG).show();
						}
						else
						// error
						{
							Toast.makeText(getActivity(),"投影"+ adapter.getTextView().getText().toString() + "失败！",Toast.LENGTH_LONG).show();
						}
					}
					break;
				default:
					Toast.makeText(getActivity(), "未获取应用列表字符串", Toast.LENGTH_LONG).show();
					break;
			}
		}
	};

	class MyAdapter extends BaseExpandableListAdapter
	{
		private Bitmap[] bitmapArrs;

		public MyAdapter(Bitmap[] bitmapArrs)
		{
			this.bitmapArrs = bitmapArrs;
		}

		// 获取组在给定的位置编号，即armTypes中元素的ID
		@Override
		public long getGroupId(int groupPosition)
		{
			return groupPosition;
		}

		// 获取在给定的组的儿童的ID，就是arms中元素的ID
		@Override
		public long getChildId(int groupPosition, int childPosition)
		{
			return childPosition;
		}

		// 获取的群体数量，得到armTypes里元素的个数
		@Override
		public int getGroupCount()
		{
			return armTypes.length;
		}

		// 取得指定组中的儿童人数，就是armTypes中每一个种族它军种的个数
		@Override
		public int getChildrenCount(int groupPosition)
		{
			return arms[groupPosition].length;
		}

		// 获取与给定的组相关的数据，得到数组armTypes中元素的数据
		@Override
		public Object getGroup(int groupPosition)
		{
			return armTypes[groupPosition];
		}

		// 获取一个视图显示给定组，存放armTypes
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent)
		{
			TextView textView = getTextView();// 调用定义的getTextView()方法
			textView.setText("     " + getGroup(groupPosition).toString());// 添加数据
			textView.setPadding(0, 20, 0, 20);
			textView.setBackgroundColor(android.graphics.Color
					.parseColor("#d9d9d9"));
			textView.setTextSize(20);

			textView.setWidth(parent.getWidth());

			return textView;
		}

		// 获取与孩子在给定的组相关的数据,得到数组arms中元素的数据
		@Override
		public Object getChild(int groupPosition, int childPosition)
		{
			return arms[groupPosition][childPosition];
		}

		// 获取一个视图显示在给定的组 的儿童的数据，就是存放arms
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent)
		{
			childView = new LinearLayout(getActivity());
			childView.setOrientation(0);// 定义为纵向排列
			ImageView logo = new ImageView(getActivity());
			if (groupPosition == 0)// 设置正运行程序的图片
			{
				// logo.setImageBitmap(bitmapArrs[childPosition]);
				Bitmap icon = getResizedBitmap(bitmapArrs[childPosition], 100,100);
				// icon.compress(Bitmap.CompressFormat.JPEG, 30);
				if (icon != null)
					logo.setImageBitmap(icon);

			} else
			// 已安装图片
			{
				logo.setImageResource(images[resourceImage(arms[groupPosition][childPosition])]);// 添加图片
			}
			logo.setPadding(20, 40, 20, 40);
			logo.invalidate();
			childView.addView(logo);
			TextView textView = getTextView();// 调用定义的getTextView()方法
			textView.setText(getChild(groupPosition, childPosition).toString());// 添加数据
			childView.addView(textView);
			textView.setGravity(Gravity.CENTER_VERTICAL);
			childView.invalidate();
			return childView;
		}

		public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight)
		{
			Bitmap resizedBitmap = bm;
			try
			{
				int width = bm.getWidth();
				int height = bm.getHeight();
				float scaleWidth = ((float) newWidth) / width;
				float scaleHeight = ((float) newHeight) / height;
				Matrix matrix = new Matrix();
				matrix.postScale(scaleWidth, scaleHeight);
				resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
						matrix, false);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			return resizedBitmap;
		}

		private int resourceImage(String str)
		{
			if (str.contains("浏览器") || str.contains("Microsoft Edge"))
			{
				return 0;
			}
			else if (str.contains("Microsoft Visual Studio"))
			{
				return 2;
			}
			else if (str.contains("微信"))
			{
				return 3;
			}
			else if (str.contains("迅雷"))
			{
				return 4;
			}
			else if (str.contains("百度云"))
			{
				return 5;
			} 
			else if (str.contains("看看影音"))
			{
				return 6;
			}
			else if (str.contains("优酷"))
			{
				return 7;
			} 
			else if(str.equals("远程桌面"))
			{
				return 8;
			}
			else
			{
				return 1;
			}
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

		// 孩子在指定的位置是可选的，即：arms中的元素是可点击的
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition)
		{
			return true;
		}

		// 表示孩子是否和组ID是跨基础数据的更改稳定
		public boolean hasStableIds()
		{
			return true;
		}
	}

	private void binderListData(String[] homeAppList, String[] vmAppList,Bitmap[] bitmapArrs)
	{
		if (("").equals(homeAppList) || homeAppList == null)
			homeAppList = new String[]{ "没有数据" };
		if (("").equals(vmAppList) || vmAppList == null)
			vmAppList = new String[]{ "没有数据" };
		
		
		String[] vmApp=new String[vmAppList.length+1];
		vmApp[0]="远程桌面";
		
		for (int i = 0; i < vmAppList.length; i++)
		{
			vmApp[i+1]=vmAppList[i];
		}
		arms = new String[][]{ homeAppList, vmApp };
		adapter = new MyAdapter(bitmapArrs);
		listView.setAdapter(adapter);
		listView.expandGroup(0);
		
		/*listView.setOnChildClickListener(new OnChildClickListener()
		{
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id)
			{
				if(groupPosition==0)
				{
					Toast.makeText(getActivity(), "第一组"+childPosition, Toast.LENGTH_SHORT).show();
				}
				if(groupPosition==1)
				{
					Toast.makeText(getActivity(), "第二组"+childPosition, Toast.LENGTH_SHORT).show();
				}
				return true;
			}
		});
		
		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo)
			{
				MenuInflater inflater = getActivity().getMenuInflater();
				inflater.inflate(R.menu.application_menu, menu);
				info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
				
			}
		});*/
		registerForContextMenu(listView);
		listView.invalidate();
	}

	private void showScreen(String[] str)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("屏幕列表");
		builder.setItems(screenList, new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				mDialog = new ProgressDialog(getActivity());
				mDialog.setTitle("投影");
				mDialog.setMessage("正在进行投影，请稍等...");
				mDialog.show();
				int optionId;
				if (ExpandableListView.getPackedPositionGroup(info.packedPosition) == 0)
				{
					optionId = OptionEnum.PROJECTIONFILE_LOCAL.ordinal();
					connectionIP = getFamilyPreferences.getString("homeServiceIp", "");
					connectionPwd = getFamilyPreferences.getString("homeServicePwd", "");
					//DeviceConnection.getInstance().setLocalOrVm(0);
					if(("").equals(connectionPwd))
					{
						Toast.makeText(getActivity(), "请到个人设置页面设置家庭服务终端信息",Toast.LENGTH_LONG).show();
					}
					else
					{
						DeviceConnection.getInstance().init(connectionIP, 64788,connectionPwd);
					}
					
				} 
				else
				{
					optionId = OptionEnum.PROJECTION_VM.ordinal();
					connectionIP = remoteIpInfo.getString("remoteIP","0000");
					connectionPwd = remoteIpInfo.getString("remotePassword", "0000");
					//DeviceConnection.getInstance().setLocalOrVm(1);
					DeviceConnection.getInstance().init(connectionIP, 64788,connectionPwd);	
				}
				vncEditor.putString("IP", connectionIP);
				vncEditor.putString("password", connectionPwd);
				vncEditor.commit();
				ScreenResponseMsg msgAppScreen = new ScreenResponseMsg(handler,getActivity(),JsonParse.Json2String(optionId,new Parameter2Option(String.valueOf(ExpandableListView
												.getPackedPositionChild(info.packedPosition)),String.valueOf(which))));
				Thread thread = new Thread(msgAppScreen);
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
		//info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
		
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		if( getUserVisibleHint() == false ) 
	    {
	        return false;
	    }
		
		 //info=(ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();
		 ContextMenuInfo minfo = item.getMenuInfo();
		 if(minfo instanceof ExpandableListView.ExpandableListContextMenuInfo){
			 info = (ExpandableListContextMenuInfo) minfo;
		 }else{
			 return false;
		 }
				switch (item.getItemId())
				{
					case R.id.screen: // 投影屏幕
						mDialog = new ProgressDialog(getActivity());
						mDialog.setTitle("屏幕");
						mDialog.setMessage("正在获取屏幕，请稍等...");
						mDialog.show();
						SendMsgAppScreen sendMsgScreen = new SendMsgAppScreen(handler,getActivity(), JsonParse.Json2String(OptionEnum.DISPLAY.ordinal(), null));
						Thread threadScreen = new Thread(sendMsgScreen);
						threadScreen.start();
						break;
				} 
		 return true;
	}

	@Override
	public void onDestroy()
	{
		handler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}

	private void setReflashData()
	{
		childView.removeAllViews();

		String remoteIp = null;
		remoteIp = getRemoteIp();
		if (StringUtil.isNullString(remoteIp))
		{
			Toast.makeText(getActivity(), "获取远程虚拟机应用失败，请登录代理服务器获取IP地址",Toast.LENGTH_LONG).show();
		} 
		else
		{
			Send_AppImage_Msg sendMsgThread = new Send_AppImage_Msg(handler,getActivity(), JsonParse.Json2String(OptionEnum.APP_LIST.ordinal(),
							new Parameter2Option("app", remoteIp)),
					new MyCallBack() 
			{
						@Override
						public void getResult(String application_arrs,Bitmap[] bitmapArrs)
						{
							System.out.println("执行回调函数:" + application_arrs);
							Message message = handler.obtainMessage();
							if (null == application_arrs || null == bitmapArrs)
							{
								message.what = 6;
								handler.sendMessage(message);
							} 
							else
							{
								message.obj = bitmapArrs;
								Bundle bundle = new Bundle();
								bundle.putString("appListMsg", application_arrs);
								message.setData(bundle);
								message.what = 2;
								handler.sendMessage(message);
							}
						}
					});
			Thread thread = new Thread(sendMsgThread);
			thread.start();
		}
	}

	@Override
	public void onReflash()
	{
		setReflashData();
		listView.reflashComplete();
	}
}

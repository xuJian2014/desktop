package com.example.fragment;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.desktop.R;
import com.example.utilTool.ReFlashExpandableListView;
import com.example.utilTool.ReFlashExpandableListView.IReflashListener;
import com.example.utilTool.ScreenResponseMsg;
import com.example.utilTool.SendMsgAppScreen;
import com.example.utilTool.Send_AppImage_Msg;
import com.example.utilTool.Send_AppImage_Msg.MyCallBack;

public class Application_List_Fragment extends Fragment implements IReflashListener
{
	private  String homeApplicationStr;//��ͥ��������Ӧ���ַ���
	private  String vmApplicationStr;//Զ�������Ӧ���ַ���
	private   String[] content_Application;//��ͥ��������Ӧ���б�
	private  String[] content_VmApp;//Զ�������Ӧ���б�
	private  String[] screenList=new String[]{};//��Ļ�б�
	private String resultStr=null;;
	private  String[] armTypes= new String[]{"��������","�Ѱ�װ"};
	private  String[][] arms = new String[][]{};//��ά���飬��ʾExpandableListview����
	private int[] images = new int[]
		     	{
		     		R.drawable.application_explorer,
		     		R.drawable.application_item,
		     		R.drawable.application_visualstudio,
		     		R.drawable.application_weixin,
		     		R.drawable.application_xunlei,
		     		R.drawable.application_baiduyun,
		     		R.drawable.application_kankan,
		     		R.drawable.application_youku
		 		};
	ReFlashExpandableListView listView;
	GridView gridView;
	View view;
	String screenStr;
	private ProgressDialog mDialog; 
	private String screenIsSuccess;
	private ExpandableListContextMenuInfo info;
	MyAdapter adapter;
	
	private LinearLayout childView;
	
	SharedPreferences remoteIpInfo;
	String remoteIp;;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		remoteIpInfo=getActivity().getSharedPreferences("Remote",Context.MODE_PRIVATE);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		view=inflater.inflate(R.layout.application_list, container, false);
		listView=(ReFlashExpandableListView) view.findViewById(R.id.listView1);
		gridView=(GridView) view.findViewById(R.id.gridView1);
		listView.setInterface(this);
		
		remoteIp=getRemoteIp();
		if(remoteIp==null||"".equals(remoteIp))
		{
			Toast.makeText(getActivity(), "��ȡԶ�������Ӧ��ʧ�ܣ����¼�����������ȡIP��ַ", Toast.LENGTH_SHORT).show();
		}
		else
		{
			
			Send_AppImage_Msg sendMsgThread = new Send_AppImage_Msg(handler,getActivity(), "app," + remoteIp, new MyCallBack() 
			{
				@Override
				public void getResult(String application_arrs,Bitmap[] bitmapArrs)
				{
					Message message=handler.obtainMessage();
					if(null==application_arrs||null==bitmapArrs)
					{
						message.what=6;
						handler.sendMessage(message);
					}
					else
					{
						System.out.println("ִ�лص�����");
						message.obj=bitmapArrs;
						Bundle bundle=new Bundle();
						bundle.putString("appListMsg", application_arrs);
						message.setData(bundle);
						message.what=2;
						handler.sendMessage(message);
					}
				}
			});
			Thread thread = new Thread(sendMsgThread);
			thread.start();
		}
		return view;
	}
	
	private String getRemoteIp()
	{
		String remoteIp=null;
		
		if(!remoteIpInfo.contains("remoteIP")) 
		{
			SharedPreferences.Editor editor=remoteIpInfo.edit();
			editor.putString("remoteIP", "");
			editor.commit();
		}
		remoteIp=remoteIpInfo.getString("remoteIP", "");
		return remoteIp;
	}
	public Handler handler=new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			switch(msg.what)
			{
				case 0:
					if(mDialog!=null)
						mDialog.cancel();
					Toast.makeText(getActivity(), "��ͥ�����IP��ַ����ȷ", Toast.LENGTH_LONG).show();
					break;
				case 1:
					if(mDialog!=null)
						mDialog.cancel();
					Toast.makeText(getActivity(), "��ͥ���������ѹر�", Toast.LENGTH_LONG).show();
					break;
				case 2: //��ȡӦ���ַ���
					resultStr=msg.getData().getString("appListMsg");
					System.out.println(resultStr);
					if(null!=resultStr && !("error".equals(resultStr)))
					{
						homeApplicationStr=resultStr.split(";;;")[0];
						vmApplicationStr=resultStr.split(";;;")[1];
						content_Application=homeApplicationStr.split("\\|\\*\\^");//��"\*^"Ϊ�ָ��
						content_VmApp=vmApplicationStr.split("\\|\\*\\^");
						Bitmap[] bitmapArrs=(Bitmap[]) msg.obj;
						//binderGridata(content_Application, bitmapArrs);
						binderListData(content_Application,content_VmApp,bitmapArrs);
						
					}
					else
					{
						Toast.makeText(getActivity(), "δ��ȡӦ���б��ַ���", Toast.LENGTH_LONG).show();
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
						SharedPreferences sharedPreferences=getActivity().getSharedPreferences("configInfo",Context.MODE_PRIVATE);
						for (int i = 0; i <screenList.length; i++)
						{
							if(sharedPreferences.contains(screenList[i]))
							{
								screenList[i]=sharedPreferences.getString(screenList[i], "...");
							}
						}
						showScreen(screenList);
					}
					break;
				case 4:
					if(mDialog!=null)
						mDialog.cancel();
					screenIsSuccess=msg.getData().getString("msg");
					if("success".equals(screenIsSuccess))
					{
						Toast.makeText(getActivity(), adapter.getTextView().getText().toString()+"�ɹ���", Toast.LENGTH_LONG).show();
					}
					else if("wait".equals(screenIsSuccess))
					{
						Toast.makeText(getActivity(), "���������У��Ժ�������...", Toast.LENGTH_LONG).show();
					}
					else if("ignore".equals(screenIsSuccess))
					{
						Toast.makeText(getActivity(), screenIsSuccess, Toast.LENGTH_LONG).show();
					}
					else if("unconnected".equals(screenIsSuccess))
					{
						Toast.makeText(getActivity(), "�Բ���δ�ܳɹ����ӣ�������...", Toast.LENGTH_LONG).show();
					}
					else
					{
						Toast.makeText(getActivity(), adapter.getTextView().getText().toString()+"ʧ�ܣ�", Toast.LENGTH_LONG).show();
					}
					
					break;
				case 5:
					if(mDialog!=null)
						mDialog.cancel();
					Toast.makeText(getActivity(), "���Ӽ�ͥ�������ķ�������", Toast.LENGTH_LONG).show();
					break;
				default:
					Toast.makeText(getActivity(), "δ��ȡӦ���б��ַ���", Toast.LENGTH_LONG).show();
					break;
			}
		}
	};

	class MyAdapter extends BaseExpandableListAdapter
    {
		   private Bitmap[] bitmapArrs;
		   public MyAdapter(Bitmap[] bitmapArrs)
		   {
			   this.bitmapArrs=bitmapArrs;
		   }
           //��ȡ���ڸ�����λ�ñ�ţ���armTypes��Ԫ�ص�ID
           @Override
           public long getGroupId(int groupPosition) 
           {
               return groupPosition;
           }
       
           //��ȡ�ڸ�������Ķ�ͯ��ID������arms��Ԫ�ص�ID
           @Override
           public long getChildId(int groupPosition, int childPosition)
           {
               return childPosition;
           }
           
           //��ȡ��Ⱥ���������õ�armTypes��Ԫ�صĸ���
           @Override
           public int getGroupCount()
           {
               return armTypes.length;
           }
           
           //ȡ��ָ�����еĶ�ͯ����������armTypes��ÿһ�����������ֵĸ���
           @Override
           public int getChildrenCount(int groupPosition)
           {
               return arms[groupPosition].length;
           }
           
           //��ȡ�����������ص����ݣ��õ�����armTypes��Ԫ�ص�����
           @Override
           public Object getGroup(int groupPosition) 
           {
               return armTypes[groupPosition];
           }

           //��ȡһ����ͼ��ʾ�����飬���armTypes
           @Override
           public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent)
           {
               TextView textView = getTextView();//���ö����getTextView()����
               textView.setText("     "+getGroup(groupPosition).toString());//�������
               textView.setPadding(0, 20, 0, 20);
               textView.setBackgroundColor(android.graphics.Color.parseColor("#d9d9d9"));
               textView.setTextSize(20);
               return textView;
           }
           
           //��ȡ�뺢���ڸ���������ص�����,�õ�����arms��Ԫ�ص�����
           @Override
           public Object getChild(int groupPosition, int childPosition)
           {
               return arms[groupPosition][childPosition];
           }
           
           //��ȡһ����ͼ��ʾ�ڸ������� �Ķ�ͯ�����ݣ����Ǵ��arms
           @Override
           public View getChildView(int groupPosition, int childPosition, boolean isLastChild,View convertView, ViewGroup parent)
           {
               childView = new LinearLayout(getActivity());
               childView.setOrientation(0);//����Ϊ��������
               ImageView logo = new ImageView(getActivity());              
               if(groupPosition==0)//���������г����ͼƬ
               {
            	   //logo.setImageBitmap(bitmapArrs[childPosition]);
            	   Bitmap icon=getResizedBitmap(bitmapArrs[childPosition],100,100);
            	  // icon.compress(Bitmap.CompressFormat.JPEG, 30);
            	   if(icon!=null)
            	   logo.setImageBitmap(icon);            	   

               }
               else //�Ѱ�װͼƬ
               {
            	   logo.setImageResource(images[resourceImage(arms[groupPosition][childPosition])]);//���ͼƬ
               }
               logo.setPadding(20, 40, 20, 40);
               logo.invalidate();
               childView.addView(logo);
               TextView textView = getTextView();//���ö����getTextView()����
               textView.setText(getChild(groupPosition,childPosition).toString());//�������
               childView.addView(textView);
               textView.setGravity(Gravity.CENTER_VERTICAL);
               childView.invalidate();  
               return childView;
           }
           public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight)
           {
        	   Bitmap resizedBitmap=bm;
        	   try
        	   {
				int width = bm.getWidth();
				int height = bm.getHeight();
				float scaleWidth = ((float) newWidth) / width;
				float scaleHeight = ((float) newHeight) / height;
				Matrix matrix = new Matrix();
				matrix.postScale(scaleWidth, scaleHeight);
				resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,matrix, false);
        	    }
        	   catch(Exception e )
        	   {
        		  e.printStackTrace();
        	   }        	   
        	    return resizedBitmap;
        	}
           
           private int resourceImage(String str)
           {
           	if(str.contains("�����")||str.contains("Microsoft Edge"))
           	{
           		return 0;
           	}
           	else if(str.contains("Microsoft Visual Studio"))
           	{
           		return 2;
           	}
           	else if(str.contains("΢��"))
           	{
           		return 3;
           	}
           	else if(str.contains("Ѹ��"))
           	{
           		return 4;
           	}
           	else if(str.contains("�ٶ���"))
           	{
           		return 5;
           	}
           	else if(str.contains("����Ӱ��"))
           	{
           		return 6;
           	}
           	else if(str.contains("�ſ�"))
           	{
           		return 7;
           	}
           	else 
           	{
           		return 1;
           	}
           }
           //����һ��TextView
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
           
           //������ָ����λ���ǿ�ѡ�ģ�����arms�е�Ԫ���ǿɵ����
           @Override
           public boolean isChildSelectable(int groupPosition,int childPosition) 
           {
               return true;
           }
           //��ʾ�����Ƿ����ID�ǿ�������ݵĸ����ȶ�
           public boolean hasStableIds()
           {
               return true;
           }
       }

	private void binderListData(String[] homeAppList,String[] vmAppList,Bitmap[] bitmapArrs)
	{
		if(("").equals(homeAppList)||homeAppList==null)
			homeAppList=new String[]{"û������"};
		if(("").equals(vmAppList)||vmAppList==null)
			vmAppList=new String[]{"û������"};
		
		arms = new String[][]{homeAppList,vmAppList};
		adapter=new MyAdapter(bitmapArrs);
		listView.setAdapter(adapter);
		listView.expandGroup(0);
		registerForContextMenu(listView);
		listView.invalidate();
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
		         ScreenResponseMsg msgAppScreen=new ScreenResponseMsg(handler, getActivity(), 
		        		 ExpandableListView.getPackedPositionGroup(info.packedPosition)+","+
		        				 ExpandableListView.getPackedPositionChild(info.packedPosition)+",screen,"+String.valueOf(which));
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
		 info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
		 MenuInflater inflater = getActivity().getMenuInflater();
		 inflater.inflate(R.menu.application_menu, menu);
		
	}
	public boolean onContextItemSelected(MenuItem item) 
	{ 
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
		childView.removeAllViews();
		
		String remoteIp=null;
		remoteIp=getRemoteIp();
		if(remoteIp==null||"".equals(remoteIp))
		{
			Toast.makeText(getActivity(), "��ȡԶ�������Ӧ��ʧ�ܣ����¼�����������ȡIP��ַ", Toast.LENGTH_LONG).show();
		}
		else
		{
			Send_AppImage_Msg sendMsgThread = new Send_AppImage_Msg(handler,getActivity(), "app," +remoteIp, new MyCallBack() 
			{
				@Override
				public void getResult(String application_arrs,Bitmap[] bitmapArrs)
				{
					System.out.println("ִ�лص�����:"+application_arrs);
					Message message=handler.obtainMessage();
					if(null==application_arrs||null==bitmapArrs)
					{
						message.what=6;
						handler.sendMessage(message);
					}
					else
					{
						message.obj=bitmapArrs;
						Bundle bundle=new Bundle();
						bundle.putString("appListMsg", application_arrs);
						message.setData(bundle);
						message.what=2;
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

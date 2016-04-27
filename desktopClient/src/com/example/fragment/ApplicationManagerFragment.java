package com.example.fragment;

import com.example.desktop.R;
import com.example.utilTool.GifView;
import com.example.utilTool.SendMsgAppMagThread;
import com.example.utilTool.SendMsgThread;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ApplicationManagerFragment extends Fragment
{

	private Button check = null;
	private Button repair = null;
	private ListView lv = null;
	//private GifView gifImage = null;
	private String resultStr = "";
	String[] str = { "媒体控制中心", "多屏投影", "文件系统权限" };

	private AnimationDrawable anim = null;
	
	private TextView tv_info=null;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_appmanager, container,
				false);
		lv = (ListView) view.findViewById(R.id.listView1);
		lv.setAdapter(new Myadapter());

		check = (Button) view.findViewById(R.id.btn_check);
		repair = (Button) view.findViewById(R.id.btn_repair);
		//gifImage = (GifView) view.findViewById(R.id.gif);
		repair.setEnabled(false);// 开始时禁用修改功能

		check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0)
			{
				SendMsgAppMagThread sendMsgThread = new SendMsgAppMagThread(handler, getActivity(), "check");
				Thread thread = new Thread(sendMsgThread);
				thread.start();
				/*if (gifImage == null)
				{
					gifImage = (GifView) lv.getChildAt(0).findViewById(R.id.gif);
				}
				gifImage.setPaused(false);
*/
				if(anim==null){
					anim = (AnimationDrawable) lv.getChildAt(0).findViewById(R.id.myImageView).getBackground();//(AnimationDrawable) hold1.image_gif.getBackground();
				}
				anim.start();
			}
		});

		repair.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0)
			{
				SendMsgAppMagThread sendMsgThread = new SendMsgAppMagThread(handler,
						getActivity(), "repair");
				Thread thread = new Thread(sendMsgThread);
				thread.start();

				/*if (gifImage == null)
				{
					gifImage = (GifView) lv.getChildAt(0)
							.findViewById(R.id.gif);
				}
				gifImage.setPaused(false);*/
				if(anim==null){
					anim = (AnimationDrawable) lv.getChildAt(0).findViewById(R.id.myImageView).getBackground();//(AnimationDrawable) hold1.image_gif.getBackground();
				}
				anim.start();
			}
		});
		return view;
	}

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg)
		{
			for(int i=1;i<=3;i++){
				tv_info = (TextView) lv.getChildAt(i).findViewById(R.id.tv_info);
				tv_info.setText("正常");
				tv_info.setTextColor(Color.GREEN);
			}
			switch (msg.what)
			{
			case 2:
				resultStr = msg.getData().getString("msg");
				if ("normal".equals(resultStr))
				{// 检测到系统应用正常运行
					Toast.makeText(getActivity(), "应用正常运行中", Toast.LENGTH_LONG)
							.show();
					
				} else if ("restored".equals(resultStr))
				{// 系统异常应用被修复
					Toast.makeText(getActivity(), "应用已修复，请继续使用",
							Toast.LENGTH_LONG).show();
					repair.setEnabled(false);
					/*if (gifImage != null)
					{
						gifImage.setPaused(true);
					}*/
				} else if ("error".equals(resultStr))
				{// 出现异常错误
					Toast.makeText(getActivity(), "出错了，请继续...",
							Toast.LENGTH_LONG).show();
				} else if (resultStr.charAt(0) >= '0'
						&& resultStr.charAt(0) <= '9')
				{//
					repair.setEnabled(true);
					String[] arrStr = resultStr.split(",");
					int[] arrNumber = new int[arrStr.length];

					for (int i = 0; i < arrNumber.length; i++)
					{
						arrNumber[i] = Integer.parseInt(arrStr[i]);
						switch (arrNumber[i])
						{
						// 调整异常应用textview背景色
						case 0:
							tv_info = (TextView) lv.getChildAt(1).findViewById(R.id.tv_info);
							tv_info.setText("异常");
							tv_info.setTextColor(Color.RED);
							break;
						case 1:
							tv_info = (TextView) lv.getChildAt(2).findViewById(R.id.tv_info);
							tv_info.setText("异常");
							tv_info.setTextColor(Color.RED);
							break;
						case 2:
							tv_info = (TextView) lv.getChildAt(3).findViewById(R.id.tv_info);
							tv_info.setText("异常");
							tv_info.setTextColor(Color.RED);
						default:
							break;
						}
					}
					Toast.makeText(getActivity(), "应用出现异常，请修复...",
							Toast.LENGTH_LONG).show();// 存在异常（未运行）应用，通知用户进行修复

				}
				break;
			default:// 出现其他错误
				Toast.makeText(getActivity(), "出错了，请重新操作", Toast.LENGTH_LONG)
						.show();
				for(int i=1;i<=3;i++){
					tv_info = (TextView) lv.getChildAt(i).findViewById(R.id.tv_info);
					tv_info.setText("未检测");
					tv_info.setTextColor(Color.GRAY);
				}
				break;
			}

		}
	};

	private class Myadapter extends BaseAdapter
	{

		private final int TYPE_1 = 0;
		private final int TYPE_2 = 1;

		@Override
		public int getItemViewType(int position)
		{
			if (position == 0)
				return TYPE_1;
			else
				return TYPE_2;
		}

		@Override
		public int getViewTypeCount()
		{
			return 2;// super.getViewTypeCount();
		}

		@Override
		public int getCount()
		{
			return 4;
		}

		@Override
		public Object getItem(int arg0)
		{
			return null;
		}

		@Override
		public long getItemId(int arg0)
		{
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ViewHold1 hold1 = null;
			ViewHold2 hold2 = null;

			int type = getItemViewType(position);
			if (convertView == null)
			{
				switch (type)
				{
				case TYPE_1:
					convertView = View
							.inflate(getActivity().getApplicationContext(),
									R.layout.item_image, null);
					hold1 = new ViewHold1();
					/*hold1.image_gif = (GifView) convertView
							.findViewById(R.id.gif);

					convertView.setTag(hold1);
					hold1.image_gif.setMovieResource(R.drawable.scan);
					hold1.image_gif.setPaused(true);*/
					hold1.image_gif = (ImageView) convertView.findViewById(R.id.myImageView);
					
					convertView.setTag(hold1);
					hold1.image_gif.setBackgroundResource(R.drawable.frame);
					break;
				case TYPE_2:
					convertView = View.inflate(getActivity()
							.getApplicationContext(), R.layout.item_text, null);
					hold2 = new ViewHold2();
					hold2.tv = (TextView) convertView
							.findViewById(R.id.tv_application);
					convertView.setTag(hold2);
					hold2.tv.setText(str[position - 1]);
					break;
				default:
					break;

				}

			} else
			{
				switch (type)
				{
				case TYPE_1:
					hold1 = (ViewHold1) convertView.getTag();
					break;
				case TYPE_2:
					hold2 = (ViewHold2) convertView.getTag();
					hold2.tv.setText(str[position - 1]);
					break;
				default:
					break;
				}
			}
			return convertView;
		}

		class ViewHold1
		{
			ImageView image_gif;
		}

		class ViewHold2
		{
			TextView tv;
		}
	}

}

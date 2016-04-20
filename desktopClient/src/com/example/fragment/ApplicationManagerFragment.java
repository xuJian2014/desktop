package com.example.fragment;

import com.example.desktop.R;
import com.example.utilTool.GifView;
import com.example.utilTool.SendMsgAppMagThread;
import com.example.utilTool.SendMsgThread;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ApplicationManagerFragment extends Fragment
{

	private Button check = null;
	private Button repair = null;
	private ListView lv = null;
	private GifView gifImage = null;
	private String resultStr = "";
	String[] str = { "ý���������", "����ͶӰ", "�ļ�ϵͳȨ��" };

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
		gifImage = (GifView) view.findViewById(R.id.gif);
		repair.setEnabled(false);// ��ʼʱ�����޸Ĺ���

		check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0)
			{
				SendMsgAppMagThread sendMsgThread = new SendMsgAppMagThread(handler, getActivity(), "check");
				Thread thread = new Thread(sendMsgThread);
				thread.start();
				if (gifImage == null)
				{
					gifImage = (GifView) lv.getChildAt(0).findViewById(R.id.gif);
				}
				gifImage.setPaused(false);

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

				if (gifImage == null)
				{
					gifImage = (GifView) lv.getChildAt(0)
							.findViewById(R.id.gif);
				}
				gifImage.setPaused(false);
			}
		});
		return view;
	}

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg)
		{
			switch (msg.what)
			{
			case 2:
				resultStr = msg.getData().getString("msg");
				if ("normal".equals(resultStr))
				{// ��⵽ϵͳӦ����������
					Toast.makeText(getActivity(), "Ӧ������������", Toast.LENGTH_LONG)
							.show();
					if (gifImage != null)
					{
						gifImage.setPaused(true);
					}
				} else if ("restored".equals(resultStr))
				{// ϵͳ�쳣Ӧ�ñ��޸�
					Toast.makeText(getActivity(), "Ӧ�����޸��������ʹ��",
							Toast.LENGTH_LONG).show();
					repair.setEnabled(false);
					if (gifImage != null)
					{
						gifImage.setPaused(true);
					}
				} else if ("error".equals(resultStr))
				{// �����쳣����
					Toast.makeText(getActivity(), "�����ˣ������...",
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
						// �����쳣Ӧ��textview����ɫ
						case 0:
							// imageView_Media.setImageResource(R.drawable.red);
							break;
						case 1:
							// imageView_projection.setImageResource(R.drawable.red);
							break;
						case 2:
							// imageView_file.setImageResource(R.drawable.red);
						default:
							break;
						}
					}
					Toast.makeText(getActivity(), "Ӧ�ó����쳣�����޸�...",
							Toast.LENGTH_LONG).show();// �����쳣��δ���У�Ӧ�ã�֪ͨ�û������޸�

				}
				break;
			default:// ������������
				Toast.makeText(getActivity(), "�����ˣ������²���", Toast.LENGTH_LONG)
						.show();
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
					hold1.image_gif = (GifView) convertView
							.findViewById(R.id.gif);

					convertView.setTag(hold1);
					hold1.image_gif.setMovieResource(R.drawable.scan);
					hold1.image_gif.setPaused(true);
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
			GifView image_gif;
		}

		class ViewHold2
		{
			TextView tv;
		}
	}

}

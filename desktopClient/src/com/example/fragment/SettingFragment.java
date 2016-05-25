package com.example.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Entity.HardDiskItem;
import com.example.connection.DeviceConnection;
import com.example.desktop.R;
import com.example.util.jsonTransfer.JsonParse;
import com.example.util.jsonTransfer.OptionEnum;
import com.example.util.jsonTransfer.Parameter2Option;
import com.example.util.jsonTransfer.Parameter3Option;
import com.example.util.jsonTransfer.ResponseMessage;
import com.example.util.jsonTransfer.ResponseMessageEnum;
import com.example.utilTool.AuthorityListAdapter;
import com.example.utilTool.SendMsgAuthority;
import com.example.utilTool.SendMsgThread;
import com.example.utilTool.Send_homeService_info;
import com.example.utilTool.StringUtil;

public class SettingFragment extends Fragment
{
	private TextView myInfo;
	private TextView proxyServer;
	private TextView homeService;
	private TextView virtualMachine;
	private TextView defaultConnectSetting;
	List<HardDiskItem> hardDiskList = new ArrayList<HardDiskItem>();
	LayoutInflater layoutInflater;
	SharedPreferences sharedPreferences;
	private String hardDiskStr;
	private String[] content_hardDisk = new String[]
	{};
	AuthorityListAdapter adapter;
	ListView listView;
	private SharedPreferences getRemotePreferences;
	private String connectionIP;
	private String connectionPwd;

	private SharedPreferences getVNCPreferences;
	private SharedPreferences.Editor vncEditor;

	private RadioButton radioButtonRemote;
	private RadioButton radioButtonFamily;

	private SharedPreferences.Editor checkIDEditor;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		layoutInflater = LayoutInflater.from(getActivity());
		sharedPreferences = getActivity().getSharedPreferences("configInfo",
				Context.MODE_PRIVATE);

		getRemotePreferences = getActivity().getSharedPreferences("Remote",
				Context.MODE_PRIVATE);
		getVNCPreferences = getActivity().getSharedPreferences("VNCConnect",
				Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
		vncEditor = getVNCPreferences.edit();

		checkIDEditor = sharedPreferences.edit();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View settingView = inflater.inflate(R.layout.fragment_setting, null);
		myInfo = (TextView) settingView.findViewById(R.id.personInfo);
		proxyServer = (TextView) settingView.findViewById(R.id.proxyServer);
		homeService = (TextView) settingView.findViewById(R.id.homeService);
		virtualMachine = (TextView) settingView
				.findViewById(R.id.virtualMachine);
		defaultConnectSetting = (TextView) settingView
				.findViewById(R.id.defaultConnectSetting);
		return settingView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		// 我的资料
		myInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0)
			{
				Toast.makeText(getActivity(), "你被点击 了个人资料设置...",
						Toast.LENGTH_SHORT).show();

			}
		});

		// 代理服务器设置
		proxyServer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				showProxyServerSetting();
			}
		});

		// 家庭服务终端设置
		homeService.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				showHomeServiceSetting();
			}
		});

		virtualMachine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				showVirtualMachineSetting();

			}
		});

		defaultConnectSetting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle("设置默认连接：");
				final View view = layoutInflater.inflate(
						R.layout.defaultconnectsetting, null);
				builder.setView(view);

				RadioGroup conGroup = (RadioGroup) view
						.findViewById(R.id.defaultSetting);
				radioButtonRemote = (RadioButton) view
						.findViewById(R.id.remoteCon);
				radioButtonFamily = (RadioButton) view
						.findViewById(R.id.familyCon);

				// radioButtonFamily.setChecked(true);
				long checkID;
				checkID = sharedPreferences.getLong("checkID", 0000);

				if (checkID == radioButtonFamily.getId())
				{
					radioButtonFamily.setChecked(true);
				} else
				{
					radioButtonRemote.setChecked(true);
				}

				conGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId)
					{
						// TODO Auto-generated method stub
						if (checkedId == radioButtonRemote.getId())
						{
							connectionIP = getRemotePreferences.getString(
									"remoteIP", "0000");
							connectionPwd = getRemotePreferences.getString(
									"remotePassword", "0000");

							DeviceConnection.getInstance().init(connectionIP,
									64788, connectionPwd);

							vncEditor.putString("IP", connectionIP);
							vncEditor.putString("password", connectionPwd);
							vncEditor.commit();

							checkIDEditor.putLong("checkID", checkedId);
							checkIDEditor.commit();
						}
						if (checkedId == radioButtonFamily.getId())
						{
							connectionIP = sharedPreferences.getString(
									"homeServiceIp", "0000");
							connectionPwd = sharedPreferences.getString(
									"homeServicePwd", "0000");
							Toast.makeText(getActivity(),
									"对不起，连接家庭服务终端发生错误++" + connectionPwd,
									Toast.LENGTH_LONG).show();

							DeviceConnection.getInstance().init(connectionIP,
									64788, connectionPwd);

							vncEditor.putString("IP", connectionIP);
							vncEditor.putString("password", connectionPwd);
							vncEditor.commit();

							checkIDEditor.putLong("checkID", checkedId);
							checkIDEditor.commit();
						}
					}

				});
				builder.setPositiveButton("保存",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int i)
							{
								// System.out.println("connectionIP:"+connectionIP+"   connectionPwd:"+connectionPwd);
								Toast.makeText(getActivity(), "保存成功",
										Toast.LENGTH_SHORT).show();
								dialog.dismiss();
							}
						});
				builder.show();
			}
		});
	}

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg)
		{
			switch (msg.what)
			{
			case 0:
				Toast.makeText(getActivity(), "对不起，家庭服务终端拒绝连接",
						Toast.LENGTH_SHORT).show();
				binderInitListData();
				break;
			case 1:
				Toast.makeText(getActivity(), "对不起，家庭服务终端已关闭",
						Toast.LENGTH_LONG).show();
				binderInitListData();
				break;
			case 2:
				hardDiskStr = msg.getData().getString("msg");
				ResponseMessage responseMessage = JsonParse
						.Json2Object(hardDiskStr);

				if (responseMessage == null)
					Toast.makeText(getActivity(), "没有硬盘可供操作!",
							Toast.LENGTH_LONG).show();
				else
				{
					int errNum = responseMessage.getErrNum();
					if (errNum == ResponseMessageEnum.ERROR.ordinal())
						Toast.makeText(getActivity(), "获取硬盘符失败!",
								Toast.LENGTH_LONG).show();
					else
					{
						SharedPreferences.Editor editor = sharedPreferences
								.edit();
						editor.putString("initHardDiskStr",
								responseMessage.getResponseMessage());
						editor.commit();
						content_hardDisk = responseMessage.getResponseMessage()
								.split(",");
						binderListData(content_hardDisk);
					}
				}

				/*
				 * if(StringUtil.isNullString(hardDiskStr)) {
				 * Toast.makeText(getActivity(), "没有硬盘可供操作!",
				 * Toast.LENGTH_LONG).show(); } else
				 * if("error".equals(hardDiskStr)) {
				 * Toast.makeText(getActivity(), "获取硬盘符失败",
				 * Toast.LENGTH_LONG).show(); } else { SharedPreferences.Editor
				 * editor=sharedPreferences.edit();
				 * editor.putString("initHardDiskStr", hardDiskStr);
				 * editor.commit(); content_hardDisk=hardDiskStr.split(",");
				 * binderListData(content_hardDisk); }
				 */
				break;
			case 3:
				String str = msg.getData().getString("msg");
				ResponseMessage responseMessage2 = JsonParse.Json2Object(str);
				if (responseMessage2 == null)
				{
					Toast.makeText(getActivity(), "对不起，操作失败!",
							Toast.LENGTH_SHORT).show();
				} else
				{
					int errNum = responseMessage2.getErrNum();
					if (errNum == ResponseMessageEnum.SUCCESSADDNO.ordinal())
					{
						Toast.makeText(getActivity(), "请输入家庭服务终端用户名和密码!",
								Toast.LENGTH_SHORT).show();
					} else if (errNum == ResponseMessageEnum.SUCCESSADDYES
							.ordinal())
					{
						Toast.makeText(getActivity(), "磁盘挂载成功!",
								Toast.LENGTH_SHORT).show();
					} else if (errNum == ResponseMessageEnum.SUCCESSDELETE
							.ordinal())
					{
						Toast.makeText(getActivity(), "删除磁盘共享成功!",
								Toast.LENGTH_SHORT).show();
					} else
					{
						Toast.makeText(getActivity(), "对不起，操作失败!",
								Toast.LENGTH_SHORT).show();
					}
				}

				/*
				 * if("successaddno".equals(str)) {
				 * Toast.makeText(getActivity(), "添加共享成功，挂载失败!",
				 * Toast.LENGTH_SHORT).show(); } else
				 * if("successaddyes".equals(str)) {
				 * Toast.makeText(getActivity(), "添加共享成功，挂载成功!",
				 * Toast.LENGTH_SHORT).show(); } else
				 * if("successdelete".equals(str)) {
				 * Toast.makeText(getActivity(), "删除共享成功!",
				 * Toast.LENGTH_SHORT).show(); } else {
				 * Toast.makeText(getActivity(), "对不起，操作失败!",
				 * Toast.LENGTH_SHORT).show(); }
				 */
				break;
			case 4:
				String responserStr = msg.getData().getString("msg");
				ResponseMessage responseMessage3 = JsonParse
						.Json2Object(responserStr);
				if (responseMessage3 == null)
				{
					Toast.makeText(getActivity(), "操作失败!", Toast.LENGTH_SHORT)
							.show();
				} else
				{
					if (responseMessage3.getErrNum() == ResponseMessageEnum.SAVESUCCESS
							.ordinal())
					{
						Toast.makeText(getActivity(), "保存成功!",
								Toast.LENGTH_SHORT).show();
					} else
					{
						Toast.makeText(getActivity(), "操作失败!",
								Toast.LENGTH_SHORT).show();
					}
				}
				/*
				 * if("savesuccess".equals(responserStr)) {
				 * Toast.makeText(getActivity(), "保存成功!",
				 * Toast.LENGTH_SHORT).show(); } else {
				 * Toast.makeText(getActivity(), "操作失败!",
				 * Toast.LENGTH_SHORT).show(); }
				 */
				break;
			case 5:
				Toast.makeText(getActivity(), "对不起，连接家庭服务终端发生错误",
						Toast.LENGTH_LONG).show();
				binderInitListData();
				break;
			case 6:
				Toast.makeText(getActivity(), "对不起，连接家庭服务终端发生错误,操作失败",
						Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		}

	};

	private void binderListData(String[] content_hardDisk)
	{
		cleanListView();
		HardDiskItem hardDiskItem;
		boolean isHaveAuthority;
		for (int i = 0; i < content_hardDisk.length; i++)
		{
			String[] temp = content_hardDisk[i].split("-");
			String hardDiskName = temp[0];

			if (Integer.valueOf(temp[1]) == 0)
				isHaveAuthority = true;
			else
				isHaveAuthority = false;
			hardDiskItem = new HardDiskItem(hardDiskName,
					R.drawable.harddisk_item, isHaveAuthority);
			hardDiskList.add(hardDiskItem);
		}
		adapter.notifyDataSetChanged();
		listView.setVisibility(View.VISIBLE);
	}

	// 选择默认盘符进行显示
	private void binderInitListData()
	{
		cleanListView();
		HardDiskItem hardDiskItem;
		String hardDiskStr = sharedPreferences.getString("initHardDiskStr",
				"默认盘1-0,默认盘2-0");
		String[] content_hardDisk = hardDiskStr.split(",");
		boolean isHaveAuthority;
		for (int i = 0; i < content_hardDisk.length; i++)
		{
			String[] temp = content_hardDisk[i].split("-");
			String hardDiskName = temp[0];

			if (Integer.valueOf(temp[1]) == 0)
				isHaveAuthority = true;
			else
				isHaveAuthority = false;
			hardDiskItem = new HardDiskItem(hardDiskName,
					R.drawable.harddisk_item, isHaveAuthority);
			hardDiskList.add(hardDiskItem);
		}
		adapter.notifyDataSetChanged();
		listView.setVisibility(View.VISIBLE);

	}

	private void cleanListView()
	{
		int size = hardDiskList.size();
		if (size > 0)
		{
			hardDiskList.removeAll(hardDiskList);
			adapter.notifyDataSetChanged();
			// listView.setAdapter(adapter);
		}
	}

	private void showVirtualMachineSetting()
	{
		View view = layoutInflater.inflate(
				R.layout.virtualmachine_setting_dialog, null);
		final EditText mEtUserName = (EditText) view
				.findViewById(R.id.username);
		final EditText mEtPassWord = (EditText) view
				.findViewById(R.id.password);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("配置用户名密码：");
		String userName = sharedPreferences.getString("userName", null);
		String passWord = sharedPreferences.getString("passWord", null);
		if (userName != null)
		{
			mEtUserName.setText(userName);
			mEtPassWord.setText(passWord);
		}
		builder.setView(view);
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int i)
			{
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("保存", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int i)
			{
				String username = mEtUserName.getText().toString();
				String password = mEtPassWord.getText().toString();
				if (StringUtil.isNullString(username)
						|| StringUtil.isNullString(password))
				{
					Toast.makeText(getActivity(), "保存失败，输入不能为空",
							Toast.LENGTH_LONG).show();
				} else
				{
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString("userName", username);
					editor.putString("passWord", password);
					if (editor.commit())
					{
						Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_LONG)
								.show();
					} else
					{
						Toast.makeText(getActivity(), "保存失败，请重新配置",
								Toast.LENGTH_LONG).show();
					}
				}
			}
		});

		builder.show();
	}

	private void showProxyServerSetting()
	{
		View textEntryView = layoutInflater.inflate(
				R.layout.proxyserver_setting_dialog, null);
		final EditText mEtProxyServerIp = (EditText) textEntryView
				.findViewById(R.id.proxyServerIp);
		final EditText mEtproxyServerPort = (EditText) textEntryView
				.findViewById(R.id.proxyServerPort);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("配置代理服务器：");
		String proxyIpAdress = sharedPreferences.getString("proxyIpAdress",
				null);
		int proxyPortNumber = sharedPreferences.getInt("proxyPortNumber", 0);

		if (proxyIpAdress != null)
		{
			mEtProxyServerIp.setText(proxyIpAdress);
			mEtproxyServerPort.setText(String.valueOf(proxyPortNumber));
		}
		builder.setView(textEntryView);
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int i)
			{
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("保存", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int i)
			{
				String proxyServerIp = mEtProxyServerIp.getText().toString();
				String proxyServerPort = mEtproxyServerPort.getText()
						.toString();
				if (StringUtil.isNullString(proxyServerIp)
						|| StringUtil.isNullString(proxyServerPort))
				{
					Toast.makeText(getActivity(), "保存失败，输入不能为空",
							Toast.LENGTH_LONG).show();
				} else if (!StringUtil.isIPAddress(proxyServerIp))
				{
					Toast.makeText(getActivity(), "对不起，你输入的IP地址格式不正确",
							Toast.LENGTH_LONG).show();
				} else
				{
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString("proxyIpAdress", proxyServerIp);
					editor.putInt("proxyPortNumber",
							Integer.valueOf(proxyServerPort));
					if (editor.commit())
					{
						Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_LONG)
								.show();
					} else
					{
						Toast.makeText(getActivity(), "保存失败，请重新配置",
								Toast.LENGTH_LONG).show();
					}
				}
			}
		});
		builder.show();
	}

	// 显示家庭服务终端设置界面
	protected void showHomeServiceSetting()
	{
		final View textEntryView = layoutInflater.inflate(
				R.layout.homeservice_setting_dialog, null);
		final EditText mEtHomeServiceIp = (EditText) textEntryView
				.findViewById(R.id.homeServiceIp);
		final EditText mEtHomeServicePort = (EditText) textEntryView
				.findViewById(R.id.homeServicePort);

		// VNC服务器密码，即监视密码
		final EditText mEtHomeServicePwd = (EditText) textEntryView
				.findViewById(R.id.homeServicePwd);

		// 万能盒子用户名和密码
		final EditText mEtHomeServiceUserName = (EditText) textEntryView
				.findViewById(R.id.homeServiceUserName);
		final EditText mEtHomeServicePassWord = (EditText) textEntryView
				.findViewById(R.id.homeServicePassWord);

		final TextView showHardDisk = (TextView) textEntryView
				.findViewById(R.id.showListView);
		final ImageView isShowListView = (ImageView) textEntryView
				.findViewById(R.id.is_showListView);

		final String remoteIp = getRemotePreferences.getString("remoteIP",
				"0000");
		listView = (ListView) textEntryView.findViewById(R.id.authority_list);
		adapter = new AuthorityListAdapter(getActivity(), hardDiskList);
		listView.setAdapter(adapter);

		showHardDisk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				if (listView.getVisibility() == View.VISIBLE)
				{
					isShowListView.setImageResource(R.drawable.list_right);
					listView.setVisibility(View.INVISIBLE);
				} else
				{
					isShowListView.setImageResource(R.drawable.list_dowm);
					SendMsgThread getHardDiskMsg = new SendMsgThread(handler,
							getActivity(), JsonParse.Json2String(
									OptionEnum.HARD_DRIVE.ordinal(), null));
					Thread thread = new Thread(getHardDiskMsg);
					thread.start();
				}
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{

				boolean checked = hardDiskList.get(position).isChecked();
				if (!checked && !"0000".equals(remoteIp))
				{
					hardDiskList.get(position).setChecked(true);

					// SendMsgAuthority sendMsgScreen = new
					// SendMsgAuthority(handler,getActivity(),
					// position+",yesroot,"+remoteIp);
					SendMsgAuthority sendMsgScreen = new SendMsgAuthority(
							handler, getActivity(), JsonParse.Json2String(
									OptionEnum.ADD_DRIVE.ordinal(),
									new Parameter2Option(String
											.valueOf(position), remoteIp)));
					Thread threadScreen = new Thread(sendMsgScreen);
					threadScreen.start();
				}

				else if (checked && !"0000".equals(remoteIp))
				{
					hardDiskList.get(position).setChecked(false);
					// SendMsgAuthority sendMsgScreen2 = new
					// SendMsgAuthority(handler,getActivity(),
					// position+",deleteroot,"+remoteIp);
					SendMsgAuthority sendMsgScreen2 = new SendMsgAuthority(
							handler, getActivity(), JsonParse.Json2String(
									OptionEnum.DELETE_DRIVE.ordinal(),
									new Parameter2Option(String
											.valueOf(position), remoteIp)));
					Thread threadScreen2 = new Thread(sendMsgScreen2);
					threadScreen2.start();
				} else
				{
					Toast.makeText(getActivity(), "对不起，远程虚拟机IP地址错误，请重新获取!",
							Toast.LENGTH_SHORT).show();
				}
				adapter.notifyDataSetChanged();
			}
		});
		String homeIpAdress = sharedPreferences
				.getString("homeServiceIp", null);
		int homePortNumber = sharedPreferences.getInt("homeServicePortNumber",
				0);
		if (homeIpAdress != null)
		{
			mEtHomeServiceIp.setText(homeIpAdress);
			mEtHomeServicePort.setText(String.valueOf(homePortNumber));
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("配置家庭终端：");
		builder.setView(textEntryView);
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int i)
			{
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("保存", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int i)
			{

				String homeServiceIp = mEtHomeServiceIp.getText().toString();
				String homeServicePwd = mEtHomeServicePwd.getText().toString();
				String homeServicePort = mEtHomeServicePort.getText()
						.toString();

				// 万能盒子用户名和密码
				String homeServiceUserName = mEtHomeServiceUserName.getText()
						.toString();
				String homeServicePassWord = mEtHomeServicePassWord.getText()
						.toString();

				if (StringUtil.isNullString(homeServiceIp)
						|| StringUtil.isNullString(homeServicePort))
				{
					Toast.makeText(getActivity(), "保存失败，输入不能为空",
							Toast.LENGTH_LONG).show();
				} else if (!StringUtil.isIPAddress(homeServiceIp))
				{
					Toast.makeText(getActivity(), "对不起，你输入的IP地址格式不正确",
							Toast.LENGTH_LONG).show();
				} else
				{
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString("homeServiceIp", homeServiceIp);
					editor.putString("homeServicePwd", homeServicePwd);
					editor.putInt("homeServicePortNumber",
							Integer.valueOf(homeServicePort));
					if (editor.commit())
					{
						Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_LONG)
								.show();
						DeviceConnection.getInstance().init(homeServiceIp,
								64788, homeServicePwd);
					} else
					{
						Toast.makeText(getActivity(), "保存失败，请重新配置",
								Toast.LENGTH_LONG).show();
					}
				}
				if (!StringUtil.isNullString(homeServiceUserName)
						&& !StringUtil.isNullString(homeServicePassWord))
				{
					// Send_homeService_info homeService_info=new
					// Send_homeService_info(handler, getActivity(),
					// "user,"+homeServiceUserName+","+homeServicePassWord+","+remoteIp);
					Send_homeService_info homeService_info = new Send_homeService_info(
							handler, getActivity(), JsonParse.Json2String(
									OptionEnum.USER.ordinal(),
									new Parameter3Option(homeServiceUserName,
											homeServicePassWord, remoteIp)));
					Thread thread = new Thread(homeService_info);
					thread.start();
				}

			}
		});
		builder.show();
	}

}

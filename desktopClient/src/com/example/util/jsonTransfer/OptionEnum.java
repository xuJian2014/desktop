package com.example.util.jsonTransfer;

public enum OptionEnum
{
	 HARD_DRIVE,  //获得本地磁盘的共享信息（harddrive\n）：（字符串，字符串，...）
     FILE,			//获得媒体库文件列表（file\n）：（）
     FILE_SYSTEM,	// 获得虚拟机磁盘信息（fileSystem\n）：（）
     USER,			//手机端发来消息，把盒子的用户名和密码发给后台（user,用户名，密码，虚拟机IP）：（字符串）
     DRIVE,			//获得虚拟机下指定文件路径的文件或文件夹（drive，文件路径）：（）
     DISPLAY,		//获取设备名称，当选择投影的应用时（display\n）：（设备名，设备名，...）
     SCREEN,		//获取设备名称，当单独显示时（screen\n）：（设备名，设备名，...）
     APP_LIST,		//获取本地和VM的应用列表（app,虚拟机ip）：（本地应用名称，虚拟机应用名称，本地应用图标长度，本地应用图标）
     ADD_DRIVE,		//添加磁盘共享（磁盘下标，yesroot，虚拟机IP）：（字符串）
     DELETE_DRIVE,	//删除磁盘共享（磁盘下标，deleteroot，虚拟机IP）：（字符串）
     PROJECTION_FILE,  //打开并投影虚拟机中指定媒体库文件（文件下标，open，屏幕下标）：（字符串）
     PROJECTION_VM,   //获取VM应用程序名称并投影（1，虚拟机程序下标，screen，屏幕下标）：（字符串）
     PROJECTIONFILE_LOCAL //获取本地应用并投影（0，本地应用下标，screen，屏幕下标）：（字符串）

}

package com.example.util.jsonTransfer;

public enum OptionEnum
{
	 HARD_DRIVE,  //0:获得本地磁盘的共享信息（harddrive\n）：（字符串，字符串，...）
     FILE,			//1:获得媒体库文件列表（file\n）：（）
     FILE_SYSTEM,	//2:获得虚拟机磁盘信息（fileSystem\n）：（）
     USER,			//3:手机端发来消息，把盒子的用户名和密码发给后台（user,用户名，密码，虚拟机IP）：（字符串）
     DRIVE,			//4:获得虚拟机下指定文件路径的文件或文件夹（drive，文件路径）：（）
     DISPLAY,		//5:获取设备名称，当选择投影的应用时（display\n）：（设备名，设备名，...）
     SCREEN,		//6:获取设备名称，当单独显示时（screen\n）：（设备名，设备名，...）
     APP_LIST,		//7:获取本地和VM的应用列表（app,虚拟机ip）：（本地应用名称，虚拟机应用名称，本地应用图标长度，本地应用图标）
     ADD_DRIVE,		//8:添加磁盘共享（磁盘下标，yesroot，虚拟机IP）：（字符串）
     DELETE_DRIVE,	//9:删除磁盘共享（磁盘下标，deleteroot，虚拟机IP）：（字符串）
     PROJECTION_FILE,  //10:打开并投影虚拟机中指定媒体库文件（文件下标，open，屏幕下标）：（字符串）
     PROJECTION_VM,   //11:获取VM应用程序名称并投影（1，虚拟机程序下标，screen，屏幕下标）：（字符串）
     PROJECTIONFILE_LOCAL, //12:获取本地应用并投影（0，本地应用下标，screen，屏幕下标）：（字符串）
     PROJECTION_FILE_SYSTEM,  //13:打开并投影文件系统中指定媒体库文件
     
     Local_File_Videos, //14:本地视频文件
     Local_File_Music,  //15:本地音乐文件
     Local_File_Office, //16:本地文档文件	
     Local_File_Photos, //17:本地图像文件
     Remote_File_Videos,//18:远程视频文件
     Remote_File_Music, //19:远程音乐文件
     Remote_File_Office,//20:远程文档文件
     Remote_File_Photos //21:远程图像文件
}

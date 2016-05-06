package com.example.util.jsonTransfer;

public enum ResponseMessageEnum
{
	 SUCCESS,//操作成功
     ERROR,//操作失败
     UNCONNECTED,//没有找到指定连接设备
     WAIT,//正在连接，等待连接完成后重试
     SAVESUCCESS,//用户名密码保存成功
     SUCCESSADDNO,//添加权限完成,挂载失败
     SUCCESSADDYES,//添加权限完成,挂载成功
     SUCCESSDELETE,//删除权限成功

}

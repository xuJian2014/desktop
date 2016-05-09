package com.example.Entity;

import java.io.Serializable;

public class RequestMesg implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String userName;
	private String passWord;
	private String vmname; //�������
	private String ip; //��ͥ�ն�IP��ַ
	private int command;
	private int portNumber; //�ֻ��˼����˿ں�
	private int location; //0�����أ�1����Ǳ���
	private String cpuNum;  //cpu����
	private String memorySize; //�ڴ��С
	
	public RequestMesg(String userName, String passWord, String vmname,
			int command, int portNumber, String cpuNum, String memorySize)
	{
		super();
		this.userName = userName;
		this.passWord = passWord;
		this.vmname = vmname;
		this.command = command;
		this.portNumber = portNumber;
		this.cpuNum = cpuNum;
		this.memorySize = memorySize;
	}
	
	public RequestMesg(String userName, String passWord, String vmname,
			String ip, int command, int portNumber, int location)
	{
		super();
		this.userName = userName;
		this.passWord = passWord;
		this.vmname = vmname;
		this.ip = ip;
		this.command = command;
		this.portNumber = portNumber;
		this.location = location;
	}
	public String getIp() 
	{
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getLocation() {
		return location;
	}
	public void setLocation(int location) {
		this.location = location;
	}
	public RequestMesg(String userName, String passWord, int command,
			int portNumber) {
		super();
		this.userName = userName;
		this.passWord = passWord;
		this.command = command;
		this.portNumber = portNumber;
	}
	public String getVmname() {
		return vmname;
	}
	public void setVmname(String vmname) {
		this.vmname = vmname;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public int getCommand() {
		return command;
	}
	public void setCommand(int command) {
		this.command = command;
	}
	public int getPortNumber() {
		return portNumber;
	}
	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}
	
	public String getCpuNum()
	{
		return cpuNum;
	}
	public void setCpuNum(String cpuNum)
	{
		this.cpuNum = cpuNum;
	}
	public String getMemorySize()
	{
		return memorySize;
	}
	public void setMemorySize(String memorySize)
	{
		this.memorySize = memorySize;
	}
	public RequestMesg(String userName, String passWord, String ip,
			int command, int portNumber) {
		super();
		this.userName = userName;
		this.passWord = passWord;
		this.ip = ip;
		this.command = command;
		this.portNumber = portNumber;
	}
	
	
}

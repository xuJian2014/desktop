package com.example.touch;

public class TouchFlag {

	private String ip;
	private String pwd;
	private boolean isValid = false;
	private boolean authentificated = false;

	private static TouchFlag instance = new TouchFlag();

	private TouchFlag() {
	}

	public static TouchFlag getInstance() {
		return instance;
	}

	public String getIp() {
		return ip;
	}

	public synchronized void setIp(String ip) {
		this.ip = ip;
	}

	public String getPwd() {
		return pwd;
	}

	public synchronized void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public boolean isAuthentificated() {
		return authentificated;
	}

	public void setAuthentificated(boolean authentificated) {
		this.authentificated = authentificated;
	}
}

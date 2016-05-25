package com.example.touch;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.example.action.AuthentificationResponseAction;
import com.example.action.ControllerDroidAction;

public class ConnectServer {

	private Socket socket = null;
	private OutputStream outputStream;
	private DataInputStream dataInputStream;
	private static ConnectServer instance = new ConnectServer();

	private ConnectServer() {
	}

	public static ConnectServer getInstance() {
		return instance;
	}

	public void connect(String ip) throws Exception {
		if (socket == null) {
			socket = new Socket();
			socket.connect(new InetSocketAddress(ip, 64888), 1000);
			outputStream = socket.getOutputStream();
			dataInputStream = new DataInputStream(socket.getInputStream());

			socket.setPerformancePreferences(0, 2, 1);
			socket.setTcpNoDelay(true);
			socket.setReceiveBufferSize(1024 * 1024);
			socket.setSendBufferSize(1024 * 1024);
		}
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public synchronized void sendAction(ControllerDroidAction action) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			action.toDataOutputStream(new DataOutputStream(baos));
			outputStream.write(baos.toByteArray());
			outputStream.flush();
		} catch (IOException e) {
			this.close();
		}
	}

	public synchronized AuthentificationResponseAction recvAction() {
		boolean authentificated = false;
		try {
			byte type = this.dataInputStream.readByte();
			if (type == ControllerDroidAction.AUTHENTIFICATION_RESPONSE) {
				authentificated = this.dataInputStream.readBoolean();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new AuthentificationResponseAction(authentificated);
	}

	public void close() {
		if (socket != null) {
			try {
				socket.close();
				socket = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

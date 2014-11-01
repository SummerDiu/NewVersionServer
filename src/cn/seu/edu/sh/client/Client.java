package cn.seu.edu.sh.client;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {
	private DatagramSocket socket;
	private int id;
	private final InetAddress ip;
	private int port;

	public Client(DatagramSocket socket, int id,InetAddress ip,int port) {
		this.socket = socket;
		this.setId(id);
		this.ip = ip;
		this.setPort(port);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public InetAddress getIp() {
		return ip;
	}

}

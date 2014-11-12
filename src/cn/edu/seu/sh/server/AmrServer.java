package cn.edu.seu.sh.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.LinkedList;

import cn.edu.seu.sh.config.CommonConfig;
import cn.edu.seu.sh.packet.FramePacket;
import cn.edu.seu.sh.thread.DownThread;
import cn.edu.seu.sh.thread.UpThread;
import cn.seu.edu.sh.client.Client;
import cn.seu.edu.sh.client.ClientManager;

public class AmrServer {
	private static final int upPort = CommonConfig.AUDIO_SERVER_UP_PORT;//与客户端发送数据时packet指定的端口一致
	private static final int downPort = CommonConfig.AUDIO_SERVER_DOWN_PORT;
	public final int BUFFER_SIZE = 50*100;

	private DatagramSocket receiveSocket;
	private DatagramSocket sendSocket;	
	
	private UpThread upThread = null;
	private DownThread downThread = null;
	
	private int bufferSize;
	
	public ClientManager clientManager;
	public ArrayList<Client> clientList = null;
	ArrayList<Client> disconnectClientList = null;
//	public LinkedList<FramePacket> packetList;	
	public LinkedList<DatagramPacket> packetList;
	
	public AmrServer() {
		
		this.bufferSize = BUFFER_SIZE;
		try {
			receiveSocket = new DatagramSocket(upPort);
			sendSocket = new DatagramSocket();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		packetList = new LinkedList<FramePacket>();
		packetList = new LinkedList<DatagramPacket>();
		clientList = new ArrayList<Client>();
		disconnectClientList = new ArrayList<Client>();
		clientManager = new ClientManager(clientList,disconnectClientList);
		upThread = new UpThread(AmrServer.this,receiveSocket);
		downThread = new DownThread(AmrServer.this,sendSocket);
	}
	
	public void start() {
		upThread.start();		
		clientManager.initClientList();
		downThread.start();
	}
	
	public void stop() {
		if (receiveSocket != null) {
			receiveSocket.close();
			receiveSocket = null;
		}
		
		if (sendSocket != null) {
			sendSocket.close();
			sendSocket = null;
		}
		
		if(upThread != null){
			upThread.stopThread();
			upThread = null;
		}
		
		if(downThread != null){
			downThread.stopThread();
			downThread = null;
		}

		packetList.clear();
		packetList = null;
		clientManager.clearClientManager();

	}
	
	public synchronized DatagramPacket takeAwayFirstPacket() {
		if (packetList.size() <= 0) {
			return null;
		}
//		FramePacket fp = packetList.getFirst();
		DatagramPacket packet = packetList.getFirst();
		if (packet == null) {
			return null;
		}
//		FramePacket packet = new FramePacket(fp);
		packetList.removeFirst();
		return packet;
	}
	
//	public byte[] takeAwayFirstFrame() {
//		FramePacket packet = takeAwayFirstPacket();
//		if (packet == null) {
//			return null;
//		}
//		return packet.getFrame();
//	}

}

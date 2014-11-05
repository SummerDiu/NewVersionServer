package cn.edu.seu.sh.thread;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

import cn.edu.seu.sh.packet.FramePacket;
import cn.edu.seu.sh.server.AmrServer;

public class UpThread extends Thread {
	AmrServer amrServer = null;
	private boolean keepRunning = true;
	private DatagramSocket receiveSocket= null;
	int n = 0;
	
	public UpThread(AmrServer amrServer,DatagramSocket receiveSocket){
		this.amrServer = amrServer;
		this.receiveSocket = receiveSocket;
	}

	@Override
	public void run() {
		byte[] data = new byte[32];	/////////1024*10
		while (keepRunning){
			try{
				DatagramPacket packet = new DatagramPacket(data, data.length);
				receiveSocket.receive(packet);
				System.out.println("ServerReceivePacket--->from:"+packet.getAddress()
						+"length:"+packet.getLength()+
		            			"content:"+Arrays.toString(packet.getData()));
				
				System.out.println(packet.getLength()+"--/--"+packet.getData().length);
				
				addPacketToBuffer(new FramePacket(packet.getData(), packet.getData().length));
//				addPacketToBuffer(new FramePacket(packet.getData(), packet.getLength()));

			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void stopThread(){
		keepRunning = false;
	}
	
	public void addPacketToBuffer(FramePacket packet) {
		if (amrServer.packetList.size() > amrServer.BUFFER_SIZE) {
			amrServer.takeAwayFirstPacket();
		}
		amrServer.packetList.addLast(packet);
	}
	
	

}

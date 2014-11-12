package cn.edu.seu.sh.thread;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Arrays;

import cn.edu.seu.sh.server.AmrServer;
import cn.seu.edu.sh.client.Client;

public class DownThread extends Thread {
	AmrServer amrServer= null;
	private boolean keepRunning = true;
	private DatagramSocket sendSocket = null;
	int n = 0;
	
	public DownThread(AmrServer amrServer,DatagramSocket sendSocket){
		this.amrServer = amrServer;
		this.sendSocket = sendSocket;
	}

	@Override
	public void run() {
		while(keepRunning){
			try{
				sendDataToAllClient();
				Thread.sleep(5);
			}catch(Exception e)	{	
				e.printStackTrace();
			}			
		}		
	}	
		
	public void stopThread(){
		keepRunning = false;
	}
	
	public void sendDataToAllClient() throws Exception {
		boolean isBufferEmpty = (amrServer.packetList.size()==0);
		if (isBufferEmpty || amrServer.clientList.size() <= 0) {
			return;
		}
//		byte[] block = amrServer.takeAwayFirstFrame();
		DatagramPacket packet = amrServer.takeAwayFirstPacket();
		byte[] block = packet.getData();
		int length = packet.getData().length;
		ArrayList<Integer> disConnectClient = new ArrayList<Integer>();
		for (int ix = 0; ix < amrServer.clientList.size(); ++ix) {
			Client client = amrServer.clientList.get(ix);
			
			try {
				if (!isBufferEmpty) {
					if (packet == null){
						continue;
					}
					DatagramPacket sendPacket = new DatagramPacket(block,length,client.getIp(),
							client.getPort());
					sendSocket.send(sendPacket);	
					while(n++ <100)
					System.out.println("ServerSendPacket:--->to"+sendPacket.getAddress()+
							"length:"+sendPacket.getLength()+
	            			"content:"+Arrays.toString(sendPacket.getData()));
				}
			} catch (Exception e) {
				e.printStackTrace();
				disConnectClient.add(ix);
//				System.out.println("send data to id=" + client.getId() + " error" + " :"
//						+ e.toString());				
			}
		}
		disConnectClient.clear();
		disConnectClient = null;
		block = null;
		packet = null;
	}
	
}

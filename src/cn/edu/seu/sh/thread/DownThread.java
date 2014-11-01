package cn.edu.seu.sh.thread;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

import cn.edu.seu.sh.server.AmrServer;
import cn.seu.edu.sh.client.Client;

public class DownThread extends Thread {
	AmrServer amrServer= null;
	private boolean keepRunning = true;
	private DatagramSocket sendSocket = null;
	
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
		byte[] block = amrServer.takeAwayFirstFrame();
		ArrayList<Integer> disConnectClient = new ArrayList<Integer>();
		for (int ix = 0; ix < amrServer.clientList.size(); ++ix) {
			Client client = amrServer.clientList.get(ix);
			
			try {
				if (!isBufferEmpty) {
					if (block == null){
						continue;
					}						
					DatagramPacket pack = new DatagramPacket(block,	block.length,client.getIp(),
															client.getPort());
					sendSocket.send(pack);					
//					System.out.println("发往："+pack.getAddress().toString()+"	");
				}
			} catch (Exception e) {
				disConnectClient.add(ix);
//				System.out.println("send data to id=" + client.getId() + " error" + " :"
//						+ e.toString());				
			}
		}
		disConnectClient.clear();
		disConnectClient = null;
		block = null;
	}
	
}

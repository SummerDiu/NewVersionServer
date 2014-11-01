package cn.seu.edu.sh.client;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import cn.edu.seu.sh.config.CommonConfig;

public class ClientManager {
	private ArrayList<Client> clientList;
	private ArrayList<Client> disconnectClientList;
	
	public ClientManager(ArrayList<Client> clientList,ArrayList<Client> disconnectClientList){
		this.clientList = clientList;
		this.disconnectClientList = disconnectClientList;
	}
	
	public void initClientList(){
		
		Client clientA,clientB;
		try {
			clientA = new Client(new DatagramSocket(),
					0, InetAddress.getByName(CommonConfig.CLIENT_A_IP_ADDRESS), 
					CommonConfig.CLIENT_A_PORT);		
			clientB = new Client(new DatagramSocket(),
					1, InetAddress.getByName(CommonConfig.CLIENT_B_IP_ADDRESS), 
					CommonConfig.CLIENT_B_PORT);
			clientList.add(clientA);
			clientList.add(clientB);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}		
	}
	
	public void clearClientManager(){
		clientList.clear();
		clientList = null;
		
		disconnectClientList.clear();
		disconnectClientList = null;
		
		
	}
	
	public void addClient(DatagramSocket clientSocket, int id,InetAddress ip,int port) {
		clientList.add(new Client(clientSocket, id,ip,port));
	}

	public void removeClient(int id) {
		int needRemoveIndex = -1;
		for (int ix = 0; ix < clientList.size(); ++ix) {
			Client client = clientList.get(ix);
			if (client.getId() == id) {
				needRemoveIndex = ix;
				break;
			}
		}
		if (needRemoveIndex != -1) {
			clientList.remove(needRemoveIndex);
		}
	}
	
}

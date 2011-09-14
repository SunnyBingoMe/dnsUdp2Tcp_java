package query;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.concurrent.Callable;
import util.Global;

public class UdpQuery implements Callable<DatagramPacket>{
	private byte[]		queryData;
	private InetAddress	server;
	private int			port;

	public UdpQuery(byte[] queryData, InetAddress server, int port){
		this.queryData = queryData;
		this.server = server;
		this.port = port;
	}

	long	timeBegin;

	@Override
	public DatagramPacket call(){
		timeBegin = System.currentTimeMillis();
		DatagramSocket udpQuery = null;
		try{
			udpQuery = new DatagramSocket();
			udpQuery.setSoTimeout(Global.config.getUdpTimeout());
			DatagramPacket query = Global.packetFromData(queryData);
			query.setAddress(server);
			query.setPort(port);
			udpQuery.send(query);
			DatagramPacket response = null;
			while(!timeout())
				try{
					DatagramPacket temp = Global.newPacket();
					udpQuery.receive(temp);
					response = temp;
				}
				catch(SocketTimeoutException e){
				}
			udpQuery.close();
			return response;
		}
		catch(Exception e){
		}
		try{
			udpQuery.close();
		}
		catch(Exception e){
		}
		return null;
	}

	private boolean timeout(){
		return System.currentTimeMillis() - timeBegin
				+ Global.config.getUdpTimeout() >= Global.config
				.getQueryTimeout();
	}
}

package query;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.Callable;
import util.Global;

public class TcpQuery implements Callable<DatagramPacket>{
	private byte[]		queryData;
	private InetAddress	server;
	private int			port;

	public TcpQuery(byte[] queryData, InetAddress server, int port){
		this.queryData = queryData;
		this.server = server;
		this.port = port;
	}

	@Override
	public DatagramPacket call(){
		Socket tcpQuery = null;
		try{
			tcpQuery = new Socket(server, port);
			tcpQuery.setSoTimeout(Global.config.getTcpTimeout());
			tcpQuery.getOutputStream().write(tcpFromUdp(queryData));
			DatagramPacket response = Global
					.packetFromData(udpFromTcp(readFromSocket(tcpQuery)));
			tcpQuery.close();
			return response;
		}
		catch(Exception e){
		}
		try{
			tcpQuery.close();
		}
		catch(Exception e){
		}
		return null;
	}

	private byte[] readFromSocket(Socket socket) throws IOException{
		byte[] data = Global.newBuffer();
		int length = socket.getInputStream().read(data);
		return Arrays.copyOf(data, length);
	}

	private byte[] tcpFromUdp(byte[] udpData){
		byte[] tcpData = new byte[udpData.length + 2];
		tcpData[0] = (byte)(udpData.length >> 8 & 0xFF);
		tcpData[1] = (byte)(udpData.length & 0xFF);
		for(int i = 0; i < udpData.length; i++)
			tcpData[i + 2] = udpData[i];
		return tcpData;
	}

	private byte[] udpFromTcp(byte[] tcpData){
		if(tcpData.length > 2)
			return Arrays.copyOfRange(tcpData, 2, tcpData.length);
		else
			return new byte[0];
	}
}

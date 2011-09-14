package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import query.DnsQuery;
import util.Global;

public class DnsServer{
	private DatagramSocket	udpListen;

	public DnsServer(int port) throws SocketException{
		udpListen = new DatagramSocket(port);
	}

	public void run(){
		DatagramPacket clientQuery = Global.newPacket();
		while(true)
			try{
				udpListen.receive(clientQuery);
				Thread query = new Thread(new DnsQuery(clientQuery, udpListen));
				query.start();
			}
			catch(Exception e){
			}
	}

	public static void main(String[] args){
		int localPort = Global.config.getLocalPort();
		try{
			new DnsServer(localPort).run();
		}
		catch(SocketException e){
		}
	}
}

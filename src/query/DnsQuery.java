package query;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import util.Global;
import util.Statistics;

public class DnsQuery implements Runnable{
	private byte[]			queryData;
	private InetAddress		clientAddress;
	private int				clientPort;
	private DatagramSocket	responseSocket;

	public DnsQuery(DatagramPacket clientQuery, DatagramSocket socket){
		queryData = Global.dataFromPacket(clientQuery);
		clientAddress = clientQuery.getAddress();
		clientPort = clientQuery.getPort();
		responseSocket = socket;
	}

	@Override
	public void run(){
		long timeBegin = System.currentTimeMillis();
		List<Future<DatagramPacket>> queryFutures = new ArrayList<Future<DatagramPacket>>();
		for(InetAddress server:Global.config.getRemoteServers()){
			int port = Global.config.getRemotePort();
			queryFutures.add(Global.executor.submit(new TcpQuery(queryData,
					server, port)));
			queryFutures.add(Global.executor.submit(new UdpQuery(queryData,
					server, port)));
		}
		int timeLimit = Global.config.getQueryTimeout() + 100;
		for(Future<DatagramPacket> future:queryFutures)
			try{
				DatagramPacket response = future.get(timeLimit,
						TimeUnit.MILLISECONDS);
				timeLimit = 100;
				if(response != null){
					response.setAddress(clientAddress);
					response.setPort(clientPort);
					responseSocket.send(response);
					Statistics.success(System.currentTimeMillis() - timeBegin);
					return;
				}
			}
			catch(Exception e){
			}
		Statistics.fail(System.currentTimeMillis() - timeBegin);
	}
}

package util;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Configuration{
	private static int			DEFAULT_LOCAL_PORT		= 53;
	private static int			DEFAULT_REMOTE_PORT		= 53;
	private static String		DEFAULT_REMOTE_SERVER	= "2001:470:20::2";
	private static int			DEFAULT_QUERY_TIMEOUT	= 1000;
	private static int			DEFAULT_TCP_TIMEOUT		= 800;
	private static int			DEFAULT_UDP_TIMEOUT		= 200;
	private int					localPort;
	private int					remotePort;
	private List<InetAddress>	remoteServers;
	private int					queryTimeout;
	private int					tcpTimeout;
	private int					udpTimeout;

	public Configuration(String path){
		Properties properties = new Properties();
		try{
			properties.load(new FileInputStream(new File(path)));
			try{
				localPort = Integer.parseInt(properties
						.getProperty("localport"));
			}
			catch(Exception e){
				localPort = DEFAULT_LOCAL_PORT;
			}
			try{
				remotePort = Integer.parseInt(properties
						.getProperty("remoteport"));
			}
			catch(Exception e){
				remotePort = DEFAULT_REMOTE_PORT;
			}
			remoteServers = new ArrayList<InetAddress>();
			for(String ip:properties.getProperty("remoteserver").split(","))
				try{
					remoteServers.add(InetAddress.getByName(ip));
				}
				catch(Exception e){
				}
			if(remoteServers.isEmpty())
				try{
					remoteServers.add(InetAddress
							.getByName(DEFAULT_REMOTE_SERVER));
				}
				catch(Exception e1){
					remoteServers = null;
				}
			try{
				queryTimeout = Integer.parseInt(properties
						.getProperty("querytimeout"));
			}
			catch(Exception e){
				queryTimeout = DEFAULT_QUERY_TIMEOUT;
			}
			try{
				tcpTimeout = Integer.parseInt(properties
						.getProperty("tcptimeout"));
			}
			catch(Exception e){
				tcpTimeout = DEFAULT_TCP_TIMEOUT;
			}
			try{
				udpTimeout = Integer.parseInt(properties
						.getProperty("udptimeout"));
			}
			catch(Exception e){
				udpTimeout = DEFAULT_UDP_TIMEOUT;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public int getLocalPort(){
		return localPort;
	}

	public int getRemotePort(){
		return remotePort;
	}

	public List<InetAddress> getRemoteServers(){
		return remoteServers;
	}

	public int getQueryTimeout(){
		return queryTimeout;
	}

	public int getTcpTimeout(){
		return tcpTimeout;
	}

	public int getUdpTimeout(){
		return udpTimeout;
	}
}

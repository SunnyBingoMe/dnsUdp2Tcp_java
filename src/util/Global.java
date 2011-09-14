package util;

import java.net.DatagramPacket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Global{
	private static final String		CONFIG_FILE_NAME	= "dns.conf";
	public static Configuration		config				= new Configuration(
																CONFIG_FILE_NAME);
	public static Logger			logger				= Logger.getLogger("DNS");
	public static ExecutorService	executor			= Executors
																.newCachedThreadPool();

	public static byte[] dataFromPacket(DatagramPacket packet){
		return Arrays.copyOf(packet.getData(), packet.getLength());
	}

	public static DatagramPacket packetFromData(byte[] data){
		return new DatagramPacket(data, data.length);
	}

	private static final int	BUFFER_LENGTH	= 65536;

	public static DatagramPacket newPacket(){
		return new DatagramPacket(new byte[BUFFER_LENGTH], BUFFER_LENGTH);
	}

	public static byte[] newBuffer(){
		return new byte[BUFFER_LENGTH];
	}
}

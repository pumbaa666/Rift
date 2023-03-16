package ch.correvon.rift.serverAlert.tools;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class WakeOnLan
{
	public static String sendWOL(String ip, String mac, int port)
	{
		try
		{
			byte[] macBytes = getMacBytes(mac);
			byte[] bytes = new byte[6 + 16 * macBytes.length];
			for(int i = 0; i < 6; i++)
				bytes[i] = (byte)0xff;
			for(int i = 6; i < bytes.length; i += macBytes.length)
				System.arraycopy(macBytes, 0, bytes, i, macBytes.length);

			InetAddress address = InetAddress.getByName(ip);
			DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
			DatagramSocket socket = new DatagramSocket();
			socket.send(packet);
			socket.close();
			return null;
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
			return e.getMessage();
		}
		catch(UnknownHostException e)
		{
			e.printStackTrace();
			return "Hôte inconnu (IP)"+e.getMessage();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return e.getMessage();
		}
	}

	private static byte[] getMacBytes(String macStr) throws IllegalArgumentException
	{
		byte[] bytes = new byte[6];
		String[] hex = macStr.split("(\\:|\\-)");
		if(hex.length != 6)
			throw new IllegalArgumentException("Invalid MAC address.");
		try
		{
			for(int i = 0; i < 6; i++)
				bytes[i] = (byte)Integer.parseInt(hex[i], 16);
		}
		catch(NumberFormatException e)
		{
			throw new IllegalArgumentException("Invalid hex digit in MAC address.");
		}
		return bytes;
	}
}

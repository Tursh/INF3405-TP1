package com.tremblar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerConnection {

	private boolean connected = false;
	private Socket socket;
	
	DataInputStream in;
	DataOutputStream out;
	
	public ServerConnection(String ipAdress, int port)
	{
		try {
			socket = new Socket(ipAdress, port);
		} catch (Exception e) {
			System.out.println("Connection could not be established! \n" + e);
			return;
		}
		
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		
			String serverMsg = in.readUTF(); 
			
			System.out.println(serverMsg);
			
			connected = serverMsg.matches("Connection Established");

		} catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public void sendMessage(String msg)
	{
		try {
			out.writeUTF("send " + msg);
		if(!in.readUTF().matches("message received"))
		{
			System.out.println("An error occured while sending this message: " + msg);
		}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendImage(String fileName)
	{
		try {
			ImageSender.SendImage(socket, fileName);
		} catch (Exception e) {
			System.out.println("Could not send image! Error: " + e.getMessage());
		}
	}
	
	public void receiveImage(String fileName)
	{
		try {
			ImageReceiver.receiveImage(socket, fileName);
		} catch (Exception e) {
			System.out.println("Could not receive image! Error: " + e.getMessage());
		}
	}
	
	public void login(String clientName, String password)
	{
		
	}
	
	public void close()
	{
		if(connected)
		{
			try {
				out.writeUTF("close");
				if(in.readUTF().matches("connection closing"))
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			connected = false;
		}
	}

	public boolean isConnected() {
		return connected;
	}
	
}

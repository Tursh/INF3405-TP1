package com.tremblar;

import java.io.DataInputStream;
import java.net.Socket;

public class Client {
	
	public static String SERVER_ADRESS = "127.0.0.1";
	public static int SERVER_PORT = 5000;

	private static Socket socket;
	
	public static void main(String[] args) throws Exception
	{
		socket = new Socket(SERVER_ADRESS, SERVER_PORT);
		
		System.out.format("The server is running on %s:%d%n", SERVER_ADRESS, SERVER_PORT);
		
		DataInputStream in = new DataInputStream(socket.getInputStream());
		
		String helloMessageFromServer = in.readUTF();
		System.out.println(helloMessageFromServer);
		
		socket.close();
	}
}

package com.tremblar;

import java.io.DataInputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	private static Socket socket;
	private static Scanner scan;
	
	public static void main(String[] args) throws Exception
	{
		scan = new Scanner(System.in);
		
		System.out.print("Enter the server IP: ");
		String serverAdress = scan.next();
		
		System.out.print("Enter the server port: ");
		int serverPort = scan.nextInt();
		
		socket = new Socket(serverAdress, serverPort);
		
		System.out.format("The server is running on %s:%d%n", serverAdress, serverPort);
		
		DataInputStream in = new DataInputStream(socket.getInputStream());
		
		String helloMessageFromServer = in.readUTF();
		System.out.println(helloMessageFromServer);
		
		socket.close();
	}
}

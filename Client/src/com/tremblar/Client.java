package com.tremblar;

import java.util.Scanner;

public class Client {

	private static ServerConnection connection;
	private static Scanner scan;
	
	public static void main(String[] args) throws Exception
	{
		scan = new Scanner(System.in);
		
		String serverAddress;
		do {
			System.out.print("Enter the server IP: ");
			serverAddress = scan.next();
		} while (!EntryValidator.verifyIpAddress(serverAddress));
		
		int serverPort;
		do {
			System.out.print("Enter the server port: ");
			serverPort = scan.nextInt();
		} while (!EntryValidator.verifyPort(serverPort));
		
		connection = new ServerConnection(serverAddress, serverPort);
		
		do
		{
			String command = scan.nextLine();
			
			if(command.isEmpty())
				continue;
			
			String[] words = command.split(" ");
			
			switch(words[0])
            {
			case "send":
				connection.sendMessage(command.substring(5));
				break;
            case "login":
            	//TODO: What to call to login
            	break;
            case "image":
            	connection.sendImage(command.substring(6));
            	break;
            case "close":
            	connection.close();
            	break;
            default:
            	System.out.println("Unknown command");
            }
			
		}while(connection.isConnected());
	}
}

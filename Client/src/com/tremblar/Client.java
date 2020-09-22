package com.tremblar;

import java.util.Scanner;

public class Client {

	private static ServerConnection connection;
	private static Scanner scan;
	
	public static void main(String[] args) throws Exception
	{
		scan = new Scanner(System.in);
		
		System.out.print("Enter the server IP: ");
		String serverAdress = scan.next();
		
		System.out.print("Enter the server port: ");
		int serverPort = scan.nextInt();
		
		connection = new ServerConnection(serverAdress, serverPort);
		
		do
		{
			String command = scan.nextLine();
			
			if(command.isEmpty())
				continue;
			
			String[] words = command.split(" ");
			
			switch(words[0])
            {
			case "send":
				connection.sendMessage(command.substring(4));
				break;
            case "login":
            	//TODO: What to call to login
            	break;
            case "image":
            	//TODO: What to call to load image
            	break;
            case "close":
            	connection.close();
            default:
            	System.out.println("Unknown command");
            }
			
		}while(connection.isConnected());
	}
}

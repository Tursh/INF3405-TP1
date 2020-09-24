package com.tremblar;

import java.util.Scanner;

public class Client {

	private static ServerConnection connection;
	private static Scanner scan;
	
	public static void main(String[] args) throws Exception
	{
		scan = new Scanner(System.in);
		
		String serverAddress;
		boolean isValidServerAddress = false;
		do {
			System.out.print("Enter the server IP: ");
			serverAddress = scan.next();
			String[] addressBytes = serverAddress.split("\\.");
			if (addressBytes.length != 4) {
				System.out.println("Server IP must have 4 bytes");
				continue;
			}
			
			isValidServerAddress = true;
			for (int i = 0; i < addressBytes.length; i++) {
				try {
					isValidServerAddress &= Integer.parseInt(addressBytes[i]) >= 0;
					isValidServerAddress &= Integer.parseInt(addressBytes[i]) <= 255;
				} catch (NumberFormatException e) {
					System.out.println("Not all entries were numbers - " + e);
					isValidServerAddress = false;
				}
				if (!isValidServerAddress) {
					System.out.println("Server IP can only contain numbers between 0 and 255");
					break;
				}
			}
			
		} while (!isValidServerAddress);
		
		int serverPort;
		do {
			System.out.print("Enter the server port: ");
			serverPort = scan.nextInt();
			
			if (serverPort < 5000 || serverPort > 5050) {
				System.out.println("Server port must be between 5000 and 5050");
			}
		} while (serverPort < 5000 || serverPort > 5050);
		
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
            	break;
            default:
            	System.out.println("Unknown command");
            }
			
		}while(connection.isConnected());
	}
}

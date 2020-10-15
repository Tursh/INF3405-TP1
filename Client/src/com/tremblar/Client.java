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
        	System.out.print("Enter command: ");
        	
        	String command;
        	
        	do
        	{
			 command = scan.nextLine();
        	} while (command.isEmpty());
			
			if(command.isEmpty())
				continue;
			
			String[] words = command.split(" ");
			
			switch(words[0])
            {
			case "send":
				connection.sendMessage(command.substring(5));
				break;
            case "login":
            	connection.sendLogin(words[1], words[2]);
            	break;
            case "image":
            	connection.sendImage(command.substring(6));
            	break;
            case "close":
            	connection.close();
            	break;
            case "register":
            	connection.sendRegister(words[0], words[1]);
            	break;
            case "help":
            	System.out.println(
            			  "Command list:\n"
            			+ "- 'login'	- Login to your account 		- usage: login <Username> <Password>\n"
            			+ "- 'register	- Create a new account			- usage: register <Username> <Password>\n"
            			+ "These commands needs you to be logged in:\n"
            			+ "- 'send'		- Send message to server		- usage: send <msg>\n"
            			+ "- 'image'	- Send image to server			- usage: image <Image Filename>\n"
            			+ "- 'close'	- Close connection to server"
            			);
            	break;
            default:
            	System.out.println("Unknown command. Write 'help' for command list");
            }
			
		}while(connection.isConnected());
	}
}

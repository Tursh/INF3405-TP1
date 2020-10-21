package com.tremblar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {
	private Socket socket; // the client socket
    private int clientNumber;
    private String clientName;
    private boolean running;
  
    public PasswordManager pm;
    
    public ClientHandler(Socket socket, int clientNumber, PasswordManager manager) throws IOException{
    	running = true;
        this.socket = socket;
        this.clientNumber = clientNumber;
        pm = manager;
        System.out.println("new connection with client number " + this.clientNumber +
            " at " + socket);
        send("Connection Established");
    }
    
    private String receive()
    {
    	String msg;
    	try{
            DataInputStream in = new DataInputStream(socket.getInputStream());
            msg = in.readUTF();
        }catch(IOException e){
        	if(socket.isClosed())
        		System.out.println("Connection interupted with client " + clientNumber);
        	else
        		System.out.println(e);
        	running = false;
            return null;
        }
    	return msg;
    }
    
    private void send(String msg)
    {
        try{
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(msg);
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public void run(){
        while(running)
        {
        	//Receive message
        	String msg;
        	do{
        		msg = receive();
        	}while(msg == null && running);
        	
            String[] words = null;
            words = msg.split(" ");
            
            String response = "";

            
            switch(words[0])
            {
            case "login":
            	//Check if already logged in
            	if(clientName != null)
            	{
            		response = "You are already logged in as " + clientName;
            		break;
            	}
            	
            	//Check for the syntax
            	if(words.length != 3)
				{
					response = "You incorrectly tried to login. Type 'help' to see how";
					break;
				}
				
				System.out.println(words[1] + " is logging in...");
				
				//Does the client exist
				if(pm.clientOk(words[1], words[2])) {
					response = "Login success. Welcome back!";
					System.out.println(words[1] + " is logged in succesfully!");
				}
				if(pm.doesClientExist(words[1]))
				{
					response = "Wrong password for " + words[1];
					System.out.println("Wrong password for " + words[1]);
					break;
				}
				//If not register the client
				else { 
					System.out.println(words[1] + " is not registered.\nNow registering " + words[1] + "...");
					if(pm.insertClient(words[1], words[2]))
					{
						response = "Your account is now registered. Welcome " + words[1];
						System.out.println(words[1] + " is now registered and logged in!");
					}
				}
				
				clientName = words[1];
				
            	break;
            case "send":
            	if(clientName != null)
            	{
            		System.out.println(clientName + ": " + msg.substring(5));
            		response = "message received";
            	}
            	else
            		response = "You need to be logged in to send messages. Type help to see how";
            	break;
            case "image":
            	if(clientName != null)
            	{
            		ImageReceiver.printImageInfo(clientName, socket, words[1]);
            		ImageReceiver.receiveImage(socket, words[1]);
            		try {
            			ImageSender.SendImage(socket, words[1]);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
            	else
            		response = "You need to be logged in to send images. Type help to see how";
            	break;
            case "close":
            	running = false;
            	response = "connection closing";
            	break;
            default:
            	response = "unknown command";
            }
            
            send(response);
            
        }
      
        stopSocket();
    }
    
    private void stopSocket()
    {
    	try{
    		socket.close();
        }catch(IOException e){
        	System.out.println(e);
        }

        System.out.println("Connection closed with client " + clientNumber);
    }
}

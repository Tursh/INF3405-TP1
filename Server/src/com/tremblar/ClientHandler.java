package com.tremblar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {
	private Socket socket; // the client socket
    private int clientNumber;
    private boolean running;

    public ClientHandler(Socket socket, int clientNumber){
    	running = true;
        this.socket = socket;
        this.clientNumber = clientNumber;
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
        do
        {
        	//Receive message
        	String msg;
        	do
        	{
        		msg = receive();
        	}while(msg == null);
        	
            String[] words = msg.split(" ");
            
            String response = "";
            
            switch(words[0])
            {
            case "login":
            	//TODO: What to call to login
            	break;
            case "register":
            	//TODO: What to call to register
            	break;
            case "send":
            	System.out.println("Message received from client " + clientNumber + ": " + msg.substring(5));
            	response = "message received";
            	break;
            case "image":
            	ImageReceiver.printImageInfo(socket, words[1]);
            	ImageReceiver.receiveImage(socket, words[1]);
            	break;
            case "close":
            	running = false;
            	response = "connection closing";
            	break;
            default:
            	response = "unknown command";
            }
            
            send(response);
            
        }while(running);
      
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

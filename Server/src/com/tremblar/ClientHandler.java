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
            String msg = receive();
            String[] words = msg.split(" ");
            
            String response = "";
            
            switch(words[0])
            {
            case "send":
            	System.out.println("Message received from client " + clientNumber + ": " + msg.substring(5));
            	response = "message received";
            	break;
            case "login":
            	//TODO: What to call to login
            	break;
            case "image":
            	//TODO: What to call to load image
            	break;
            case "close":
            	running = false;
            	response = "connection closing";
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

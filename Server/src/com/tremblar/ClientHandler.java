package com.tremblar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientHandler extends Thread {
	private Socket socket; // the client socket
    private int clientNumber;
    private boolean running;
  
    public static PasswordManager manager;

    private boolean clientLogin(String client, String pw) throws IOException{
		if(manager.clientOk(client, pw)){
			System.out.println("login success. Welcome back!");
			return true;
		}
		else{
			System.out.println("there was an error with the username or the password, please try again");
			return false;
		}
	}
    
    public ClientHandler(Socket socket, int clientNumber, PasswordManager manager) throws IOException{
    	running = true;
        this.socket = socket;
        this.clientNumber = clientNumber;
        ClientHandler.manager = manager;
        System.out.println("new connection with client number " + this.clientNumber +
            " at " + socket);
        send("Connection Established");
        this.run();
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
        do
        {
        	//Receive message
        	String msg;
        	do{
        		msg = receive();
        	}while(msg == null && running);
        	System.out.println(msg);
            String[] words = null;
            if (msg != null) {
            	msg.split(" ");
            }
            
            String response = "";
            System.out.println(msg);
            
            switch(words[0])
            {
            case "login":{
            	try {
					System.out.println("loggin in ... ");
            		if(this.clientLogin(words[1], words[2])) {
            			response = "login success. welcome back!";
            		}else {
            			response = "error occured with password or username. please try again "
            					+ "or register";
            		}
            		
				} catch (IOException e) {/*nothing to do here*/}
            	break;
            }
            case "register":
            	boolean success = ClientHandler.manager.insertClient(words[1], words[0]);
            	if (success) {
            		response = "register successful. please login";
            	}else {
            		response = "username already exits. pick another one";
            	}
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

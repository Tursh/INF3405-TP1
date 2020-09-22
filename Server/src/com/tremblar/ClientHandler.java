package com.tremblar;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {
	private Socket socket; // the client socket
    private int clientNumber;

    public ClientHandler(Socket socket, int clientNumber){
        this.socket = socket;
        this.clientNumber = clientNumber;
        System.out.println("new connection with client number " + this.clientNumber +
            " at " + socket);
    }
    
    public void run(){
        System.out.println("running");
        try{
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF("message from server to client " + clientNumber);
            
        }catch(IOException e){
            System.out.println(e);
        }finally{
            try{
                socket.close();
            }catch(IOException e){
                System.out.println(e);
            }
            System.out.println("connection closed");
        }
    }
}

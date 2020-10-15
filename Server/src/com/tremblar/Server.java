package com.tremblar;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.io.DataOutputStream;
import java.io.IOException;

public class Server{
	
	public static String SERVER_ADRESS = "127.0.0.1";
	public static int SERVER_PORT = 5000;
	private static PasswordManager pm;
	
	private static ServerSocket listener;
	private static int clientCount = 0;
	
	public static void main(String[] args) throws Exception{
		
		/*
        Enabling SO_REUSEADDR prior to binding the socket using bind(SocketAddress)
        allows the socket to be bound even though a previous connection is in a timeout state.
        */
		pm = new PasswordManager("passwords.json");
        listener = new ServerSocket();
        listener.setReuseAddress(true);
        InetAddress serverIP = InetAddress.getByName(SERVER_ADRESS);
        System.out.println("test client ok");
        System.out.println(pm.clientOk("FOO", "BAR"));
        // "binding" a socket to an address and a port
        // only an address = unbound & cannot receive data
        // bound = can receive data and has a port number
        listener.bind(new InetSocketAddress(serverIP, SERVER_PORT));
        System.out.format("the server is running on %s:%d%n", SERVER_ADRESS, SERVER_PORT);
        try{
            /*listen for a client to connect*/
            /* when a client connects, the Run() function of ClientHandler
            is executed */
            while(true){
                // note : connect is blocking (wait for the client to connect)
                System.out.println("waiting for connection ... ");
                new ClientHandler(listener.accept(), clientCount++, pm).start();
            }
        }
        finally{
            // close the connection
            listener.close();
        }
		
	}
}

package com.tremblar;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class ImageSender {

	public static final int PACKET_SIZE = 100;
	
	public static void SendImage(Socket socket, String fileName) throws Exception
	{
		System.out.println("Sending Image...");
		
		BufferedImage image;
		
		//Load image from file
		try {
		 image = ImageIO.read(new File(fileName)); 
		}
		catch(Exception e)
		{
			System.out.println("The image could not be send because the given file doesn't exist");
			return;
		}
		
		DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
		
		//Telling the server that we are sending an image
		outputStream.writeUTF("image " + fileName);
		outputStream.flush();
		
		//Load the image to a byte array
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();	
        ImageIO.write(image, "jpg", byteArrayOutputStream);
        byte[] imageArray = byteArrayOutputStream.toByteArray();
       
        //Get image size
        int imageSize = byteArrayOutputStream.size();

        //Send in 100 bytes packets
        for(int i = 0; i < imageSize; i += PACKET_SIZE)
        {
        	//Get and send packet size
        	int packetSize = imageSize - i < PACKET_SIZE ? imageSize - i : PACKET_SIZE;
        	byte[] size = ByteBuffer.allocate(4).putInt(packetSize).array();
        	outputStream.write(size);
        	
        	outputStream.write(Arrays.copyOfRange(imageArray, i, i + packetSize));
            outputStream.flush();
        }
        outputStream.writeUTF("Done Sending");
        
        System.out.println("Image sent succesfully!");
	}
	
}

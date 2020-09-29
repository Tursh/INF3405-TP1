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
		outputStream.writeUTF("image");
		outputStream.flush();
		
		//Load the image to a byte stream
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();	
        ImageIO.write(image, "jpg", byteArrayOutputStream);
        
        int imageSize = byteArrayOutputStream.size();
        byte[] imageArray = byteArrayOutputStream.toByteArray();
        
        for(int i = 0; i < imageSize / 100; i += 100)
        {
        	outputStream.write(i % 100);
        	
        	outputStream.write(Arrays.copyOfRange(imageArray, i, i + ((imageSize - i) < 100 ? imageSize - i : 100)));
        }
        //Send
        outputStream.flush();
		
	}
	
}

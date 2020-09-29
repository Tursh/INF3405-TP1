package com.tremblar;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

public class ImageReceiver {
	
	public static void receiveImage(Socket socket, String fileName) {
		try
		{
		// From https://stackoverflow.com/questions/25086868/how-to-send-images-through-sockets-in-java
		DataInputStream inputStream = new DataInputStream(socket.getInputStream());

        byte[] sizeAr = new byte[4];
        inputStream.read(sizeAr);
        int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

        byte[] imageAr = new byte[size];
        inputStream.read(imageAr);

        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));

        System.out.println("Image " + fileName + " was received");
        ImageIO.write(image, "jpg", new File(fileName));
		}
		catch(Exception e)
		{
			System.out.println("Could not receive image. Error: " + e.getMessage());
		}
	}
}

package com.tremblar;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import javax.imageio.ImageIO;

public class ImageReceiver {
	
	public static void receiveImage(Socket socket, String fileName) {
		try
		{
			// From https://stackoverflow.com/questions/25086868/how-to-send-images-through-sockets-in-java
			
			DataInputStream inputStream = new DataInputStream(socket.getInputStream());
			
			byte[] imageArray = new byte[0];
			while (inputStream.readUTF().equals("Chunk incoming")) {
				byte[] sizeAr = new byte[4];
				inputStream.read(sizeAr);
				int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
				
				byte[] partOfImageAr = new byte[size];
				inputStream.read(partOfImageAr);
				
				// Concatenate new array to image
				byte[] oldArray = imageArray.clone();
				imageArray = Arrays.copyOf(oldArray, oldArray.length + partOfImageAr.length);
				System.arraycopy(partOfImageAr, 0, imageArray, oldArray.length, partOfImageAr.length);
			}
			
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageArray));
			
			System.out.println("Image " + fileName + " was received");
			
			// Apply filter
			image = Sobel.process(image);
			
			// Save filtered image
			ImageIO.write(image, "jpg", new File(fileName));
		}
		catch(Exception e)
		{
			System.out.println("Could not receive image. Error: " + e.getMessage());
		}
	}
	
	public static void printImageInfo(Socket socket, String fileName) {
		String clientIpAddressAndPort = socket.getRemoteSocketAddress().toString().replace("/","");
		LocalDate date = LocalDate.now();
		String time = LocalTime.now().truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_LOCAL_TIME);
		
		System.out.println("[Username - " + clientIpAddressAndPort + " - " + date + "@" + time
				+ "] : Image " + fileName + " reçue pour traitement.");
	}
}

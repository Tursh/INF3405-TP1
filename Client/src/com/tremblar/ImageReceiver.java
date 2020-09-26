package com.tremblar;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

import javax.imageio.ImageIO;

class ImageReceiver{
	private String fileType;
	ImageReceiver(String fileType){
		this.fileType = fileType;
	}
	// receives an image from the server and saves it
	void receiveAndSave(Socket socket, String fileName) throws IOException{
		DataInputStream receivedStream = new DataInputStream(socket.getInputStream());
    	BufferedImage receivedImage = ImageIO.read(receivedStream);
    	ImageIO.write(receivedImage, this.fileType, new File(fileName) );	
	}
}
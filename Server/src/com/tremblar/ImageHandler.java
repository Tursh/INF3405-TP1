package com.tremblar;

import java.io.*;
import java.net.Socket;
import java.awt.image.BufferedImage;

import javax.imageio.*;

public class ImageHandler{
    ImageHandler(){
    	// no need to do anything here, just a placeholder
    }
 
    public BufferedImage receiveAndProcessImage(Socket socket) throws IOException{
    	// takes the data output stream from the socket, converts it to an
    	// image, applies Sobel and returns it
    	DataInputStream receivedStream = new DataInputStream(socket.getInputStream());
    	BufferedImage receivedImage = ImageIO.read(receivedStream);
    	return Sobel.process(receivedImage);
    }
    public void sendImage(Socket socket, String fileType) throws IOException{
    	// process the image and return sobel
    	BufferedImage requestedImage = receiveAndProcessImage(socket);
    	// get the output stream
    	DataOutputStream out = new DataOutputStream(socket.getOutputStream());
    	// write the buffered image to the socket output
    	ImageIO.write(requestedImage,fileType,out);
    }
}
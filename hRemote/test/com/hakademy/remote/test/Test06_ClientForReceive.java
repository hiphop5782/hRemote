package com.hakademy.remote.test;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

//TCP Client(for helper) - java.net
public class Test06_ClientForReceive {
	public static void main(String[] args) throws IOException {
		//connect
		Socket socket = new Socket("localhost", 36500);
		
		int limit = 1 * 1024  * 1024;
		byte[] buffer = new byte[limit];
		
		DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		
		while(true) {
			int size = in.readInt();
			System.out.println("size = " + size);
			in.readFully(buffer, 0, size);
			
			//converting
			ByteArrayInputStream bais = new ByteArrayInputStream(buffer, 0, size);
			BufferedImage image = ImageIO.read(bais);
			
			//save image
			ImageIO.write(image, "jpg", new File("tmp-receive.jpg"));
		}
		
		//socket close
		//socket.close();
	}
}

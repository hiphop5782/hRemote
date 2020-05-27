package com.hakademy.remote.test;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;

import com.hakademy.utility.screen.ScreenManager;

//TCP Server(for user) - java.net
public class Test07_ClientForSend {
	public static void main(String[] args) throws IOException {
		//IPv4 server open
		ServerSocket server = new ServerSocket(36500);
		
		//accept
		Socket socket = server.accept();
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		
		//capture screen
		ScreenManager manager = ScreenManager.getManager();
		
		for(int i=0; i < 10; i++) {
			BufferedImage image = manager.getCurrentMonitorImage();
			
			//convert to bytebuffer
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", new File("tmp-send.jpg"));//test
			ImageIO.write(image, "jpg", baos);
			baos.flush();
			byte[] buffer = baos.toByteArray();
			baos.close();
			System.out.println("compress image size");
			System.out.println(buffer.length);
			
			//send
			out.writeInt(buffer.length);
			out.flush();
			out.write(buffer);
			out.flush();
		}
		
		//server close
		socket.close();
		server.close();
	}
}

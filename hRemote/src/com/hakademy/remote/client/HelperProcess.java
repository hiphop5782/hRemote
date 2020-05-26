package com.hakademy.remote.client;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

import com.hakademy.utility.object.annotation.Component;

/**
 * Helper(receiver) process 
 */
@Component
public class HelperProcess extends RemoteProcess{
	private String host;
	private int port;
	private int limit = 10 * 1024 * 1024;//1MB
	private byte[] buffer = new byte[limit];
	
	public HelperProcess() {}
	
	public void setHost(String host) {
		this.host = host;
	}
	public String getHost() {
		return host;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getPort() {
		return port;
	}
	public void connect() throws UnknownHostException, IOException {
		this.socket = new Socket(host, port);
		this.out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		this.in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		this.setDaemon(true);
		this.start();
	}
	
	private ScreenReceiveHandler handler;
	public void setHandler(ScreenReceiveHandler handler) {
		this.handler = handler;
	}
	
	@Override
	public void run() {
		while(liveFlag) {
			try {
				//read size and data
				int size = in.readInt();
				in.readFully(buffer, 0, size);
				
				//convert to byte array
				ByteArrayInputStream bais = new ByteArrayInputStream(buffer, 0, size);
				
				//conver to buffered image
				BufferedImage image = ImageIO.read(bais);
				
				//send to handler if exist
				System.out.println("receive = "+handler);
				if(handler != null) {
					handler.screenReceived(image);
				}
			}
			catch(Exception e) {
				/*receive error(skip) */
				e.printStackTrace();
			}
		}
	}
	
}





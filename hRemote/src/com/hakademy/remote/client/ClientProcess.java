package com.hakademy.remote.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;

import com.hakademy.utility.object.annotation.Component;
import com.hakademy.utility.screen.ScreenManager;

@Component
public class ClientProcess extends RemoteProcess{
	private ServerSocket server;
	
	private int port, frame;
	public void setPort(int port) {
		this.port = port;
	}
	public int getPort() {
		return port;
	}
	public void setFrame(int frame) {
		this.frame = frame;
	}
	public int getFrame() {
		return frame;
	}
	
	private ScreenManager manager;
	
	public ClientProcess() throws IOException {
		this.manager = ScreenManager.getManager();
	}
	
	public void connect() throws IOException {
		this.server = new ServerSocket(port);
		this.socket = server.accept();
		this.out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		this.in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		this.setDaemon(true);
		this.start();
	}
	
	@Override
	public void run() {
		while(liveFlag) {
			try {
				send(manager.getCurrentMonitorImageDataAsJpg());
				
				Thread.sleep(1000/frame);
			}
			catch(Exception e) {/*send error(skip)*/}
		}
	}
	
	private void send(byte[] data) throws IOException {
		System.out.println("send = " + data.length);
		out.write(data.length);
		out.flush();
		out.write(data);
		out.flush();
	}
}

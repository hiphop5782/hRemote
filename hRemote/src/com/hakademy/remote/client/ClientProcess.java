package com.hakademy.remote.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hakademy.remote.mapper.DataFromClient;
import com.hakademy.utility.object.annotation.Component;
import com.hakademy.utility.screen.ScreenManager;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Component
@Data
@EqualsAndHashCode(callSuper=false)
public class ClientProcess extends RemoteProcess{
	private ServerSocket server;
	
	private int port, frame;
	private int screen = ScreenManager.MAIN_MONITOR;
	
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
		try {
			while(liveFlag) {
				send(manager.getMonitorImageDataAsJpg(screen));
				Thread.sleep(1000/frame);
			}
		}
		catch(Exception e) {
			/*send error(skip)*/
			e.printStackTrace();
			this.kill();
			this.interrupt();
		}
	}
	
	private ObjectMapper mapper = new ObjectMapper();
	private void send(byte[] data) throws IOException {
		DataFromClient d = new DataFromClient();
		d.setScreenNumber(screen);
		d.setScreenData(data);
		d.setMonitorCount(manager.getMonitorCount());
		byte[] b = mapper.writeValueAsBytes(d);
		out.writeInt(b.length);
		out.flush();
		out.write(b);
		out.flush();
	}
}

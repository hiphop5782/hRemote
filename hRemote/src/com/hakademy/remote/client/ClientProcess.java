package com.hakademy.remote.client;

import java.awt.Robot;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hakademy.remote.mapper.DataFromClient;
import com.hakademy.remote.mapper.DataFromHelper;
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

	private byte[] buffer = new byte[100 * 1024];
	
	private ObjectMapper readMapper = new ObjectMapper();
	private ObjectMapper writeMapper = new ObjectMapper();
	
	private Thread receiver;
	private Runnable receiveAction = ()->{
		while(liveFlag) {
			try {
				int size = in.readInt();
				in.readFully(buffer, 0, size);
				
				//convert
				DataFromHelper data = readMapper.readValue(buffer, DataFromHelper.class);
				doSomething(data);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	private Robot robot;
	public ClientProcess() {
		this.manager = ScreenManager.getManager();
		try {
			robot = new Robot();
		}
		catch(Exception e) {}
	}
	
	public void connect() throws IOException {
		this.server = new ServerSocket(port);
		this.socket = server.accept();
		this.out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		this.in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		this.receiver = new Thread(receiveAction);
		this.receiver.setDaemon(true);
		this.receiver.start();
		this.setDaemon(true);
		this.start();
	}
	
	@Override
	public void run() {
		try {
			while(liveFlag) {
				send(manager.getMonitorImageDataAsPng(screen));
				Thread.sleep(1000/frame);
			}
		}
		catch(Exception e) {
			/*send error(skip)*/
			e.printStackTrace();
			this.kill();
			this.interrupt();
			System.exit(-1);
		}
	}
	
	private void send(byte[] data) throws IOException {
		DataFromClient d = new DataFromClient();
		d.setScreenNumber(screen);
		d.setScreenData(data);
		d.setMonitorCount(manager.getMonitorCount());
		byte[] b = writeMapper.writeValueAsBytes(d);
		out.writeInt(b.length);
		out.flush();
		out.write(b);
		out.flush();
	}
	
	private void doSomething(DataFromHelper data) {
		switch(data.getHeader()) {
		case CHANGE_SCREEN: 
			this.screen = data.getScreenNumber();
			break;
		case MOUSE_CLICK_CONTROL:
			robot.mousePress(data.getMouseButton());
			robot.mouseRelease(data.getMouseButton());
			break;
		case MOUSE_MOVE_CONTROL:
			robot.mouseMove(data.getXpos(), data.getYpos());
			break;
		case KEYBOARD_CONTROL:
			robot.keyPress(data.getKeyCode());
			robot.keyRelease(data.getKeyCode());
			break;
		default:
			break;
		}
	}
}

package com.hakademy.remote.client;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
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
			robot.setAutoDelay(1);
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
			changeScreenAction(data);
			break;
		case MOUSE_PRESS_CONTROL:
			mousePressAction(data); break;
		case MOUSE_RELEASE_CONTROL:
			mouseReleaseAction(data); break;
		case MOUSE_CLICK_CONTROL:
			mouseClickAction(data); break;
		case MOUSE_MOVE_CONTROL:
			mouseMoveAction(data); break;
		case KEYBOARD_PRESS_CONTROL:
			keyboardPressAction(data); break;
		case KEYBOARD_RELEASE_CONTROL:
			keyboardReleaseAction(data); break;
		case KEYBOARD_TYPE_CONTROL:
			keyboardTypeAction(data); break;
		default:
			break;
		}
	}
	
	private void changeScreenAction(DataFromHelper data) {
		this.screen = data.getScreenNumber();
	}

	private void keyboardTypeAction(DataFromHelper data) {
		keyboardPressAction(data);
		keyboardReleaseAction(data);
	}
	
	private void keyboardReleaseAction(DataFromHelper data) {
		robot.keyRelease(data.getKeyCode());
	}
	
	private void keyboardPressAction(DataFromHelper data) {
		robot.keyPress(data.getKeyCode());
	}
	
	private void mouseMoveAction(DataFromHelper data) {
		Rectangle rect = manager.getMonitorRect(screen);
		int xpos = rect.width * data.getXpos() / data.getWidth();
		int ypos = rect.height * data.getYpos() / data.getHeight();
		robot.mouseMove(xpos, ypos);
	}
	
	private void mousePressAction(DataFromHelper data) {
		switch(data.getMouseButton()) {
		case MouseEvent.BUTTON1:
			robot.mousePress(InputEvent.BUTTON1_MASK);		break;
		case MouseEvent.BUTTON2:
			robot.mousePress(InputEvent.BUTTON2_MASK);		break;
		case MouseEvent.BUTTON3:
			robot.mousePress(InputEvent.BUTTON3_MASK);		break;
		}
	}
	
	private void mouseReleaseAction(DataFromHelper data) {
		switch(data.getMouseButton()) {
		case MouseEvent.BUTTON1:
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
			break;
		case MouseEvent.BUTTON2:
			robot.mouseRelease(InputEvent.BUTTON2_MASK);
			break;
		case MouseEvent.BUTTON3:
			robot.mouseRelease(InputEvent.BUTTON3_MASK);
			break;
		}
	}
	
	private void mouseClickAction(DataFromHelper data) {
		mousePressAction(data);
		mouseReleaseAction(data);
	}
}

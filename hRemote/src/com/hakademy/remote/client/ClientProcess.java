package com.hakademy.remote.client;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.URL;

import javax.swing.JOptionPane;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hakademy.remote.client.ui.HelperFrame;
import com.hakademy.remote.entity.Client;
import com.hakademy.remote.log.LogManager;
import com.hakademy.remote.mapper.DataFromClient;
import com.hakademy.remote.mapper.DataFromHelper;
import com.hakademy.utility.hook.KeyboardHook;
import com.hakademy.utility.object.annotation.Component;
import com.hakademy.utility.object.annotation.Inject;
import com.hakademy.utility.screen.ScreenManager;
import com.hakademy.utility.ui.DialogManager;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.coley.simplejna.Keyboard;

@Component
@Data
@EqualsAndHashCode(callSuper=false)
public class ClientProcess extends RemoteProcess{
	private ServerSocket server;
	
	private int frame;
	private int screen = ScreenManager.MAIN_MONITOR;
	
	private ScreenManager manager;

	private byte[] buffer = new byte[1 * 1024 * 1024];
	
	private ObjectMapper readMapper = new ObjectMapper();
	private ObjectMapper writeMapper = new ObjectMapper();
	
	private Client client;
	
	@Inject
	private HelperFrame helperFrame;
	
	private Thread receiver;
	private Runnable receiveAction = ()->{
		try {
			while(liveFlag) {
				int size = in.readInt();
				in.readFully(buffer, 0, size);
				
				//convert
				DataFromHelper data = readMapper.readValue(buffer, DataFromHelper.class);
				doSomething(data);
			}
		}
		catch(Exception e) {
			kill();
			JOptionPane.showMessageDialog(null, "원격제어가 종료되었습니다");
			try{
				connect();
			}catch(Exception err) {
				DialogManager.alert(helperFrame, "오류 발생 : "+e.getMessage());
			};
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
	
	public static final String serverUrl = "http://www.sysout.co.kr/remote/";
//	public static final String serverUrl = "http://localhost:5555/remote/";
	
	/**
	 * Remote Server에 Regist 데이터 전송하는 메소드
	 * @throws IOException
	 */
	public boolean regist() throws IOException{
		URL url = new URL(serverUrl);
		HttpURLConnection connection = null;
		
		try{
			connection = (HttpURLConnection)url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "close");//강제종료(없으면 연결이 유지됨)
			connection.setConnectTimeout(10000);
			
//			PrintWriter out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
//			out.write("name="+name);
//			out.close();
			
			int code = connection.getResponseCode();
			if(code == 200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				ObjectMapper mapper = new ObjectMapper();
				client = mapper.readValue(reader, Client.class);
				LogManager.info(client.toString());
				return true;
			}
			throw new HttpRetryException("서버 응답이 없습니다", code);
		}
		finally {
			if(connection != null) {
				connection.disconnect();
				LogManager.info("연결 종료");
			}
		}
	}
	
	public void changeState(boolean connectable) throws IOException {
		if(client == null) return;
		
		client.setConnectable(connectable);
		
		HttpURLConnection connection = null;
		try {
			URL url = new URL(serverUrl);
			connection = (HttpURLConnection)url.openConnection();
			
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setConnectTimeout(10000);
			connection.setRequestProperty("Connection", "close");
			connection.setRequestProperty("Accept", "*/*");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestMethod("PUT");
			
			ObjectMapper mapper = new ObjectMapper();
			PrintWriter out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
			out.write(mapper.writeValueAsString(client));
			out.flush();
			
			int code = connection.getResponseCode();
			if(code != 200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
				client = mapper.readValue(reader, Client.class);
				reader.close();
			}
		}
		finally {
			if(connection != null) {
				connection.disconnect();
			}
		}
	}
	
	public void connect() throws IOException {
		if(client != null) {
			this.server = new ServerSocket(client.getPort());
			this.socket = server.accept();
			this.out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			this.in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			this.receiver = new Thread(receiveAction);
			this.receiver.setDaemon(true);
			this.receiver.start();
			this.setDaemon(true);
			this.start();
		}
		else{
			throw new IOException("등록이 정상적으로 이루어지지 않았습니다");
		}
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

	private KeyboardHook hook = KeyboardHook.getInstance();
	private void keyboardTypeAction(DataFromHelper data) {
		LogManager.info("receive typed = " + data.getKeyCode());
		Keyboard.pressKey(data.getKeyCode());
	}
	
	private void keyboardReleaseAction(DataFromHelper data) {
		LogManager.info("receive release = " + data.getKeyCode());
		Keyboard.sendKeyUp(data.getKeyCode());
	}
	
	private void keyboardPressAction(DataFromHelper data) {
		LogManager.info("receive press = " + data.getKeyCode());
		Keyboard.sendKeyDown(data.getKeyCode());
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

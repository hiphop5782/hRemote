package com.hakademy.remote.client;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hakademy.remote.client.ui.HelperMenu;
import com.hakademy.remote.client.ui.HelperPanel;
import com.hakademy.remote.command.CommandHeader;
import com.hakademy.remote.entity.Client;
import com.hakademy.remote.handler.ScreenInformationHandler;
import com.hakademy.remote.handler.ScreenReceiveHandler;
import com.hakademy.remote.log.LogManager;
import com.hakademy.remote.mapper.DataFromClient;
import com.hakademy.remote.mapper.DataFromHelper;
import com.hakademy.utility.object.annotation.Component;
import com.hakademy.utility.object.annotation.Inject;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.coley.simplejna.hook.key.KeyEventReceiver;
import me.coley.simplejna.hook.key.KeyHookManager;

/**
 * Helper(receiver) process 
 */
@Component
@Data
@EqualsAndHashCode(callSuper=false)
public class HelperProcess extends RemoteProcess{
	private int limit = 10 * 1024 * 1024;//1MB
	private byte[] buffer = new byte[limit];
	
	private boolean preventFlag = false;
	public void enablePrevent() {
		preventFlag = true;
	}
	public void disablePrevent() {
		preventFlag = false;
	}
	public boolean isPrevent() {
		return preventFlag;
	}
	
	private KeyHookManager manager = new KeyHookManager();
	private KeyEventReceiver receiver = new KeyEventReceiver(manager) {
		@Override
		public boolean onKeyUpdate(SystemState sysState, PressState pressState, int time, int vkCode) {
			if(pressState == PressState.DOWN) {
				sendKeyboardPressCommand(vkCode);
			}
			else if(pressState == PressState.UP){
				sendKeyboardReleaseCommand(vkCode);
			}
			return preventFlag;
		}
	};
	
	public void startHook() {
		enablePrevent();
		manager.hook(receiver);
	}
	public void stopHook() {
		manager.unhook(receiver);
		disablePrevent();
	}
	
	@Inject
	private HelperMenu menu;
	
	@Inject
	private HelperPanel panel;
	
	private Client client;
//	public static final String serverUrl = "http://www.sysout.co.kr/remote/find/";
	public static final String serverUrl = "http://localhost:5555/remote/find/";
	public void findClient(String clientSecret) throws IOException {
		URL url = new URL(serverUrl + URLEncoder.encode(clientSecret, "UTF-8"));
		System.out.println(url.toExternalForm());
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection)url.openConnection();
			
			connection.setConnectTimeout(10000);//timeout
			connection.setDefaultUseCaches(false);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Connection", "close");
			connection.addRequestProperty("Accept", "*/*");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			
//			PrintWriter out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
//			out.write(clientSecret);
//			out.flush();
			
			int code = connection.getResponseCode();
			if(code != 200) {
				throw new HttpRetryException("서버 응답 오류 : "+code, code);
			}
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			ObjectMapper mapper = new ObjectMapper();
			this.client = mapper.readValue(reader, Client.class);
			reader.close();
		}
		finally {
			if(connection != null) {
				connection.disconnect();
			}
		}
	}
	
	public HelperProcess() {}
	
	public void connect() throws UnknownHostException, IOException {
		connect(client.getIp(), client.getPort());
	}
	
	public void connect(String host, int port) throws UnknownHostException, IOException {
		this.socket = new Socket(host, port);
		this.out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		this.in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		this.setDaemon(true);
		this.start();
	}
	
	private ScreenReceiveHandler imageHandler;
	private ScreenInformationHandler infoHandler;
	
	public void sendData(DataFromHelper data) {
		try {
			byte[] b = writeMapper.writeValueAsBytes(data);
			out.writeInt(b.length);
			out.flush();
			out.write(b);
			out.flush();
		}
		catch(IOException e) {
			LogManager.error("명령 전송 오류", e);
			JOptionPane.showMessageDialog(panel, "상대방이 연결을 종료했습니다");
			System.exit(1);
		}
	}
	public void sendKeyboardTypeCommand(int keyCode)  {
		sendData(DataFromHelper.builder()
				.header(CommandHeader.KEYBOARD_TYPE_CONTROL)
				.keyCode(keyCode)
				.build());
	}
	public void sendKeyboardPressCommand(int keyCode) {
		LogManager.info("send press = "+keyCode);
		sendData(DataFromHelper.builder()
				.header(CommandHeader.KEYBOARD_PRESS_CONTROL)
				.keyCode(keyCode)
				.build());
	}
	public void sendKeyboardReleaseCommand(int keyCode) {
		LogManager.info("send release = "+keyCode);
		sendData(DataFromHelper.builder()
				.header(CommandHeader.KEYBOARD_RELEASE_CONTROL)
				.keyCode(keyCode)
				.build());
	}
	public void sendMousePressCommand(int button) {
		sendData(DataFromHelper.builder()
				.header(CommandHeader.MOUSE_PRESS_CONTROL)
				.mouseButton(button)
				.build());	
	}
	public void sendMouseReleaseCommand(int button) {
		sendData(DataFromHelper.builder()
				.header(CommandHeader.MOUSE_RELEASE_CONTROL)
				.mouseButton(button)
				.build());
	}
	public void sendMouseMoveCommand(int x, int y) {
		sendData(DataFromHelper.builder()
						.header(CommandHeader.MOUSE_MOVE_CONTROL)
						.width(panel.getWidth())
						.height(panel.getHeight())
						.xpos(x).ypos(y)
					.build());
	}
	public void sendMouseClickCommand(int button) {
		sendData(DataFromHelper.builder()
				.header(CommandHeader.MOUSE_CLICK_CONTROL)
				.mouseButton(button)
			.build());	
	}
	public void sendChangeScreenCommand(int screen) throws IOException {
		sendData(DataFromHelper.builder()
						.header(CommandHeader.CHANGE_SCREEN)
						.screenNumber(screen)
					.build());
	}
	
	private ObjectMapper readMapper = new ObjectMapper();
	private ObjectMapper writeMapper = new ObjectMapper();
	
	@Override
	public void run() {
		try {
			while(liveFlag) {
				//read size and data
				int size = in.readInt();
				in.readFully(buffer, 0, size);
				
				//convert
				DataFromClient data = readMapper.readValue(buffer, DataFromClient.class);
				
				//convert to byte array
				ByteArrayInputStream bais = new ByteArrayInputStream(data.getScreenData());
				
				//conver to buffered image
				BufferedImage image = ImageIO.read(bais);
				
				//send to handler if exist
				if(imageHandler != null) {
					imageHandler.screenReceived(image);
				}
				
				if(infoHandler != null) {
					infoHandler.informationReceived(data);
				}
				
			}
		}
		catch(Exception e) {
			/*receive error(skip) */
			e.printStackTrace();
		}
	}
	
}





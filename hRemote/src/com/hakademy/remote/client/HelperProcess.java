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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hakademy.remote.client.ui.HelperMenu;
import com.hakademy.remote.client.ui.HelperPanel;
import com.hakademy.remote.command.CommandHeader;
import com.hakademy.remote.handler.ScreenInformationHandler;
import com.hakademy.remote.handler.ScreenReceiveHandler;
import com.hakademy.remote.log.LogManager;
import com.hakademy.remote.mapper.DataFromClient;
import com.hakademy.remote.mapper.DataFromHelper;
import com.hakademy.utility.hook.KeyboardHook;
import com.hakademy.utility.object.annotation.Component;
import com.hakademy.utility.object.annotation.Inject;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.coley.simplejna.hook.key.KeyEventReceiver;
import me.coley.simplejna.hook.key.KeyHookManager;
import me.coley.simplejna.hook.key.KeyEventReceiver.PressState;
import me.coley.simplejna.hook.key.KeyEventReceiver.SystemState;

/**
 * Helper(receiver) process 
 */
@Component
@Data
@EqualsAndHashCode(callSuper=false)
public class HelperProcess extends RemoteProcess{
	private String host;
	private int port;
	private int limit = 10 * 1024 * 1024;//1MB
	private byte[] buffer = new byte[limit];
	
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
			return false;
		}
	};
	
	public void startHook() {
		manager.hook(receiver);
	}
	public void stopHook() {
		manager.unhook(receiver);
	}
	
	@Inject
	private HelperMenu menu;
	
	@Inject
	private HelperPanel panel;
	
	public HelperProcess() {}
	
	public void connect() throws UnknownHostException, IOException {
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





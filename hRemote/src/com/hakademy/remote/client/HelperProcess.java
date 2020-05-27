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
import com.hakademy.remote.command.CommandHeader;
import com.hakademy.remote.handler.ScreenInformationHandler;
import com.hakademy.remote.handler.ScreenReceiveHandler;
import com.hakademy.remote.mapper.DataFromClient;
import com.hakademy.remote.mapper.DataFromHelper;
import com.hakademy.utility.object.annotation.Component;

import lombok.Data;
import lombok.EqualsAndHashCode;

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
	
	public void sendData(DataFromHelper data) throws IOException{
		out.write(writeMapper.writeValueAsBytes(data));
		out.flush();
	}
	public void sendKeyboardCommand(int keyCode) throws IOException {
		sendData(DataFromHelper.builder()
							.header(CommandHeader.KEYBOARD_CONTROL)
							.keyCode(keyCode)
						.build());
	}
	public void sendMouseMoveCommand(int x, int y) throws IOException {
		sendData(DataFromHelper.builder()
							.header(CommandHeader.MOUSE_MOVE_CONTROL)
							.xpos(x).ypos(y)
						.build());
	}
	public void sendMouseClickCommand(int button) throws IOException {
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
		while(liveFlag) {
			try {
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
				
			}
			catch(Exception e) {
				/*receive error(skip) */
				e.printStackTrace();
			}
		}
	}
	
}





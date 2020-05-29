package com.hakademy.remote.client.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JPanel;

import com.hakademy.remote.HRemoteApplication;
import com.hakademy.remote.client.HelperProcess;
import com.hakademy.remote.handler.ScreenInformationHandler;
import com.hakademy.remote.handler.ScreenReceiveHandler;
import com.hakademy.utility.object.annotation.Component;
import com.hakademy.utility.object.annotation.Inject;

import lombok.Getter;

@Component
public class HelperPanel extends JPanel{
	private static final long serialVersionUID = 1L;

	private BufferedImage image;
	
	@Inject
	@Getter
	private HelperProcess process;
	
	private ScreenReceiveHandler imageHandler = image->{
		draw(image);
	};
	private ScreenInformationHandler infoHandler = data->{
		HelperMenu menu = HRemoteApplication.getBean(HelperMenu.class);
		menu.setScreenSize(data.getMonitorCount());
		menu.setCurrentScreen(data.getScreenNumber());
	};
	
	public HelperPanel() {}
	
	public void connect() throws UnknownHostException, IOException {
//		this.process.setHost("localhost");
//		this.process.setPort(36500);
		this.process.setImageHandler(imageHandler);
		this.process.setInfoHandler(infoHandler);
		this.process.connect();
	}
	
	public void draw(BufferedImage image) {
		this.image = image;
		this.repaint();
	}
	@Override
	public void update(Graphics g) {
		paint(g);
	}
	@Override
	public void paint(Graphics g) {
		if(image != null)
			g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
	}
}

package com.hakademy.remote.client.ui;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
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
	
	private MouseListener mListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			try {
				process.sendMouseClickCommand(e.getButton());
			} catch (IOException err) {
				err.printStackTrace();
			}
		}
	};
	
	private MouseMotionListener moListener = new MouseMotionAdapter() {
		@Override
		public void mouseMoved(MouseEvent e) {
			try {
				process.sendMouseMoveCommand(e.getX(), e.getY());
			}
			catch(Exception err) {
				err.printStackTrace();
			}
		}
	};
	
	public HelperPanel() {}
	
	public void connect() throws UnknownHostException, IOException {
//		this.process.setHost("localhost");
//		this.process.setPort(36500);
		this.addMouseListener(mListener);
		this.addMouseMotionListener(moListener);
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

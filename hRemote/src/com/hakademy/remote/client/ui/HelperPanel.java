package com.hakademy.remote.client.ui;

import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JPanel;

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
	
	@Inject
	private HelperFrame frame;
	
	@Inject
	private HelperMenu menu;
	
	private ScreenReceiveHandler imageHandler = image->{
		draw(image);
	};
	private ScreenInformationHandler infoHandler = data->{
		menu.setScreenSize(data.getMonitorCount());
		menu.setCurrentScreen(data.getScreenNumber());
	};
	
	private MouseListener mListener = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			process.sendMousePressCommand(e.getButton());
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			process.sendMouseReleaseCommand(e.getButton());
		}
		@Override
		public void mouseClicked(MouseEvent e) {
//			process.sendMouseClickCommand(e.getButton());
		}
	};
	
	private KeyListener kListener = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			System.out.println("keyPressed = " + e.getExtendedKeyCode());
			process.sendKeyboardPressCommand(e.getExtendedKeyCode());
		}
		public void keyReleased(KeyEvent e) {
			System.out.println("keyReleased = " + e.getExtendedKeyCode());
			process.sendKeyboardReleaseCommand(e.getExtendedKeyCode());
		}
		public void keyTyped(KeyEvent e) {
			System.out.println("keyTyped = " + e.getExtendedKeyCode());
//			process.sendKeyboardTypeCommand(e.getExtendedKeyCode());
		}
	};
	
	private MouseMotionListener moListener = new MouseMotionAdapter() {
		@Override
		public void mouseDragged(MouseEvent e) {
			process.sendMouseMoveCommand(e.getX(), e.getY());
		}
		@Override
		public void mouseMoved(MouseEvent e) {
			process.sendMouseMoveCommand(e.getX(), e.getY());
		}
	};
	
	public HelperPanel() {}
	
	public void connect() throws UnknownHostException, IOException {
//		this.process.setHost("localhost");
//		this.process.setPort(36500);
		this.addMouseListener(mListener);
		this.addMouseMotionListener(moListener);
		frame.addKeyListener(kListener);
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

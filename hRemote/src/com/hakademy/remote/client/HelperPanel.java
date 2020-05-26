package com.hakademy.remote.client;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JPanel;

import com.hakademy.utility.object.annotation.Component;
import com.hakademy.utility.object.annotation.Inject;

@Component
public class HelperPanel extends JPanel{
	private static final long serialVersionUID = 1L;

	private BufferedImage image;
	
	@Inject
	private HelperProcess process;
	
	private ScreenReceiveHandler handler = image->{
		draw(image);
	};
	
	public HelperPanel() {}
	
	public void connect() throws UnknownHostException, IOException {
		this.process.setHost("localhost");
		this.process.setHandler(handler);
		this.process.setPort(36500);
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

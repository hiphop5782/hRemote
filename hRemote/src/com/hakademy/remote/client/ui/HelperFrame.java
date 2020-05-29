package com.hakademy.remote.client.ui;

import java.io.IOException;

import javax.swing.JFrame;

import com.hakademy.utility.object.annotation.Component;
import com.hakademy.utility.object.annotation.Inject;

import lombok.Getter;

@Component
public class HelperFrame extends JFrame{
	
	@Inject @Getter
	private HelperPanel panel;
	
	@Inject @Getter
	private HelperMenu menu;
	
	public HelperFrame() {
		setTitle("H-Remote");
		setAlwaysOnTop(true);
		setSize(500, 500);
		setLocationByPlatform(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void start(String host) throws IOException {
		start(host, 36500);
	}
	
	public void start(String host, int port) throws IOException {
		setContentPane(panel);
		setJMenuBar(menu);
		setVisible(true);
		panel.getProcess().setHost(host);
		panel.getProcess().setPort(port);
		panel.connect();
	}
	
}

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
	
	public void start() throws IOException {
		setContentPane(panel);
		setJMenuBar(menu);
		setVisible(true);
		panel.getProcess().setHost("localhost");
		panel.getProcess().setPort(36500);
		panel.connect();
	}
	
}

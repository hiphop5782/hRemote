package com.hakademy.remote.client.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.JFrame;

import com.hakademy.remote.client.HelperProcess;
import com.hakademy.utility.object.annotation.Component;
import com.hakademy.utility.object.annotation.Inject;

import lombok.Getter;

@Component
public class HelperFrame extends JFrame{
	
	@Inject @Getter
	private HelperPanel panel;
	
	@Inject @Getter
	private HelperMenu menu;
	
	@Inject
	private HelperProcess process;
	
	private WindowFocusListener fListener = new WindowFocusListener() {
		@Override
		public void windowLostFocus(WindowEvent e) {
			process.getKeyHook().stopHook();
		}
		
		@Override
		public void windowGainedFocus(WindowEvent e) {
			process.getKeyHook().startHookAsEnablePrevent();
		}
	};
	
	public HelperFrame() {
		setTitle("H-Remote");
		setAlwaysOnTop(true);
		setSize(500, 500);
		setLocationByPlatform(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowFocusListener(fListener);
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

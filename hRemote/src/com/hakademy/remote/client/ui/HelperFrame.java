package com.hakademy.remote.client.ui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hakademy.remote.client.HelperProcess;
import com.hakademy.remote.entity.Client;
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
		public void windowGainedFocus(WindowEvent e) {
			process.startHook();
		}
		@Override
		public void windowLostFocus(WindowEvent e) {
			process.stopHook();
		}
	};
	
	public HelperFrame() {
		setTitle("H-Remote");
		setSize(500, 500);
		setLocationByPlatform(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowFocusListener(fListener);
	}
	
	public void display() throws IOException {
		setContentPane(panel);
		setJMenuBar(menu);
		setVisible(true);
		String clientSecret = JOptionPane.showInputDialog(panel, "Client Secret 입력", "Client 설정", JOptionPane.PLAIN_MESSAGE);
		start(clientSecret);
	}
	
	public void start(String clientSecret) throws IOException{
		process.findClient(clientSecret);
		panel.connect();
	}
	
	public void start(String host, int port) throws IOException {
		panel.connect(host, port); 
	}
	
}

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
	
	private Client client;
	
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
		setAlwaysOnTop(true);
		setSize(500, 500);
		setLocationByPlatform(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowFocusListener(fListener);
	}
	
	public void start(long client) throws IOException{
		URL url = new URL("http://localhost:5555/remote/?client="+client);
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection)url.openConnection();
			
			connection.setConnectTimeout(10000);//timeout
			connection.setDefaultUseCaches(false);
			connection.setRequestProperty("Connection", "close");
			connection.setDoInput(true);
			
			int code = connection.getResponseCode();
			if(code != 200) {
				throw new HttpRetryException("서버가 응답하지 않습니다", code);
			}
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			ObjectMapper mapper = new ObjectMapper();
			this.client = mapper.readValue(reader, Client.class);
			reader.close();
			
			start(this.client.getIp(), this.client.getPort());
		}
		finally {
			if(connection != null) {
				connection.disconnect();
			}
		}
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

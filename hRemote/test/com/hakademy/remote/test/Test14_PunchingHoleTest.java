package com.hakademy.remote.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.hakademy.remote.HRemoteApplication;
import com.hakademy.remote.client.ClientProcess;

public class Test14_PunchingHoleTest {
	public static void main(String[] args) throws IOException {
//		URL url = new URL("http://localhost:5555/remote/");
//		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//		
//		connection.setRequestMethod("POST");
//		connection.setDoOutput(true);
//		System.out.println(connection.getResponseCode());
//		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//		
//		while(true) {
//			String line = reader.readLine();
//			if(line == null) break;
//			System.out.println(line);
//		}
//		
//		connection.disconnect();
		
		ClientProcess cp = HRemoteApplication.getBean(ClientProcess.class);
		System.out.println(cp.regist("test"));
	}
}

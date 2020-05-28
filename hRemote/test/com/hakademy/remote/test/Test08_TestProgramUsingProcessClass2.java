package com.hakademy.remote.test;

import java.io.IOException;

import javax.swing.JFrame;

import com.hakademy.remote.client.HelperPanel;
import com.hakademy.remote.client.HelperProcess;
import com.hakademy.utility.object.InMemoryObjectLoader;

public class Test08_TestProgramUsingProcessClass2 {
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, IOException, InterruptedException {
		InMemoryObjectLoader loader = new InMemoryObjectLoader("com.hakademy.remote");
		HelperPanel p = loader.getBean(HelperPanel.class);
		HelperProcess proc = loader.getBean(HelperProcess.class);
		proc.setHost("192.168.0.8");
		proc.setPort(36500);
		JFrame fr = new JFrame();
		fr.setAlwaysOnTop(true);
		fr.setSize(500, 500);
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.setContentPane(p);
		fr.setVisible(true);
		p.connect();
//		proc.connect();
//		Thread.currentThread().join();
	}
}
